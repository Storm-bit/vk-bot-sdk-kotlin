package com.github.stormbit.sdk.vkapi.methods.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LinkResponse(
    @SerialName("link") val link: String? = null
)