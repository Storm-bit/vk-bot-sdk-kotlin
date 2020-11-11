package com.github.stormbit.sdk.events.message

import com.github.stormbit.sdk.events.Event
import com.github.stormbit.sdk.objects.Message

class VoiceMessageEvent(val message: Message) : Event
