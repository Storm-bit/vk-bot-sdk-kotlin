package com.github.stormbit.sdk.vkapi.methods.friends

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FriendsListItem(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null
)