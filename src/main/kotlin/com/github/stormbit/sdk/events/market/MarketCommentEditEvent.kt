package com.github.stormbit.sdk.events.market

import com.github.stormbit.sdk.events.Event
import kotlinx.serialization.json.JsonObject

class MarketCommentEditEvent(val obj: JsonObject) : Event