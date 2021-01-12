package com.github.stormbit.sdk.events.wall

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

class WallReplyNewEvent(val obj: JsonObject) : Event