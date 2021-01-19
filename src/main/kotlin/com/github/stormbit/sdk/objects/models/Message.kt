package com.github.stormbit.sdk.objects.models

import com.github.stormbit.sdk.objects.attachments.*
import com.github.stormbit.sdk.utils.isChatPeerId
import com.github.stormbit.sdk.objects.attachments.AttachmentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    @SerialName("id") val id: Int,
    @SerialName("peer_id") val peerId: Int,
    @SerialName("from_id") val fromId: Int,
    @SerialName("date") val date: Int,
    @SerialName("text") val text: String,
    @SerialName("conversation_message_id") val conversationMessageId: Int,
    @SerialName("out") val out: Int? = null,
    @SerialName("important") val isImportant: Boolean? = null,
    @SerialName("is_hidden") val isHidden: Boolean? = null,
    @SerialName("update_time") val updateTime: Int? = null,
    @SerialName("attachments") val attachments: List<Attachment> = emptyList(),
    @SerialName("geo") val geo: Geo? = null,
    @SerialName("payload") val payload: MessagePayload? = null,
    @SerialName("fwd_messages") val forwardedMessages: List<Forward> = emptyList(),
    @SerialName("reply_message") val replyMessage: Forward? = null,
    @SerialName("action") val serviceAction: ServiceAction? = null,
    @SerialName("random_id") val randomId: Int? = null,
    @SerialName("ref") val ref: String? = null,
    @SerialName("ref_source") val refSource: String? = null,
    @SerialName("chat_id") val chatId: Int? = null,
    @SerialName("admin_author_id") val adminAuthorId: Int? = null,
    @SerialName("expire_ttl") val expireTtl: Int? = null,
    @SerialName("is_expired") val isExpired: Boolean? = null,
    @SerialName("ttl") val ttl: Int? = null
) {

    val isFromChat: Boolean get() = peerId.isChatPeerId
    val isServiceAction: Boolean get() = serviceAction != null

    @Serializable
    data class Forward(
        @SerialName("id") val id: Int? = null,
        @SerialName("conversation_message_id") val conversationMessageId: Int? = null,
        @SerialName("peer_id") val peerId: Int? = null,
        @SerialName("from_id") val fromId: Int,
        @SerialName("date") val date: Int,
        @SerialName("text") val body: String,
        @SerialName("geo") val geo: Geo? = null,
        @SerialName("update_time") val updateTime: Int? = null,
        @SerialName("items") val attachments: List<Attachment>? = null,
        @SerialName("fwd_messages") val forwardedMessages: List<Forward>? = null
    )

    @Serializable
    data class Pinned(
        @SerialName("id") val id: Int,
        @SerialName("from_id") val fromId: Int,
        @SerialName("peer_id") val peerId: Int,
        @SerialName("date") val date: Int,
        @SerialName("text") val body: String,
        @SerialName("conversation_message_id") val conversationMessageId: Int? = null,
        @SerialName("geo") val geo: Geo? = null,
        @SerialName("items") val attachments: List<Attachment>? = null,
        @SerialName("fwd_messages") val forwardedMessages: List<Forward>? = null
    )

    @Serializable
    data class Attachment(
        @SerialName("type")  val type: AttachmentType,
        @SerialName("photo") val photo: Photo? = null,
        @SerialName("video") val video: Video? = null,
        @SerialName("audio") val audio: Audio? = null,
        @SerialName("audio_message") val voice: Voice? = null,
        @SerialName("sticker") val sticker: Sticker? = null,
        @SerialName("doc") val document: Document? = null
    )

    @Serializable
    data class Geo(
        @SerialName("type") val type: String,
        @SerialName("coordinates") val coordinates: Coordinates,
        @SerialName("place") val place: Place? = null
    ) {
        @Serializable
        data class Coordinates(
            @SerialName("latitude") val latitude: Double,
            @SerialName("longitude") val longitude: Double
        )

        @Serializable
        data class Place(
            @SerialName("title") val title: String,
            @SerialName("country") val country: String,
            @SerialName("city") val city: String
        )
    }
}