package com.github.stormbit.sdk.vkapi.methods.photos

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.models.PrivacySettings
import com.github.stormbit.sdk.utils.parametersOf
import com.github.stormbit.sdk.utils.toInt
import com.github.stormbit.sdk.vkapi.methods.*
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import javax.print.attribute.standard.Media

@Suppress("unused")
class PhotosApiAsync(private val client: Client) : MethodsContext() {

    fun confirmTag(
            photoId: Int,
            tagId: Int,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.confirmTag.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("photo_id", photoId)
            append("tag_id", tagId)
            append("owner_id", ownerId)
        }
    )


    fun copy(
            ownerId: Int,
            photoId: Int,
            accessKey: String? = null,
            callback: Callback<JsonObject?>
    ) = Methods.copy.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("owner_id", ownerId)
            append("photo_id", photoId)
            append("access_key", accessKey)
        }
    )


    fun createAlbum(
        title: String,
        description: String? = null,
        privacyView: PrivacySettings,
        privacyComment: PrivacySettings,
        groupId: Int? = null,
        isUploadByAdminsOnly: Boolean,
        isCommentsDisabled: Boolean,
        callback: Callback<JsonObject?>
    ) = Methods.createAlbum.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("title", title)
            append("description", description)
            append("privacy_view", privacyView.toRequestString())
            append("privacy_comment", privacyComment.toRequestString())
            append("group_id", groupId)
            append("upload_by_admins_only", isUploadByAdminsOnly.toInt())
            append("comments_disabled", isCommentsDisabled.toInt())
        }
    )


    fun createComment(
            photoId: Int,
            ownerId: Int? = null,
            message: String? = null,
            attachments: List<String>? = null,
            stickerId: Int? = null,
            fromGroup: Boolean,
            accessKey: String? = null,
            guid: String? = null,
            callback: Callback<JsonObject?>
    ) = Methods.createComment.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("photo_id", photoId)
            append("owner_id", ownerId)
            append("message", message)
            append("items", attachments?.joinToString(","))
            append("sticker_id", stickerId)
            append("from_group", fromGroup.toInt())
            append("access_key", accessKey)
            append("guid", guid)
        }
    )


    fun delete(
            photoId: Int,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.delete.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("photo_id", photoId)
            append("owner_id", ownerId)
        }
    )


    fun deleteAlbum(
            albumId: Int,
            groupId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.deleteAlbum.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("album_id", albumId)
            append("group_id", groupId)
        }
    )


    fun deleteComment(
            commentId: Int,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.deleteComment.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("comment_id", commentId)
            append("owner_id", ownerId)
        }
    )


    fun edit(
            photoId: Int,
            ownerId: Int? = null,
            caption: String,
            latitude: Double? = null,
            longitude: Double? = null,
            placeName: String? = null,
            foursquareId: String? = null,
            deletePlace: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.edit.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("photo_id", photoId)
            append("owner_id", ownerId)
            append("caption", caption)
            append("latitude", latitude)
            append("longitude", longitude)
            append("place_str", placeName)
            append("foursquare_id", foursquareId)
            append("delete_place", deletePlace.toInt())
        }
    )


    fun editAlbum(
        albumId: Int,
        ownerId: Int? = null,
        title: String? = null,
        description: String? = null,
        privacyView: PrivacySettings? = null,
        privacyComment: PrivacySettings? = null,
        isUploadByAdminsOnly: Boolean? = null,
        isCommentsDisabled: Boolean? = null,
        callback: Callback<JsonObject?>
    ) = Methods.editAlbum.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("album_id", albumId)
            append("owner_id", ownerId)
            append("title", title)
            append("description", description)
            append("privacy_view", privacyView?.toRequestString())
            append("privacy_comment", privacyComment?.toRequestString())
            append("upload_by_admins_only", isUploadByAdminsOnly?.toInt())
            append("comments_disabled", isCommentsDisabled?.toInt())
        }
    )


    fun editComment(
            commentId: Int,
            ownerId: Int? = null,
            message: String? = null,
            attachments: List<String>? = null,
            callback: Callback<JsonObject?>
    ) = Methods.editComment.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("comment_id", commentId)
            append("owner_id", ownerId)
            append("message", message)
            append("items", attachments?.joinToString(","))
        }
    )


    fun get(
            album: PhotoAlbumType,
            ownerId: Int? = null,
            photoIds: List<Int>? = null,
            reverse: Boolean,
            extended: Boolean,
            feedType: FeedType? = null,
            feed: Int? = null,
            offset: Int,
            count: Int,
            callback: Callback<JsonObject?>
    ) = Methods.get.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("album_id", album.value)
            append("owner_id", ownerId)
            append("photo_ids", photoIds?.joinToString(","))
            append("rev", reverse.toInt())
            append("extended", extended.toInt())
            append("feed_type", feedType?.value)
            append("feed", feed)
            append("offset", offset)
            append("count", count)
        }
    )


    fun getAlbums(
            albumIds: List<Int>? = null,
            ownerId: Int? = null,
            offset: Int,
            count: Int,
            needSystem: Boolean,
            needCovers: Boolean,
            needPhotoSizes: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getAlbums.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("album_ids", albumIds?.joinToString(","))
            append("owner_id", ownerId)
            append("offset", offset)
            append("count", count)
            append("need_system", needSystem.toInt())
            append("need_covers", needCovers.toInt())
            append("photo_sizes", needPhotoSizes.toInt())
        }
    )


    fun getAlbumsCountGroup(
            groupId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getAlbumsCount.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("group_id", groupId)
        }
    )


    fun getAlbumsCountUser(
            userId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.getAlbumsCount.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("user_id", userId)
        }
    )


    fun getAll(
            ownerId: Int? = null,
            extended: Boolean,
            offset: Int,
            count: Int,
            noServiceAlbums: Boolean,
            needHidden: Boolean,
            skipHidden: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getAll.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("owner_id", ownerId)
            append("extended", extended.toInt())
            append("offset", offset)
            append("count", count)
            append("no_service_albums", noServiceAlbums.toInt())
            append("need_hidden", needHidden.toInt())
            append("skip_hidden", skipHidden.toInt())
        }
    )


    fun getAllComments(
            ownerId: Int? = null,
            albumId: Int? = null,
            needLikes: Boolean,
            offset: Int,
            count: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getAllComments.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("owner_id", ownerId)
            append("album_id", albumId)
            append("need_likes", needLikes.toInt())
            append("offset", offset)
            append("count", count)
        }
    )


    fun getById(
            photos: List<Media>,
            extended: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getById.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("photos", photos.joinToString(","))
            append("extended", extended.toInt())
        }
    )


    fun getChatUploadServer(
            chatId: Int,
            cropX: Int? = null,
            cropY: Int? = null,
            cropWidth: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.getChatUploadServer.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("chat_id", chatId)
            append("crop_x", cropX)
            append("crop_y", cropY)
            append("crop_width", cropWidth)
        }
    )


    fun getComments(
            photoId: Int,
            ownerId: Int? = null,
            needLikes: Boolean,
            startCommentId: Int? = null,
            offset: Int,
            count: Int,
            sort: CommentsSort,
            accessKey: String? = null,
            extended: Boolean,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = Methods.getComments.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("photo_id", photoId)
            append("owner_id", ownerId)
            append("need_likes", needLikes.toInt())
            append("start_comment_id", startCommentId)
            append("offset", offset)
            append("count", count)
            append("sort", sort.value)
            append("access_key", accessKey)
            append("extended", extended.toInt())
            append("fields", fields.joinToString(",") { it.value })
        }
    )


    fun getMarketAlbumUploadServer(
            groupId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getMarketAlbumUploadServer.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("group_id", groupId)
        }
    )


    fun getMarketUploadServer(
            groupId: Int,
            isMainPhoto: Boolean,
            cropX: Int? = null,
            cropY: Int? = null,
            cropWidth: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.getMarketUploadServer.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("group_id", groupId)
            append("main_photo", isMainPhoto.toInt())
            append("crop_x", cropX)
            append("crop_y", cropY)
            append("crop_width", cropWidth)
        }
    )


    fun getMessagesUploadServer(
            peerId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getMessagesUploadServer.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("peer_id", peerId)
        }
    )


    fun getNewTags(
            offset: Int,
            count: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getNewTags.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("offset", offset)
            append("count", count)
        }
    )


    fun getOwnerCoverPhotoUploadServer(
            groupId: Int,
            cropX: Int,
            cropY: Int,
            cropX2: Int,
            cropY2: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getOwnerCoverPhotoUploadServer.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("group_id", groupId)
            append("crop_x", cropX)
            append("crop_y", cropY)
            append("crop_x2", cropX2)
            append("crop_y2", cropY2)
        }
    )


    fun getOwnerPhotoUploadServer(
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.getOwnerPhotoUploadServer.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("owner_id", ownerId)
        }
    )


    fun getTags(
            photoId: Int,
            ownerId: Int? = null,
            accessKey: String? = null,
            callback: Callback<JsonObject?>
    ) = Methods.getTags.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("photo_id", photoId)
            append("owner_id", ownerId)
            append("access_key", accessKey)
        }
    )


    fun getUploadServer(
            albumId: Int,
            groupId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.getUploadServer.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("album_id", albumId)
            append("group_id", groupId)
        }
    )


    fun getUserPhotos(
            userId: Int? = null,
            offset: Int,
            count: Int,
            extended: Boolean,
            sortDescending: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getUserPhotos.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("user_id", userId)
            append("offset", offset)
            append("count", count)
            append("extended", extended.toInt())
            append("sort", sortDescending.not().toInt())
        }
    )


    fun getWallUploadServer(
            groupId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.getWallUploadServer.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("group_id", groupId)
        }
    )


    fun makeCover(
            photoId: Int,
            albumId: Int,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.makeCover.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("photo_id", photoId)
            append("album_id", albumId)
            append("owner_id", ownerId)
        }
    )


    fun move(
            photoId: Int,
            targetAlbumId: Int,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.move.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("photo_id", photoId)
            append("target_album_id", targetAlbumId)
            append("owner_id", ownerId)
        }
    )


    fun appendTag(
            photoId: Int,
            userId: Int,
            x: Double,
            y: Double,
            x2: Double,
            y2: Double,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.appendTag.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("photo_id", photoId)
            append("user_id", userId)
            append("x", x)
            append("y", y)
            append("x2", x2)
            append("y2", y2)
            append("owner_id", ownerId)
        }
    )


    fun removeTag(
            photoId: Int,
            tagId: Int,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.removeTag.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("photo_id", photoId)
            append("tag_id", tagId)
            append("owner_id", ownerId)
        }
    )


    fun reorderAlbums(
            albumId: Int,
            before: Int? = null,
            after: Int? = null,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.reorderAlbums.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("album_id", albumId)
            append("before", before)
            append("after", after)
            append("owner_id", ownerId)
        }
    )


    fun reorderPhotos(
            photoId: Int,
            before: Int? = null,
            after: Int? = null,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.reorderPhotos.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("photo_id", photoId)
            append("before", before)
            append("after", after)
            append("owner_id", ownerId)
        }
    )


    fun report(
            ownerId: Int,
            photoId: Int,
            reason: PostReportComplaintType,
            callback: Callback<JsonObject?>
    ) = Methods.report.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("owner_id", ownerId)
            append("photo_id", photoId)
            append("reason", reason.value)
        }
    )


    fun reportComment(
            ownerId: Int,
            commentId: Int,
            reason: PostReportComplaintType,
            callback: Callback<JsonObject?>
    ) = Methods.reportComment.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("owner_id", ownerId)
            append("comment_id", commentId)
            append("reason", reason.value)
        }
    )


    fun restore(
            photoId: Int,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.restore.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("photo_id", photoId)
            append("owner_id", ownerId)
        }
    )


    fun restoreComment(
            commentId: Int,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.restoreComment.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("comment_id", commentId)
            append("owner_id", ownerId)
        }
    )


    fun save(
            albumId: Int,
            server: Int,
            photosList: String,
            hash: String,
            latitude: Double? = null,
            longitude: Double? = null,
            caption: String? = null,
            groupId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.save.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("album_id", albumId)
            append("server", server)
            append("photos_list", photosList)
            append("hash", hash)
            append("latitude", latitude)
            append("longitude", longitude)
            append("caption", caption)
            append("group_id", groupId)
        }
    )


    fun saveMarketAlbumPhoto(
            groupId: Int,
            server: Int,
            photo: String,
            hash: String,
            callback: Callback<JsonObject?>
    ) = Methods.saveMarketAlbumPhoto.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("group_id", groupId)
            append("server", server)
            append("photo", photo)
            append("hash", hash)
        }
    )


    fun saveMarketPhoto(
            server: Int,
            photo: String,
            hash: String,
            cropData: String? = null,
            cropHash: String? = null,
            groupId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.saveMarketPhoto.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("server", server)
            append("photo", photo)
            append("hash", hash)
            append("crop_data", cropData)
            append("crop_hash", cropHash)
            append("group_id", groupId)
        }
    )


    fun saveMessagesPhoto(
            server: Int,
            photo: String,
            hash: String,
            callback: Callback<JsonArray?>
    ) = Methods.saveMessagesPhoto.call(
        client, JsonArray.serializer(), callback, parametersOf {
            append("server", server)
            append("photo", photo)
            append("hash", hash)
        }
    )


    fun saveOwnerCoverPhoto(
            photo: String,
            hash: String,
            callback: Callback<JsonObject?>
    ) = Methods.saveOwnerCoverPhoto.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("photo", photo)
            append("hash", hash)
        }
    )


    fun saveOwnerPhoto(
            server: Int,
            photo: String,
            hash: String,
            callback: Callback<JsonObject?>
    ) = Methods.saveOwnerPhoto.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("server", server)
            append("photo", photo)
            append("hash", hash)
        }
    )


    fun saveWallPhoto(
            server: Int,
            photo: String,
            hash: String,
            userId: Int? = null,
            groupId: Int? = null,
            latitude: Double? = null,
            longitude: Double? = null,
            caption: String? = null,
            callback: Callback<JsonObject?>
    ) = Methods.saveWallPhoto.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("server", server)
            append("photo", photo)
            append("hash", hash)
            append("user_id", userId)
            append("group_id", groupId)
            append("latitude", latitude)
            append("longitude", longitude)
            append("caption", caption)
        }
    )


    fun search(
            query: String? = null,
            latitude: Double? = null,
            longitude: Double? = null,
            startTime: Int? = null,
            endTime: Int? = null,
            sort: PhotoSearchSort,
            offset: Int,
            count: Int,
            radius: Int,
            callback: Callback<JsonObject?>
    ) = Methods.search.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("q", query)
            append("lat", latitude)
            append("long", longitude)
            append("start_time", startTime)
            append("end_time", endTime)
            append("sort", sort.value)
            append("offset", offset)
            append("count", count)
            append("radius", radius)
        }
    )

    private object Methods {
        private const val it = "photos."
        const val confirmTag = it + "confirmTag"
        const val copy = it + "copy"
        const val createAlbum = it + "createAlbum"
        const val createComment = it + "createComment"
        const val delete = it + "delete"
        const val deleteAlbum = it + "deleteAlbum"
        const val deleteComment = it + "deleteComment"
        const val edit = it + "edit"
        const val editAlbum = it + "editAlbum"
        const val editComment = it + "editComment"
        const val get = it + "get"
        const val getAlbums = it + "getAlbums"
        const val getAlbumsCount = it + "getAlbumsCount"
        const val getAll = it + "getAll"
        const val getAllComments = it + "getAllComments"
        const val getById = it + "getById"
        const val getChatUploadServer = it + "getChatUploadServer"
        const val getComments = it + "getComments"
        const val getMarketAlbumUploadServer = it + "getMarketAlbumUploadServer"
        const val getMarketUploadServer = it + "getMarketUploadServer"
        const val getMessagesUploadServer = it + "getMessagesUploadServer"
        const val getNewTags = it + "getNewTags"
        const val getOwnerCoverPhotoUploadServer = it + "getOwnerCoverPhotoUploadServer"
        const val getOwnerPhotoUploadServer = it + "getOwnerPhotoUploadServer"
        const val getTags = it + "getTags"
        const val getUploadServer = it + "getUploadServer"
        const val getUserPhotos = it + "getUserPhotos"
        const val getWallUploadServer = it + "getWallUploadServer"
        const val makeCover = it + "makeCover"
        const val move = it + "move"
        const val appendTag = it + "appendTag"
        const val removeTag = it + "removeTag"
        const val reorderAlbums = it + "reorderAlbums"
        const val reorderPhotos = it + "reorderPhotos"
        const val report = it + "report"
        const val reportComment = it + "reportComment"
        const val restore = it + "restore"
        const val restoreComment = it + "restoreComment"
        const val save = it + "save"
        const val saveMarketAlbumPhoto = it + "saveMarketAlbumPhoto"
        const val saveMarketPhoto = it + "saveMarketPhoto"
        const val saveMessagesPhoto = it + "saveMessagesPhoto"
        const val saveOwnerCoverPhoto = it + "saveOwnerCoverPhoto"
        const val saveOwnerPhoto = it + "saveOwnerPhoto"
        const val saveWallPhoto = it + "saveWallPhoto"
        const val search = it + "search"
    }
}