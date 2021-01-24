package com.github.stormbit.vksdk.clients

import com.github.stormbit.vksdk.vkapi.Auth
import com.github.stormbit.vksdk.vkapi.VkUserPermissions
import io.ktor.client.*

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
    httpClient: HttpClient
) : Client(httpClient) {

    override suspend fun auth() {
        val  auth = Auth(login, password, appId, scope, twoFactorHandler, captchaHandler)
        this.token = auth.vkLogin()

        super.auth()

        this.id = getClientId()

        log.info("Client object created with id $id")
    }
}