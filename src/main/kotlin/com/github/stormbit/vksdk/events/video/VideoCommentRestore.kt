package com.github.stormbit.vksdk.events.video

import com.github.stormbit.vksdk.events.Event
import kotlinx.serialization.json.JsonObject

data class VideoCommentRestore(val obj: JsonObject) : Event