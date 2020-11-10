package com.github.stormbit.sdk.utils.vkapi.methods.video

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.call
import com.github.stormbit.sdk.utils.attachmentString
import com.github.stormbit.sdk.utils.mediaString
import com.github.stormbit.sdk.utils.put
import com.github.stormbit.sdk.utils.vkapi.methods.*
import com.google.gson.JsonObject

@Suppress("unused")
class VideoApiAsync(private val client: Client) {
    fun add(
            videoId: Int,
            ownerId: Int,
            targetId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.add.call(client, callback, JsonObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("target_id", targetId)
    )

    fun addAlbum(
            title: String,
            privacy: PrivacySettings,
            groupId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.addAlbum.call(client, callback, JsonObject()
            .put("title", title)
            .put("privacy", privacy.toRequestString())
            .put("group_id", groupId)
    )

    fun addToAlbum(
            videoId: Int,
            ownerId: Int,
            albumIds: List<VideoAlbumType>,
            targetId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.addToAlbum.call(client, callback, JsonObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("album_ids", albumIds.joinToString(",") { it.value.toString() })
            .put("target_id", targetId)
    )

    fun createComment(
            videoId: Int,
            ownerId: Int? = null,
            message: String? = null,
            attachments: List<CommentAttachment>? = null,
            stickerId: Int? = null,
            fromGroup: Boolean,
            guid: String? = null,
            callback: Callback<JsonObject?>
    ) = Methods.createComment.call(client, callback, JsonObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("message", message)
            .put("attachments", attachments?.joinToString(",", transform = CommentAttachment::attachmentString))
            .put("sticker_id", stickerId)
            .put("from_group", fromGroup.asInt())
            .put("guid", guid)
    )

    fun delete(
            videoId: Int,
            ownerId: Int? = null,
            targetId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.delete.call(client, callback, JsonObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("target_id", targetId)
    )

    fun deleteAlbum(
            albumId: Int,
            groupId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.deleteAlbum.call(client, callback, JsonObject()
            .put("album_id", albumId)
            .put("group_id", groupId)
    )

    fun deleteComment(
            commentId: Int,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.deleteComment.call(client, callback, JsonObject()
            .put("comment_id", commentId)
            .put("owner_id", ownerId)
    )

    fun edit(
            videoId: Int,
            ownerId: Int? = null,
            name: String? = null,
            description: String? = null,
            privacyView: PrivacySettings? = null,
            privacyComment: PrivacySettings? = null,
            disableComments: Boolean? = null,
            enableRepeat: Boolean? = null,
            callback: Callback<JsonObject?>
    ) = Methods.edit.call(client, callback, JsonObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("name", name)
            .put("desc", description)
            .put("privacy_view", privacyView?.toRequestString())
            .put("privacy_comment", privacyComment?.toRequestString())
            .put("no_comments", disableComments?.asInt())
            .put("repeat", enableRepeat?.asInt())
    )

    fun editAlbum(
            albumId: Int,
            title: String,
            privacy: PrivacySettings? = null,
            groupId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.editAlbum.call(client, callback, JsonObject()
            .put("album_id", albumId)
            .put("title", title)
            .put("privacy", privacy?.toRequestString())
            .put("group_id", groupId)
    )

    fun editComment(
            commentId: Int,
            ownerId: Int? = null,
            message: String? = null,
            attachments: List<CommentAttachment>? = null,
            callback: Callback<JsonObject?>
    ) = Methods.editComment.call(client, callback, JsonObject()
            .put("comment_id", commentId)
            .put("owner_id", ownerId)
            .put("message", message)
            .put("attachments", attachments?.joinToString(",", transform = CommentAttachment::attachmentString))
    )

    fun get(
            ownerId: Int? = null,
            videos: List<Media>? = null,
            albumId: Int? = null,
            count: Int,
            offset: Int,
            extended: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.get.call(client, callback, JsonObject()
            .put("owner_id", ownerId)
            .put("videos", videos?.joinToString(",", transform = Media::mediaString))
            .put("album_id", albumId)
            .put("count", count)
            .put("offset", offset)
            .put("extended", extended.asInt())
    )

    fun getAlbumById(
            albumId: Int,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.getAlbumById.call(client, callback, JsonObject()
            .put("album_id", albumId)
            .put("owner_id", ownerId)
    )

    fun getAlbums(
            ownerId: Int? = null,
            offset: Int,
            count: Int,
            extended: Boolean,
            needSystem: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getAlbums.call(client, callback, JsonObject()
            .put("owner_id", ownerId)
            .put("offset", offset)
            .put("count", count)
            .put("extended", extended.asInt())
            .put("need_system", needSystem.asInt())
    )

    fun getAlbumsIdsByVideo(
            videoId: Int,
            ownerId: Int,
            targetId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.getAlbumsByVideo.call(client, callback, JsonObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("target_id", targetId)
            .put("extended", 0)
    )

    fun getAlbumsByVideo(
            videoId: Int,
            ownerId: Int,
            targetId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.getAlbumsByVideo.call(client, callback, JsonObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("target_id", targetId)
            .put("extended", 1)
    )

    fun getComments(
            videoId: Int,
            ownerId: Int? = null,
            needLikes: Boolean,
            startCommentId: Int? = null,
            offset: Int,
            count: Int,
            sort: CommentsSort,
            extended: Boolean,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = Methods.getComments.call(client, callback, JsonObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("need_likes", needLikes.asInt())
            .put("start_comment_id", startCommentId)
            .put("offset", offset)
            .put("count", count)
            .put("sort", sort.value)
            .put("extended", extended.asInt())
            .put("fields", fields.joinToString(",") { it.value })
    )

    fun removeFromAlbum(
            videoId: Int,
            ownerId: Int,
            albumIds: List<VideoAlbumType>,
            targetId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.removeFromAlbum.call(client, callback, JsonObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("album_ids", albumIds.joinToString(",") { it.value.toString() })
            .put("target_id", targetId)
    )

    fun reorderAlbums(
            albumId: Int,
            before: Int? = null,
            after: Int? = null,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.reorderAlbums.call(client, callback, JsonObject()
            .put("album_id", albumId)
            .put("before", before)
            .put("after", after)
            .put("owner_id", ownerId)
    )

    fun reorderVideos(
            videoId: Int,
            ownerId: Int,
            albumId: Int,
            before: Media? = null,
            after: Media? = null,
            targetId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.reorderVideos.call(client, callback, JsonObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("album_id", albumId)
            .put("before_owner_id", before?.ownerId)
            .put("before_video_id", before?.id)
            .put("after_owner_id", after?.ownerId)
            .put("after_video_id", after?.id)
            .put("target_id", targetId)
    )

    fun report(
            videoId: Int,
            ownerId: Int,
            reason: PostReportComplaintType,
            comment: String? = null,
            searchQuery: String? = null,
            callback: Callback<JsonObject?>
    ) = Methods.report.call(client, callback, JsonObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("reason", reason.value)
            .put("comment", comment)
            .put("search_query", searchQuery)
    )

    fun reportComment(
            ownerId: Int,
            commentId: Int,
            reason: PostReportComplaintType,
            callback: Callback<JsonObject?>
    ) = Methods.reportComment.call(client, callback, JsonObject()
            .put("owner_id", ownerId)
            .put("comment_id", commentId)
            .put("reason", reason.value)
    )

    fun restore(
            videoId: Int,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.restore.call(client, callback, JsonObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
    )

    fun restoreComment(
            commentId: Int,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.restoreComment.call(client, callback, JsonObject()
            .put("comment_id", commentId)
            .put("owner_id", ownerId)
    )

    fun save(
            name: String? = null,
            description: String? = null,
            isPrivate: Boolean,
            publishOnWall: Boolean,
            link: String? = null,
            groupId: Int? = null,
            albumId: VideoAlbumType? = null,
            privacyView: PrivacySettings,
            privacyComment: PrivacySettings,
            disableComments: Boolean,
            enableRepeat: Boolean,
            enableCompression: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.save.call(client, callback, JsonObject()
            .put("name", name)
            .put("description", description)
            .put("is_private", isPrivate.asInt())
            .put("wallpost", publishOnWall.asInt())
            .put("link", link)
            .put("group_id", groupId)
            .put("album_id", albumId?.value)
            .put("privacy_view", privacyView.toRequestString())
            .put("privacy_comment", privacyComment.toRequestString())
            .put("no_comments", disableComments.asInt())
            .put("repeat", enableRepeat.asInt())
            .put("compression", enableCompression.asInt())
    )

    fun search(
            query: String,
            sort: VideoSearchSort,
            onlyHd: Boolean,
            disableSafeSearch: Boolean,
            filters: List<VideoSearchFilter>? = null,
            searchOwn: Boolean,
            offset: Int,
            count: Int,
            longerThan: Int? = null,
            shorterThan: Int? = null,
            extended: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.search.call(client, callback, JsonObject()
            .put("q", query)
            .put("sort", sort.value)
            .put("hd", onlyHd.asInt())
            .put("adult", disableSafeSearch.asInt())
            .put("filters", filters?.joinToString(",") { it.value })
            .put("search_own", searchOwn.asInt())
            .put("offset", offset)
            .put("count", count)
            .put("longer", longerThan)
            .put("shorter", shorterThan)
            .put("extended", extended.asInt())
    )

    private object Methods {
        private const val it = "video."
        const val add = it + "add"
        const val addAlbum = it + "addAlbum"
        const val addToAlbum = it + "addToAlbum"
        const val createComment = it + "createComment"
        const val delete = it + "delete"
        const val deleteAlbum = it + "deleteAlbum"
        const val deleteComment = it + "deleteComment"
        const val edit = it + "edit"
        const val editAlbum = it + "editAlbum"
        const val editComment = it + "editComment"
        const val get = it + "get"
        const val getAlbumById = it + "getAlbumById"
        const val getAlbums = it + "getAlbums"
        const val getAlbumsByVideo = it + "getAlbumsByVideo"
        const val getComments = it + "getComments"
        const val removeFromAlbum = it + "removeFromAlbum"
        const val reorderAlbums = it + "reorderAlbums"
        const val reorderVideos = it + "reorderVideos"
        const val report = it + "report"
        const val reportComment = it + "reportComment"
        const val restore = it + "restore"
        const val restoreComment = it + "restoreComment"
        const val save = it + "save"
        const val search = it + "search"
    }
}