package com.github.stormbit.sdk.clients

import com.github.stormbit.sdk.longpoll.LongPoll
import com.github.stormbit.sdk.vkapi.API
import com.github.stormbit.sdk.vkapi.Auth
import com.github.stormbit.sdk.vkapi.Executor

/**
 * User client, that contains important methods to work with users
 *
 * @param login    Login of your VK account
 * @param password Password of your VK account
 * @param twoFactorListener Listener for 2fa
 * @param captchaListener Listener for captcha
 */
class User(
    private val login: String,
    private val password: String,
    private val appId: Int = 6222115,
    private val scope: Int = 140488159,
    private val twoFactorListener: Auth.TwoFactorListener? = null,
    private val captchaListener: Auth.CaptchaListener? = null,
) : Client() {

    override fun auth() {
        this.auth = Auth(login, password, appId, scope, twoFactorListener, captchaListener)
        this.token = this.auth.vkLogin()

        this.api = API(this, Executor(this))
        this.id = getClientId()
        this.longPoll = LongPoll(this)

        log.info("Client object created with id $id")
    }
}