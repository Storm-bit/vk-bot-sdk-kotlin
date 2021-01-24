package com.github.stormbit.vksdk.events.chat

import com.github.stormbit.vksdk.events.ServiceActionEvent
import com.github.stormbit.vksdk.events.message.ChatMessageEvent
import com.github.stormbit.vksdk.objects.Message

data class ChatTitleUpdateEvent(
    val oldTitle: String,
    val newTitle: String,
    val fromId: Int,
    val chatId: Int,
    override val message: Message
) : ChatMessageEvent(message), ServiceActionEvent