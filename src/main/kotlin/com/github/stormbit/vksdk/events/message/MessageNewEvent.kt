package com.github.stormbit.vksdk.events.message

import com.github.stormbit.vksdk.events.Event
import com.github.stormbit.vksdk.objects.Message

data class MessageNewEvent(val message: Message) : Event
