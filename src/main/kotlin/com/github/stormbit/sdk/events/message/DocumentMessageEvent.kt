package com.github.stormbit.sdk.events.message

import com.github.stormbit.sdk.events.AttachmentEvent
import com.github.stormbit.sdk.objects.Message

data class DocumentMessageEvent(override val message: Message) : AttachmentEvent