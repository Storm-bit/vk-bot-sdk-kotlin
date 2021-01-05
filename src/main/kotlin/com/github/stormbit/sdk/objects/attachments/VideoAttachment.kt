package com.github.stormbit.sdk.objects.attachments

import com.google.gson.annotations.SerializedName

data class VideoAttachment(
    @SerializedName("access_key") val accessKey: String,
    @SerializedName("can_add") val canAdd: Int,
    val comments: Int,
    val date: Int,
    val description: String,
    val duration: Int,
    @SerializedName("first_frame") val firstFrame: List<FirstFrame>,
    val height: Int,
    val id: Int,
    val image: List<Image>,
    @SerializedName("is_favorite") val isFavorite: Boolean,
    @SerializedName("owner_id") val ownerId: Int,
    val title: String,
    @SerializedName("track_code") val trackCode: String,
    val type: String,
    val views: Int,
    val width: Int
) : Attachment {
    data class Image(
        val height: Int,
        val url: String,
        val width: Int,
        val with_padding: Int
    )

    data class FirstFrame(
        val height: Int,
        val url: String,
        val width: Int
    )
}