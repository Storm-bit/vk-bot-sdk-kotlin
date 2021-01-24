package com.github.stormbit.vksdk.clients

import io.ktor.client.*

/**
 * Community client
 *
 * @param accessToken Access token of VK Group
 * @param groupId ID of VK Group
 */
@Suppress("unused")
class GroupClient(private val accessToken: String, val groupId: Int, httpClient: HttpClient) : Client(httpClient)  {
    override suspend fun auth() {
        this.token = accessToken
        this.id = groupId

        super.auth()

        log.info("Client object created with id $id")
    }
}