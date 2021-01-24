package com.github.stormbit.vksdk.events.friend

import com.github.stormbit.vksdk.events.Event

data class FriendOnlineEvent(val userId: Int, val timestamp: Int) : Event