package com.github.stormbit.vksdk.vkapi.methods

import com.github.stormbit.vksdk.clients.Client
import com.github.stormbit.vksdk.vkapi.VkApiRequest
import io.ktor.http.*
import kotlinx.serialization.KSerializer

abstract class MethodsContext(private val client: Client) {
    fun <T : Any> String.call(
        serializer: KSerializer<T>,
        block: ParametersBuilder.() -> Unit = {}
    ): VkApiRequest<T> = VkApiRequest(
        client,
        HttpMethod.Get,
        this,
        Parameters.build(block),
        serializer
    )
}