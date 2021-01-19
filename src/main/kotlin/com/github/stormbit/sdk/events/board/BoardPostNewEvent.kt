package com.github.stormbit.sdk.events.board

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

@Suppress("unused")
data class BoardPostNewEvent(val obj: JsonObject) : Event