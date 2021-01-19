package com.github.stormbit.sdk.events.chat

import com.github.stormbit.sdk.events.ServiceActionEvent
import com.github.stormbit.sdk.events.message.ChatMessageEvent
import com.github.stormbit.sdk.objects.Message

data class ChatLeaveEvent(
    val fromId: Int,
    val userId: Int,
    val chatId: Int,
    override val message: Message
) : ChatMessageEvent(message), ServiceActionEvent