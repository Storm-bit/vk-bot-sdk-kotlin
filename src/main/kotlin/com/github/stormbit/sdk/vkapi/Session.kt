package com.github.stormbit.sdk.vkapi

import net.dongliu.requests.Cookie
import net.dongliu.requests.RequestBuilder
import net.dongliu.requests.Requests
import net.dongliu.requests.Session

@Suppress("unused")
class Session {
    private val session: Session = Requests.session()
    private var cookies: MutableList<Cookie> = this.session.currentCookies()

    constructor()

    constructor(cookies: MutableList<Cookie>) {
        this.cookies = cookies
    }

    fun get(url: String): RequestBuilder {
        return this.session.get(url).cookies(cookies)
    }

    fun post(url: String): RequestBuilder {
        return this.session.post(url).cookies(cookies)
    }

    fun sessionCookies(): MutableList<Cookie> {
        return this.session.currentCookies()
    }
}