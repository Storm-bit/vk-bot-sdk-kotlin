package com.github.stormbit.vksdk.events.chat

import com.github.stormbit.vksdk.events.ServiceActionEvent
import com.github.stormbit.vksdk.objects.Message

class ChatInviteUserByLinkEvent(val userId: Int, override val message: Message) : ServiceActionEvent