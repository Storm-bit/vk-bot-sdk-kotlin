package com.github.stormbit.sdk.objects.attachments

import com.google.gson.annotations.SerializedName

data class VoiceAttachment(
    @SerializedName("access_key") val accessKey: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("link_mp3") val linkMp3: String,
    @SerializedName("link_ogg") val linkOgg: String,
    @SerializedName("owner_id") val ownerId: Int,
    @SerializedName("transcript") val transcript: String,
    @SerializedName("transcript_state") val transcriptState: String,
    @SerializedName("waveform") val waveform: List<Int>
) : Attachment