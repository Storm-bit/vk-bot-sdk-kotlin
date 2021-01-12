package com.github.stormbit.sdk.objects.attachments

import com.github.stormbit.sdk.vkapi.methods.Attachment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Audio(
    @SerialName("access_key") override val accessKey: String?,
    @SerialName("artist") val artist: String,
    @SerialName("date") val date: Int,
    @SerialName("duration") val duration: Int,
    @SerialName("id") override val id: Int,
    @SerialName("is_explicit") val isExplicit: Boolean,
    @SerialName("is_focus_track") val isFocusTrack: Boolean,
    @SerialName("main_artists") val mainArtists: List<MainArtist>? = null,
    @SerialName("owner_id") override val ownerId: Int,
    @SerialName("short_videos_allowed") val shortVideosAllowed: Boolean,
    @SerialName("stories_allowed") val storiesAllowed: Boolean,
    @SerialName("stories_cover_allowed") val storiesCoverAllowed: Boolean,
    @SerialName("title") val title: String,
    @SerialName("track_code") val trackCode: String,
    @SerialName("url") val url: String) : Attachment {

    override val typeAttachment: AttachmentType get() = AttachmentType.AUDIO

    val mp3Url: String get() {
        val url = url.replace("/index.m3u8", ".mp3")

        val parts = url.split("/")

        return parts.indices.filter { it != 4 }.joinToString("/") { parts[it] }
    }


    @Serializable
    data class MainArtist(
        @SerialName("domain") val domain: String,
        @SerialName("id") val id: String,
        @SerialName("name") val name: String
    )
}