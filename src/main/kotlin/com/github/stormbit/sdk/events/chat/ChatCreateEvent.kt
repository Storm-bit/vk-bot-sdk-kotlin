package com.github.stormbit.sdk.events.chat

import com.github.stormbit.sdk.events.Event

class ChatCreateEvent(val title: String, val from: Int, val chatId: Int) : Event