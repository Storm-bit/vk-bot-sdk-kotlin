package com.github.stormbit.sdk.events.message

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

class MessageReplyEvent(val obj: JsonObject) : Event