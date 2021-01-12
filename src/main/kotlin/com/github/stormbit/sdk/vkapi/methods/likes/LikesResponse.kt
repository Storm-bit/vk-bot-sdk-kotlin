package com.github.stormbit.sdk.vkapi.methods.likes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LikesResponse(
    @SerialName("likes") val likes: Int? = null
)