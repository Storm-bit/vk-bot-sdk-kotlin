package com.github.stormbit.vksdk.clients

import com.github.stormbit.vksdk.objects.Captcha

fun interface TwoFactorHandler {
    fun handle(): Pair<String, Boolean>?
}

fun interface CaptchaHandler {
    fun handle(captcha: Captcha): String?
}