package com.github.stormbit.sdk.events.message

import com.github.stormbit.sdk.events.Event
import com.google.gson.JsonObject

class MessageAllowEvent(val obj: JsonObject) : Event