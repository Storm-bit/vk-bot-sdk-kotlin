package com.github.stormbit.sdk.events.wall

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

data class WallReplyDelete(val obj: JsonObject) : Event