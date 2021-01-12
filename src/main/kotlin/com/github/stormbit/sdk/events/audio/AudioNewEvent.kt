package com.github.stormbit.sdk.events.audio

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

class AudioNewEvent(val obj: JsonObject) : Event