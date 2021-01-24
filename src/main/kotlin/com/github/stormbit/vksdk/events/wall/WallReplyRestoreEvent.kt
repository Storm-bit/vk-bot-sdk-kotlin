package com.github.stormbit.vksdk.events.wall

import com.github.stormbit.vksdk.events.Event
import kotlinx.serialization.json.JsonObject

data class WallReplyRestoreEvent(val obj: JsonObject) : Event