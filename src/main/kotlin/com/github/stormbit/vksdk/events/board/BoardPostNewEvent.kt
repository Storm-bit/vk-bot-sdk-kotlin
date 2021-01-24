package com.github.stormbit.vksdk.events.board

import com.github.stormbit.vksdk.events.Event
import kotlinx.serialization.json.JsonObject

@Suppress("unused")
data class BoardPostNewEvent(val obj: JsonObject) : Event