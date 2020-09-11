package com.github.stormbit.sdk.utils.vkapi.methods.video

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.callSync
import com.github.stormbit.sdk.utils.attachmentString
import com.github.stormbit.sdk.utils.mediaString
import com.github.stormbit.sdk.utils.vkapi.methods.*
import org.json.JSONObject

@Suppress("unused")
class VideoApi(private val client: Client) {
    fun add(
            videoId: Int,
            ownerId: Int,
            targetId: Int?
    ): JSONObject = Methods.add.callSync(client, JSONObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("target_id", targetId)
    )

    fun addAlbum(
            title: String,
            privacy: PrivacySettings,
            groupId: Int?
    ): JSONObject = Methods.addAlbum.callSync(client, JSONObject()
            .put("title", title)
            .put("privacy", privacy.toRequestString())
            .put("group_id", groupId)
    )

    fun addToAlbum(
            videoId: Int,
            ownerId: Int,
            albumIds: List<VideoAlbumType>,
            targetId: Int?
    ): JSONObject = Methods.addToAlbum.callSync(client, JSONObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("album_ids", albumIds.joinToString(",") { it.value.toString() })
            .put("target_id", targetId)
    )

    fun createComment(
            videoId: Int,
            ownerId: Int?,
            message: String?,
            attachments: List<CommentAttachment>?,
            stickerId: Int?,
            fromGroup: Boolean,
            guid: String?
    ): JSONObject = Methods.createComment.callSync(client, JSONObject()
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
            ownerId: Int?,
            targetId: Int?
    ): JSONObject = Methods.delete.callSync(client, JSONObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("target_id", targetId)
    )

    fun deleteAlbum(
            albumId: Int,
            groupId: Int?
    ): JSONObject = Methods.deleteAlbum.callSync(client, JSONObject()
            .put("album_id", albumId)
            .put("group_id", groupId)
    )

    fun deleteComment(
            commentId: Int,
            ownerId: Int?
    ): JSONObject = Methods.deleteComment.callSync(client, JSONObject()
            .put("comment_id", commentId)
            .put("owner_id", ownerId)
    )

    fun edit(
            videoId: Int,
            ownerId: Int?,
            name: String?,
            description: String?,
            privacyView: PrivacySettings?,
            privacyComment: PrivacySettings?,
            disableComments: Boolean?,
            enableRepeat: Boolean?
    ): JSONObject = Methods.edit.callSync(client, JSONObject()
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
            privacy: PrivacySettings?,
            groupId: Int?
    ): JSONObject = Methods.editAlbum.callSync(client, JSONObject()
            .put("album_id", albumId)
            .put("title", title)
            .put("privacy", privacy?.toRequestString())
            .put("group_id", groupId)
    )

    fun editComment(
            commentId: Int,
            ownerId: Int?,
            message: String?,
            attachments: List<CommentAttachment>?
    ): JSONObject = Methods.editComment.callSync(client, JSONObject()
            .put("comment_id", commentId)
            .put("owner_id", ownerId)
            .put("message", message)
            .put("attachments", attachments?.joinToString(",", transform = CommentAttachment::attachmentString))
    )

    fun get(
            ownerId: Int?,
            videos: List<Media>?,
            albumId: Int?,
            count: Int,
            offset: Int,
            extended: Boolean
    ): JSONObject = Methods.get.callSync(client, JSONObject()
            .put("owner_id", ownerId)
            .put("videos", videos?.joinToString(",", transform = Media::mediaString))
            .put("album_id", albumId)
            .put("count", count)
            .put("offset", offset)
            .put("extended", extended.asInt())
    )

    fun getAlbumById(
            albumId: Int,
            ownerId: Int?
    ): JSONObject = Methods.getAlbumById.callSync(client, JSONObject()
            .put("album_id", albumId)
            .put("owner_id", ownerId)
    )

    fun getAlbums(
            ownerId: Int?,
            offset: Int,
            count: Int,
            extended: Boolean,
            needSystem: Boolean
    ): JSONObject = Methods.getAlbums.callSync(client, JSONObject()
            .put("owner_id", ownerId)
            .put("offset", offset)
            .put("count", count)
            .put("extended", extended.asInt())
            .put("need_system", needSystem.asInt())
    )

    fun getAlbumsIdsByVideo(
            videoId: Int,
            ownerId: Int,
            targetId: Int?
    ): JSONObject = Methods.getAlbumsByVideo.callSync(client, JSONObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("target_id", targetId)
            .put("extended", 0)
    )

    fun getAlbumsByVideo(
            videoId: Int,
            ownerId: Int,
            targetId: Int?
    ): JSONObject = Methods.getAlbumsByVideo.callSync(client, JSONObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("target_id", targetId)
            .put("extended", 1)
    )

    fun getComments(
            videoId: Int,
            ownerId: Int?,
            needLikes: Boolean,
            startCommentId: Int?,
            offset: Int,
            count: Int,
            sort: CommentsSort,
            extended: Boolean,
            fields: List<ObjectField>
    ): JSONObject = Methods.getComments.callSync(client, JSONObject()
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
            targetId: Int?
    ): JSONObject = Methods.removeFromAlbum.callSync(client, JSONObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("album_ids", albumIds.joinToString(",") { it.value.toString() })
            .put("target_id", targetId)
    )

    fun reorderAlbums(
            albumId: Int,
            before: Int?,
            after: Int?,
            ownerId: Int?
    ):JSONObject = Methods.reorderAlbums.callSync(client, JSONObject()
            .put("album_id", albumId)
            .put("before", before)
            .put("after", after)
            .put("owner_id", ownerId)
    )

    fun reorderVideos(
            videoId: Int,
            ownerId: Int,
            albumId: Int,
            before: Media?,
            after: Media?,
            targetId: Int?
    ): JSONObject = Methods.reorderVideos.callSync(client, JSONObject()
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
            comment: String?,
            searchQuery: String?
    ): JSONObject = Methods.report.callSync(client, JSONObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
            .put("reason", reason.value)
            .put("comment", comment)
            .put("search_query", searchQuery)
    )

    fun reportComment(
            ownerId: Int,
            commentId: Int,
            reason: PostReportComplaintType
    ): JSONObject = Methods.reportComment.callSync(client, JSONObject()
            .put("owner_id", ownerId)
            .put("comment_id", commentId)
            .put("reason", reason.value)
    )

    fun restore(
            videoId: Int,
            ownerId: Int?
    ): JSONObject = Methods.restore.callSync(client, JSONObject()
            .put("video_id", videoId)
            .put("owner_id", ownerId)
    )

    fun restoreComment(
            commentId: Int,
            ownerId: Int?
    ): JSONObject = Methods.restoreComment.callSync(client, JSONObject()
            .put("comment_id", commentId)
            .put("owner_id", ownerId)
    )

    fun save(
            name: String?,
            description: String?,
            isPrivate: Boolean,
            publishOnWall: Boolean,
            link: String?,
            groupId: Int?,
            albumId: VideoAlbumType?,
            privacyView: PrivacySettings,
            privacyComment: PrivacySettings,
            disableComments: Boolean,
            enableRepeat: Boolean,
            enableCompression: Boolean
    ): JSONObject = Methods.save.callSync(client, JSONObject()
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
            filters: List<VideoSearchFilter>?,
            searchOwn: Boolean,
            offset: Int,
            count: Int,
            longerThan: Int?,
            shorterThan: Int?,
            extended: Boolean
    ): JSONObject = Methods.search.callSync(client, JSONObject()
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