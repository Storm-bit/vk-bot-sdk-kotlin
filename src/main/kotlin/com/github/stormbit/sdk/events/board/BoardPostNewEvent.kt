package com.github.stormbit.sdk.events.board

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

class BoardPostNewEvent(val obj: JsonObject) : Event