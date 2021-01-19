package com.github.stormbit.sdk.longpoll.updateshandlers

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.events.Event
import kotlinx.coroutines.channels.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

@Suppress("unused")
abstract class UpdatesHandler<T>(private val client: Client) {
    internal val log: Logger = LoggerFactory.getLogger(UpdatesHandler::class.java)

    internal var sendTyping = false

    /**
     * Maps with callbacks
     */
    internal val events = ConcurrentHashMap<String, suspend Event.() -> Unit>()

    suspend fun start() {
        eventAllocator.consumeEach {
            handleCurrentUpdate(it)
        }
    }

    fun <T : Event> registerEvent(callback: suspend T.() -> Unit, type: KClass<T>) {
        var parts = Regex("[A-Z][^A-Z]*").findAll(type.simpleName!!).map { it.value.toLowerCase() }.toList()

        if (parts.size != 2) {
            parts = parts.dropLast(1)
        }

        val eventName = parts.joinToString("_")

        events[eventName] = callback as suspend Event.() -> Unit
    }

    private val eventAllocator: Channel<T> = Channel()

    internal suspend fun send(obj: T) {
        eventAllocator.send(obj)
    }

    /**
     * Handle one event senderType LongPoll server
     */
    abstract suspend fun handleCurrentUpdate(currentUpdate: T)

    val eventsCount: Int get() = this.events.size
}