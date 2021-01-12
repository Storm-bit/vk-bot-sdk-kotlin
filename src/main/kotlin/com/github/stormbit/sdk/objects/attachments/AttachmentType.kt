package com.github.stormbit.sdk.objects.attachments

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
enum class AttachmentType(val value: String) {
    @SerialName("photo") PHOTO("photo"),
    @SerialName("video") VIDEO("video"),
    @SerialName("audio") AUDIO("audio"),
    @SerialName("doc") DOC("doc"),
    @SerialName("link") LINK("link"),
    @SerialName("market") MARKET("market"),
    @SerialName("wall") WALL("wall"),
    @SerialName("share") SHARE("share"),
    @SerialName("audio_message") VOICE("audio_message"),
    @SerialName("sticker") STICKER("sticker"),
    @SerialName("graffiti") GRAFFITI("graffiti")
}