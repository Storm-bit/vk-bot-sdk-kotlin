package com.github.stormbit.sdk.events.group

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

class GroupChangeSettingsEvent(val obj: JsonObject) : Event