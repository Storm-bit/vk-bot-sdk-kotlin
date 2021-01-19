package com.github.stormbit.sdk.clients

import com.github.stormbit.sdk.objects.Captcha

fun interface TwoFactorHandler {
    fun handle(): Pair<String, Boolean>?
}

fun interface CaptchaHandler {
    fun handle(captcha: Captcha): String?
}