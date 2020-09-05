package com.github.stormbit.sdk.callbacks;

/**
 * Updated by Storm-bit on 01/09/2020
 */
fun interface CallbackFourth<P, D, R, S> : AbstractCallback {

    fun onEvent(p: P, d: D, r: R, s: S)
}
