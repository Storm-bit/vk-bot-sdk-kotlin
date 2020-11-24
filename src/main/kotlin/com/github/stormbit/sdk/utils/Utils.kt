@file:Suppress("unused", "UNCHECKED_CAST", "RegExpRedundantEscape")

package com.github.stormbit.sdk.utils

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.Chat
import com.github.stormbit.sdk.utils.Utils.Companion.EnumDescriptor
import com.github.stormbit.sdk.utils.vkapi.Auth
import com.github.stormbit.sdk.utils.vkapi.methods.Address
import com.github.stormbit.sdk.utils.vkapi.methods.Attachment
import com.github.stormbit.sdk.utils.vkapi.methods.Media
import com.google.gson.*
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.ClassSerialDescriptorBuilder
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.overviewproject.mime_types.MimeTypeDetector
import java.io.*
import java.lang.Thread.sleep
import java.net.*
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.HashMap
import kotlin.collections.set
import kotlin.reflect.KClass


internal class CustomizedObjectTypeAdapter : TypeAdapter<Any?>() {
    private val delegate = Gson().getAdapter(Any::class.java)

    override fun write(out: JsonWriter, value: Any?) {
        delegate.write(out, value)
    }

    companion object {
        val FACTORY = object : TypeAdapterFactory {
            override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
                return if (MutableMap::class.java.isAssignableFrom(type.rawType)) {
                    (CustomizedObjectTypeAdapter() as TypeAdapter<T>)
                } else null
            }
        }
    }

    override fun read(reader: JsonReader): Any? {
        return when (reader.peek()) {
            JsonToken.BEGIN_ARRAY -> {
                val list: MutableList<Any?> = ArrayList()
                reader.beginArray()
                while (reader.hasNext()) {
                    list.add(read(reader))
                }
                reader.endArray()
                list
            }

            JsonToken.BEGIN_OBJECT -> {
                val map: MutableMap<String, Any?> = LinkedTreeMap()
                reader.beginObject()
                while (reader.hasNext()) {
                    map[reader.nextName()] = read(reader)
                }
                reader.endObject()
                map
            }

            JsonToken.STRING -> reader.nextString()

            JsonToken.NUMBER -> {
                //return in.nextDouble();
                val n = reader.nextString()
                if (n.indexOf('.') != -1) {
                    n.toDouble()
                } else n.toLong()
            }

            JsonToken.BOOLEAN -> reader.nextBoolean()

            JsonToken.NULL -> {
                reader.nextNull()
                null
            }

            else -> throw IllegalStateException()
        }
    }
}

internal val json = Json {
    encodeDefaults = false
    ignoreUnknownKeys = true
    isLenient = false
    allowStructuredMapKeys = true
    prettyPrint = false
    useArrayPolymorphism = false
}

private val adapter = CustomizedObjectTypeAdapter()

val gson = GsonBuilder()
        .serializeNulls()
        .registerTypeAdapterFactory(CustomizedObjectTypeAdapter.FACTORY)
        .setPrettyPrinting().create()

