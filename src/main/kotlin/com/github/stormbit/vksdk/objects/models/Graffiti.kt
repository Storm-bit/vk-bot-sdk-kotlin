package com.github.stormbit.vksdk.objects.models

import com.github.stormbit.vksdk.vkapi.methods.Attachment
import com.github.stormbit.vksdk.objects.attachments.AttachmentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Graffiti(
    @SerialName("id") override val id: Int,
    @SerialName("owner_id") override val ownerId: Int,
    @SerialName("url") val url: String,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
    @SerialName("access_key") override val accessKey: String? = null
) : Attachment {

    override val typeAttachment: AttachmentType get() = AttachmentType.GRAFFITI
}