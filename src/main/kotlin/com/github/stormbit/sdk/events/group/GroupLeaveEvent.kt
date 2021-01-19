package com.github.stormbit.sdk.events.group

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

data class GroupLeaveEvent(val obj: JsonObject) : Event