package com.github.stormbit.vksdk.clients

import com.github.stormbit.vksdk.vkapi.Auth
import com.github.stormbit.vksdk.vkapi.methods.audio.AudioApi
import io.ktor.client.*
import io.ktor.http.*

/**
 * Login via official VK app
 *
 * @param login    Login of your VK account
 * @param password Password of your VK account
 * @param twoFactorHandler Handler for 2fa
 * @param captchaHandler Handler for captcha
 */
class VkUserClient(
    private val login: String,
    private val password: String,
    private val twoFactorHandler: TwoFactorHandler? = null,
    private val captchaHandler: CaptchaHandler? = null,
    httpClient: HttpClient
) : Client(httpClient) {

    companion object {
        internal const val BASE_PROXY_OAUTH_URL = "https://vk-oauth-proxy.xtrafrancyz.net/"
        internal const val CLIENT_ID = 2274003
        internal const val CLIENT_SECRET = "hHbZxrka2uZ6jB1inYsH"
        internal val HEADER = mapOf("user-agent" to "VKAndroidApp/5.40-3904")
        internal const val RECEIPT = "JSv5FBbXbY:APA91bF2K9B0eh61f2WaTZvm62GOHon3-vElmVq54ZOL5PHpFkIc85WQUxUH_wae8YEUKkEzLCcUC5V4bTWNNPbjTxgZRvQ-PLONDMZWo_6hwiqhlMM7gIZHM2K2KhvX-9oCcyD1ERw4"
    }

    val audio = AudioApi(this)

    override suspend fun auth() {
        val auth = Auth(login, password, twoFactorHandler = twoFactorHandler, captchaHandler = captchaHandler)
        this.token = auth.authByVk()

        super.auth()

        this.id = getClientId()

        this.values = parametersOf("receipt", RECEIPT)
        this.headers = headersOf("user-agent", "VKAndroidApp/5.40-3904")

        log.info("Client object created with id $id")
    }
}