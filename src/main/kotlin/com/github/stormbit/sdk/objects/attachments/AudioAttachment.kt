package com.github.stormbit.sdk.objects.attachments

import com.google.gson.annotations.SerializedName

data class AudioAttachment(
    val artist: String,
    val date: Int,
    val duration: Int,
    val id: Int,
    @SerializedName("is_explicit") val isExplicit: Boolean,
    @SerializedName("is_focus_track") val isFocusTrack: Boolean,
    @SerializedName("main_artists") val mainArtists: List<MainArtist>,
    @SerializedName("owner_id") val ownerId: Int,
    @SerializedName("short_videos_allowed") val shortVideosAllowed: Boolean,
    @SerializedName("stories_allowed") val storiesAllowed: Boolean,
    @SerializedName("stories_cover_allowed") val storiesCoverAllowed: Boolean,
    val title: String,
    @SerializedName("track_code") val trackCode: String,
    val url: String
) : Attachment {
    data class MainArtist(
        val domain: String,
        val id: String,
        val name: String
    )
}

