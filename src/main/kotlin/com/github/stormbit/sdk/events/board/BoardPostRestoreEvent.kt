package com.github.stormbit.sdk.events.board

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

class BoardPostRestoreEvent(val obj: JsonObject) : Event