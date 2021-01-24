package com.github.stormbit.vksdk.longpoll.updateshandlers

import com.github.stormbit.vksdk.events.Event
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.serialization.json.JsonElement
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

abstract class UpdatesHandler<E : JsonElement> {
    internal val log: Logger = LoggerFactory.getLogger(UpdatesHandler::class.java)

    internal var sendTyping = false

    /**
     * Maps with callbacks
     */
    internal val events = ConcurrentHashMap<String, suspend Event.() -> Unit>()

    suspend fun start() {
        eventAllocator = CoroutineScope(Dispatchers.IO).actor {
            for (event in channel) {
                handleCurrentUpdate(event)
            }
        }
    }

    @Suppress("unchecked_cast")
    fun <T : Event> registerEvent(callback: suspend T.() -> Unit, type: KClass<T>) {
        var parts = Regex("[A-Z][^A-Z]*").findAll(type.simpleName!!).map { it.value.toLowerCase() }.toList()

        if (parts.size != 2) {
            parts = parts.dropLast(1)
        }

        val eventName = parts.joinToString("_")

        events[eventName] = callback as suspend Event.() -> Unit
    }

    private lateinit var eventAllocator: SendChannel<E>

    internal suspend fun send(obj: E) = eventAllocator.send(obj)

    /**
     * Handle one event senderType LongPoll server
     */
    abstract suspend fun handleCurrentUpdate(currentUpdate: E)

    val eventsCount: Int get() = this.events.size
}