package com.github.stormbit.vksdk.events.chat

import com.github.stormbit.vksdk.events.ServiceActionEvent
import com.github.stormbit.vksdk.events.message.ChatMessageEvent
import com.github.stormbit.vksdk.objects.Message

data class ChatUnpinMessageEvent(
    val fromId: Int,
    val chatId: Int,
    val messageId: Int,
    override val message: Message
) : ChatMessageEvent(message), ServiceActionEvent