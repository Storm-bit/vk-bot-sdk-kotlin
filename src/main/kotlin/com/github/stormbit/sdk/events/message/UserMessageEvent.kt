package com.github.stormbit.sdk.events.message

import com.github.stormbit.sdk.events.MessageEvent
import com.github.stormbit.sdk.objects.Message

data class UserMessageEvent(override val message: Message) : MessageEvent
