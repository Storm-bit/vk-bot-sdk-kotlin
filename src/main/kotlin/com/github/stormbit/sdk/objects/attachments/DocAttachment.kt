package com.github.stormbit.sdk.objects.attachments

import com.google.gson.annotations.SerializedName

data class DocAttachment(
    @SerializedName("access_key") val accessKey: String,
    val date: Int,
    val ext: String,
    val id: Int,
    @SerializedName("owner_id") val ownerId: Int,
    val preview: Preview?,
    val size: Int,
    val title: String,
    val type: Int,
    val url: String
) : Attachment {
    data class Preview(
        val photo: Photo
    )

    data class Photo(
        val sizes: List<PhotoAttachment.Size>
    )
}

