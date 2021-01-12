package com.github.stormbit.sdk.vkapi.calls

abstract class Call {
    lateinit var methodName: String
    lateinit var params: Map<String, Any>

    override fun toString(): String {
        return """Call{
            methodName='$methodName',
            params='$params'
        }
        """.trimIndent()
    }
}