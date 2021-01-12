package com.github.stormbit.sdk.events.chat

import com.github.stormbit.sdk.events.Event

class ChatTitleChangeEvent(val oldTitle: String, val newTitle: String, val fromId: Int, val chatId: Int) : Event