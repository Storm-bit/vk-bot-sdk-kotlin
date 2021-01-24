package com.github.stormbit.vksdk.utils

import kotlinx.serialization.*
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.ClassSerialDescriptorBuilder
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.reflect.KClass

@Serializable
data class BooleanInt(val value: Boolean) {

    @Serializer(forClass = BooleanInt::class)
    companion object : KSerializer<BooleanInt> {
        override fun serialize(encoder: Encoder, value: BooleanInt) = encoder.encodeInt(if (value.value) 1 else 0)
        override fun deserialize(decoder: Decoder): BooleanInt = BooleanInt(decoder.decodeNullableSerializableValue(Int.serializer().nullable) == 1)
    }
}

interface IntEnum {
    val value: Int
}

@Suppress("FunctionName")
fun <E : Enum<E>> EnumDescriptor(clazz: KClass<E>, cases: Array<E>): SerialDescriptor {
    return buildClassSerialDescriptor(clazz.simpleName ?: "") {
        for (case in cases) element(case.name, descriptor(case))
    }
}

private fun <E : Enum<E>> ClassSerialDescriptorBuilder.descriptor(case: E): SerialDescriptor {
    return buildClassSerialDescriptor("$serialName.${case.name}")
}

abstract class EnumIntSerializer<E>(clazz: KClass<E>, cases: Array<E>) : KSerializer<E> where E : Enum<E>, E : IntEnum {
    private val caseByInt: Map<Int, E> = cases.associateBy(IntEnum::value)
    override val descriptor: SerialDescriptor = EnumDescriptor(clazz, cases)
    override fun serialize(encoder: Encoder, value: E) = encoder.encodeInt(value.value)
    override fun deserialize(decoder: Decoder): E = caseByInt.getValue(decoder.decodeInt())
}