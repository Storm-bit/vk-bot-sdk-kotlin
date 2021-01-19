package com.github.stormbit.sdk.events.video

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

data class VideoCommentDeleteEvent(val obj: JsonObject) : Event