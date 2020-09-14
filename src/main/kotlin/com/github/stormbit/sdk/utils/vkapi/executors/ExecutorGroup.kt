package com.github.stormbit.sdk.utils.vkapi.executors

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils
import com.github.stormbit.sdk.utils.vkapi.Auth
import com.github.stormbit.sdk.utils.vkapi.Executor
import com.github.stormbit.sdk.utils.vkapi.calls.CallAsync
import org.json.JSONException
import org.json.JSONObject

class ExecutorGroup(private val client: Client, auth: Auth) : Executor(auth) {
    override fun executing() {
        val tmpQueue = ArrayList<CallAsync>()
        var count = 0

        for (call in queue) {
            tmpQueue.add(call)

            count++
        }

        if (tmpQueue.isNotEmpty()) {
            for (item in tmpQueue) {
                val method = item.methodName
                val params = item.params

                queue.removeAll(tmpQueue)

                val data = JSONObject()
                data.put("v", Utils.version)
                data.put("access_token", client.token)

                for (key in params.keySet()) {
                    data.put(key, params[key])
                }

                if (count > 0) {
                    val responseString = auth.session.post("https://api.vk.com/method/$method")
                            .body(data.toMap())
                            .send().readToText().replace("[<!>]".toRegex(), "")

                    if (LOG_REQUESTS) {
                        log.info("New executing request response: {}", responseString)
                    }

                    var response: JSONObject

                    try {
                        response = JSONObject(responseString)
                    } catch (e: JSONException) {
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