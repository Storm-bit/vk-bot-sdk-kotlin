package com.github.stormbit.vksdk.objects.attachments

import com.github.stormbit.vksdk.vkapi.methods.Attachment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Voice(
    @SerialName("access_key") override val accessKey: String? = null,
    @SerialName("duration") val duration: Int,
    @SerialName("id") override val id: Int,
    @SerialName("link_ogg") val linkOgg: String,
    @SerialName("link_mp3") val linkMp3: String,
    @SerialName("owner_id") override val ownerId: Int,
    @SerialName("transcript") val transcript: String,
    @SerialName("transcript_state") val transcriptState: String,
    @SerialName("waveform") val waveform: List<Int>
) : Attachment {

    override val typeAttachment: AttachmentType get() = AttachmentType.DOC
}