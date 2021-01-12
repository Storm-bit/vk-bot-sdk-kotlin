package com.github.stormbit.sdk.objects.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    @SerialName("invited_by") val invitedBy: Int? = null,
    @SerialName("is_admin") val isAdmin: Boolean? = null,
    @SerialName("is_owner") val isOwner: Boolean? = null,
    @SerialName("join_date") val joinDate: Int? = null,
    @SerialName("member_id") val memberId: Int? = null
)