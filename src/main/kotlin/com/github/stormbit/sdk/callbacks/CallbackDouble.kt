package com.github.stormbit.sdk.callbacks;

/**
 * Updated by Storm-bit on 01/09/2020
 */
fun interface CallbackDouble<T, R> : AbstractCallback {

    fun onEvent(t: T, r: R)
}
