@file: Suppress("unused")
package com.github.stormbit.sdk.vkapi.methods.likes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IsLikedResponse(
    @SerialName("liked") val liked: Int? = null,
    @SerialName("copied") val copied: Int? = null
)

@Serializable
class LikesResponse(
    @SerialName("likes") val likes: Int? = null
)