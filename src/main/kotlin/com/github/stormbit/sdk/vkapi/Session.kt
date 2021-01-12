package com.github.stormbit.sdk.vkapi

import net.dongliu.requests.Cookie
import net.dongliu.requests.RequestBuilder
import net.dongliu.requests.Requests
import net.dongliu.requests.Session
import java.net.URL

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

    fun put(url: String): RequestBuilder {
        return this.session.put(url).cookies(cookies)
    }

    fun head(url: String): RequestBuilder {
        return this.session.head(url).cookies(cookies)
    }

    fun delete(url: String): RequestBuilder {
        return this.session.delete(url).cookies(cookies)
    }

    fun patch(url: String): RequestBuilder {
        return this.session.patch(url).cookies(cookies)
    }

    fun get(url: URL): RequestBuilder {
        return this.session.patch(url).cookies(cookies)
    }

    fun post(url: URL): RequestBuilder {
        return this.session.post(url).cookies(cookies)
    }

    fun put(url: URL): RequestBuilder {
        return this.session.put(url).cookies(cookies)
    }

    fun head(url: URL): RequestBuilder {
        return this.session.head(url).cookies(cookies)
    }

    fun delete(url: URL): RequestBuilder {
        return this.session.delete(url).cookies(cookies)
    }

    fun patch(url: URL): RequestBuilder {
        return this.session.patch(url).cookies(cookies)
    }

    fun sessionCookies(): MutableList<Cookie> {
        return this.session.currentCookies()
    }

    fun cookies(): MutableList<Cookie> {
        return cookies
    }
}