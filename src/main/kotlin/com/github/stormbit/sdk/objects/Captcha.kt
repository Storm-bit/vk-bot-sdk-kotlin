package com.github.stormbit.sdk.objects

import com.github.stormbit.sdk.utils.vkapi.Session

@Suppress("unused")
class Captcha(private val http: Session, captcha_sid: String, url: String? = null, private var image: String? = null, private val kwargs: HashMap<String, String> = HashMap(), private val func: (parameters: Map<String, String>) -> Unit) {
    private val sid: String = captcha_sid
    private var url: String
    private var key: String? = null

    init {
        this.url = url ?: "https://api.vk.com/captcha.php?sid=$sid"
    }

    fun getUrl(): String {
        return this.url
    }

    fun getImage(): String? {
        if (this.image == null) {
            this.image = http.get(getUrl()).send().readToText()
        }

        return this.image
    }

    fun tryAgain(key: String? = null) {
        if (key != null) {
            this.key = key

            this.kwargs["captcha_sid"] = this.sid
            this.kwargs["captcha_key"] = this.key!!
        }

        return this.func(kwargs)
    }
}