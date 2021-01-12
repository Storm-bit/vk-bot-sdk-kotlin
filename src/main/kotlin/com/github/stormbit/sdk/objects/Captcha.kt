package com.github.stormbit.sdk.objects

class Captcha(sid: String, url: String? = null) {
    var url: String = url ?: "https://api.vk.com/captcha.php?sid=$sid"
}