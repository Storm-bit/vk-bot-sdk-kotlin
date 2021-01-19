package com.github.stormbit.sdk.vkapi

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class Bitmask(open var mask: Int = 0) {
    fun getValue(bit: Int): Boolean = mask and bit != 0
    fun setValue(bit: Int) { mask = mask or bit }
    fun clearValue(bit: Int) { mask = mask xor bit }

    fun bitmask(bit: Int): BitmaskDelegate = BitmaskDelegate(bit)

    class BitmaskDelegate(private val bit: Int) : ReadWriteProperty<Bitmask, Boolean> {
        override fun getValue(thisRef: Bitmask, property: KProperty<*>): Boolean = thisRef.getValue(bit)
        override fun setValue(thisRef: Bitmask, property: KProperty<*>, value: Boolean) = if (value) thisRef.setValue(bit) else thisRef.clearValue(bit)
    }
}