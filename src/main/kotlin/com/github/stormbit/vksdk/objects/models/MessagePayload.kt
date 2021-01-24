package com.github.stormbit.vksdk.objects.models

import com.github.stormbit.vksdk.utils.json
import kotlinx.serialization.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class MessagePayload(val value: String) : StringFormat by json {

    fun <T> to(loader: DeserializationStrategy<T>): T = decodeFromString(loader, value)

    inline fun <reified T : Any> to(): T = to(serializersModule.serializer())

    @Serializer(forClass = MessagePayload::class)
    companion object : KSerializer<MessagePayload>, StringFormat by json {

        override fun serialize(encoder: Encoder, value: MessagePayload) = encoder.encodeString(value.value)

        override fun deserialize(decoder: Decoder): MessagePayload = MessagePayload(decoder.decodeString())

        fun <T> from(strategy: SerializationStrategy<T>, value: T): MessagePayload = MessagePayload(json.encodeToString(strategy, value))

        inline fun <reified T : Any> from(value: T): MessagePayload = from(serializersModule.serializer(), value)
    }
}