package com.github.stormbit.sdk.objects.attachments

import com.google.gson.annotations.SerializedName

data class VideoAttachment(
    @SerializedName("access_key") val accessKey: String,
    @SerializedName("can_add") val canAdd: Int,
    @SerializedName("comments") val comments: Int,
    @SerializedName("date") val date: Int,
    @SerializedName("description") val description: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("first_frame") val firstFrame: List<FirstFrame>,
    @SerializedName("height") val height: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("image") val image: List<Image>,
    @SerializedName("is_favorite") val isFavorite: Boolean,
    @SerializedName("owner_id") val ownerId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("track_code") val trackCode: String,
    @SerializedName("type") val type: String,
    @SerializedName("views") val views: Int,
    @SerializedName("width") val width: Int
) : Attachment {
    data class FirstFrame(
        @SerializedName("height") val height: Int,
        @SerializedName("url") val url: String,
        @SerializedName("width") val width: Int
    )

    data class Image(
        @SerializedName("height") val height: Int,
        @SerializedName("url") val url: String,
        @SerializedName("width") val width: Int,
        @SerializedName("with_padding") val withPadding: Int
    )
}