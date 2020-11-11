package com.github.stormbit.sdk.events.message

import com.github.stormbit.sdk.events.Event
import com.github.stormbit.sdk.objects.Message

class MessageWithForwardsEvent(val message: Message) : Event