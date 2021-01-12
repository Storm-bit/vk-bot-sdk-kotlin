package com.github.stormbit.sdk.vkapi

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.utils.TIME_OUT
import com.github.stormbit.sdk.utils.json
import com.github.stormbit.sdk.vkapi.calls.Call
import com.github.stormbit.sdk.vkapi.calls.CallAsync
import kotlinx.serialization.encodeToString
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

class Executor(private val client: Client) {
    internal val log: Logger = LoggerFactory.getLogger(Executor::class.java)

    @Volatile
    internal var queue = CopyOnWriteArrayList<CallAsync<Any>>()

    companion object {
        /**
         * We can call 'execute' method no more than three times per second.
         * 1000/3 ~ 333 milliseconds
         */
        const val delay = 335
    }

    init {
        Client.scheduler.scheduleWithFixedDelay(this::executing, 0, delay.toLong(), TimeUnit.MILLISECONDS)
    }

    /**
     * Method that makes 'execute' requests
     * with first 25 calls from queue.
     */
    private fun executing() {
        val call = if (queue.isNotEmpty()) queue.shift()!! else return

        try {
            val method = call.methodName
            val params = call.params

            val data = mutableMapOf<String, Any?>(
                "v" to VK_API_VERSION,
                "access_token" to client.token
            )

            data.putAll(client.values)
            data.putAll(params)

            val responseString: String = client.auth.session.post(BASE_API_URL + method)
                .body(data)
                .headers(client.headers)
                .timeout(TIME_OUT)
                .send().readToText()

            if (responseString.contains("error")) {
                throw Exception(responseString)
            }

            val response = responseString.toJsonObject()["response"]!!

            val result = json.decodeFromJsonElement(call.serializer, response)

            call.callback?.onResult(result)
        } catch (e: Exception) {
            log.error("Some error occurred when calling VK API method '${call.methodName}', error is: ", e)
            call.callback?.onResult(null)
        }
    }

    /**
     * Method that makes string in json format from call object.
     *
     * @param call Call object
     * @return String 'API.method.name({param:value})'
     */
    fun codeForExecute(call: Call): String {
        return "return API." + call.methodName + '(' + json.encodeToString(call.params) + ')' + ';'
    }

    /**
     * Method that puts all requests in a queue.
     *
     * @param call Call to be executed.
     */
    fun execute(call: CallAsync<Any>) {
        queue.add(call)
    }
}