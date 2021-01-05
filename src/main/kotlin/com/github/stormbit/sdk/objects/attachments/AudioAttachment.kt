package com.github.stormbit.sdk.objects.attachments

import com.google.gson.annotations.SerializedName

data class AudioAttachment(
    @SerializedName("artist") val artist: String,
    @SerializedName("date") val date: Int,
    @SerializedName("duration") val duration: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("is_explicit") val isExplicit: Boolean,
    @SerializedName("is_focus_track") val isFocusTrack: Boolean,
    @SerializedName("main_artists") val mainArtists: List<MainArtist>,
    @SerializedName("owner_id") val ownerId: Int,
    @SerializedName("short_videos_allowed") val shortVideosAllowed: Boolean,
    @SerializedName("stories_allowed") val storiesAllowed: Boolean,
    @SerializedName("stories_cover_allowed") val storiesCoverAllowed: Boolean,
    @SerializedName("title") val title: String,
    @SerializedName("track_code") val trackCode: String,
    @SerializedName("url") val url: String
) : Attachment {
    data class MainArtist(
        @SerializedName("domain") val domain: String,
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String
    )
}