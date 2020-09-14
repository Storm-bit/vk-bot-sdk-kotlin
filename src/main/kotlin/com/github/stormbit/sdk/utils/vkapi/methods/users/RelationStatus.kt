package com.github.stormbit.sdk.utils.vkapi.methods.users

import com.github.stormbit.sdk.utils.EnumIntSerializer
import com.github.stormbit.sdk.utils.Utils
import kotlinx.serialization.Serializable

@Serializable(with = RelationStatus.Companion::class)
@Suppress("unused")
enum class RelationStatus(override val value: Int) : Utils.Companion.IntEnum {
    SINGLE(1),
    RELATIONSHIP(2),
    ENGAGED(3),
    MARRIED(4),
    COMPLICATED(5),
    ACTIVELY_SEARCHING(6),
    LOVE(7),
    CIVIL_UNION(8),
    NOT_SPECIFIED(0);

    companion object : EnumIntSerializer<RelationStatus>(RelationStatus::class, values())
}