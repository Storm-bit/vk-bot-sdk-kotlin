package com.github.stormbit.sdk.objects

import com.github.stormbit.sdk.utils.vkapi.Session

@Suppress("unused", "CanBeParameter")
class Captcha(private val http: Session, val sid: String, url: String? = null, private var image: String? = null, private val kwargs: HashMap<String, String> = HashMap()) {
    var url: String = url ?: "https://api.vk.com/captcha.php?sid=$sid"
}