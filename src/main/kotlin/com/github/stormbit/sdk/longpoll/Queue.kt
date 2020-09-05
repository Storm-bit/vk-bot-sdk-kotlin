package com.github.stormbit.sdk.longpoll

import org.json.JSONArray
import org.json.JSONObject
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
    var updatesUser = CopyOnWriteArrayList<JSONArray>()
    @Volatile
    var updatesGroup = CopyOnWriteArrayList<JSONObject>()

    /**
     * We add all of updates from longpoll server
     * to queue
     *
     * @param elements Array of updates
     */
    fun putAll(elements: JSONArray) {
        elements.forEach {
            if (it is JSONArray) {
                updatesUser.add(it)
            } else if (it is JSONObject) {
                updatesGroup.add(it)
            }
        }
    }
}