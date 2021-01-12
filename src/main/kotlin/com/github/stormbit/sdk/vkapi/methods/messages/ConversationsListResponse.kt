package com.github.stormbit.sdk.vkapi.methods.messages

import com.github.stormbit.sdk.objects.models.Community
import com.github.stormbit.sdk.objects.models.Conversation
import com.github.stormbit.sdk.objects.models.Message
import com.github.stormbit.sdk.objects.models.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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