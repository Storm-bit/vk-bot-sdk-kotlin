package com.github.stormbit.sdk.utils.vkapi.calls

import org.json.JSONObject
import java.util.*

class CallSync : Call {
    constructor(methodName: String, params: JSONObject) {
        this.methodName = methodName
        this.params = params
    }

    override fun toString(): String {
        return """
            Call{
                methodName='$methodName',
                params='$params'
            }
        """.trimIndent()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CallSync) return false

        return Objects.equals(methodName, other.methodName) &&
                Objects.equals(params.toMap(), other.params.toMap())
    }
}