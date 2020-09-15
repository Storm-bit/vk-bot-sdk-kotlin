package com.github.stormbit.sdk.utils.vkapi.executors

import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.utils.Utils.Companion.map
import com.github.stormbit.sdk.utils.Utils.Companion.toJsonObject
import com.github.stormbit.sdk.utils.vkapi.Auth
import com.github.stormbit.sdk.utils.vkapi.Executor
import com.github.stormbit.sdk.utils.vkapi.calls.CallAsync
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import kotlin.collections.ArrayList

class ExecutorUser(auth: Auth) : Executor(auth) {

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

                if (!Utils.hashes.has(method)) {
                    Utils.getHash(auth, method)
                }

                queue.removeAt(index)

                val data = JsonObject()
                data.put("act", "a_run_method")
                data.put("al", 1)
                data.put("hash", Utils.hashes.get(method))
                data.put("method", method)
                data.put("param_v", Utils.version)

                for (key in params.keySet()) {
                    data.put("param_$key", params[key])
                }

                // Execute
                if (count > 0) {
                    val responseString: String = auth.session.post(Utils.userApiUrl)
                            .body(data.map())
                            .send().readToText().replace("[<!>]".toRegex(), "").substring(2)

                    if (LOG_REQUESTS) {
                        log.error("New executing request response: {}", responseString)
                    }

                    var response: JsonObject

                    try {
                        response = toJsonObject(toJsonObject(responseString).asJsonObject.getAsJsonArray("payload").getJsonArray(1).getString(0))
                    } catch (e: JsonParseException) {
                        tmpQueue.forEach { call: CallAsync -> call.callback.onResult(null) }
                        log.error("Bad response from executing: {}, params: {}", responseString, data.toString())
                        return
                    }

                    if (!response.has("response")) {
                        log.error("No 'response' object when executing code, VK response: {}", response)
                        tmpQueue.forEach { call: CallAsync -> call.callback.onResult(null) }
                        return
                    }

                    (0..count).forEach { i: Int -> tmpQueue[i].callback.onResult(response) }
                }
            }
        }
    }
}