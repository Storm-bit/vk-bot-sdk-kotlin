package com.github.stormbit.sdk.utils.vkapi.methods

import java.text.SimpleDateFormat
import java.util.*

class GMTDate {
    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("ddMMyyyy")

    val timestamp: Long

    constructor(day: Int) {
        calendar.set(Calendar.DAY_OF_MONTH, day)

        timestamp = calendar.timeInMillis / 1000
    }

    constructor(day: Int, month: Int) {
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month-1)

        timestamp = calendar.timeInMillis / 1000
    }

    constructor(day: Int, month: Int, year: Int) {
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month-1)
        calendar.set(Calendar.YEAR, year)

        timestamp = calendar.timeInMillis / 1000
    }

    fun toDMYString(): String {
        return formatter.format(calendar.time)
    }
}