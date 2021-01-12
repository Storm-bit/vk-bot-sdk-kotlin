package com.github.stormbit.sdk.events.chat

import com.github.stormbit.sdk.events.Event

class ChatJoinEvent(val fromId: Int, val userId: Int, val chatId: Int) : Event