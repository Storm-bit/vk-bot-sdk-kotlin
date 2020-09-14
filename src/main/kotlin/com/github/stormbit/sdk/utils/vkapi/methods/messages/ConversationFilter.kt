package com.github.stormbit.sdk.utils.vkapi.methods.messages

import java.io.Serializable

@Suppress("unused")
enum class ConversationFilter(val value: String) : Serializable {
    ALL("all"),
    UNREAD("unread"),
    IMPORTANT("important"),
    UNANSWERED("unanswered");
}