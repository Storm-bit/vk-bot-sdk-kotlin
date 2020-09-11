package com.github.stormbit.sdk.utils

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.EnumDescriptor
import com.github.stormbit.sdk.utils.vkapi.Auth
import com.github.stormbit.sdk.utils.vkapi.methods.Address
import com.github.stormbit.sdk.utils.vkapi.methods.Attachment
import com.github.stormbit.sdk.utils.vkapi.methods.Media
import io.ktor.util.date.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.ClassSerialDescriptorBuilder
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.net.URLEncoder
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.HashMap
import kotlin.collections.set
import kotlin.reflect.KClass

internal val json = Json {
    encodeDefaults = false
    ignoreUnknownKeys = true
    isLenient = false
    allowStructuredMapKeys = true
    prettyPrint = false
    useArrayPolymorphism = false

}

class Utils {
    companion object {
        val hashes = JSONObject()
        const val version = 5.122
        const val userApiUrl = "https://vk.com/dev"

        val RE_CAPTCHAID = Regex("onLoginCaptcha\\('(\\d+)'")
        val AUTH_HASH = Regex("\\{.*?act: 'a_authcheck_code'.+?hash: '([a-z_0-9]+)'.*?}")

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
        fun getBiggestPhotoUrl(photos: JSONArray): String {
            val currentBiggestPhoto: String

            val sizes: MutableMap<Int, String> = HashMap()

            for (obj in photos) {
                if (obj is JSONObject) {
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

        fun String.callSync(client: Client, params: JSONObject?): JSONObject = client.api.callSync(this, params)

        fun JSONObject.map(): Map<String, Any> {
            val map = HashMap<String, Any>()

            for (key in this.keySet()) {
                map[key] = this.get(key)
            }

            return map
        }

        /**
         * Convert params query to map
         *
         * @param query query
         * @return JSONObject query
         */
        fun explodeQuery(query: String): JSONObject {
            var query = query

            try {
                query = URLEncoder.encode(query, "UTF-8")
            } catch (ignored: UnsupportedEncodingException) {
            }

            val map: MutableMap<String?, Any?> = HashMap()

            val arr = query.split("&".toRegex()).toTypedArray()

            for (param in arr) {
                val tmp_arr = param.split("=".toRegex())
                val key = tmp_arr[0];
                val value = tmp_arr[1]

                if (tmp_arr[1].contains(",")) {
                    map[key] = JSONArray(listOf(*value.split(",".toRegex()).toTypedArray()))
                } else {
                    map[key] = value
                }
            }

            return JSONObject(map)
        }

        fun getHash(auth: Auth, method: String) {
            val html = auth.session.get("https://vk.com/dev/$method").send().readToText()
            val hash_0 = regexSearch("onclick=\"Dev.methodRun\\('(.+?)', this\\);", html, 1)

            check(hash_0!!.isNotEmpty()) { "Method is not valid" }

            hashes.put(method, hash_0)
        }

        fun regexSearch(pattern: Regex, string: String, group: Int = 0): String? {
            return pattern.find(string)?.groups?.get(group)?.value
        }

        fun regexSearch(pattern: String, string: String, group: Int = 0): String? {
            return Regex(pattern).find(string)?.groups?.get(group)?.value
        }

        fun getId(client: Client): Int {
            val response: JSONObject = client.users.get().getJSONArray("response").getJSONObject(0)

            return response.getInt("id")
        }

        fun guessFileNameByContentType(contentType: String): String {
            var contentType = contentType

            contentType = contentType
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

        @Throws(IOException::class)
        fun getMimeType(bytes: ByteArray?): String {
            val `is`: InputStream = BufferedInputStream(ByteArrayInputStream(bytes))

            var mimeType = URLConnection.guessContentTypeFromStream(`is`)

            mimeType = mimeType.substring(mimeType.lastIndexOf('/') + 1).replace("jpeg", "jpg")

            return mimeType
        }

        fun close(conn: URLConnection) {
            if (conn is HttpURLConnection) {
                conn.disconnect()
            }
        }

        @Throws(IOException::class)
        fun copy(input: InputStream, output: OutputStream): Int {
            val count = copyLarge(input, output)
            return if (count > 2147483647L) -1 else count.toInt()
        }

        @Throws(IOException::class)
        fun copyLarge(input: InputStream, output: OutputStream): Long {
            return copy(input, output, 4096)
        }

        @Throws(IOException::class)
        fun copy(input: InputStream, output: OutputStream, bufferSize: Int): Long {
            return copyLarge(input, output, ByteArray(bufferSize))
        }

        @Throws(IOException::class)
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
        @Throws(IOException::class)
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

        @Throws(IOException::class)
        fun toByteArray(urlConn: URLConnection): ByteArray {
            val inputStream = urlConn.getInputStream()
            val var2 = toByteArray(inputStream)

            return var2
        }

        @Throws(IOException::class)
        fun toByteArray(input: InputStream): ByteArray {
            val output = ByteArrayOutputStream()
            copy(input, output)
            return output.toByteArray()
        }

        interface IntEnum {
            val value: Int
        }

        internal inline val GMTDate.unixtime: Int
            get() = (GMTDate(seconds, minutes, hours, dayOfMonth, month, year).timestamp / 1000).toInt()

        fun GMTDate.toDMYString(): String = buildString {
            append(dayOfMonth.toString().padStart(2, '0'))
            append((month.ordinal + 1).toString().padStart(2, '0'))
            append(year.toString().padStart(4, '0'))
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