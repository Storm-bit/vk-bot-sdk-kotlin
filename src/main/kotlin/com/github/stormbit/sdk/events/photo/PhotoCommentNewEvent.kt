package com.github.stormbit.sdk.events.photo

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

data class PhotoCommentNewEvent(val obj: JsonObject) : Event