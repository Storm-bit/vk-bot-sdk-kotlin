package com.github.stormbit.sdk.objects.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LongPollServerResponse(
    @SerialName("key") val key: String? = null,
    @SerialName("pts") val pts: Int? = null,
    @SerialName("server") val server: String? = null,
    @SerialName("ts") val ts: Int? = null
)