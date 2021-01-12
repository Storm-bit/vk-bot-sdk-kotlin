package com.github.stormbit.sdk.objects.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
data class Address(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("address") val address: String,
    @SerialName("country_id") val countryId: Int,
    @SerialName("city_id") val cityId: Int,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("additional_address") val additionalAddress: String? = null,
    @SerialName("metro_station_id") val metroStationId: Int? = null,
    @SerialName("phone") val phone: String? = null,
    @SerialName("work_info_status") val workInfoStatus: WorkInfoStatus? = null,
    @SerialName("timetable") val timetable: Timetable? = null,
    @SerialName("time_offset") val timeOffset: Int? = null) {

    @Serializable
    enum class WorkInfoStatus(val value: String) {
        @SerialName("no_information")
        NO_INFORMATION("no_information"),

        @SerialName("temporarily_closed")
        TEMPORARILY_CLOSED("temporarily_closed"),

        @SerialName("timetable")
        TIMETABLE("timetable"),

        @SerialName("always_opened")
        ALWAYS_OPENED("always_opened"),

        @SerialName("forever_closed")
        FOREVER_CLOSED("forever_closed")
    }

    @Serializable
    data class Timetable(
        @SerialName("mon") val monday: Times? = null,
        @SerialName("tue") val tuesday: Times? = null,
        @SerialName("wed") val wednesday: Times? = null,
        @SerialName("thu") val thursday: Times? = null,
        @SerialName("fri") val friday: Times? = null,
        @SerialName("sat") val saturday: Times? = null,
        @SerialName("sun") val sunday: Times? = null) {

        @Serializable
        data class Times(
            @SerialName("open_time") val openTime: Int,
            @SerialName("close_time") val closeTime: Int,
            @SerialName("break_open_time") val breakOpenTime: Int,
            @SerialName("break_close_time") val breakCloseTime: Int
        )
    }
}