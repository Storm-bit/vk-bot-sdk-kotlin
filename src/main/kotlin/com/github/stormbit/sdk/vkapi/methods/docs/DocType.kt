package com.github.stormbit.sdk.vkapi.methods.docs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocType(
    @SerialName("count") val count: Int,
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)