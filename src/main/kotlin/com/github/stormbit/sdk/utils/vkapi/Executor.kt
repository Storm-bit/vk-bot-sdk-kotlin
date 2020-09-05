package com.github.stormbit.sdk.utils.vkapi

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.vkapi.calls.Call
import com.github.stormbit.sdk.utils.vkapi.calls.CallAsync
import com.github.stormbit.sdk.utils.vkapi.calls.CallSync
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

abstract class Executor(protected val auth: Auth) {
    val log = LoggerFactory.getLogger(Executor::class.java)

    @Volatile
    protected var queue = ArrayList<CallAsync>()

    companion object {
        var LOG_REQUESTS = false

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
    protected abstract fun executing()

    /**
     * Method that makes string in json format from call object.
     *
     * @param call Call object
     * @return String 'API.method.name({param:value})'
     * @see Call
     *
     * @see CallAsync
     *
     * @see CallSync
     */
    fun codeForExecute(call: Call): String {
        return "API." + call.methodName + '(' + call.params.toString() + ')'
    }

    /**
     * Method that puts all requests in a queue.
     *
     * @param call Call to be executed.
     */
    fun execute(call: CallAsync) {
        queue.add(call)
    }
}