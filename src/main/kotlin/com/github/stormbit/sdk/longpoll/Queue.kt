package com.github.stormbit.sdk.longpoll

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by Storm-bit
 *
 * Queue of updates
 */
class Queue {
    /**
     * List of updates that we need to handle
     */
    @Volatile
    var updatesUser = CopyOnWriteArrayList<JsonArray>()

    @Volatile
    var updatesGroup = CopyOnWriteArrayList<JsonObject>()

    /**
     * We add all of updates from longpoll server
     * to queue
     *
     * @param elements Array of updates
     */
    fun putAll(elements: JsonArray) {
        elements.forEach {
            if (it is JsonArray) {
                updatesUser.add(it)
            } else if (it is JsonObject) {
                updatesGroup.add(it)
            }
        }
    }
}