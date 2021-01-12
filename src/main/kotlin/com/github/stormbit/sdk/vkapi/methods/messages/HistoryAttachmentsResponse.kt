package com.github.stormbit.sdk.vkapi.methods.messages

import com.github.stormbit.sdk.objects.models.Message
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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