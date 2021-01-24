package com.github.stormbit.vksdk.objects.models.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
enum class DocTypes(val type: String) {
    @SerialName("doc") DOC("doc"),
    @SerialName("audio_message") AUDIO_MESSAGE("audio_message"),
    @SerialName("graffiti") GRAFFITI("graffiti");
}