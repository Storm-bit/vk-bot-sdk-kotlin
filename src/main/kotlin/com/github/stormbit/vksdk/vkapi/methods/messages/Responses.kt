@file: Suppress("unused")
package com.github.stormbit.vksdk.vkapi.methods.messages

import com.github.stormbit.vksdk.objects.models.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LinkResponse(
    @SerialName("link") val link: String? = null
)


@Serializable
data class HistoryAttachmentsResponse(
    @SerialName("items") val items: List<Item> = emptyList(),
    @SerialName("next_from") val nextFrom: String? = null
) {

    @Serializable
    data class Item(
        @SerialName("message_id") val messagesId: Int? = null,
        @SerialName("attachment") val attachment: Message.Attachment? = null
    )
}


@Serializable
data class ConversationsListResponse(
    @SerialName("count") val count: Int,
    @SerialName("groups") val groups: List<Community>? = null,
    @SerialName("items") val items: List<Item> = emptyList(),
    @SerialName("profiles") val profiles: List<User>? = null,
    @SerialName("unread_count") val unreadCount: Int? = null
) {
    @Serializable
    data class Item(
        @SerialName("conversation") val conversation: Conversation,
        @SerialName("last_message") val last_message: Message
    )
}


@Serializable
data class ChatChangePhotoResponse(
    @SerialName("message_id") val messageId: Int,
    @SerialName("chat") val chat: Chat
)


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