package com.github.stormbit.sdk.utils.vkapi

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.utils.vkapi.calls.CallAsync
import com.github.stormbit.sdk.utils.vkapi.calls.CallSync
import org.json.JSONArray
import org.json.JSONObject
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

    abstract fun call(method: String, params: Any?, callback: Callback<JSONObject?>)

    fun execute(code: String): JSONObject {
        return JSONObject(callSync("execute", JSONObject().put("code", code)))
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

    fun execute(vararg calls: CallSync): JSONArray {
        val code = StringBuilder("return [")
        for (i in calls.indices) {
            val codeTmp: String = executor.codeForExecute(calls[i])
            code.append(codeTmp)
            if (i < calls.size - 1) {
                code.append(',')
            }
        }
        code.append("];")

        var response: JSONObject? = null

        try {
            response = JSONObject(callSync("execute", JSONObject().put("code", URLEncoder.encode(code.toString(), "UTF-8"))))
        } catch (ignored: UnsupportedEncodingException) {
        }

        return response!!.getJSONArray("response")
    }

    /**
     * Call to VK API
     *
     * @param method Method name
     * @param params Params as string, JSONObject or Map
     * @return JSONObject response of VK answer
     */
    abstract fun callSync(method: String, params: Any?): JSONObject

    /**
     * Call to VK API
     *
     * @param method Method name
     * @param params Floating count of params
     * @return JSONObject response of VK answer
     */
    abstract fun callSync(method: String, vararg params: Any?): JSONObject
}