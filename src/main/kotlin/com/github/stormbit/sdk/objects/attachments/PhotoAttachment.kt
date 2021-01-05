package com.github.stormbit.sdk.objects.attachments

import com.google.gson.annotations.SerializedName

data class PhotoAttachment(
    @SerializedName("access_key") val accessKey: String,
    @SerializedName("album_id") val albumId: Int,
    val date: Int,
    val has_tags: Boolean,
    val id: Int,
    @SerializedName("owner_id") val ownerId: Int,
    val sizes: List<Size>,
    val text: String
) : Attachment {
    data class Size(
        @SerializedName("height") val height: Int,
        @SerializedName("type") val type: String,
        @SerializedName("url") val url: String,
        @SerializedName("width") val width: Int
    ) : Attachment
}