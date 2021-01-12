package com.github.stormbit.sdk.events.market

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

class MarketCommentRestoreEvent(val obj: JsonObject) : Event