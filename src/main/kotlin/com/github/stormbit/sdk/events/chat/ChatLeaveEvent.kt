package com.github.stormbit.sdk.events.chat

import com.github.stormbit.sdk.events.Event

class ChatLeaveEvent(val from: Int, val userId: Int, val chatId: Int) : Event