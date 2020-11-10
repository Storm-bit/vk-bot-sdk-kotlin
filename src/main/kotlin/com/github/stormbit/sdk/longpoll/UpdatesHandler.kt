package com.github.stormbit.sdk.longpoll

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.clients.Client.Companion.scheduler
import com.github.stormbit.sdk.events.Event
import com.google.gson.JsonArray
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

@Suppress("unused")
abstract class UpdatesHandler(private val client: Client) : Thread() {
    @Volatile protected var queue = Queue()

    var sendTyping = false

    /**
     * Maps with callbacks
     */
    val events = ConcurrentHashMap<String, Event.() -> Unit>()

    fun handle(updates: JsonArray) {
        this.queue.putAll(updates)
    }

    override fun run() {
        scheduler.scheduleWithFixedDelay(this::handleCurrentUpdate, 0, 1, TimeUnit.MILLISECONDS)
    }

    inline fun <reified T : Event> registerEvent(noinline callback: T.() -> Unit) {
        var parts = Regex("[A-Z][^A-Z]*").findAll(T::class.simpleName!!).map { it.value.toLowerCase() }.toList()

        parts = parts.subList(0, parts.size-1)

        val eventName = parts.joinToString("_")

        events[eventName] = callback as Event.() -> Unit
    }

    /**
     * Handle one event from longpoll server
     */
    abstract fun handleCurrentUpdate()

    fun eventsCount(): Int = this.events.size

    fun commandsCount(): Int = this.client.commands.size
}