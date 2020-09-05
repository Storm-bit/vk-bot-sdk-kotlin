package com.github.stormbit.sdk.utils.vkapi

import com.github.stormbit.sdk.exceptions.NotValidAuthorization
import com.github.stormbit.sdk.utils.Utils
import net.dongliu.requests.Header
import net.dongliu.requests.Requests
import net.dongliu.requests.Session
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.FormElement
import java.util.function.BiConsumer
import java.util.function.BinaryOperator
import java.util.function.Supplier
import java.util.stream.Collector

class Auth {
    private var login: String? = null
    private var password: String? = null

    private val STRING_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36"
    private val USER_AGENT = Header("User-Agent", STRING_USER_AGENT)
    private val AUTH_HASH = Regex("\\{.*?act: 'a_authcheck_code'.+?hash: '([a-z_0-9]+)'.*?}")
    private val FORM_ID = "quick_login_form"

    val session: Session = Requests.session()
    var listener: Listener? = null

    constructor(login: String, password: String) {
        this.login = login
        this.password = password
    }

    constructor(login: String, password: String, listener: Listener) {
        this.login = login
        this.password = password
        this.listener = listener
    }

    constructor()

    fun auth() {
        if (login == null || password == null) return

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
    }

    private fun _pass_twofactor(response: String) {
        val pair = listener?.twoFactor()

        val authHash = Utils.regexSearch(AUTH_HASH, response, 1)
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

    private fun <K, V> HashMap<K, V>.putIfAbsent(key: K, value: V): V? {
        var v: V? = get(key)

        if (v == null) {
            v = put(key, value)
        }

        return v
    }

    interface Listener {
        fun twoFactor(): Pair<String, Boolean>
    }
}