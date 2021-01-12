package com.github.stormbit.sdk.vkapi.methods.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecentCall(
    @SerialName("call") val call: Call,
    @SerialName("message_id") val messageId: Int
) {
    @Serializable
    data class Call(
        @SerialName("initiator_id") val initiatorId: Int,
        @SerialName("receiver_id") val receiverId: Int,
        @SerialName("state") val state: State,
        @SerialName("duration") val duration: Int
    ) {

        @Serializable
        enum class State(val value: String) {
            @SerialName("canceled_by_initiator") CANCELED_BY_INITIATOR("canceled_by_initiator"),
            @SerialName("canceled_by_receiver") CANCELED_BY_RECEIVER("canceled_by_receiver"),
            @SerialName("reached") REACHED("reached")
        }
    }
}