package com.github.stormbit.vksdk.objects.models

import com.github.stormbit.vksdk.events.Event
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class MessageEvent(
    @SerialName("event_id") val eventId: String,
    @SerialName("payload") val payload: JsonObject,
    @SerialName("peer_id") val peerId: Int,
    @SerialName("user_id") val userId: Int
) : Event