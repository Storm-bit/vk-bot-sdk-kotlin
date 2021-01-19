package com.github.stormbit.sdk.events.chat

import com.github.stormbit.sdk.events.ServiceActionEvent
import com.github.stormbit.sdk.events.message.ChatMessageEvent
import com.github.stormbit.sdk.objects.Message

data class ChatUnpinMessageEvent(
    val fromId: Int,
    val chatId: Int,
    val messageId: Int,
    override val message: Message
) : ChatMessageEvent(message), ServiceActionEvent