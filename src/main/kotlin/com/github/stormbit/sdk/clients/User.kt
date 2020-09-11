package com.github.stormbit.sdk.clients

import com.github.stormbit.sdk.utils.vkapi.Auth

/**
 * User client, that contains important methods to work with users
 *
 * Not need now to put methods there: use API.call
 */
class User : Client {
    /**
     * Constructor
     *
     * @param login    Login of your VK bot
     * @param password Password of your VK bot
     * @param twoFactorListener Listener for 2fa
     * @param captchaListener Listener for captcha
     */
    constructor(login: String, password: String, saveCookie: Boolean = false, loadFromCookie: Boolean = false, twoFactorListener: Auth.TwoFactorListener? = null, captchaListener: Auth.CaptchaListener? = null) : super(login, password, saveCookie, loadFromCookie, twoFactorListener,  captchaListener)
}