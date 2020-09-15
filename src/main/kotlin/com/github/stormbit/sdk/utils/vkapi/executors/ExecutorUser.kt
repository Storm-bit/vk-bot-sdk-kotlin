package com.github.stormbit.sdk.utils.vkapi.executors

import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.utils.Utils.Companion.map
import com.github.stormbit.sdk.utils.Utils.Companion.shift
import com.github.stormbit.sdk.utils.vkapi.Auth
import com.github.stormbit.sdk.utils.vkapi.Executor
import com.google.gson.JsonObject
import com.google.gson.JsonParseException

class ExecutorUser(auth: Auth) : Executor(auth) {
    override fun executing() {
        val call = if (queue.isNotEmpty()) queue.shift()!! else return

        val method = call.methodName
        val params = call.params

        if (!Utils.hashes.has(method)) {
            Utils.getHash(auth, method)
        }

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
        val responseString: String = auth.session.post(Utils.userApiUrl)
                .body(data.map())
                .send().readToText().replace("[<!>]".toRegex(), "").substring(2)

        if (LOG_REQUESTS) {
            log.error("New executing request response: {}", responseString)
        }

        val response: JsonObject

        try {
            response = Utils.toJsonObject(Utils.toJsonObject(responseString).getAsJsonArray("payload").getJsonArray(1).getString(0))
        } catch (e: JsonParseException) {
            call.callback.onResult(null)
            log.error("Bad response from executing: {}, params: {}", responseString, data.toString())
            return
        }

        if (!response.has("response")) {
            log.error("No 'response' object when executing code, VK response: {}", response)
            call.callback.onResult(null)
            return
        }

        call.callback.onResult(response)
    }
}