package com.github.stormbit.vksdk.vkapi.methods.video

import com.github.stormbit.vksdk.clients.Client
import com.github.stormbit.vksdk.objects.models.PrivacySettings
import com.github.stormbit.vksdk.utils.append
import com.github.stormbit.vksdk.utils.attachmentString
import com.github.stormbit.vksdk.utils.mediaString
import com.github.stormbit.vksdk.utils.toInt
import com.github.stormbit.vksdk.vkapi.VkApiRequest
import com.github.stormbit.vksdk.vkapi.methods.*
import kotlinx.serialization.json.JsonObject

@Suppress("unused")
class VideoApi(client: Client) : MethodsContext(client) {

    fun add(
        videoId: Int,
        ownerId: Int,
        targetId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.add.call(JsonObject.serializer()) {
        append("video_id", videoId)
        append("owner_id", ownerId)
        append("target_id", targetId)
    }


    fun addAlbum(
        title: String,
        privacy: PrivacySettings,
        groupId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.addAlbum.call(JsonObject.serializer()) {
        append("title", title)
        append("privacy", privacy.toRequestString())
        append("group_id", groupId)
    }


    fun addToAlbum(
        videoId: Int,
        ownerId: Int,
        albumIds: List<VideoAlbumType>,
        targetId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.addToAlbum.call(JsonObject.serializer()) {
        append("video_id", videoId)
        append("owner_id", ownerId)
        append("album_ids", albumIds.joinToString(",") { it.value.toString() })
        append("target_id", targetId)
    }


    fun createComment(
        videoId: Int,
        ownerId: Int? = null,
        message: String? = null,
        attachments: List<CommentAttachment>? = null,
        stickerId: Int? = null,
        fromGroup: Boolean,
        guid: String? = null
    ): VkApiRequest<JsonObject> = Methods.createComment.call(JsonObject.serializer()) {
        append("video_id", videoId)
        append("owner_id", ownerId)
        append("message", message)
        append("items", attachments?.joinToString(",", transform = CommentAttachment::attachmentString))
        append("sticker_id", stickerId)
        append("from_group", fromGroup.toInt())
        append("guid", guid)
    }


    fun delete(
        videoId: Int,
        ownerId: Int? = null,
        targetId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.delete.call(JsonObject.serializer()) {
        append("video_id", videoId)
        append("owner_id", ownerId)
        append("target_id", targetId)
    }


    fun deleteAlbum(
        albumId: Int,
        groupId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.deleteAlbum.call(JsonObject.serializer()) {
        append("album_id", albumId)
        append("group_id", groupId)
    }


    fun deleteComment(
        commentId: Int,
        ownerId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.deleteComment.call(JsonObject.serializer()) {
        append("comment_id", commentId)
        append("owner_id", ownerId)
    }


    fun edit(
        videoId: Int,
        ownerId: Int? = null,
        name: String? = null,
        description: String? = null,
        privacyView: PrivacySettings? = null,
        privacyComment: PrivacySettings? = null,
        disableComments: Boolean? = null,
        enableRepeat: Boolean? = null
    ): VkApiRequest<JsonObject> = Methods.edit.call(JsonObject.serializer()) {
        append("video_id", videoId)
        append("owner_id", ownerId)
        append("name", name)
        append("desc", description)
        append("privacy_view", privacyView?.toRequestString())
        append("privacy_comment", privacyComment?.toRequestString())
        append("no_comments", disableComments?.toInt())
        append("repeat", enableRepeat?.toInt())
    }


    fun editAlbum(
        albumId: Int,
        title: String,
        privacy: PrivacySettings? = null,
        groupId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.editAlbum.call(JsonObject.serializer()) {
        append("album_id", albumId)
        append("title", title)
        append("privacy", privacy?.toRequestString())
        append("group_id", groupId)
    }


    fun editComment(
        commentId: Int,
        ownerId: Int? = null,
        message: String? = null,
        attachments: List<CommentAttachment>? = null
    ): VkApiRequest<JsonObject> = Methods.editComment.call(JsonObject.serializer()) {
        append("comment_id", commentId)
        append("owner_id", ownerId)
        append("message", message)
        append("items", attachments?.joinToString(",", transform = CommentAttachment::attachmentString))
    }


    fun get(
        ownerId: Int? = null,
        videos: List<Media>? = null,
        albumId: Int? = null,
        count: Int,
        offset: Int,
        extended: Boolean
    ): VkApiRequest<JsonObject> = Methods.get.call(JsonObject.serializer()) {
        append("owner_id", ownerId)
        append("videos", videos?.joinToString(",", transform = Media::mediaString))
        append("album_id", albumId)
        append("count", count)
        append("offset", offset)
        append("extended", extended.toInt())
    }


    fun getAlbumById(
        albumId: Int,
        ownerId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.getAlbumById.call(JsonObject.serializer()) {
        append("album_id", albumId)
        append("owner_id", ownerId)
    }


    fun getAlbums(
        ownerId: Int? = null,
        offset: Int,
        count: Int,
        extended: Boolean,
        needSystem: Boolean
    ): VkApiRequest<JsonObject> = Methods.getAlbums.call(JsonObject.serializer()) {
        append("owner_id", ownerId)
        append("offset", offset)
        append("count", count)
        append("extended", extended.toInt())
        append("need_system", needSystem.toInt())
    }


    fun getAlbumsIdsByVideo(
        videoId: Int,
        ownerId: Int,
        targetId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.getAlbumsByVideo.call(JsonObject.serializer()) {
        append("video_id", videoId)
        append("owner_id", ownerId)
        append("target_id", targetId)
        append("extended", 0)
    }


    fun getAlbumsByVideo(
        videoId: Int,
        ownerId: Int,
        targetId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.getAlbumsByVideo.call(JsonObject.serializer()) {
        append("video_id", videoId)
        append("owner_id", ownerId)
        append("target_id", targetId)
        append("extended", 1)
    }


    fun getComments(
        videoId: Int,
        ownerId: Int? = null,
        needLikes: Boolean,
        startCommentId: Int? = null,
        offset: Int,
        count: Int,
        sort: CommentsSort,
        extended: Boolean,
        fields: List<ObjectField>
    ): VkApiRequest<JsonObject> = Methods.getComments.call(JsonObject.serializer()) {
        append("video_id", videoId)
        append("owner_id", ownerId)
        append("need_likes", needLikes.toInt())
        append("start_comment_id", startCommentId)
        append("offset", offset)
        append("count", count)
        append("sort", sort.value)
        append("extended", extended.toInt())
        append("fields", fields.joinToString(",") { it.value })
    }


    fun removeFromAlbum(
        videoId: Int,
        ownerId: Int,
        albumIds: List<VideoAlbumType>,
        targetId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.removeFromAlbum.call(JsonObject.serializer()) {
        append("video_id", videoId)
        append("owner_id", ownerId)
        append("album_ids", albumIds.joinToString(",") { it.value.toString() })
        append("target_id", targetId)
    }


    fun reorderAlbums(
        albumId: Int,
        before: Int? = null,
        after: Int? = null,
        ownerId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.reorderAlbums.call(JsonObject.serializer()) {
        append("album_id", albumId)
        append("before", before)
        append("after", after)
        append("owner_id", ownerId)
    }


    fun reorderVideos(
        videoId: Int,
        ownerId: Int,
        albumId: Int,
        before: Media? = null,
        after: Media? = null,
        targetId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.reorderVideos.call(JsonObject.serializer()) {
        append("video_id", videoId)
        append("owner_id", ownerId)
        append("album_id", albumId)
        append("before_owner_id", before?.ownerId)
        append("before_video_id", before?.id)
        append("after_owner_id", after?.ownerId)
        append("after_video_id", after?.id)
        append("target_id", targetId)
    }


    fun report(
        videoId: Int,
        ownerId: Int,
        reason: PostReportComplaintType,
        comment: String? = null,
        searchQuery: String? = null
    ): VkApiRequest<JsonObject> = Methods.report.call(JsonObject.serializer()) {
        append("video_id", videoId)
        append("owner_id", ownerId)
        append("reason", reason.value)
        append("comment", comment)
        append("search_query", searchQuery)
    }


    fun reportComment(
        ownerId: Int,
        commentId: Int,
        reason: PostReportComplaintType
    ): VkApiRequest<JsonObject> = Methods.reportComment.call(JsonObject.serializer()) {
        append("owner_id", ownerId)
        append("comment_id", commentId)
        append("reason", reason.value)
    }


    fun restore(
        videoId: Int,
        ownerId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.restore.call(JsonObject.serializer()) {
        append("video_id", videoId)
        append("owner_id", ownerId)
    }


    fun restoreComment(
        commentId: Int,
        ownerId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.restoreComment.call(JsonObject.serializer()) {
        append("comment_id", commentId)
        append("owner_id", ownerId)
    }


    fun save(
        name: String? = null,
        description: String? = null,
        isPrivate: Boolean,
        publishOnWall: Boolean,
        link: String? = null,
        groupId: Int? = null,
        albumId: VideoAlbumType? = null,
        privacyView: PrivacySettings? = null,
        privacyComment: PrivacySettings? = null,
        disableComments: Boolean = false,
        enableRepeat: Boolean = false,
        enableCompression: Boolean = false
    ): VkApiRequest<VideoSaveResponse> = Methods.save.call(VideoSaveResponse.serializer()) {
        append("name", name)
        append("description", description)
        append("is_private", isPrivate.toInt())
        append("wallpost", publishOnWall.toInt())
        append("link", link)
        append("group_id", groupId)
        append("album_id", albumId?.value)
        append("privacy_view", privacyView?.toRequestString())
        append("privacy_comment", privacyComment?.toRequestString())
        append("no_comments", disableComments.toInt())
        append("repeat", enableRepeat.toInt())
        append("compression", enableCompression.toInt())
    }


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
        extended: Boolean
    ): VkApiRequest<JsonObject> = Methods.search.call(JsonObject.serializer()) {
        append("q", query)
        append("sort", sort.value)
        append("hd", onlyHd.toInt())
        append("adult", disableSafeSearch.toInt())
        append("filters", filters?.joinToString(",") { it.value })
        append("search_own", searchOwn.toInt())
        append("offset", offset)
        append("count", count)
        append("longer", longerThan)
        append("shorter", shorterThan)
        append("extended", extended.toInt())
    }


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