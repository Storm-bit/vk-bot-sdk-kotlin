package com.github.stormbit.sdk.utils.vkapi.apis

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.utils.Utils.Companion.map
import com.github.stormbit.sdk.utils.Utils.Companion.toJsonObject
import com.github.stormbit.sdk.utils.vkapi.API
import com.github.stormbit.sdk.utils.vkapi.calls.CallAsync
import com.github.stormbit.sdk.utils.vkapi.executors.ExecutorUser
import com.google.gson.JsonObject
import java.util.*

/**
 * Created by Storm-bit on 02/09/2020
 *
 * API for users
 */
class APIUser(private val client: Client) : API(ExecutorUser(client.auth)) {

    override fun call(method: String, params: Any?, callback: Callback<JsonObject?>) {
        try {
            var parameters = JsonObject()
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
                    if (!Utils.hashes.has(method)) {
                        Utils.getHash(client.auth, method)
                    }

                    val data = JsonObject()
                    data.put("act", "a_run_method")
                    data.put("al", 1)
                    data.put("hash", Utils.hashes.getString(method))
                    data.put("method", method)
                    data.put("param_v", Utils.version)

                    for (key in parameters.keySet()) {
                        data.put("param_$key", parameters[key])
                    }

                    val responseString: String = client.auth.session.post(Utils.userApiUrl)
                            .body(data.map())
                            .send().readToText().replace("[<!>]".toRegex(), "").substring(2)

                    return toJsonObject(toJsonObject(responseString).getAsJsonArray("payload").getJsonArray(1).getString(0))
                }
            }
        } catch (e: java.lang.Exception) {
            log.error("Some error occurred when calling VK API: {${e.message}}")
        }

        return JsonObject()
    }

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