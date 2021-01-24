package com.github.stormbit.vksdk.events.market

import com.github.stormbit.vksdk.events.Event
import kotlinx.serialization.json.JsonObject

data class MarketCommentRestoreEvent(val obj: JsonObject) : Event