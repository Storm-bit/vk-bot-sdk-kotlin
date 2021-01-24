package com.github.stormbit.vksdk.objects.attachments

import com.github.stormbit.vksdk.vkapi.methods.Attachment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Video(
    @SerialName("access_key") override val accessKey: String,
    @SerialName("can_add") val canAdd: Int,
    @SerialName("comments") val comments: Int,
    @SerialName("date") val date: Int,
    @SerialName("description") val description: String,
    @SerialName("duration") val duration: Int,
    @SerialName("first_frame") val firstFrame: List<FirstFrame>,
    @SerialName("height") val height: Int,
    @SerialName("id") override val id: Int,
    @SerialName("image") val image: List<Image>,
    @SerialName("is_favorite") val isFavorite: Boolean,
    @SerialName("owner_id") override val ownerId: Int,
    @SerialName("title") val title: String,
    @SerialName("track_code") val trackCode: String,
    @SerialName("type") val type: String,
    @SerialName("views") val views: Int,
    @SerialName("width") val width: Int) : Attachment {

    override val typeAttachment: AttachmentType get() = AttachmentType.VIDEO

    @Serializable
    data class FirstFrame(
        @SerialName("height") val height: Int,
        @SerialName("url") val url: String,
        @SerialName("width") val width: Int
    )

    @Serializable
    data class Image(
        @SerialName("height") val height: Int,
        @SerialName("url") val url: String,
        @SerialName("width") val width: Int,
        @SerialName("with_padding") val withPadding: Int? = null
    )
}