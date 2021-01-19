package com.github.stormbit.sdk.events

import com.github.stormbit.sdk.objects.Message

interface MessageEvent : Event {
    val message: Message
}