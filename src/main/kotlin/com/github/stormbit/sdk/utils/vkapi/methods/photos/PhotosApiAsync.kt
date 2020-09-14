package com.github.stormbit.sdk.utils.vkapi.methods.photos

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.call
import com.github.stormbit.sdk.utils.put
import com.github.stormbit.sdk.utils.vkapi.methods.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import javax.print.attribute.standard.Media

@Suppress("unused")
class PhotosApiAsync(private val client: Client) {

    fun confirmTag(
            photoId: Int,
            tagId: Int,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.confirmTag.call(client, callback, JsonObject()
            .put("photo_id", photoId)
            .put("tag_id", tagId)
            .put("owner_id", ownerId)
    )


    fun copy(
            ownerId: Int,
            photoId: Int,
            accessKey: String?,
            callback: Callback<JsonObject?>
    ) = Methods.copy.call(client, callback, JsonObject()
            .put("owner_id", ownerId)
            .put("photo_id", photoId)
            .put("access_key", accessKey)
    )


    fun createAlbum(
            title: String,
            description: String?,
            privacyView: PrivacySettings,
            privacyComment: PrivacySettings,
            groupId: Int?,
            isUploadByAdminsOnly: Boolean,
            isCommentsDisabled: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.createAlbum.call(client, callback, JsonObject()
            .put("title", title)
            .put("description", description)
            .put("privacy_view", privacyView.toRequestString())
            .put("privacy_comment", privacyComment.toRequestString())
            .put("group_id", groupId)
            .put("upload_by_admins_only", isUploadByAdminsOnly.asInt())
            .put("comments_disabled", isCommentsDisabled.asInt())
    )


    fun createComment(
            photoId: Int,
            ownerId: Int?,
            message: String?,
            attachments: List<String>?,
            stickerId: Int?,
            fromGroup: Boolean,
            accessKey: String?,
            guid: String?,
            callback: Callback<JsonObject?>
    ) = Methods.createComment.call(client, callback, JsonObject()
            .put("photo_id", photoId)
            .put("owner_id", ownerId)
            .put("message", message)
            .put("attachments", attachments?.joinToString(","))
            .put("sticker_id", stickerId)
            .put("from_group", fromGroup.asInt())
            .put("access_key", accessKey)
            .put("guid", guid)
    )


    fun delete(
            photoId: Int,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.delete.call(client, callback, JsonObject()
            .put("photo_id", photoId)
            .put("owner_id", ownerId)
    )


    fun deleteAlbum(
            albumId: Int,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.deleteAlbum.call(client, callback, JsonObject()
            .put("album_id", albumId)
            .put("group_id", groupId)
    )


    fun deleteComment(
            commentId: Int,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.deleteComment.call(client, callback, JsonObject()
            .put("comment_id", commentId)
            .put("owner_id", ownerId)
    )


    fun edit(
            photoId: Int,
            ownerId: Int?,
            caption: String,
            latitude: Double?,
            longitude: Double?,
            placeName: String?,
            foursquareId: String?,
            deletePlace: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.edit.call(client, callback, JsonObject()
            .put("photo_id", photoId)
            .put("owner_id", ownerId)
            .put("caption", caption)
            .put("latitude", latitude)
            .put("longitude", longitude)
            .put("place_str", placeName)
            .put("foursquare_id", foursquareId)
            .put("delete_place", deletePlace.asInt())
    )


    fun editAlbum(
            albumId: Int,
            ownerId: Int?,
            title: String?,
            description: String?,
            privacyView: PrivacySettings?,
            privacyComment: PrivacySettings?,
            isUploadByAdminsOnly: Boolean?,
            isCommentsDisabled: Boolean?,
            callback: Callback<JsonObject?>
    ) = Methods.editAlbum.call(client, callback, JsonObject()
            .put("album_id", albumId)
            .put("owner_id", ownerId)
            .put("title", title)
            .put("description", description)
            .put("privacy_view", privacyView?.toRequestString())
            .put("privacy_comment", privacyComment?.toRequestString())
            .put("upload_by_admins_only", isUploadByAdminsOnly?.asInt())
            .put("comments_disabled", isCommentsDisabled?.asInt())
    )


    fun editComment(
            commentId: Int,
            ownerId: Int?,
            message: String?,
            attachments: List<String>?,
            callback: Callback<JsonObject?>
    ) = Methods.editComment.call(client, callback, JsonObject()
            .put("comment_id", commentId)
            .put("owner_id", ownerId)
            .put("message", message)
            .put("attachments", attachments?.joinToString(","))
    )


    fun get(
            album: PhotoAlbumType,
            ownerId: Int?,
            photoIds: List<Int>?,
            reverse: Boolean,
            extended: Boolean,
            feedType: FeedType?,
            feed: Int?,
            offset: Int,
            count: Int,
            callback: Callback<JsonObject?>
    ) = Methods.get.call(client, callback, JsonObject()
            .put("album_id", album.value)
            .put("owner_id", ownerId)
            .put("photo_ids", photoIds?.joinToString(","))
            .put("rev", reverse.asInt())
            .put("extended", extended.asInt())
            .put("feed_type", feedType?.value)
            .put("feed", feed)
            .put("offset", offset)
            .put("count", count)
    )


    fun getAlbums(
            albumIds: List<Int>?,
            ownerId: Int?,
            offset: Int,
            count: Int,
            needSystem: Boolean,
            needCovers: Boolean,
            needPhotoSizes: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getAlbums.call(client, callback, JsonObject()
            .put("album_ids", albumIds?.joinToString(","))
            .put("owner_id", ownerId)
            .put("offset", offset)
            .put("count", count)
            .put("need_system", needSystem.asInt())
            .put("need_covers", needCovers.asInt())
            .put("photo_sizes", needPhotoSizes.asInt())
    )


    fun getAlbumsCountGroup(
            groupId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getAlbumsCount.call(client, callback, JsonObject()
            .put("group_id", groupId)
    )


    fun getAlbumsCountUser(
            userId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.getAlbumsCount.call(client, callback, JsonObject()
            .put("user_id", userId)
    )


    fun getAll(
            ownerId: Int?,
            extended: Boolean,
            offset: Int,
            count: Int,
            noServiceAlbums: Boolean,
            needHidden: Boolean,
            skipHidden: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getAll.call(client, callback, JsonObject()
            .put("owner_id", ownerId)
            .put("extended", extended.asInt())
            .put("offset", offset)
            .put("count", count)
            .put("no_service_albums", noServiceAlbums.asInt())
            .put("need_hidden", needHidden.asInt())
            .put("skip_hidden", skipHidden.asInt())
    )


    fun getAllComments(
            ownerId: Int?,
            albumId: Int?,
            needLikes: Boolean,
            offset: Int,
            count: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getAllComments.call(client, callback, JsonObject()
            .put("owner_id", ownerId)
            .put("album_id", albumId)
            .put("need_likes", needLikes.asInt())
            .put("offset", offset)
            .put("count", count)
    )


    fun getById(
            photos: List<Media>,
            extended: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getById.call(client, callback, JsonObject()
            .put("photos", photos.joinToString(","))
            .put("extended", extended.asInt())
    )


    fun getChatUploadServer(
            chatId: Int,
            cropX: Int? = null,
            cropY: Int? = null,
            cropWidth: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.getChatUploadServer.call(client, callback, JsonObject()
            .put("chat_id", chatId)
            .put("crop_x", cropX)
            .put("crop_y", cropY)
            .put("crop_width", cropWidth)
    )


    fun getComments(
            photoId: Int,
            ownerId: Int?,
            needLikes: Boolean,
            startCommentId: Int?,
            offset: Int,
            count: Int,
            sort: CommentsSort,
            accessKey: String?,
            extended: Boolean,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = Methods.getComments.call(client, callback, JsonObject()
            .put("photo_id", photoId)
            .put("owner_id", ownerId)
            .put("need_likes", needLikes.asInt())
            .put("start_comment_id", startCommentId)
            .put("offset", offset)
            .put("count", count)
            .put("sort", sort.value)
            .put("access_key", accessKey)
            .put("extended", extended.asInt())
            .put("fields", fields.joinToString(",") { it.value })
    )


    fun getMarketAlbumUploadServer(
            groupId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getMarketAlbumUploadServer.call(client, callback, JsonObject()
            .put("group_id", groupId)
    )


    fun getMarketUploadServer(
            groupId: Int,
            isMainPhoto: Boolean,
            cropX: Int?,
            cropY: Int?,
            cropWidth: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.getMarketUploadServer.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("main_photo", isMainPhoto.asInt())
            .put("crop_x", cropX)
            .put("crop_y", cropY)
            .put("crop_width", cropWidth)
    )


    fun getMessagesUploadServer(
            peerId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getMessagesUploadServer.call(client, JsonObject()
            .put("peer_id", peerId), callback = {callback.onResult(it!!.getAsJsonObject("response"))}
    )


    fun getNewTags(
            offset: Int,
            count: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getNewTags.call(client, callback, JsonObject()
            .put("offset", offset)
            .put("count", count)
    )


    fun getOwnerCoverPhotoUploadServer(
            groupId: Int,
            cropX: Int,
            cropY: Int,
            cropX2: Int,
            cropY2: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getOwnerCoverPhotoUploadServer.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("crop_x", cropX)
            .put("crop_y", cropY)
            .put("crop_x2", cropX2)
            .put("crop_y2", cropY2)
    )


    fun getOwnerPhotoUploadServer(
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.getOwnerPhotoUploadServer.call(client, callback, JsonObject()
            .put("owner_id", ownerId)
    )


    fun getTags(
            photoId: Int,
            ownerId: Int?,
            accessKey: String?,
            callback: Callback<JsonObject?>
    ) = Methods.getTags.call(client, callback, JsonObject()
            .put("photo_id", photoId)
            .put("owner_id", ownerId)
            .put("access_key", accessKey)
    )


    fun getUploadServer(
            albumId: Int,
            groupId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.getUploadServer.call(client, callback, JsonObject()
            .put("album_id", albumId)
            .put("group_id", groupId)
    )


    fun getUserPhotos(
            userId: Int?,
            offset: Int,
            count: Int,
            extended: Boolean,
            sortDescending: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getUserPhotos.call(client, callback, JsonObject()
            .put("user_id", userId)
            .put("offset", offset)
            .put("count", count)
            .put("extended", extended.asInt())
            .put("sort", sortDescending.not().asInt())
    )


    fun getWallUploadServer(
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.getWallUploadServer.call(client, callback, JsonObject()
            .put("group_id", groupId)
    )


    fun makeCover(
            photoId: Int,
            albumId: Int,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.makeCover.call(client, callback, JsonObject()
            .put("photo_id", photoId)
            .put("album_id", albumId)
            .put("owner_id", ownerId)
    )


    fun move(
            photoId: Int,
            targetAlbumId: Int,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.move.call(client, callback, JsonObject()
            .put("photo_id", photoId)
            .put("target_album_id", targetAlbumId)
            .put("owner_id", ownerId)
    )


    fun putTag(
            photoId: Int,
            userId: Int,
            x: Double,
            y: Double,
            x2: Double,
            y2: Double,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.putTag.call(client, callback, JsonObject()
            .put("photo_id", photoId)
            .put("user_id", userId)
            .put("x", x)
            .put("y", y)
            .put("x2", x2)
            .put("y2", y2)
            .put("owner_id", ownerId)
    )


    fun removeTag(
            photoId: Int,
            tagId: Int,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.removeTag.call(client, callback, JsonObject()
            .put("photo_id", photoId)
            .put("tag_id", tagId)
            .put("owner_id", ownerId)
    )


    fun reorderAlbums(
            albumId: Int,
            before: Int?,
            after: Int?,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.reorderAlbums.call(client, callback, JsonObject()
            .put("album_id", albumId)
            .put("before", before)
            .put("after", after)
            .put("owner_id", ownerId)
    )


    fun reorderPhotos(
            photoId: Int,
            before: Int?,
            after: Int?,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.reorderPhotos.call(client, callback, JsonObject()
            .put("photo_id", photoId)
            .put("before", before)
            .put("after", after)
            .put("owner_id", ownerId)
    )


    fun report(
            ownerId: Int,
            photoId: Int,
            reason: PostReportComplaintType,
            callback: Callback<JsonObject?>
    ) = Methods.report.call(client, callback, JsonObject()
            .put("owner_id", ownerId)
            .put("photo_id", photoId)
            .put("reason", reason.value)
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
            photoId: Int,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.restore.call(client, callback, JsonObject()
            .put("photo_id", photoId)
            .put("owner_id", ownerId)
    )


    fun restoreComment(
            commentId: Int,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.restoreComment.call(client, callback, JsonObject()
            .put("comment_id", commentId)
            .put("owner_id", ownerId)
    )


    fun save(
            albumId: Int,
            server: Int,
            photosList: String,
            hash: String,
            latitude: Double?,
            longitude: Double?,
            caption: String?,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.save.call(client, callback, JsonObject()
            .put("album_id", albumId)
            .put("server", server)
            .put("photos_list", photosList)
            .put("hash", hash)
            .put("latitude", latitude)
            .put("longitude", longitude)
            .put("caption", caption)
            .put("group_id", groupId)
    )


    fun saveMarketAlbumPhoto(
            groupId: Int,
            server: Int,
            photo: String,
            hash: String,
            callback: Callback<JsonObject?>
    ) = Methods.saveMarketAlbumPhoto.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("server", server)
            .put("photo", photo)
            .put("hash", hash)
    )


    fun saveMarketPhoto(
            server: Int,
            photo: String,
            hash: String,
            cropData: String?,
            cropHash: String?,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.saveMarketPhoto.call(client, callback, JsonObject()
            .put("server", server)
            .put("photo", photo)
            .put("hash", hash)
            .put("crop_data", cropData)
            .put("crop_hash", cropHash)
            .put("group_id", groupId)
    )


    fun saveMessagesPhoto(
            server: Int,
            photo: String,
            hash: String,
            callback: Callback<JsonArray?>
    ) = Methods.saveMessagesPhoto.call(client, JsonObject()
            .put("server", server)
            .put("photo", photo)
            .put("hash", hash),

            callback = {callback.onResult(it!!.getAsJsonArray("response"))}
    )


    fun saveOwnerCoverPhoto(
            photo: String,
            hash: String,
            callback: Callback<JsonObject?>
    ) = Methods.saveOwnerCoverPhoto.call(client, callback, JsonObject()
            .put("photo", photo)
            .put("hash", hash)
    )


    fun saveOwnerPhoto(
            server: Int,
            photo: String,
            hash: String,
            callback: Callback<JsonObject?>
    ) = Methods.saveOwnerPhoto.call(client, callback, JsonObject()
            .put("server", server)
            .put("photo", photo)
            .put("hash", hash)
    )


    fun saveWallPhoto(
            server: Int,
            photo: String,
            hash: String,
            userId: Int?,
            groupId: Int?,
            latitude: Double?,
            longitude: Double?,
            caption: String?,
            callback: Callback<JsonObject?>
    ) = Methods.saveWallPhoto.call(client, callback, JsonObject()
            .put("server", server)
            .put("photo", photo)
            .put("hash", hash)
            .put("user_id", userId)
            .put("group_id", groupId)
            .put("latitude", latitude)
            .put("longitude", longitude)
            .put("caption", caption)
    )


    fun search(
            query: String?,
            latitude: Double?,
            longitude: Double?,
            startTime: Int?,
            endTime: Int?,
            sort: PhotoSearchSort,
            offset: Int,
            count: Int,
            radius: Int,
            callback: Callback<JsonObject?>
    ) = Methods.search.call(client, callback, JsonObject()
            .put("q", query)
            .put("lat", latitude)
            .put("long", longitude)
            .put("start_time", startTime)
            .put("end_time", endTime)
            .put("sort", sort.value)
            .put("offset", offset)
            .put("count", count)
            .put("radius", radius)
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
        const val putTag = it + "putTag"
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