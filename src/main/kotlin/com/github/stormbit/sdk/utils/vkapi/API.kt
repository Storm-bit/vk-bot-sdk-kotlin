package com.github.stormbit.sdk.utils.vkapi

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.utils.gson
import com.github.stormbit.sdk.utils.put
import com.github.stormbit.sdk.utils.vkapi.calls.CallAsync
import com.github.stormbit.sdk.utils.vkapi.calls.CallSync
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

@Suppress("unused")
abstract class API(protected val executor: Executor) {
    protected val log: Logger = LoggerFactory.getLogger(API::class.java)

    private var executionStarted = false

    init {
        if (!executionStarted) executionStarted = true
    }

    abstract fun call(method: String, params: Any?, callback: Callback<JsonObject?>)

    abstract fun call(method: String, callback: Callback<JsonObject?>, vararg params: Any?)

    fun execute(code: String): JsonObject {
        return gson.toJsonTree(callSync("execute", JsonObject().put("code", code))).asJsonObject
    }

    fun execute(vararg calls: CallAsync) {
        if (calls.size < 26) {
            for (call in calls) {
                executor.execute(call)
            }
        } else {
            val newCalls = arrayOfNulls<CallAsync>(25)
            System.arraycopy(calls, 0, newCalls, 0, 25)

            for (call in newCalls) {
                executor.execute(call!!)
            }
        }
    }

    fun execute(vararg calls: CallSync): JsonArray {
        val code = StringBuilder("return [")
        for (i in calls.indices) {
            val codeTmp: String = executor.codeForExecute(calls[i])
            code.append(codeTmp)
            if (i < calls.size - 1) {
                code.append(',')
            }
        }
        code.append("];")

        var response: JsonObject? = null

        try {
            response = gson.toJsonTree(callSync("execute", JsonObject().put("code", URLEncoder.encode(code.toString(), "UTF-8")))).asJsonObject
        } catch (ignored: UnsupportedEncodingException) {
        }

        return response!!.getAsJsonArray("response")
    }

    /**
     * Call to VK API
     *
     * @param method Method name
     * @param params Params as string, JSONObject or Map
     * @return JSONObject response of VK answer
     */
    abstract fun callSync(method: String, params: Any?): JsonObject

    /**
     * Call to VK API
     *
     * @param method Method name
     * @param params Floating count of params
     * @return JSONObject response of VK answer
     */
    abstract fun callSync(method: String, vararg params: Any?): JsonObject
}