package com.github.stormbit.sdk.utils.vkapi.apis

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils
import com.github.stormbit.sdk.utils.Utils.Companion.map
import com.github.stormbit.sdk.utils.Utils.Companion.toJsonObject
import com.github.stormbit.sdk.utils.put
import com.github.stormbit.sdk.utils.vkapi.API
import com.github.stormbit.sdk.utils.vkapi.calls.CallAsync
import com.github.stormbit.sdk.utils.vkapi.executors.ExecutorGroup
import com.google.gson.JsonObject
import java.util.*

class APIGroup(private val client: Client) : API(ExecutorGroup(client, client.auth)) {

    /**
     * Call to VK API
     *
     * @param method   Method name
     * @param params   Params as string, JSONObject or Map
     * @param callback Callback to return the response
     */
    override fun call(method: String, params: Any?, callback: Callback<JsonObject?>) {
        try {
            var parameters = JsonObject()
            if (params != null) {
                var good = false

                // Work with map
                if (params is Map<*, *>) {
                    parameters = toJsonObject(params as Map<String, Any>?)
                    good = true
                }

                // with JO
                if (params is JsonObject) {
                    parameters = params
                    good = true
                }

                // or string
                if (params is String) {
                    val s = params.toString()
                    if (s.startsWith("{")) {
                        parameters = toJsonObject(s)
                        good = true
                    } else {
                        if (s.contains("&") && s.contains("=")) {
                            parameters = Utils.explodeQuery(s)
                            good = true
                        }
                    }
                }
                if (good) {
                    val call = CallAsync(method, parameters, callback)
                    executor.execute(call)
                }
            }
        } catch (e: Exception) {
            log.error("Some error occurred when calling VK API method {} with params {}, error is {}", method, params.toString(), e.message)
        }
    }

    override fun call(method: String, callback: Callback<JsonObject?>, vararg params: Any?) {
        try {
            if (params.size == 1) {
                return this.call(method, params[0], callback)
            }

            if (params.size > 1 && params.size % 2 == 0) {
                val map: MutableMap<String, Any> = HashMap()

                var i = 0

                while (i < params.size - 1) {
                    map[params[i].toString()] = params[i + 1]!!
                    i += 2
                }

                return this.call(method, map, callback)
            }

            return this.call(method, HashMap<String, Any>(), callback)
        } catch (e: java.lang.Exception) {
            log.error("Some error occurred when calling VK API: {}", e.message)
        }

        callback.onResult(JsonObject())
    }

    /**
     * Call to VK API
     *
     * @param method Method name
     * @param params Params as string, JSONObject or Map
     * @return JSONObject response of VK answer
     */
    override fun callSync(method: String, params: Any?): JsonObject {
        try {
            var parameters = JsonObject()

            if (params != null) {
                var good = false

                // Work with map
                if (params is Map<*, *>) {
                    parameters = toJsonObject(params as Map<String, Any>?)
                    good = true
                }

                // with JO
                if (params is JsonObject) {
                    parameters = params
                    good = true
                }

                // or string
                if (params is String) {
                    val s = params.toString()
                    if (s.startsWith("{")) {
                        parameters = toJsonObject(s)
                        good = true
                    } else {
                        if (s.contains("&") && s.contains("=")) {
                            parameters = Utils.explodeQuery(s)
                            good = true
                        }
                    }
                }

                if (good) {
                    val data = JsonObject()
                    data.put("v", Utils.version)
                    data.put("access_token", client.token!!)

                    for (key in parameters.keySet()) {
                        data.put(key, parameters[key])
                    }

                    val responseString: String = client.auth.session.post("https://api.vk.com/method/$method")
                            .body(data.map())
                            .send().readToText().replace("[<!>]".toRegex(), "")

                    return toJsonObject(responseString)
                }
            }
        } catch (e: java.lang.Exception) {
            log.error("Some error occurred when calling VK API: {}", e.message)
        }

        return JsonObject()
    }

    /**
     * Call to VK API
     *
     * @param method Method name
     * @param params Floating count of params
     * @return JSONObject response of VK answer
     */
    override fun callSync(method: String, vararg params: Any?): JsonObject {
        try {
            if (params.size == 1) {
                return this.callSync(method, params[0])
            }

            if (params.size > 1 && params.size % 2 == 0) {
                val map: MutableMap<String, Any> = HashMap()

                var i = 0

                while (i < params.size - 1) {
                    map[params[i].toString()] = params[i + 1]!!
                    i += 2
                }

                return this.callSync(method, map)
            }

            return this.callSync(method, HashMap<String, Any>())
        } catch (e: java.lang.Exception) {
            log.error("Some error occurred when calling VK API: {}", e.message)
        }

        return JsonObject()
    }
}