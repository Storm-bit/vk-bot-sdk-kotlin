package com.github.stormbit.sdk.events.board

import com.github.stormbit.sdk.events.Event
import com.google.gson.JsonObject

class BoardPostDeleteEvent(val obj: JsonObject) : Event