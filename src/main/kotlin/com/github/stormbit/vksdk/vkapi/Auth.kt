package com.github.stormbit.vksdk.vkapi

import com.github.stormbit.vksdk.clients.CaptchaHandler
import com.github.stormbit.vksdk.clients.TwoFactorHandler
import com.github.stormbit.vksdk.clients.VkUserClient
import com.github.stormbit.vksdk.exceptions.*
import com.github.stormbit.vksdk.objects.Captcha
import com.github.stormbit.vksdk.utils.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import net.dongliu.requests.Header
import net.dongliu.requests.Requests
import org.slf4j.Logger
import org.slf4j.LoggerFactory

internal val RE_LOGIN_HASH = Regex("name=\"lg_h\" value=\"([a-z0-9]+)\"")
internal val RE_CAPTCHA_ID = Regex("onLoginCaptcha\\('(\\d+)'")
internal val RE_AUTH_HASH = Regex("\\{.*?act: 'a_authcheck_code'.+?hash: '([a-z_0-9]+)'.*}")
internal val RE_TOKEN_URL = Regex("location\\.href = \"(.*?)\"\\+addr;")

class Auth(
    private val login: String,
    private val password: String,
    private var appId: Int = 6222115,
    scope: VkUserPermissions = DEFAULT_USER_SCOPES,
    var twoFactorHandler: TwoFactorHandler? = null,
    var captchaHandler: CaptchaHandler? = null,
    private val redirectUrl: String = ""
) {
    companion object {
        private const val STRING_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36"
        private val USER_AGENT = Header("User-Agent", STRING_USER_AGENT)
        private val DEFAULT_USER_SCOPES = VkUserPermissions().apply {
            allPermissions = true
        }
    }

    private val log: Logger = LoggerFactory.getLogger(Auth::class.java)

    private val scope: Int = scope.mask

    private val session = Session()


    fun authByVk(): String {
        val response = oauth()
        val accessToken = response.getString("access_token")!!

        val response2 = refreshToken(accessToken)["response"]!!.jsonObject

        return response2.getString("token")!!
    }

    private fun oauth(parameters: Map<String, Any> = emptyMap()): JsonObject {
        log.info("Logging in...")

        val prms = hashMapOf<String, Any>(
            "grant_type" to "password",
            "client_id" to VkUserClient.CLIENT_ID,
            "client_secret" to VkUserClient.CLIENT_SECRET,
            "username" to login,
            "password" to password,
            "v" to VK_API_VERSION,
            "2fa_supported" to 1,
        )

        prms.putAll(parameters)

        val responseString = Requests.get(VkUserClient.BASE_PROXY_OAUTH_URL + "token")
            .body(prms)
            .headers(VkUserClient.HEADER)
            .send().readToText()

        val response = json.parseToJsonElement(responseString).jsonObject

        if ("error" in response.toString()) {
            if ("need_captcha" == response.getString("error")) {
                log.info("Captcha code is required")

                val captchaSid = response.getString("captcha_sid")!!
                val captcha = Captcha(captchaSid)

                val code = captchaHandler?.handle(captcha) ?: throw CaptchaException("Captcha listener is required")
                return oauth(mapOf(
                    "captcha_sid" to captchaSid,
                    "captcha_key" to code
                ))
            } else if ("need_validation" == response.getString("error")) {
                if ("2fa_sms" in response.toString()) {
                    val (code, rememberDevice) = twoFactorHandler?.handle() ?: throw TwoFactorException("Two Factor listener is not initialized")

                    return oauth(mapOf(
                        "code" to code,
                        "remember" to rememberDevice.toInt()
                    ))
                } else if ("ban_info" in response.toString()) {
                    throw BanException(response.getString("error_description"))
                }
            } else if ("invalid_request" == response.getString("error")) {
                if ("wrong_otp" in response.toString()) {
                    val (code, rememberDevice) = twoFactorHandler?.handle() ?: throw TwoFactorException("Two Factor listener is not initialized")

                    return oauth(mapOf(
                        "code" to code,
                        "remember" to rememberDevice.toInt()
                    ))
                }
            } else {
                throw AuthException("Error: ${response["error_description"]}")
            }
        }

        log.info("Authentication succeeded!")

        return response
    }

    private fun refreshToken(accessToken: String): JsonObject {
        val params = mapOf(
            "access_token" to accessToken,
            "receipt" to VkUserClient.RECEIPT,
            "v" to VK_API_VERSION
        )

        val response = json.parseToJsonElement(Requests.get(BASE_API_URL + "auth.refreshToken")
            .body(params)
            .headers(VkUserClient.HEADER)
            .send().readToText()).jsonObject

        if ("error" in response.toString()) {
            throw AuthException("Error: ${response["error_description"]}")
        }

        return response
    }

    private fun passTwoFactor(response: String): String {
        val (code, rememberDevice) = twoFactorHandler?.handle() ?: throw TwoFactorException("Two Factor listener is not initialized")

        val authHash = regexSearch(RE_AUTH_HASH, response, 1)!!

        val values = mapOf(
            "act" to "a_authcheck_code",
            "al" to "1",
            "code" to code,
            "remember" to rememberDevice.toInt(),
            "hash" to authHash
        )

        val resp = session.post("https://vk.com/al_login.php")
            .headers(USER_AGENT)
            .body(values)
            .send().readToText()

        val data = resp.replace("[<!>-]".toRegex(), "").toJsonObject()
        val status = data.getJsonArray("payload")!!.getInt(0)

        when {
            status == 4 -> {
                val path = data.getJsonArray("payload")!!.getJsonArray(1).getString(0).replace("[\\\\\"]".toRegex(), "")
                return session.get("https://vk.com/$path").send().readToText()
            }

            listOf(0, 8).contains(status) -> return passTwoFactor(response)

            status == 2 -> throw TwoFactorException("ReCaptcha required")
        }

        throw TwoFactorException("Two factor authentication failed")
    }

    /**
     * VK Authentication with getting cookies remixsid

     * @param captchaSid id of captcha
     * @param captchaKey response of captcha
     * @return token
     */
    fun vkLogin(captchaSid: String? = null, captchaKey: String? = null): String {
        log.info("Logging in...")

        var response = session.get("https://vk.com/")
            .userAgent(STRING_USER_AGENT)
            .send().readToText()

        val params = mutableMapOf<String, Any>(
            "act" to "login",
            "role" to "al_frame",
            "_origin" to "https://vk.com",
            "utf8" to 1,
            "email" to login,
            "pass" to password,
            "lg_h" to regexSearch(RE_LOGIN_HASH, response, 1)!!
        )

        if (captchaSid != null && captchaKey != null) {
            log.info("Using captcha code: $captchaSid: $captchaKey")

            params["captcha_sid"] = captchaSid
            params["captcha_key"] = captchaKey
        }

        response = session.post("https://login.vk.com/")
            .body(params)
            .send().readToText()

        when {
            response.contains("act=authcheck") -> {
                log.info("Two Factor is required")

                response = session.get("https://vk.com/login?act=authcheck").send().readToText()
                passTwoFactor(response)
            }

            response.contains("onLoginCaptcha(") -> {
                log.info("Captcha code is required")

                val sid = regexSearch(RE_CAPTCHA_ID, response)!!
                val captcha = Captcha(sid)

                val code = captchaHandler?.handle(captcha) ?: throw CaptchaException("Captcha listener is required")
                vkLogin(captchaSid, code)
            }

            response.contains("onLoginReCaptcha(") -> {
                log.info("Captcha code is required (recaptcha)")

                val sid = System.currentTimeMillis().toString()
                val captcha = Captcha(sid)

                val code = captchaHandler?.handle(captcha) ?: throw CaptchaException("Captcha listener is required")
                vkLogin(captchaSid, code)
            }

            response.contains("onLoginFailed(4") -> {
                throw NotValidAuthorization("Incorrect login or password")
            }

            else -> {
                if (checkSid()) log.info("Got remixsid")

                else throw AuthException("Unknown error")
            }
        }

        return apiLogin()
    }

    /**
     * Получение токена через Desktop приложение
     * @return token
     */
    private fun apiLogin(): String {
        for (cookie in listOf("p", "l")) {
            if (session.cookies.none { it.name() == cookie && it.domain() == "login.vk.com" }) {
                throw AuthException("API auth error (no login cookies)")
            }
        }

        val params = mapOf(
            "client_id" to appId,
            "scope" to scope,
            "response_type" to "token",
            "redirect_url" to redirectUrl
        )

        var response = session.get("https://oauth.vk.com/authorize")
            .userAgent(STRING_USER_AGENT)
            .params(params)
            .send()

        if (response.url().contains("act=blocked")) {
            throw BanException("Account is blocked")
        }

        if (!response.url().contains("access_token")) {
            val url = regexSearch(RE_TOKEN_URL, response.readToText(), 1)

            if (url != null) {
                response = session
                    .get(url)
                    .userAgent(STRING_USER_AGENT)
                    .send()
            }
        }

        when {
            response.url().contains("access_token") -> {
                val parts = response.url()
                    .split("#", limit = 2)[1]
                    .split("&").subList(0, 1)

                val token = parts[0].split("=")[1]

                log.info("Authentication succeeded!")

                return token
            }

            response.url().contains("oauth.vk.com/error") -> {
                val errorData = response.readToText().toJsonObject()

                var errorText = errorData.getString("error_description")!!

                if (errorText.contains("@vk.com")) {
                    errorText = errorData.getString("error")!!
                }

                throw AuthException("VK auth error: $errorText")
            }

            else -> throw AuthException("Unknown auth error")
        }
    }

    private fun checkSid(): Boolean {
        return session.cookies.any { it.name() == "remixsid" } || session.cookies.any { it.name() == "remixsid6" }
    }
}