package com.github.stormbit.sdk.vkapi.methods.users

import com.github.stormbit.sdk.utils.EnumIntSerializer
import com.github.stormbit.sdk.utils.IntEnum
import kotlinx.serialization.Serializable

@Serializable(with = Sex.Companion::class)
@Suppress("unused")
enum class Sex(override val value: Int) : IntEnum {
    FEMALE(1),
    MALE(2),
    NOT_SPECIFIED(0);

    companion object : EnumIntSerializer<Sex>(Sex::class, values())
}