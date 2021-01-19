package com.github.stormbit.sdk.objects.models.enums

import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
enum class ConversationFilter(val value: String) {
    ALL("all"),
    UNREAD("unread"),
    IMPORTANT("important"),
    UNANSWERED("unanswered");
}