package com.github.stormbit.sdk.vkapi.methods.messages

import com.github.stormbit.sdk.objects.models.Chat
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatChangePhotoResponse(
    @SerialName("message_id") val messageId: Int,
    @SerialName("chat") val chat: Chat
)
