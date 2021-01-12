package com.github.stormbit.sdk.vkapi.methods

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadServerResponse(
    @SerialName("upload_url") val uploadUrl: String
)