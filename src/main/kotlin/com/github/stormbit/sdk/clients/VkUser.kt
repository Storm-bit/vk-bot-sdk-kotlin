package com.github.stormbit.sdk.clients

import com.github.stormbit.sdk.longpoll.LongPoll
import com.github.stormbit.sdk.vkapi.Auth
import com.github.stormbit.sdk.vkapi.API
import com.github.stormbit.sdk.vkapi.Executor
import com.github.stormbit.sdk.vkapi.methods.audio.AudioApi
import com.github.stormbit.sdk.vkapi.methods.audio.AudioApiAsync

/**
 * Login via official VK app
 *
 * @param login    Login of your VK account
 * @param password Password of your VK account
 * @param twoFactorListener Listener for 2fa
 * @param captchaListener Listener for captcha
 */
class VkUser(
    private val login: String,
    private val password: String,
    private val twoFactorListener: Auth.TwoFactorListener? = null,
    private val captchaListener: Auth.CaptchaListener? = null
) : Client() {

    companion object {
        internal const val BASE_PROXY_OAUTH_URL = "https://vk-oauth-proxy.xtrafrancyz.net/"
        internal const val CLIENT_ID = 2274003
        internal const val CLIENT_SECRET = "hHbZxrka2uZ6jB1inYsH"
        internal val HEADER = mapOf("user-agent" to "VKAndroidApp/5.40-3904")
        internal const val RECEIPT = "JSv5FBbXbY:APA91bF2K9B0eh61f2WaTZvm62GOHon3-vElmVq54ZOL5PHpFkIc85WQUxUH_wae8YEUKkEzLCcUC5V4bTWNNPbjTxgZRvQ-PLONDMZWo_6hwiqhlMM7gIZHM2K2KhvX-9oCcyD1ERw4"
    }

    val audio = AudioApi(this)
    val audioAsync = AudioApiAsync(this)

    override fun auth() {
        this.auth = Auth(login, password, twoFactorListener = twoFactorListener, captchaListener = captchaListener)
        this.token = this.auth.authByVk()

        this.api = API(this, Executor(this))
        this.id = getClientId()
        this.longPoll = LongPoll(this)

        this.values = mapOf("receipt" to RECEIPT)
        this.headers = HEADER

        log.info("Client object created with id $id")
    }
}