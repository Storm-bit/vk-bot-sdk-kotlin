package com.github.stormbit.sdk.vkapi

import com.github.stormbit.sdk.clients.VkUser
import com.github.stormbit.sdk.exceptions.*
import com.github.stormbit.sdk.objects.Captcha
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.utils.Utils.Companion.regexSearch
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

class Auth {
    private val log: Logger = LoggerFactory.getLogger(Auth::class.java)
    private lateinit var login: String
    private lateinit var password: String

    private var appId: Int = 6222115
    private var scope: Int = DEFAULT_USER_SCOPES
    private var redirectUrl: String = ""

    companion object {
        private const val STRING_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36"
        private val USER_AGENT = Header("User-Agent", STRING_USER_AGENT)
        private val DEFAULT_USER_SCOPES = VkUserPermissions.values().sumBy { it.value }
    }

    var session: Session = Session()
    private var twoFactorListener: TwoFactorListener? = null
    private var captchaListener: CaptchaListener? = null

    constructor(login: String, password: String, appId: Int = 6222115, scope: Int = DEFAULT_USER_SCOPES, twoFactorListener: TwoFactorListener? = null, captchaListener: CaptchaListener? = null, redirectUrl: String = "") {
        this.login = login
        this.password = password
        this.appId = appId
        this.scope = scope
        this.twoFactorListener = twoFactorListener
        this.captchaListener = captchaListener
        this.redirectUrl = redirectUrl
    }

    constructor()

    fun authByVk(): String {
        val response = oauth()
        val accessToken = response.getString("access_token")!!

        val response2 = refreshToken(accessToken)["response"]!!.jsonObject

        return response2.getString("token")!!
    }

    private fun oauth(parameters: Map<String, Any> = HashMap()): JsonObject {
        log.info("Logging in...")

        val prms = hashMapOf<String, Any>(
            "grant_type" to "password",
            "client_id" to VkUser.CLIENT_ID,
            "client_secret" to VkUser.CLIENT_SECRET,
            "username" to login,
            "password" to password,
            "v" to VK_API_VERSION,
            "2fa_supported" to 1,
        )

        prms.putAll(parameters)

        val responseString = Requests.get(VkUser.BASE_PROXY_OAUTH_URL + "token")
            .body(prms)
            .headers(VkUser.HEADER)
            .timeout(TIME_OUT)
            .send().readToText()

        val response = json.parseToJsonElement(responseString).jsonObject

        if ("error" in response.toString()) {
            if ("need_captcha" == response.getString("error")) {
                log.info("Captcha code is required")

                val captchaSid = response.getString("captcha_sid")!!
                val captcha = Captcha(captchaSid)

                val code = captchaListener?.onCaptcha(captcha) ?: throw CaptchaException("Captcha listener is required")
                return oauth(mapOf(
                    "captcha_sid" to captchaSid,
                    "captcha_key" to code
                ))
            } else if ("need_validation" == response.getString("error")) {
                if ("2fa_sms" in response.toString()) {
                    val (code, rememberDevice) = twoFactorListener?.twoFactor() ?: throw TwoFactorException("Two Factor listener is not initialized")

                    return oauth(mapOf(
                        "code" to code,
                        "remember" to rememberDevice.toInt()
                    ))
                } else if ("ban_info" in response.toString()) {
                    throw BanException(response.getString("error_description"))
                }
            } else if ("invalid_request" == response.getString("error")) {
                if ("wrong_otp" in response.toString()) {
                    val (code, rememberDevice) = twoFactorListener?.twoFactor() ?: throw TwoFactorException("Two Factor listener is not initialized")

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

    private fun refreshToken(access_token: String): JsonObject {
        val params = mapOf(
            "access_token" to access_token,
            "receipt" to VkUser.RECEIPT,
            "v" to VK_API_VERSION
        )

        val response = json.parseToJsonElement(Requests.get(BASE_API_URL + "auth.refreshToken")
            .body(params)
            .headers(VkUser.HEADER)
            .timeout(TIME_OUT)
            .send().readToText()).jsonObject

        if ("error" in response.toString()) {
            throw AuthException("Error: ${response["error_description"]}")
        }

        return response
    }

    private fun passTwoFactor(response: String): String {
        val (code, rememberDevice) = twoFactorListener?.twoFactor() ?: throw TwoFactorException("Two Factor listener is not initialized")

        val authHash = regexSearch(RE_AUTH_HASH, response, 1)!!

        val values = HashMap<String, Any>()

        values["act"] = "a_authcheck_code"
        values["al"] = "1"
        values["code"] = code
        values["remember"] = rememberDevice.toInt()
        values["hash"] = authHash

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

    fun interface TwoFactorListener {
        fun twoFactor(): Pair<String, Boolean>
    }

    fun interface CaptchaListener {
        fun onCaptcha(captcha: Captcha): String
    }

    /**
     * Авторизация ВКонтакте с получением cookies remixsid

     * @param captcha_sid id капчи
     * @param captcha_key ответ капчи
     * @return token
     */
    fun vkLogin(captcha_sid: String? = null, captcha_key: String? = null): String {
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

        if (captcha_sid != null && captcha_key != null) {
            log.info("Using captcha code: $captcha_sid: $captcha_key")

            params["captcha_sid"] = captcha_sid
            params["captcha_key"] = captcha_key
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

                val captchaSid = regexSearch(RE_CAPTCHA_ID, response)!!
                val captcha = Captcha(captchaSid)

                val code = captchaListener?.onCaptcha(captcha) ?: throw CaptchaException("Captcha listener is required")
                vkLogin(captchaSid, code)
            }

            response.contains("onLoginReCaptcha(") -> {
                log.info("Captcha code is required (recaptcha)")

                val captchaSid = System.currentTimeMillis().toString()
                val captcha = Captcha(captchaSid)

                val code = captchaListener?.onCaptcha(captcha) ?: throw CaptchaException("Captcha listener is required")
                vkLogin(captchaSid, code)
            }

            response.contains("onLoginFailed(4") -> {
                throw NotValidAuthorization("Incorrect login or password")
            }

            else -> {
                if (checkSid()) log.info("Got remixsid")

                else throw AuthException("Unknown error.")
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
            if (session.sessionCookies().none { it.name() == cookie && it.domain() == "login.vk.com" }) {
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

            else -> throw AuthException("Unknown api auth error")
        }
    }

    private fun checkSid(): Boolean {
        return session.sessionCookies().any { it.name() == "remixsid" } || session.sessionCookies().any { it.name() == "remixsid6" }
    }
}