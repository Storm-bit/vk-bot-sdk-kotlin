package com.github.stormbit.sdk.events.chat

import com.github.stormbit.sdk.events.ServiceActionEvent
import com.github.stormbit.sdk.objects.Message

class ChatGroupCallInProgressEvent(override val message: Message) : ServiceActionEvent