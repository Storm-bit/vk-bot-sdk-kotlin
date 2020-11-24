package com.github.stormbit.sdk.clients

import com.github.stormbit.sdk.utils.vkapi.Auth
/**
 * Login via official VK app
 */
class VkUser(login: String, password: String, twoFactorListener: Auth.TwoFactorListener? = null, captchaListener: Auth.CaptchaListener? = null) : Client(login, password, twoFactorListener, captchaListener)