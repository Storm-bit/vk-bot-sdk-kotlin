package com.github.stormbit.sdk.vkapi

import com.github.stormbit.sdk.utils.*
import kotlinx.serialization.json.JsonObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
class API internal constructor() {
    internal val log: Logger = LoggerFactory.getLogger(API::class.java)

    suspend fun <T : Any> executeMethod(request: VkApiRequest<T>): T {
        val responseString = request().receive<String>()
        return when (responseString.contains("error")) {
            false -> json.decodeFromJsonElement(
                request.serializer,
                json.decodeFromString(JsonObject.serializer(), responseString)["response"]!!
            )

            true -> throw Exception(responseString)
        }
    }

    suspend fun <T : Any> uploadFile(request: UploadFilesRequest<T>): T {
        val responseString = request().receive<String>()
        return when (responseString.contains("error")) {
            false -> json.decodeFromJsonElement(
                request.serializer,
                json.decodeFromString(JsonObject.serializer(), responseString)
            )

            true -> throw Exception(responseString)
        }
    }
}