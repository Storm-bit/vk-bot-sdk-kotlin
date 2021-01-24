package com.github.stormbit.vksdk.events.group

import com.github.stormbit.vksdk.events.Event
import kotlinx.serialization.json.JsonObject

data class GroupLeaveEvent(val obj: JsonObject) : Event