package com.github.stormbit.sdk.events.chat

import com.github.stormbit.sdk.events.Event

data class ChatPinMessageEvent(val fromId: Int, val chatId: Int, val messageId: Int) : Event
