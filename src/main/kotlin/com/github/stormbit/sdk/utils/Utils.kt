@file:Suppress("unused", "UNCHECKED_CAST", "RegExpRedundantEscape")

package com.github.stormbit.sdk.utils

import com.github.stormbit.sdk.objects.Chat
import com.github.stormbit.sdk.objects.models.Address
import com.github.stormbit.sdk.vkapi.methods.Attachment
import com.github.stormbit.sdk.vkapi.methods.Media
import kotlinx.serialization.json.*
import org.overviewproject.mime_types.MimeTypeDetector
import java.io.*
import java.net.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.pow

class Utils {
    companion object {
        fun regexSearch(pattern: Regex, string: String, group: Int = 0): String? {
            return pattern.find(string)?.groups?.get(group)?.value
        }

        internal fun guessFileNameByContentType(contentType: String): String {
            var parsedContentType = contentType
                .replace("mpeg", "mp3")
                .replace("svg+xml", "svg")
                .replace("javascript", "js")
                .replace("plain", "txt")
                .replace("markdown", "md")

            val mainType = parsedContentType.substring(0, parsedContentType.indexOf('/'))

            if (parsedContentType.contains(" ")) {
                parsedContentType = parsedContentType.substring(0, parsedContentType.indexOf(' '))
            }

            var subType = parsedContentType.substring(parsedContentType.lastIndexOf('/') + 1)

            if (subType.contains("-") || subType.contains(".") || subType.contains("+")) subType = "unknown"

            return "$mainType.$subType"
        }

        internal fun getMimeType(bytes: ByteArray?): String {
            val inputStream = BufferedInputStream(ByteArrayInputStream(bytes))

            val contentType = MimeTypeDetector().detectMimeType("file", inputStream)

            return contentType.substring(contentType.lastIndexOf('/') + 1).replace("jpeg", "jpg")
        }
    }
}

class ParametersBuilder : HashMap<String, Any>() {
    fun append(key: String, value: Any?) {
        if (value != null) this[key] = value
    }
}

fun parametersOf(builder: ParametersBuilder.() -> Unit): Map<String, Any> {
    val parametersBuilder = ParametersBuilder()
    builder(parametersBuilder)

    return parametersBuilder
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

/**
 * Analog method of 'shift()' method from javascript
 *
 * @return First element of list, and then remove it
 */
internal fun <T> CopyOnWriteArrayList<T>.shift(): T? {
    if (this.size > 0) {
        val item = this[0]
        this.removeAt(0)
        return item
    }

    return null
}

internal fun URLConnection.close() {
    if (this is HttpURLConnection) {
        this.disconnect()
    }
}

internal fun URL.toByteArray(): ByteArray {
    val conn = this.openConnection()
    return try {
        conn.toByteArray()
    } finally {
        conn.close()
    }
}

internal fun URLConnection.toByteArray(): ByteArray {
    val inputStream = this.getInputStream()

    return inputStream.readAllBytes()
}

fun String.toJsonObject(): JsonObject = Json.parseToJsonElement(this).jsonObject

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

infix fun Int.`**`(component: Int): Int = toDouble().pow(component.toDouble()).toInt()

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