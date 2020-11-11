package com.github.stormbit.sdk.events.board

import com.github.stormbit.sdk.events.Event
import com.google.gson.JsonObject

class BoardPostEditEvent(val obj: JsonObject) : Event