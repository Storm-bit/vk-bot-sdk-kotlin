package com.github.stormbit.sdk.events.message

import com.github.stormbit.sdk.events.Event
import com.google.gson.JsonObject

class MessageDenyEvent(val obj: JsonObject) : Event