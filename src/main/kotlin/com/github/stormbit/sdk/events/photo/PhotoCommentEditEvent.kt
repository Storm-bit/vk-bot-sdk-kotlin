package com.github.stormbit.sdk.events.photo

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

data class PhotoCommentEditEvent(val obj: JsonObject) : Event