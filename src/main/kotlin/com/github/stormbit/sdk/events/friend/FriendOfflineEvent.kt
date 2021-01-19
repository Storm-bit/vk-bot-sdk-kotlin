package com.github.stormbit.sdk.events.friend

import com.github.stormbit.sdk.events.Event

data class FriendOfflineEvent(val userId: Int, val timestamp: Int) : Event