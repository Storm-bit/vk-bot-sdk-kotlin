package com.github.stormbit.vksdk.events.audio

import com.github.stormbit.vksdk.events.Event
import kotlinx.serialization.json.JsonObject

@Suppress("unused")
data class AudioNewEvent(val obj: JsonObject) : Event