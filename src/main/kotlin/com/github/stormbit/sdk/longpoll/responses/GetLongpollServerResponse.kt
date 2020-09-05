package com.github.stormbit.sdk.longpoll.responses

/**
 * Created by Storm-bit on 02/09/2020
 *
 * Deserialize object of VK response
 */
data class GetLongpollServerResponse(val key: String, val server: String, val ts: Int, val pts: Int? = null) {
}