package com.github.stormbit.sdk.utils.vkapi.methods

import java.io.Serializable

enum class AttachmentType(val value: String) : Serializable {
    PHOTO("photo"),
    VIDEO("video"),
    AUDIO("audio"),
    DOC("doc"),
    LINK("link"),
    MARKET("market"),
    WALL("wall"),
    SHARE("share")
}