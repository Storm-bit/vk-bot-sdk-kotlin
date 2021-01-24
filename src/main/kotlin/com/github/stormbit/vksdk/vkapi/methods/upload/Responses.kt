package com.github.stormbit.vksdk.vkapi.methods.upload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadDocumentResponse(
    @SerialName("file") val file: String)

@Serializable
data class UploadPhotoResponse(
    @SerialName("server") val server: Int,
    @SerialName("photo") val photo: String,
    @SerialName("hash") val hash: String)

@Serializable
data class UploadAlbumPhotoResponse(
    @SerialName("server") val server: Int,
    @SerialName("photos_list") val photo: String,
    @SerialName("aid") val albumId: Int,
    @SerialName("hash") val hash: String)

@Serializable
data class UploadMarketAlbumPhotoResponse(
    @SerialName("server") val server: Int,
    @SerialName("photo") val photo: String,
    @SerialName("gid") val groupId: Int,
    @SerialName("hash") val hash: String)

@Serializable
data class UploadMarketPhotoResponse(
    @SerialName("server") val server: Int,
    @SerialName("photo") val photo: String,
    @SerialName("hash") val hash: String,
    @SerialName("crop_data") val cropData: String? = null,
    @SerialName("crop_hash") val cropHash: String? = null)

@Serializable
data class UploadOwnerCoverPhotoResponse(
    @SerialName("photo") val photo: String,
    @SerialName("hash") val hash: String)

@Serializable
data class UploadOwnerPhotoResponse(
    @SerialName("server") val server: Int,
    @SerialName("photo") val photo: String,
    @SerialName("hash") val hash: String,
    @SerialName("mid") val moderatorId: Int,
    @SerialName("message_code") val messageCode: Int,
    @SerialName("profile_aid") val profileAlbumId: Int)

@Serializable
data class UploadVideoResponse(
    @SerialName("video_id") val videoId: Int,
    @SerialName("owner_id") val ownerId: Int,
    @SerialName("size") val size: Int,
    @SerialName("video_hash") val videoHash: String)