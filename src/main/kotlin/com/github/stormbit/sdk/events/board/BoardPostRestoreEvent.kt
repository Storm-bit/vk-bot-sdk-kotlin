package com.github.stormbit.sdk.events.board

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

@Suppress("unused")
data class BoardPostRestoreEvent(val obj: JsonObject) : Event