package com.github.stormbit.vksdk.events.market

import com.github.stormbit.vksdk.events.Event
import kotlinx.serialization.json.JsonObject

data class MarketCommentEditEvent(val obj: JsonObject) : Event