package com.github.stormbit.sdk.objects.models

import com.github.stormbit.sdk.utils.BooleanInt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
data class Tag(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: Int,
    @SerialName("placer_id") val placerId: Int,
    @SerialName("tagged_name") val taggedName: String,
    @SerialName("date") val date: Int,
    @SerialName("x") val x: Double,
    @SerialName("y") val y: Double,
    @SerialName("x2") val x2: Double,
    @SerialName("y2") val y2: Double,
    @SerialName("viewed") val isViewed: BooleanInt) {

    val isTextTag: Boolean get() = userId == 0
}