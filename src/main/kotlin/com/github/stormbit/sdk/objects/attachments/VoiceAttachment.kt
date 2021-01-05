package com.github.stormbit.sdk.objects.attachments

import com.google.gson.annotations.SerializedName

data class VoiceAttachment(
    @SerializedName("access_key") val accessKey: String,
    val duration: Int,
    val id: Int,
    val link_mp3: String,
    val link_ogg: String,
    @SerializedName("owner_id") val ownerId: Int,
    val transcript: String,
    @SerializedName("transcript_state") val transcriptState: String,
    val waveform: List<Int>
) : Attachment