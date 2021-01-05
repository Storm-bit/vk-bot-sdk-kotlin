package com.github.stormbit.sdk.objects.attachments

import com.google.gson.annotations.SerializedName

data class PhotoAttachment(
    @SerializedName("access_key") val accessKey: String,
    @SerializedName("album_id") val albumId: Int,
    @SerializedName("date") val date: Int,
    @SerializedName("has_tags") val hasTags: Boolean,
    @SerializedName("id") val id: Int,
    @SerializedName("owner_id") val ownerId: Int,
    @SerializedName("sizes") val sizes: List<Size>,
    @SerializedName("text") val text: String
) : Attachment {
    data class Size(
        @SerializedName("height") val height: Int,
        @SerializedName("type") val type: String,
        @SerializedName("url") val url: String,
        @SerializedName("width") val width: Int
    )
}