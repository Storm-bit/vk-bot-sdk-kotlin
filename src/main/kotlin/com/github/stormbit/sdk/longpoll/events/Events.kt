package com.github.stormbit.sdk.longpoll.events

/**
 * Created by Storm-bit
 *
 * LongPoll events enum
 */
enum class Events(val value: String) {
    MESSAGE_NEW("message_new"),
    MESSAGE_REPLY("message_reply"),
    MESSAGE_EDIT("message_edit"),
    MESSAGE_TYPING_STATE("message_typing_state"),
    MESSAGE_ALLOW("message_allow"),
    MESSAGE_DENY("message_deny"),
    MESSAGE_EVENT("message_event"),

    CHAT_CREATE("chat_create"),
    CHAT_PHOTO_CHANGE("chat_photo_change"),
    CHAT_PHOTO_REMOVE("chat_photo_remove"),
    CHAT_TITLE_CHANGE("chat_title_change"),
    CHAT_JOIN("chat_join"),
    CHAT_LEAVE("chat_leave"),
    CHAT_UNPIN_MESSAGE("chat_unpin_message"),
    CHAT_PIN_MESSAGE("chat_pin_message"),

    PHOTO_NEW("photo_new"),
    PHOTO_COMMENT_NEW("photo_comment_new"),
    PHOTO_COMMENT_EDIT("photo_comment_edit"),
    PHOTO_COMMENT_RESTORE("photo_comment_restore"),
    PHOTO_COMMENT_DELETE("photo_comment_delete"),

    AUDIO_NEW("audio_new"),

    VIDEO_NEW("video_new"),
    VIDEO_COMMENT_NEW("video_comment_new"),
    VIDEO_COMMENT_EDIT("video_comment_edit"),
    VIDEO_COMMENT_RESTORE("video_comment_restore"),
    VIDEO_COMMENT_DELETE("video_comment_delete"),

    WALL_POST_NEW("wall_post_new"),
    WALL_REPOST("wall_repost"),
    WALL_REPLY_NEW("wall_reply_new"),
    WALL_REPLY_EDIT("wall_reply_edit"),
    WALL_REPLY_RESTORE("wall_reply_restore"),
    WALL_REPLY_DELETE("wall_reply_delete"),

    BOARD_POST_NEW("board_post_new"),
    BOARD_POST_EDIT("board_post_edit"),
    BOARD_POST_RESTORE("board_post_restore"),
    BOARD_POST_DELETE("board_post_delete"),

    MARKET_COMMENT_NEW("market_comment_new"),
    MARKET_COMMENT_EDIT("market_comment_edit"),
    MARKET_COMMENT_RESTORE("market_comment_restore"),
    MARKET_COMMENT_DELETE("market_comment_delete"),

    GROUP_LEAVE("group_leave"),
    GROUP_JOIN("group_join"),

    USER_BLOCK("user_block"),
    USER_UNBLOCK("user_unblock"),

    POLL_VOTE_NEW("poll_vote_new"),

    GROUP_OFFICERS_EDIT("group_officers_edit"),
    GROUP_CHANGE_SETTINGS("group_change_settings"),
    GROUP_CHANGE_PHOTO("group_change_photo"),

    VKPAY_TRANSACTION("vkpay_transaction"),

    KEYBOARD_EVENT("keyboard_event"),

    TYPING("typing"),
    FRIEND_ONLINE("friend_online"),
    FRIEND_OFFLINE("friend_offline"),
    EVERY("every");

    companion object {
        private val map = values().associateBy(Events::value)
        fun fromString(type: String) = map[type]
    }
}