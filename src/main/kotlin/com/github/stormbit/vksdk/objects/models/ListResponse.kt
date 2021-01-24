package com.github.stormbit.vksdk.objects.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListResponse<T>(
    @SerialName("count") val count: Int,
    @SerialName("items") val items: List<T>
)