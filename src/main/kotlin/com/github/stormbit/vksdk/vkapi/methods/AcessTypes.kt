@file:Suppress("unused")

package com.github.stormbit.vksdk.vkapi.methods

import com.github.stormbit.vksdk.utils.EnumIntSerializer
import com.github.stormbit.vksdk.utils.IntEnum
import kotlinx.serialization.Serializable

@Serializable(with = PublicUnitAccessType.Companion::class)
enum class PublicUnitAccessType(override val value: Int) : IntEnum {
    DISABLED(0),
    OPEN(1);

    companion object : EnumIntSerializer<PublicUnitAccessType>(PublicUnitAccessType::class, values())
}

@Serializable(with = GroupUnitAccessType.Companion::class)
enum class GroupUnitAccessType(override val value: Int) : IntEnum {
    DISABLED(0),
    OPEN(1),
    LIMITED(2);

    companion object : EnumIntSerializer<GroupUnitAccessType>(GroupUnitAccessType::class, values())
}

@Serializable(with = GroupUnitAccessTypeExtended.Companion::class)
enum class GroupUnitAccessTypeExtended(override val value: Int) : IntEnum {
    DISABLED(0),
    OPEN(1),
    LIMITED(2),
    CLOSED(3);

    companion object : EnumIntSerializer<GroupUnitAccessTypeExtended>(GroupUnitAccessTypeExtended::class, values())
}