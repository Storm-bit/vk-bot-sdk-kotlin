package com.github.stormbit.vksdk.vkapi.methods.docs

import com.github.stormbit.vksdk.objects.attachments.Document
import com.github.stormbit.vksdk.objects.attachments.Voice
import com.github.stormbit.vksdk.objects.models.Graffiti
import com.github.stormbit.vksdk.objects.models.enums.DocTypes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocumentSaveResponse(
    @SerialName("type") val type: DocTypes,
    @SerialName("graffiti") val graffiti: Graffiti? = null,
    @SerialName("audio_message") val voice: Voice? = null,
    @SerialName("doc") val document: Document? = null
)

@Serializable
data class DocType(
    @SerialName("count") val count: Int,
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)