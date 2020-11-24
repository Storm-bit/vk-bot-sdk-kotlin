package com.github.stormbit.sdk.utils.vkapi.executors

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils
import com.github.stormbit.sdk.utils.Utils.Companion.map
import com.github.stormbit.sdk.utils.Utils.Companion.shift
import com.github.stormbit.sdk.utils.put
import com.github.stormbit.sdk.utils.vkapi.Auth
import com.github.stormbit.sdk.utils.vkapi.Executor
import com.google.gson.JsonObject
import com.google.gson.JsonParseException

class ExecutorGroup(private val client: Client, auth: Auth) : Executor(auth) {
    override fun executing() {
        val call = if (queue.isNotEmpty()) queue.shift()!! else return

        val method = call.methodName
        val params = call.params

        val data = JsonObject()
        data.put("v", Utils.VK_API_VERSION)
        data.put("access_token", client.token!!)

        for (key in params.keySet()) {
            data.put(key, params[key])
        }

        val responseString = auth.session.post("https://api.vk.com/method/$method")
                .body(data.map())
                .send().readToText().replace("[<!>]".toRegex(), "")

        if (LOG_REQUESTS) {
            log.info("New executing request response: {}", responseString)
        }

        val response: JsonObject

        try {
            response = Utils.toJsonObject(responseString)
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