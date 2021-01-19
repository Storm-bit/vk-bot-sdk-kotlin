package com.github.stormbit.sdk.events.photo

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

data class PhotoNewEvent(val obj: JsonObject) : Event