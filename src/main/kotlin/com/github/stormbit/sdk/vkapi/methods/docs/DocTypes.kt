package com.github.stormbit.sdk.vkapi.methods.docs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class DocTypes(val type: String) {
    @SerialName("doc") DOC("doc"),
    @SerialName("audio_message") AUDIO_MESSAGE("audio_message"),
    @SerialName("graffiti") GRAFFITI("graffiti");
}