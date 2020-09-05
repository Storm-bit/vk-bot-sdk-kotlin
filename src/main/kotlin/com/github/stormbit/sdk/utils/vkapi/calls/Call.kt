package com.github.stormbit.sdk.utils.vkapi.calls

import org.json.JSONObject

abstract class Call {
    lateinit var methodName: String
    lateinit var params: JSONObject

    override fun toString(): String {
        return """Call{
            methodName='$methodName',
            params='$params'
        }
        """.trimIndent()
    }
}