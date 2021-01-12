package com.github.stormbit.sdk.vkapi.methods.photos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoUploadServerResponse(
    @SerialName("album_id") val albumId: Int? = null,
    @SerialName("upload_url") val uploadUrl: String? = null,
    @SerialName("user_id") val userId: Int? = null
)