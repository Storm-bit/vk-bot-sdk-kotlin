package com.github.stormbit.sdk.events.poll

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

data class PollVoteViewEvent(val obj: JsonObject) : Event