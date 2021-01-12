package com.github.stormbit.sdk.events.chat

import com.github.stormbit.sdk.events.Event

class ChatCreateEvent(val title: String, val fromId: Int, val chatId: Int) : Event