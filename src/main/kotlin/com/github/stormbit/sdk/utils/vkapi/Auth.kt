package com.github.stormbit.sdk.utils.vkapi

import com.github.stormbit.sdk.exceptions.CaptchaException
import com.github.stormbit.sdk.exceptions.NotValidAuthorization
import com.github.stormbit.sdk.exceptions.TwoFactorException
import com.github.stormbit.sdk.objects.Captcha
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.utils.Utils.Companion.AUTH_HASH
import com.github.stormbit.sdk.utils.Utils.Companion.RE_CAPTCHAID
import com.github.stormbit.sdk.utils.Utils.Companion.toJsonObject
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.dongliu.requests.Cookie
import net.dongliu.requests.Header
import net.dongliu.requests.Requests
import org.jsoup.Jsoup
import org.jsoup.nodes.FormElement
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class Auth {
    private val log: Logger = LoggerFactory.getLogger(Auth::class.java)
    private var login: String? = null
    private var password: String? = null

    private var isSaveCookie: Boolean = false
    private var isLoadFromCookie: Boolean = false
    private val cookiesMap = HashMap<String, Any>()
    private val cookiesFile = File("cookies.json")

    companion object {
        private const val HOST_API = "api.vk.com"
        internal const val TIME_OUT = 10000
        internal val HEADER = mapOf("user-agent" to "VKAndroidApp/5.40-3904")
        internal const val BASE_API_URL = "https://$HOST_API/method/"
        internal const val RECEIPT = "JSv5FBbXbY:APA91bF2K9B0eh61f2WaTZvm62GOHon3-vElmVq54ZOL5PHpFkIc85WQUxUH_wae8YEUKkEzLCcUC5V4bTWNNPbjTxgZRvQ-PLONDMZWo_6hwiqhlMM7gIZHM2K2KhvX-9oCcyD1ERw4"
        internal const val STRING_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36"

        private val USER_AGENT = Header("User-Agent", STRING_USER_AGENT)
        private const val FORM_ID = "quick_login_form"
        private const val HOST_OAUTH_PROXY = "vk-oauth-proxy.xtrafrancyz.net"
        private const val BASE_PROXY_OAUTH_URL = "https://$HOST_OAUTH_PROXY/"
        private val client_keys = listOf(
            listOf(2274003, "hHbZxrka2uZ6jB1inYsH")
        )
    }

    var session: Session = Session()
    private var twoFactorListener: TwoFactorListener? = null
    private var captchaListener: CaptchaListener? = null

    constructor(login: String, password: String, saveCookie: Boolean = false, loadFromCookie: Boolean = false, twoFactorListener: TwoFactorListener? = null, captchaListener: CaptchaListener? = null) {
        this.login = login
        this.password = password
        this.isSaveCookie = saveCookie
        this.isLoadFromCookie = loadFromCookie
        this.twoFactorListener = twoFactorListener
        this.captchaListener = captchaListener
    }

    constructor(login: String, password: String, twoFactorListener: TwoFactorListener? = null, captchaListener: CaptchaListener? = null) {
        this.login = login
        this.password = password
        this.twoFactorListener = twoFactorListener
        this.captchaListener = captchaListener
    }

    constructor()

    fun auth(parameters: Map<String, String> = HashMap()) {
        if (login == null || password == null) return

        if (isLoadFromCookie && cookiesFile.exists()) {
            val json = toJsonObject(cookiesFile.readText())

            val cookieList = json.getAsJsonArray("cookies").map {
                (it as JsonObject)
                Cookie(it.getString("domain"), it.getString("path"), it.getString("name"), it.getString("value"), it.getLong("expiry"), it.getBoolean("secure"), it.getBoolean("hostOnly"))
            }.toTypedArray()

            session = Session(cookieList.toMutableList())
            return
        }

        val data = session.get("https://vk.com/")
            .headers(USER_AGENT)
            .send().readToText()

        val form = Jsoup.parse(data).getElementById(FORM_ID) as FormElement

        setData(form, login!!, password!!)

        val formData = form.formData()

        val params = formData.map { it.key() to it.value() }.toMap() as HashMap<String, String>

        params.putAll(parameters)

        var response = session.post(form.attr("action"))
            .headers(USER_AGENT)
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

                val captchaSid = Utils.regexSearch(RE_CAPTCHAID, response)
                val captcha = Captcha(session, captchaSid!!)

                val code = captchaListener?.onCaptcha(captcha) ?: throw CaptchaException("Captcha listener is required")
                auth(mapOf(
                    "captcha_sid" to captchaSid,
                    "captcha_key" to code
                ))
            }
            response.contains("onLoginReCaptcha(") -> {
                log.info("Captcha code is required (recaptcha)")

                val captchaSid = System.currentTimeMillis().toString()
                val captcha = Captcha(session, captchaSid)

                val code = captchaListener?.onCaptcha(captcha) ?: throw CaptchaException("Captcha listener is required")
                auth(mapOf(
                    "captcha_sid" to captchaSid,
                    "captcha_key" to code
                ))
            }
            response.contains("onLoginFailed(4") -> throw NotValidAuthorization("Incorrect login or password")
        }

        if (isSaveCookie) {
            if (!cookiesFile.exists()) cookiesFile.createNewFile()

            cookiesMap["cookies"] = session.sessionCookies()
            cookiesFile.writeText(toJsonObject(cookiesMap).toString())
        }
    }

    fun authViaVK(): String {
        val response = oauth()
        val accessToken = response.getString("access_token")

        val response2 = refreshToken(accessToken).getAsJsonObject("response")

        return response2.getString("token")
    }

    private fun oauth(parameters: Map<String, Any> = HashMap()): JsonObject {

        val prms = hashMapOf(
            "grant_type" to "password",
            "client_id" to client_keys[0][0],
            "client_secret" to client_keys[0][1],
            "username" to login,
            "password" to password,
            "v" to Utils.VK_API_VERSION,
            "2fa_supported" to "1",
        )

        prms.putAll(parameters)

        val responseString = Requests.get(BASE_PROXY_OAUTH_URL + "token")
            .body(prms)
            .headers(HEADER)
            .timeout(TIME_OUT)
            .send().readToText()

        val response = JsonParser.parseString(responseString).asJsonObject

        if ("error" in response.toString()) {
            if ("need_captcha" == response["error"].asString) {
                log.info("Captcha code is required")

                val captchaSid = response.getString("captcha_sid")
                val captcha = Captcha(session, captchaSid)

                val code = captchaListener?.onCaptcha(captcha) ?: throw CaptchaException("Captcha listener is required")
                return oauth(mapOf(
                    "captcha_sid" to captchaSid,
                    "captcha_key" to code
                ))
            } else if ("need_validation" == response["error"].asString) {
                if ("2fa_sms" in response.toString()) {
                    val pair = twoFactorListener?.twoFactor() ?: throw TwoFactorException("Two Factor listener is not initialized")

                    return oauth(mapOf(
                        "code" to pair.first,
                        "remember" to pair.second.toInt()
                    ))
                } else if ("ban_info" in response.toString()) {
                    // TODO
                }
            } else if ("invalid_request" == response["error"].asString) {
                if ("wrong_otp" in response.toString()) {
                    val pair = twoFactorListener?.twoFactor() ?: throw TwoFactorException("Two Factor listener is not initialized")

                    return oauth(mapOf(
                        "code" to pair.first,
                        "remember" to pair.second.toInt()
                    ))
                }
            } else {
                throw Exception("Error: ${response["error_description"]}")
            }
        }

        return response
    }

    private fun refreshToken(access_token: String): JsonObject {
        val params = mapOf(
            "access_token" to access_token,
            "receipt" to RECEIPT,
            "v" to Utils.VK_API_VERSION
        )

        val response = JsonParser.parseString(Requests.get(BASE_API_URL + "auth.refreshToken")
            .body(params)
            .headers(HEADER)
            .timeout(TIME_OUT)
            .send().readToText()).asJsonObject

        if ("error" in response.toString()) {
            throw Exception("Error: ${response["error_description"]}")
        }

        return response
    }

    private fun passTwoFactor(response: String): String {
        val pair = twoFactorListener?.twoFactor() ?: throw TwoFactorException("Two Factor listener is not initialized")

        val authHash = Utils.regexSearch(AUTH_HASH, response, 1)!!

        val values = HashMap<String, Any>()
        values["act"] = "a_authcheck_code"
        values["al"] = "1"
        values["code"] = pair.first
        values["remember"] = if (pair.second) 1 else 0
        values["hash"] = authHash

        val resp = session.post("https://vk.com/al_login.php")
            .headers(USER_AGENT)
            .body(values)
            .send().readToText()

        val data = toJsonObject(resp.replace("[<!>-]".toRegex(), ""))
        val status = data.getAsJsonArray("payload").getInt(0)

        when {
            status == 4 -> {
                val path = data.getAsJsonArray("payload").getJsonArray(1).getString(0).replace("[\\\\\"]".toRegex(), "")
                return session.get("https://vk.com/$path").send().readToText()
            }

            listOf(0, 8).contains(status) -> return passTwoFactor(response)

            status == 2 -> throw TwoFactorException("ReCaptcha required")
        }

        throw TwoFactorException("Two factor authentication failed")
    }

    private fun setData(form: FormElement, login: String, password: String) {
        val loginField = form.select("[name=email]").first()
        val passwordField = form.select("[name=pass]").first()

        loginField.`val`(login)
        passwordField.`val`(password)

        form.submit()
            .userAgent(STRING_USER_AGENT)
            .execute()
    }

    fun interface TwoFactorListener {
        fun twoFactor(): Pair<String, Boolean>
    }

    fun interface CaptchaListener {
        fun onCaptcha(captcha: Captcha): String
    }
}