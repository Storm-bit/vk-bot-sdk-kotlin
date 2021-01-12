package com.github.stormbit.sdk.vkapi.methods.docs

import com.github.stormbit.sdk.objects.attachments.Voice
import com.github.stormbit.sdk.objects.attachments.Document
import com.github.stormbit.sdk.objects.models.Graffiti
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocumentSaveResponse(
    @SerialName("type") val type: DocTypes,
    @SerialName("graffiti") val graffiti: Graffiti? = null,
    @SerialName("audio_message") val voice: Voice? = null,
    @SerialName("doc") val document: Document? = null
)