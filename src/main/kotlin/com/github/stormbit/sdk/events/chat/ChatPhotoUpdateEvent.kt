package com.github.stormbit.sdk.events.chat

import com.github.stormbit.sdk.events.Event

class ChatPhotoUpdateEvent(val photo: String, val fromId: Int, val chatId: Int) : Event