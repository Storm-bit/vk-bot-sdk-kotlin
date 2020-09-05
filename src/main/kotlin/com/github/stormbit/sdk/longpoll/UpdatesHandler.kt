package com.github.stormbit.sdk.longpoll

import com.github.stormbit.sdk.callbacks.AbstractCallback
import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.clients.Client.Companion.scheduler
import org.json.JSONArray
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

@Suppress("unused")
abstract class UpdatesHandler(private val client: Client) : Thread() {
    @Volatile
    protected var queue = Queue()

    @Volatile
    var sendTyping = false

    /**
     * Maps with callbacks
     */
    protected val callbacks = ConcurrentHashMap<String, Callback<Any>>()
    protected val chatCallbacks = ConcurrentHashMap<String, AbstractCallback>()
    protected val abstractCallbacks = ConcurrentHashMap<String, AbstractCallback>()

    fun handle(updates: JSONArray) {
        this.queue.putAll(updates)
    }

    override fun run() {
        scheduler.scheduleWithFixedDelay(this::handleCurrentUpdate, 0, 1, TimeUnit.MILLISECONDS)
    }

    /**
     * Handle one event from longpoll server
     */
    abstract fun handleCurrentUpdate()

    fun registerCallback(name: String, callback: Callback<*>) {
        this.callbacks[name] = callback as Callback<Any>
    }

    fun registerChatCallback(name: String, chatCallback: AbstractCallback) {
        this.chatCallbacks[name] = chatCallback
    }

    fun registerAbstractCallback(name: String, abstractCallback: AbstractCallback) {
        this.abstractCallbacks[name] = abstractCallback
    }

    fun callbacksCount(): Int = this.callbacks.size

    fun abstractCallbacksCount(): Int = this.abstractCallbacks.size

    fun chatCallbacksCount(): Int = this.chatCallbacks.size

    fun commandsCount(): Int = this.client.commands.size
}