@file: Suppress("unused")
package com.github.stormbit.sdk.vkapi.methods.photos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoUploadServerResponse(
    @SerialName("album_id") val albumId: Int,
    @SerialName("upload_url") val uploadUrl: String,
    @SerialName("user_id") val userId: Int
)

@Serializable
data class OwnerCoverPhotoResponse(
    @SerialName("url") val url: String,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int
)

@Serializable
data class OwnerPhotoResponse(
    @SerialName("photo_hash") val photoHash: String,
    @SerialName("photo_src") val photoSrc: String,
    @SerialName("photo_src_big") val photoSrcBig: String,
    @SerialName("photo_src_small") val photoSrcSmall: String,
    @SerialName("saved") val isSaved: Int,
    @SerialName("post_id") val postId: String,
)