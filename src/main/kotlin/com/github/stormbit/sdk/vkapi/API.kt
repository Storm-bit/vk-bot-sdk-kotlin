package com.github.stormbit.sdk.vkapi

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.utils.TIME_OUT
import com.github.stormbit.sdk.utils.json
import com.github.stormbit.sdk.vkapi.calls.CallAsync
import com.github.stormbit.sdk.vkapi.calls.CallSync
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URLEncoder

@Suppress("unused")
class API(private val client: Client, private val executor: Executor) {
    internal val log: Logger = LoggerFactory.getLogger(API::class.java)

    fun execute(code: String): JsonObject? {
        return callSync(JsonObject.serializer(), "execute", mapOf("code" to code))
    }

    fun execute(vararg calls: CallAsync<Any>) {
        if (calls.size < 26) {
            for (call in calls) {
                executor.execute(call)
            }
        } else {
            val newCalls = arrayOfNulls<CallAsync<Any>>(25)
            System.arraycopy(calls, 0, newCalls, 0, 25)

            for (call in newCalls) {
                executor.execute(call!!)
            }
        }
    }

    fun execute(vararg calls: CallSync): JsonArray? {
        val code = StringBuilder("return [")

        for (i in calls.indices) {
            val codeTmp: String = executor.codeForExecute(calls[i])
            code.append(codeTmp)
            if (i < calls.size - 1) {
                code.append(',')
            }
        }

        code.append("];")

        val response: JsonObject?

        try {
            response = callSync(JsonObject.serializer(), "execute", parametersOf {
                append("code", URLEncoder.encode(code.toString(), "UTF-8"))
            })
        } catch (e: SerializationException) {
            log.error("Some error occurred when calling VK API method, error is: ", e)
            return null
        }

        return response?.jsonArray
    }

    fun <T : Any> call(
        serializer: KSerializer<T>,
        method: String,
        params: Map<String, Any>?,
        callback: Callback<T?>?
    ) {
        try {
            val parameters = params ?: mapOf()

            val call = CallAsync(method, parameters, callback, serializer)
            executor.execute(call as CallAsync<Any>)
        } catch (e: Exception) {
            log.error("Some error occurred when calling VK API method '$method', error is: ", e)
            callback?.onResult(null)
        }
    }

    fun <T : Any> callSync(serializer: KSerializer<T>, method: String, params: Map<String, Any>?): T? {
        try {
            val parameters: Map<String, Any> = params ?: mapOf()

            val data = mutableMapOf<String, Any?>(
                "v" to VK_API_VERSION,
                "access_token" to client.token
            )

            data.putAll(client.values)
            data.putAll(parameters)

            val responseString: String = client.auth.session.post(BASE_API_URL + method)
                .body(data)
                .headers(client.headers)
                .timeout(TIME_OUT)
                .send().readToText()

            if (responseString.contains("error")) {
                throw Exception(responseString)
            }

            val response = responseString.toJsonObject()["response"]!!

            return json.decodeFromJsonElement(serializer, response)
        } catch (e: Exception) {
            log.error("Some error occurred when calling VK API method '$method', error is: ", e)
        }

        return null
    }
}