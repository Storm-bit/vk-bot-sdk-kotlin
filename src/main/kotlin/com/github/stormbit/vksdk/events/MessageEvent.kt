package com.github.stormbit.vksdk.events

import com.github.stormbit.vksdk.objects.Message

interface MessageEvent : Event {
    val message: Message
}