class Utils {
    companion object {
        val hashes = JsonObject()
        const val VK_API_VERSION = 5.122
        const val userApiUrl = "https://vk.com/dev"

        val RE_CAPTCHAID = Regex("onLoginCaptcha\\('(\\d+)'")
        val AUTH_HASH = Regex("\\{.*?act: 'a_authcheck_code'.+?hash: '([a-z_0-9]+)'.*\\}")

        /**
         * Analog method of 'shift()' method from javascript
         *
         * @return First element of list, and then remove it
         */
        fun <T> CopyOnWriteArrayList<T>.shift(): T? {
            if (this.size > 0) {
                val item = this[0]
                this.removeAt(0)
                return item
            }

            return null
        }

        /**
         * @param photos JSONArray with photo objects
         * @return URL of biggest image file
         */
        fun getBiggestPhotoUrl(photos: JsonArray): String {
            val currentBiggestPhoto: String

            val sizes: MutableMap<Int, String> = HashMap()

            for (obj in photos) {
                if (obj is JsonObject) {
                    val width = obj.getInt("width")
                    val url = obj.getString("url")
                    sizes[width] = url
                }
            }

            currentBiggestPhoto = sizes.getValue(Collections.max(sizes.keys))
            return currentBiggestPhoto
        }

        fun Boolean.asInt(): Int {
            return if (this) 1 else 0
        }

        fun toJsonObject(string: String): JsonObject = JsonParser.parseString(string).asJsonObject

        fun toJsonObject(map: Map<String, Any>?): JsonObject = gson.toJsonTree(map).asJsonObject

        fun String.callSync(client: Client, params: JsonObject?): JsonObject = client.api.callSync(this, params)
        fun String.callSync(client: Client, vararg params: Any?): JsonObject = client.api.callSync(this, *params)

        fun String.call(client: Client, params: JsonObject?, callback: Callback<JsonObject?>) = client.api.call(this, params, callback)
        fun String.call(client: Client, callback: Callback<JsonObject?>, vararg params: Any?) = client.api.call(this, callback, *params)

        fun JsonObject.map(): Map<String, Any> {
            val type = object : TypeToken<Map<String, Any?>>() {}.type
            val result = gson.fromJson<Map<String, Any?>>(this, type).filter {
                it.value != null
            } as Map<String, Any>
            return result
        }

        /**
         * Convert params query to map
         *
         * @param query query
         * @return JSONObject query
         */
        fun explodeQuery(query: String): JsonObject {
            val query = URLEncoder.encode(query, "UTF-8")

            val map: MutableMap<String, Any> = HashMap()

            val arr = query.split("&".toRegex()).toTypedArray()

            for (param in arr) {
                val tmp_arr = param.split("=".toRegex())
                val key = tmp_arr[0]
                val value = tmp_arr[1]

                if (tmp_arr[1].contains(",")) {
                    map[key] = gson.toJsonTree(listOf(*value.split(",".toRegex()).toTypedArray())).asJsonArray
                } else {
                    map[key] = value
                }
            }

            return toJsonObject(map)
        }

        fun getHash(auth: Auth, method: String) {
            val cookies = hashMapOf<String, String>()

            auth.session.sessionCookies().forEach {
                cookies[it.name()] = it.value()
            }

            sleep(1000)

            val response = Jsoup.connect("https://vk.com/dev/$method")
                    .userAgent(Auth.STRING_USER_AGENT)
                    .cookies(cookies).execute()

            val hash = regexSearch(Regex("onclick=\"Dev.methodRun\\('(.+?)', this\\);"), response.body(), 1)

            hashes.addProperty(method, hash)
        }

        fun regexSearch(pattern: Regex, string: String, group: Int = 0): String? {
            return pattern.find(string)?.groups?.get(group)?.value
        }

        fun getId(client: Client): Int {
            val response: JsonObject = client.users.get().getAsJsonArray("response").getJsonObject(0)

            return response.getInt("id")
        }

        fun guessFileNameByContentType(contentType: String): String {
            var contentType = contentType
                    .replace("mpeg", "mp3")
                    .replace("svg+xml", "svg")
                    .replace("javascript", "js")
                    .replace("plain", "txt")
                    .replace("markdown", "md")

            val mainType = contentType.substring(0, contentType.indexOf('/'))

            if (contentType.contains(" ")) {
                contentType = contentType.substring(0, contentType.indexOf(' '))
            }

            var subType = contentType.substring(contentType.lastIndexOf('/') + 1)

            if (subType.contains("-") || subType.contains(".") || subType.contains("+")) subType = "unknown"

            return "$mainType.$subType"
        }

        fun getMimeType(bytes: ByteArray?): String {
            val `is`: InputStream = BufferedInputStream(ByteArrayInputStream(bytes))

            val mimeTypeDetector = MimeTypeDetector()
            val contentType = mimeTypeDetector.detectMimeType("file", `is`)

            val mimeType = contentType.substring(contentType.lastIndexOf('/') + 1).replace("jpeg", "jpg")

            return mimeType
        }

        fun close(conn: URLConnection) {
            if (conn is HttpURLConnection) {
                conn.disconnect()
            }
        }

        fun copy(input: InputStream, output: OutputStream): Int {
            val count = copyLarge(input, output)
            return if (count > 2147483647L) -1 else count.toInt()
        }

        fun copyLarge(input: InputStream, output: OutputStream): Long {
            return copy(input, output, 4096)
        }

        fun copy(input: InputStream, output: OutputStream, bufferSize: Int): Long {
            return copyLarge(input, output, ByteArray(bufferSize))
        }

        fun copyLarge(input: InputStream, output: OutputStream, buffer: ByteArray): Long {
            var n: Int

            var count = 0L

            while (input.read(buffer).also { n = it } != -1) {
                output.write(buffer, 0, n)
                count += n.toLong()
            }

            return count
        }

        /**
         * Methods from commons-lang library of Apache
         * Added to not use the library for several methods
         */
        fun toByteArray(url: URL): ByteArray {
            val conn = url.openConnection()
            val var2: ByteArray
            var2 = try {
                toByteArray(conn)
            } finally {
                close(conn)
            }
            return var2
        }

        fun toByteArray(urlConn: URLConnection): ByteArray {
            val inputStream = urlConn.getInputStream()
            val var2 = toByteArray(inputStream)

            return var2
        }

        fun toByteArray(input: InputStream): ByteArray {
            val output = ByteArrayOutputStream()
            copy(input, output)
            return output.toByteArray()
        }

        interface IntEnum {
            val value: Int
        }

        @ExperimentalSerializationApi
        @Suppress("FunctionName")
        fun <E : Enum<E>> EnumDescriptor(clazz: KClass<E>, cases: Array<E>): SerialDescriptor {
            return buildClassSerialDescriptor(clazz.simpleName ?: "") {
                for (case in cases) element(case.name, descriptor(case))
            }
        }

        @ExperimentalSerializationApi
        private fun <E : Enum<E>> ClassSerialDescriptorBuilder.descriptor(case: E): SerialDescriptor {
            return buildClassSerialDescriptor("$serialName.${case.name}")
        }
    }
}

