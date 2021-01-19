package com.github.stormbit.sdk.objects.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Suppress("unused")
data class ServiceAction(
    @SerialName("type") val type: Type,
    @SerialName("member_id") val memberId: Int? = null,
    @SerialName("text") val conversationTitle: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("photo") val photo: SimplePhoto? = null) {

    val isActionFromEmail: Boolean get() = memberId?.let { it < 0 } ?: false

    @Serializable
    enum class Type(val value: String) {
        @SerialName("chat_photo_update") CHAT_PHOTO_UPDATE("chat_photo_update"),
        @SerialName("chat_photo_remove") CHAT_PHOTO_REMOVE("chat_photo_remove"),
        @SerialName("chat_create") CHAT_CREATE("chat_create"),
        @SerialName("chat_title_update") CHAT_TITLE_UPDATE("chat_title_update"),
        @SerialName("chat_invite_user") CHAT_INVITE_USER("chat_invite_user"),
        @SerialName("chat_kick_user") CHAT_KICK_USER("chat_kick_user"),
        @SerialName("chat_pin_message") CHAT_PIN_MESSAGE("chat_pin_message"),
        @SerialName("chat_unpin_message") CHAT_UNPIN_MESSAGE("chat_unpin_message"),
        @SerialName("chat_invite_user_by_link") CHAT_INVITE_USER_BY_LINK("chat_invite_user_by_link"),
        @SerialName("chat_screenshot") CHAT_SCREENSHOT("chat_screenshot"),
        @SerialName("chat_group_call_in_progress") CHAT_GROUP_CALL_IN_PROGRESS("group_call_in_progress"),
        @SerialName("chat_invite_user_by_call") CHAT_INVITE_USER_BY_CALL("chat_invite_user_by_call")
    }
}
