package com.github.stormbit.vksdk.events.photo

import com.github.stormbit.vksdk.events.Event
import kotlinx.serialization.json.JsonObject

data class PhotoCommentNewEvent(val obj: JsonObject) : Event