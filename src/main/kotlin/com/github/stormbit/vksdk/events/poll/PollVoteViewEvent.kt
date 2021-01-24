package com.github.stormbit.vksdk.events.poll

import com.github.stormbit.vksdk.events.Event
import kotlinx.serialization.json.JsonObject

data class PollVoteViewEvent(val obj: JsonObject) : Event