package com.github.stormbit.sdk.events.group

import com.github.stormbit.sdk.events.Event
import com.google.gson.JsonObject

class GroupChangeSettingsEvent(val obj: JsonObject) : Event