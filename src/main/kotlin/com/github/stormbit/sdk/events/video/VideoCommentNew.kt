package com.github.stormbit.sdk.events.video

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

data class VideoCommentNew(val obj: JsonObject) : Event