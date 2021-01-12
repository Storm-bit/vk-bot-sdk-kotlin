package com.github.stormbit.sdk.vkapi.calls

import com.github.stormbit.sdk.callbacks.Callback
import kotlinx.serialization.KSerializer
import java.util.*

class CallAsync<T : Any> : Call {
    val serializer: KSerializer<T>
    val callback: Callback<T?>?

    constructor(methodName: String, params: Map<String, Any>, callback: Callback<T?>?, serializer: KSerializer<T>) {
        this.methodName = methodName
        this.params = params
        this.callback = callback
        this.serializer = serializer
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CallAsync<*>) return false

        return Objects.equals(methodName, other.methodName) &&
                Objects.equals(params.toMap(), other.params.toMap()) &&
                Objects.equals(callback, other.callback)
    }

    override fun hashCode(): Int {
        var result = serializer.hashCode()
        result = 31 * result + callback.hashCode()
        return result
    }
}