package com.github.stormbit.vksdk.vkapi

import net.dongliu.requests.Cookie
import net.dongliu.requests.RequestBuilder
import net.dongliu.requests.Requests

class Session {
    private val session = Requests.session()
    val cookies: List<Cookie> get() = this.session.currentCookies()

    fun get(url: String): RequestBuilder {
        return session.get(url).cookies(cookies)
    }

    fun post(url: String): RequestBuilder {
        return session.post(url).cookies(cookies)
    }
}