package com.github.stormbit.sdk.exceptions

import java.lang.Exception

class CaptchaException(override val message: String) : Exception(message)