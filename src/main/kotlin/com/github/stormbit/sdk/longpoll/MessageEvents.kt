package com.github.stormbit.sdk.longpoll

enum class MessageEvents(val value: String) {
    VOICE_MESSAGE("on_voice_message"),
    DOC_MESSAGE("on_doc_message"),
    AUDIO_MESSAGE("on_audio_message"),
    VIDEO_MESSAGE("on_video_message"),
    WALL_MESSAGE("on_wall_message"),
    PHOTO_MESSAGE("on_photo_message"),
    LINK_MESSAGE("on_link_message"),
    STICKER_MESSAGE("on_sticker_message"),
    SIMPLE_TEXT_MESSAGE("on_text_message"),
    MESSAGE("on_message"),
    CHAT_MESSAGE("on_chat_message"),
    MESSAGE_WITH_FORWARDS("on_message_with_forwards");
}