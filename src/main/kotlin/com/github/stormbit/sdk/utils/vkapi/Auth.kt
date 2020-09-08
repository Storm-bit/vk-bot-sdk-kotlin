package com.github.stormbit.sdk.utils.vkapi

import com.github.stormbit.sdk.exceptions.NotValidAuthorization
import com.github.stormbit.sdk.exceptions.TwoFactorException
import com.github.stormbit.sdk.utils.Utils
import net.dongliu.requests.Cookie
import net.dongliu.requests.Header
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.FormElement
import java.io.File
import java.util.stream.Collector

class Auth {
    private var login: String? = null
    private var password: String? = null

    private var isSaveCookie: Boolean = false
    private var isLoadFromCookie: Boolean = false
    private val cookiesMap = HashMap<String, Any>()
    private val cookiesFile = File("cookies.json")

    private val STRING_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36"
    private val USER_AGENT = Header("User-Agent", STRING_USER_AGENT)
    private val AUTH_HASH = Regex("\\{.*?act: 'a_authcheck_code'.+?hash: '([a-z_0-9]+)'.*?}")
    private val FORM_ID = "quick_login_form"

    var session: Session = Session()
    var listener: Listener? = null

    constructor(login: String, password: String, saveCookie: Boolean = false, loadFromCookie: Boolean = false) {
        this.login = login
        this.password = password
        this.isSaveCookie = saveCookie
        this.isLoadFromCookie = loadFromCookie
    }

    constructor(login: String, password: String, saveCookie: Boolean = false, loadFromCookie: Boolean = false, listener: Listener) {
        this.login = login
        this.password = password
        this.isSaveCookie = saveCookie
        this.isLoadFromCookie = loadFromCookie
        this.listener = listener
    }

    constructor()

    fun auth() {
        if (login == null || password == null) return

        if (isLoadFromCookie && cookiesFile.exists()) {
            val json = JSONObject(cookiesFile.readText())
            val cookieList = json.getJSONArray("cookies").map {
                (it as JSONObject)
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

        val eloquentCollector = Collector.of({ HashMap() }, { map: HashMap<String, String>, e: Connection.KeyVal -> putUnique(map, e.key(), e.value()) },
                { m1: HashMap<String, String>, m2: HashMap<String, String> ->
                    m2.forEach { (k: String, v: String) -> putUnique(m1, k, v) }
                    m1
                })

        val params = formData.stream().collect(eloquentCollector)

        var response = session.post(form.attr("action"))
                .headers(USER_AGENT)
                .body(params)
                .send().readToText()

        if (response.contains("act=authcheck")) {
            response = session.get("https://vk.com/login?act=authcheck").send().readToText()
            _pass_twofactor(response)
        } else if (!response.contains("onLoginDone")) {
            throw NotValidAuthorization("Incorrect login or password")
        }

        if (isSaveCookie) {
            if (!cookiesFile.exists()) cookiesFile.createNewFile()

            cookiesMap["cookies"] = session.sessionCookies()
            cookiesFile.writeText(JSONObject(cookiesMap).toString(4))
        }
    }

    private fun _pass_twofactor(response: String): String {
        val pair = listener?.twoFactor()

        val authHash = Utils.regexSearch(AUTH_HASH, response, 1)!!

        val values = HashMap<String, Any>()
        values["act"] = "a_authcheck_code"
        values["al"] = "1"
        values["code"] = pair!!.first
        values["remember"] = if (pair.second) 1 else 0
        values["hash"] = authHash

        val resp = session.post("https://vk.com/al_login.php")
                .headers(USER_AGENT)
                .body(values)
                .send().readToText()

        val data = JSONObject(resp.replace("[<!>-]".toRegex(), ""))
        val status = data.getJSONArray("payload").getInt(0)

        when {
            status == 4 -> {
                val path = data.getJSONArray("payload").getJSONArray(1).getString(0).replace("[\\\\\"]".toRegex(), "")
                return session.get("https://vk.com/$path").send().readToText()
            }
            listOf(0, 4).contains(status) -> {
                return _pass_twofactor(response)
            }
            status == 2 -> {
                throw TwoFactorException("ReCaptcha required")
            }
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

    private fun <K, V> putUnique(map: HashMap<K, V>, key: K, value: V) {
        val value2: V? = map.putIfAbsent(key, value)

        if (value2 != null) throw IllegalStateException("Duplicate key '$key' (attempted merging incoming value '$value' with existing '$value2')")
    }

    interface Listener {
        fun twoFactor(): Pair<String, Boolean>
    }
}