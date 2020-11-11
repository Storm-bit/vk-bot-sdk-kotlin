package com.github.stormbit.sdk.events.message

import com.github.stormbit.sdk.events.Event
import com.github.stormbit.sdk.objects.Message

class ChatMessageEvent(val message: Message) : Event