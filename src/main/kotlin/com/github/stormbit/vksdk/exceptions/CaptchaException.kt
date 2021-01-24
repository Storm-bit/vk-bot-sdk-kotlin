package com.github.stormbit.vksdk.exceptions

import java.lang.Exception

class CaptchaException(override val message: String) : Exception(message)