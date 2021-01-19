package com.github.stormbit.sdk.clients

import com.github.stormbit.sdk.vkapi.Auth

/**
 * Community client
 *
 * @param accessToken Access token of VK Group
 * @param groupId ID of VK Group
 */
@Suppress("unused")
class GroupClient(private val accessToken: String, val groupId: Int) : Client()  {
    override suspend fun auth() {
        this.token = accessToken
        this.id = groupId
        this.auth = Auth()

        super.auth()

        log.info("Client object created with id $id")
    }
}