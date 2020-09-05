package com.github.stormbit.sdk.utils.vkapi.methods.users;

import com.github.stormbit.sdk.utils.EnumIntSerializer
import com.github.stormbit.sdk.utils.Utils
import kotlinx.serialization.Serializable;

@Serializable(with = Sex.Companion::class)
enum class Sex(override val value: Int) : Utils.Companion.IntEnum {
    FEMALE(1),
    MALE(2),
    NOT_SPECIFIED(0);

    companion object : EnumIntSerializer<Sex>(Sex::class, values())
}