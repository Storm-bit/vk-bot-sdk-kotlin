package com.github.stormbit.vksdk.events.market

import com.github.stormbit.vksdk.events.Event
import kotlinx.serialization.json.JsonObject

data class MarketCommentNewEvent(val obj: JsonObject) : Event