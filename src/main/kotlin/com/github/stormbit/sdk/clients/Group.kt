package com.github.stormbit.sdk.clients

import com.github.stormbit.sdk.longpoll.LongPoll
import com.github.stormbit.sdk.vkapi.Auth
import com.github.stormbit.sdk.vkapi.API
import com.github.stormbit.sdk.vkapi.Executor

/**
 * Community client
 *
 * @param accessToken Access token of VK Group
 * @param groupId ID of VK Group
 */
@Suppress("unused")
class Group(private val accessToken: String, val groupId: Int) : Client() {
    override fun auth() {
        this.auth = Auth()

        this.id = groupId
        this.token = accessToken
        this.api = API(this, Executor(this))
        this.longPoll = LongPoll(this)

        log.info("Client object created with id $id")
    }
}