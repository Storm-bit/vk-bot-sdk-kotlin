package com.github.stormbit.vksdk.events

import com.github.stormbit.vksdk.objects.Message

@Suppress("unused")
class CommandEvent(val args: List<String>, val message: Message)