package com.github.stormbit.vksdk.events.photo

import com.github.stormbit.vksdk.events.Event
import kotlinx.serialization.json.JsonObject

data class PhotoCommentDeleteEvent(val obj: JsonObject) : Event