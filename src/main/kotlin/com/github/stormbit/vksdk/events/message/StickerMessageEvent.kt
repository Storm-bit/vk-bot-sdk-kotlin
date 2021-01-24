package com.github.stormbit.vksdk.events.message

import com.github.stormbit.vksdk.events.AttachmentEvent
import com.github.stormbit.vksdk.objects.Message

data class StickerMessageEvent(override val message: Message) : AttachmentEvent