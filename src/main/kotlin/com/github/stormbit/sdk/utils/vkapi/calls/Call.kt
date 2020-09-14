package com.github.stormbit.sdk.utils.vkapi.calls

import com.google.gson.JsonObject

abstract class Call {
    lateinit var methodName: String
    lateinit var params: JsonObject

    override fun toString(): String {
        return """Call{
            methodName='$methodName',
            params='$params'
        }
        """.trimIndent()
    }
}