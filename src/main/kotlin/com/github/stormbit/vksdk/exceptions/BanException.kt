package com.github.stormbit.vksdk.exceptions

import java.lang.Exception

class BanException(override val message: String?) : Exception(message)