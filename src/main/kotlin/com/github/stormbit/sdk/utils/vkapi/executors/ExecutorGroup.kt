package com.github.stormbit.sdk.utils.vkapi.executors

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils
import com.github.stormbit.sdk.utils.Utils.Companion.map
import com.github.stormbit.sdk.utils.put
import com.github.stormbit.sdk.utils.vkapi.Auth
import com.github.stormbit.sdk.utils.vkapi.Executor
import com.github.stormbit.sdk.utils.vkapi.calls.CallAsync
import com.google.gson.JsonObject
import com.google.gson.JsonParseException

class ExecutorGroup(private val client: Client, auth: Auth) : Executor(auth) {
    override fun executing() {
        val tmpQueue = ArrayList<CallAsync>()
        var count = 0

        for (call in queue) {
            tmpQueue.add(call)

            count++
        }

        if (tmpQueue.isNotEmpty()) {
            for ((index, item) in tmpQueue.withIndex()) {
                val method = item.methodName
                val params = item.params

                queue.removeAt(index)

                val data = JsonObject()
                data.put("v", Utils.version)
                data.put("access_token", client.token!!)

                for (key in params.keySet()) {
                    data.put(key, params[key])
                }

                if (count > 0) {
                    val responseString = auth.session.post("https://api.vk.com/method/$method")
                            .body(data.map())
                            .send().readToText().replace("[<!>]".toRegex(), "")

                    if (LOG_REQUESTS) {
                        log.info("New executing request response: {}", responseString)
                    }

                    var response: JsonObject

                    try {
                        response = Utils.toJsonObject(responseString)
                    } catch (e: JsonParseException) {
                        tmpQueue.forEach { it.callback.onResult(null) }
                        log.error("Bad response from executing: {}, params: {}", responseString, data.toString())
                        return
                    }

                    if (!response.has("response")) {
                        log.error("No 'response' object when executing code, VK response: {}", response)
                        tmpQueue.forEach { call: CallAsync -> call.callback.onResult(null) }

                        return
                    }

                    (0..count).forEach { tmpQueue[it].callback.onResult(response) }
                }
            }
        }
    }
}