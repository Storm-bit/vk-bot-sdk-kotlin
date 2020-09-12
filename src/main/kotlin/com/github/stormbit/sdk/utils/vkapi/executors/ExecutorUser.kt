package com.github.stormbit.sdk.utils.vkapi.executors

import com.github.stormbit.sdk.utils.Utils
import com.github.stormbit.sdk.utils.vkapi.Auth
import com.github.stormbit.sdk.utils.vkapi.Executor
import com.github.stormbit.sdk.utils.vkapi.calls.CallAsync
import org.json.JSONException
import org.json.JSONObject
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
            for (item in tmpQueue) {
                val method = item.methodName
                val params = item.params

                if (!Utils.hashes.has(method)) {
                    Utils.getHash(auth, method)
                }

                queue.removeAll(tmpQueue)

                val data = JSONObject()
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
                            .body(data.toMap())
                            .send().readToText().replace("[<!>]", "").substring(2)

                    if (LOG_REQUESTS) {
                        log.error("New executing request response: {}", responseString)
                    }

                    var response: JSONObject

                    try {
                        response = JSONObject(JSONObject(responseString).getJSONArray("payload").getJSONArray(1).getString(0))
                    } catch (e: JSONException) {
                        tmpQueue.forEach { call: CallAsync -> call.callback.onResult(null) }
                        log.error("Bad response from executing: {}, params: {}", responseString, data.toString())
                        return
                    }

                    if (!response.has("response")) {
                        log.error("No 'response' object when executing code, VK response: {}", response)
                        tmpQueue.forEach { call: CallAsync -> call.callback.onResult(null) }
                        return
                    }

                    val responses = response.getJSONObject("response")

                    (0..count).forEach { i: Int -> tmpQueue[i].callback.onResult(responses) }
                }
            }
        }
    }
}