package com.github.stormbit.sdk.events

import com.github.stormbit.sdk.objects.Message

interface AttachmentEvent : MessageEvent {
    override val message: Message
}