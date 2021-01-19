package com.github.stormbit.sdk.events.friend

import com.github.stormbit.sdk.events.Event

data class FriendOnlineEvent(val userId: Int, val timestamp: Int) : Event