package com.github.stormbit.sdk.utils.vkapi.calls

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.utils.Utils.Companion.map
import com.google.gson.JsonObject
import java.util.*

class CallAsync : Call {
    val callback: Callback<JsonObject?>

    constructor(methodName: String, params: JsonObject, callback: Callback<JsonObject?>) {
        this.methodName = methodName
        this.params = params
        this.callback = callback
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CallAsync) return false

        return Objects.equals(methodName, other.methodName) &&
                Objects.equals(params.map(), other.params.map()) &&
                Objects.equals(callback, other.callback)
    }

}