abstract class EnumIntSerializer<E>(clazz: KClass<E>, cases: Array<E>) : KSerializer<E> where E : Enum<E>, E : Utils.Companion.IntEnum {
    private val caseByInt: Map<Int, E> = cases.associateBy(Utils.Companion.IntEnum::value)
    override val descriptor: SerialDescriptor = EnumDescriptor(clazz, cases)
    override fun serialize(encoder: Encoder, value: E) = encoder.encodeInt(value.value)
    override fun deserialize(decoder: Decoder): E = caseByInt.getValue(decoder.decodeInt())
}

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

fun JsonObject.getString(key: String): String = this[key].asString
fun JsonObject.getInt(key: String): Int = this[key].asInt
fun JsonObject.getLong(key: String): Long = this[key].asLong
fun JsonObject.getBoolean(key: String): Boolean = this[key].asBoolean

fun JsonArray.getString(index: Int): String = this[index].asString
fun JsonArray.getInt(index: Int): Int = this[index].asInt
fun JsonArray.getJsonArray(index: Int): JsonArray = this[index].asJsonArray
fun JsonArray.getJsonObject(index: Int): JsonObject = this[index].asJsonObject


fun JsonObject.put(key: String, value: String?): JsonObject {
    this.addProperty(key, value)
    return this
}

fun JsonObject.put(key: String, value: Number?): JsonObject {
    this.addProperty(key, value)
    return this
}

fun JsonObject.put(key: String, value: Char?): JsonObject {
    this.addProperty(key, value)
    return this
}

fun JsonObject.put(key: String, value: Boolean?): JsonObject {
    this.addProperty(key, value)
    return this
}

fun JsonObject.put(key: String, value: JsonElement?): JsonObject {
    this.add(key, value)
    return this
}

inline val Int.peerIdToGroupId: Int get() = -this
inline val Int.peerIdToChatId: Int get() = this - Chat.CHAT_PREFIX

inline val Int.groupIdToPeerId: Int get() = -this
inline val Int.chatIdToPeerId: Int get() = this + Chat.CHAT_PREFIX

inline val Int.isGroupId: Boolean get() = this < 0
inline val Int.isChatPeerId: Boolean get() = this > Chat.CHAT_PREFIX
inline val Int.isEmailPeerId: Boolean get() = this < 0 && (-this).isChatPeerId
inline val Int.isUserPeerId: Boolean get() = !isGroupId && !isChatPeerId && !isEmailPeerId
fun Boolean.toInt() = if (this) 1 else 0