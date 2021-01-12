package com.github.stormbit.sdk.longpoll.updateshandlers

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.clients.Client.Companion.scheduler
import com.github.stormbit.sdk.events.Event
import com.github.stormbit.sdk.longpoll.Queue
import kotlinx.serialization.json.JsonArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

@Suppress("unused")
abstract class UpdatesHandler(private val client: Client) : Thread() {
    internal val log: Logger = LoggerFactory.getLogger(UpdatesHandler::class.java)

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