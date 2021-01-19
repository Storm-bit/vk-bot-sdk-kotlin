package com.github.stormbit.sdk.events.chat

import com.github.stormbit.sdk.events.ServiceActionEvent
import com.github.stormbit.sdk.events.message.ChatMessageEvent
import com.github.stormbit.sdk.objects.Message

data class ChatCreateEvent(
    val title: String,
    val fromId: Int, val
    chatId: Int,
    override val message: Message
) : ChatMessageEvent(message), ServiceActionEvent