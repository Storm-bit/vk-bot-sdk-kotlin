package com.github.stormbit.sdk.objects.attachments

import com.google.gson.annotations.SerializedName

data class StickerAttachment(
    @SerializedName("images") val images: List<Image>,
    @SerializedName("images_with_background") val imagesWithBackground: List<ImagesWithBackground>,
    @SerializedName("product_id") val productId: Int,
    @SerializedName("sticker_id") val stickerId: Int
) : Attachment {
    data class Image(
        @SerializedName("height") val height: Int,
        @SerializedName("url") val url: String,
        @SerializedName("width") val width: Int
    )

    data class ImagesWithBackground(
        @SerializedName("height") val height: Int,
        @SerializedName("url") val url: String,
        @SerializedName("width") val width: Int
    )
}