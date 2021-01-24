package com.github.stormbit.vksdk.events.board

import com.github.stormbit.vksdk.events.Event
import kotlinx.serialization.json.JsonObject

@Suppress("unused")
data class BoardPostDeleteEvent(val obj: JsonObject) : Event