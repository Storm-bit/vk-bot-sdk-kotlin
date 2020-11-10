package com.github.stormbit.sdk.callbacks;

fun interface Callback<T : Any?> {

    fun onResult(obj: T)
}
