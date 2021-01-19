package com.github.stormbit.sdk.events

import com.github.stormbit.sdk.objects.Message

@Suppress("unused")
class CommandEvent(val args: List<String>, val message: Message)