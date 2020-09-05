package com.github.stormbit.sdk.callbacks;

/**
 * For all com.github.stormbit.sdk.callbacks compatibility.
 */
fun interface Callback<T : Any?> {

    fun onResult(obj: T)
}
