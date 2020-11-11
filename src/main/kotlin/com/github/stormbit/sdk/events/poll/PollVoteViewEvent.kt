package com.github.stormbit.sdk.events.poll

import com.github.stormbit.sdk.events.Event
import com.google.gson.JsonObject

class PollVoteViewEvent(val obj: JsonObject) : Event