package com.github.stormbit.sdk.longpoll

enum class MessageEvents(val value: String) {
    VOICE_MESSAGE("voice_message"),
    DOC_MESSAGE("doc_message"),
    AUDIO_MESSAGE("audio_message"),
    VIDEO_MESSAGE("video_message"),
    WALL_MESSAGE("wall_message"),
    PHOTO_MESSAGE("photo_message"),
    LINK_MESSAGE("link_message"),
    STICKER_MESSAGE("sticker_message"),
    SIMPLE_TEXT_MESSAGE("text_message"),
    MESSAGE("message_new"),
    CHAT_MESSAGE("chat_message"),
    MESSAGE_WITH_FORWARDS("message_with_forwards");
}