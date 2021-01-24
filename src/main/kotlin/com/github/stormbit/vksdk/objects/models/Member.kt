package com.github.stormbit.vksdk.objects.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    @SerialName("invited_by") val invitedBy: Int? = null,
    @SerialName("is_admin") val isAdmin: Boolean = false,
    @SerialName("join_date") val joinDate: Int? = null,
    @SerialName("member_id") val memberId: Int,
    @SerialName("can_kick") val canKick: Boolean = false,
    @SerialName("is_owner") val isOwner: Boolean = false
)