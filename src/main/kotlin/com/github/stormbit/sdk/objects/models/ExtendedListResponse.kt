package com.github.stormbit.sdk.objects.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExtendedListResponse<T>(
    @SerialName("count") val count: Int,
    @SerialName("items") val items: List<T>,
    @SerialName("profiles") val profiles: List<User>? = null,
    @SerialName("groups") val groups: List<Community>? = null,
    @SerialName("conversations") val conversations: List<Conversation>? = null
)