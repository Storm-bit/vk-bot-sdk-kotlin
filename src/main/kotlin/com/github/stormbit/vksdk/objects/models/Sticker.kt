package com.github.stormbit.vksdk.objects.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sticker(
    @SerialName("images") val images: List<Image>,
    @SerialName("images_with_background") val imagesWithBackground: List<ImagesWithBackground>,
    @SerialName("product_id") val productId: Int,
    @SerialName("sticker_id") val stickerId: Int
)  {

    @Serializable
    data class Image(
        @SerialName("url") val url: String,
        @SerialName("height") val height: Int,
        @SerialName("width") val width: Int
    )

    @Serializable
    data class ImagesWithBackground(
        @SerialName("url") val url: String,
        @SerialName("height") val height: Int,
        @SerialName("width") val width: Int
    )
}