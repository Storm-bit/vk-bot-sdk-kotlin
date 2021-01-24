package com.github.stormbit.vksdk.events.chat

import com.github.stormbit.vksdk.events.ServiceActionEvent
import com.github.stormbit.vksdk.objects.Message

class ChatGroupCallInProgressEvent(override val message: Message) : ServiceActionEvent