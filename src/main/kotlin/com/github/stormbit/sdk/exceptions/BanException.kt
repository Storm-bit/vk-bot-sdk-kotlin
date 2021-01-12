package com.github.stormbit.sdk.exceptions

import java.lang.Exception

class BanException(override val message: String?) : Exception(message)