@file:Suppress("unused", "UNCHECKED_CAST", "RegExpRedundantEscape")

package com.github.stormbit.vksdk.utils

import com.github.stormbit.vksdk.objects.Chat
import com.github.stormbit.vksdk.objects.Message
import com.github.stormbit.vksdk.objects.models.Address
import com.github.stormbit.vksdk.vkapi.UploadableFile
import com.github.stormbit.vksdk.vkapi.methods.Attachment
import com.github.stormbit.vksdk.vkapi.methods.Media
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.util.date.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.json.*
import kotlin.math.pow

internal fun regexSearch(pattern: Regex, string: String, group: Int = 0): String? {
    return pattern.find(string)?.groups?.get(group)?.value
}

fun String.toJsonObject(): JsonObject = Json.parseToJsonElement(this).jsonObject

fun Map<*, *>.toJsonObject(): JsonObject = JsonObject(map {
    it.key.toString() to it.value.toJsonElement()
}.toMap())

fun Any?.toJsonElement(): JsonElement = when (this) {
    null -> JsonNull
    is Number -> JsonPrimitive(this)
    is String -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    is Map<*, *> -> this.toJsonObject()
    is Iterable<*> -> JsonArray(this.map { it.toJsonElement() })
    is Array<*> -> JsonArray(this.map { it.toJsonElement() })
    else -> JsonPrimitive(this.toString())
}

internal val json = Json {
    encodeDefaults = false
    ignoreUnknownKeys = true
    isLenient = false
    allowStructuredMapKeys = true
    prettyPrint = false
    useArrayPolymorphism = false
}

const val VK_API_VERSION = 5.122
internal const val TIME_OUT = 10000
internal const val BASE_API_URL = "https://api.vk.com/method/"

internal fun Address.Timetable.serialize(): String = json.encodeToString(Address.Timetable.serializer(), this)

inline val Media.mediaString: String
    get() = buildString {
        append(ownerId)
        append("_").append(id)
        if (accessKey != null) append("_").append(accessKey!!)
    }

inline val Attachment.attachmentString: String
    get() = buildString {
        append(typeAttachment.value)
        append(mediaString)
    }

internal inline val GMTDate.unixtime: Int
    get() = (GMTDate(seconds, minutes, hours, dayOfMonth, month, year).timestamp / 1000).toInt()

fun GMTDate.toDMYString(): String = buildString {
    append(dayOfMonth.toString().padStart(2, '0'))
    append((month.ordinal + 1).toString().padStart(2, '0'))
    append(year.toString().padStart(4, '0'))
}

internal inline val Pair<String, String>.formPart: FormPart<String>
    get() = FormPart(first, second, Headers.Empty)

internal inline val UploadableFile.formPart: FormPart<ByteReadPacket>
    get() = FormPart(key, ByteReadPacket(content.bytes), content.filename.filenameHeader)

private inline val String.filenameHeader: Headers
    get() = headersOf(HttpHeaders.ContentDisposition, "filename=$this")


infix fun Int.pow(component: Int): Int = toDouble().pow(component.toDouble()).toInt()

fun JsonObject.getString(key: String): String? = this[key]?.jsonPrimitive?.content
fun JsonObject.getInt(key: String): Int? = this[key]?.jsonPrimitive?.int
fun JsonObject.getLong(key: String): Long? = this[key]?.jsonPrimitive?.long
fun JsonObject.getBoolean(key: String): Boolean? = this[key]?.jsonPrimitive?.boolean
fun JsonObject.getJsonArray(key: String): JsonArray? = this[key]?.jsonArray
fun JsonObject.getJsonObject(key: String): JsonObject? = this[key]?.jsonObject

fun JsonArray.getString(index: Int): String = this[index].jsonPrimitive.content
fun JsonArray.getInt(index: Int): Int = this[index].jsonPrimitive.int
fun JsonArray.getLong(index: Int): Long = this[index].jsonPrimitive.long
fun JsonArray.getBoolean(index: Int): Boolean = this[index].jsonPrimitive.boolean
fun JsonArray.getJsonArray(index: Int): JsonArray = this[index].jsonArray
fun JsonArray.getJsonObject(index: Int): JsonObject = this[index].jsonObject

inline val Int.peerIdToGroupId: Int get() = -this
inline val Int.peerIdToChatId: Int get() = this - Chat.CHAT_PREFIX
inline val Int.groupIdToPeerId: Int get() = -this
inline val Int.chatIdToPeerId: Int get() = this + Chat.CHAT_PREFIX
inline val Int.isGroupId: Boolean get() = this < 0

inline val Int.isChatPeerId: Boolean get() = this > Chat.CHAT_PREFIX
inline val Int.isEmailPeerId: Boolean get() = this < 0 && (-this).isChatPeerId
inline val Int.isUserPeerId: Boolean get() = !isGroupId && !isChatPeerId && !isEmailPeerId

fun Boolean.toInt() = if (this) 1 else 0
fun Int.toBoolean() = this > 0

fun getSenderType(peerId: Int): Message.SenderType = when {
    peerId > 2000000000 -> Message.SenderType.CHAT
    peerId < 0 -> Message.SenderType.COMMUNITY
    else -> Message.SenderType.USER
}

@Suppress("NOTHING_TO_INLINE")
internal inline fun ParametersBuilder.append(first: String, second: Any?) {
    if (second != null && second.toString().isNotEmpty()) append(first, second.toString())
}

@Suppress("NOTHING_TO_INLINE")
inline fun <K : Any, V : Any> map(key: KSerializer<K>, value: KSerializer<V>) = MapSerializer(key, value)