package com.github.stormbit.sdk.events.chat

import com.github.stormbit.sdk.events.Event
import com.google.gson.JsonObject

class ChatPhotoUpdateEvent(val photo: JsonObject, val from: Int, val chatId: Int) : Event