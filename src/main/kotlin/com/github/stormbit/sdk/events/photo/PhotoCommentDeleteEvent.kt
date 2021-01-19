package com.github.stormbit.sdk.events.photo

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

data class PhotoCommentDeleteEvent(val obj: JsonObject) : Event