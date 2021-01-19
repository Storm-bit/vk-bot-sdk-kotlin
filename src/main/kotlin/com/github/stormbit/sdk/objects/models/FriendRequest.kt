package com.github.stormbit.sdk.objects.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FriendRequest(
    @SerialName("user_id") val userId: Int,
    @SerialName("senderType") val from: Int? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("mutual") val mutual: Mutual? = null
) {

    @Serializable
    data class Mutual(
        @SerialName("count") val count: Int,
        @SerialName("users") val users: List<Int>
    )
}
