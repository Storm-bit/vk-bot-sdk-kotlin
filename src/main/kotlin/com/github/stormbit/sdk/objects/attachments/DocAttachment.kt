package com.github.stormbit.sdk.objects.attachments

import com.google.gson.annotations.SerializedName

data class DocAttachment(
    @SerializedName("access_key") val accessKey: String,
    @SerializedName("date") val date: Int,
    @SerializedName("ext") val ext: String,
    @SerializedName("id") val id: Int,
    @SerializedName("owner_id") val ownerId: Int,
    @SerializedName("preview") val preview: Preview,
    @SerializedName("size") val size: Int,
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: Int,
    @SerializedName("url") val url: String
) : Attachment {
    data class Preview(
        @SerializedName("photo") val photo: Photo
    ) {
        data class Photo(
            @SerializedName("sizes") val sizes: List<Size>
        ) {
            data class Size(
                @SerializedName("height") val height: Int,
                @SerializedName("src") val src: String,
                @SerializedName("type") val type: String,
                @SerializedName("width") val width: Int
            )
        }
    }
}