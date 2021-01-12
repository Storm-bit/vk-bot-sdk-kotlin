package com.github.stormbit.sdk.events

import kotlinx.serialization.json.JsonObject

class EveryEvent(val obj: JsonObject) : Event