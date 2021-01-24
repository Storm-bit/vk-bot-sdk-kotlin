package com.github.stormbit.vksdk.events.message

import com.github.stormbit.vksdk.events.MessageEvent
import com.github.stormbit.vksdk.objects.Message

data class ForwardedMessageEvent(override val message: Message) : MessageEvent