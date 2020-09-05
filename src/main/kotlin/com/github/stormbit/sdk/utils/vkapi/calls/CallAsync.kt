package com.github.stormbit.sdk.utils.vkapi.calls

import com.github.stormbit.sdk.callbacks.Callback
import org.json.JSONObject
import java.util.*

class CallAsync : Call {
    val callback: Callback<JSONObject?>

    constructor(methodName: String, params: JSONObject, callback: Callback<JSONObject?>) {
        this.methodName = methodName
        this.params = params
        this.callback = callback
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CallAsync) return false

        return Objects.equals(methodName, other.methodName) &&
                Objects.equals(params.toMap(), other.params.toMap()) &&
                Objects.equals(callback, other.callback)
    }

}