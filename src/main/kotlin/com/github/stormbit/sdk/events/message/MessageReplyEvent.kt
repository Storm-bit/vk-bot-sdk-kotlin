package com.github.stormbit.sdk.events.message

import com.github.stormbit.sdk.events.Event
import com.google.gson.JsonObject

class MessageReplyEvent(val obj: JsonObject) : Event