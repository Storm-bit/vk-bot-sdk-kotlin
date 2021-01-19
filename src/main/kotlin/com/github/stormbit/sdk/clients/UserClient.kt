package com.github.stormbit.sdk.clients

import com.github.stormbit.sdk.vkapi.Auth
import com.github.stormbit.sdk.vkapi.VkUserPermissions

/**
 * User client, that contains important methods to work with users
 *
 * @param login    Login of your VK account
 * @param password Password of your VK account
 * @param twoFactorHandler Handler for 2fa
 * @param captchaHandler Handler for captcha
 */
class UserClient(
    private val login: String,
    private val password: String,
    private val appId: Int = 6222115,
    private val scope: VkUserPermissions = VkUserPermissions().apply { allPermissions = true },
    private val twoFactorHandler: TwoFactorHandler? = null,
    private val captchaHandler: CaptchaHandler? = null,
) : Client() {

    override suspend fun auth() {
        this.auth = Auth(login, password, appId, scope, twoFactorHandler, captchaHandler)
        this.token = this.auth.vkLogin()

        super.auth()

        this.id = getClientId()

        log.info("Client object created with id $id")
    }
}