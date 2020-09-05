package com.github.stormbit.sdk.callbacks;

/**
 * Created by Storm-bit on 01/09/20
 */
fun interface CallbackTriple<T, M, R> : AbstractCallback {

    fun onEvent(t: T, m: M, r: R)
}
