package com.github.stormbit.sdk.vkapi.methods.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoSaveResponse(
    @SerialName("access_key") val accessKey: String,
    @SerialName("description") val description: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("owner_id") val ownerId: Int,
    @SerialName("upload_url") val uploadUrl: String,
    @SerialName("vid") val vid: Int? = null
)