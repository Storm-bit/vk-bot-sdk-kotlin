package com.github.stormbit.sdk.vkapi.methods

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import kotlinx.serialization.KSerializer

abstract class MethodsContext {
    inline fun <reified T : Any> String.callSync(
        client: Client,
        serializer: KSerializer<T>,
        params: Map<String, Any>?
    ): T? = client.api.callSync(serializer, this, params)

    inline fun <reified T : Any> String.call(
        client: Client,
        serializer: KSerializer<T>,
        callback: Callback<T?>?,
        params: Map<String, Any>?
    ) = client.api.call(serializer, this, params, callback)
}