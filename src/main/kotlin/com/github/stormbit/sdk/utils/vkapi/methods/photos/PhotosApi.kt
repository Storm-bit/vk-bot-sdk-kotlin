package com.github.stormbit.sdk.utils.vkapi.methods.photos

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.callSync
import com.github.stormbit.sdk.utils.vkapi.methods.*
import org.json.JSONArray
import org.json.JSONObject
import javax.print.attribute.standard.Media

@Suppress("unused")
class PhotosApi(private val client: Client) {

    fun confirmTag(
            photoId: Int,
            tagId: Int,
            ownerId: Int?
    ): JSONObject = Methods.confirmTag.callSync(client, JSONObject()
            .put("photo_id", photoId)
            .put("tag_id", tagId)
            .put("owner_id", ownerId)
    )


    fun copy(
            ownerId: Int,
            photoId: Int,
            accessKey: String?
    ): JSONObject = Methods.copy.callSync(client, JSONObject()
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
            isCommentsDisabled: Boolean
    ): JSONObject = Methods.createAlbum.callSync(client, JSONObject()
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
            guid: String?
    ): JSONObject = Methods.createComment.callSync(client, JSONObject()
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
            ownerId: Int?
    ): JSONObject = Methods.delete.callSync(client, JSONObject()
            .put("photo_id", photoId)
            .put("owner_id", ownerId)
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
            photoId: Int,
            ownerId: Int?,
            caption: String,
            latitude: Double?,
            longitude: Double?,
            placeName: String?,
            foursquareId: String?,
            deletePlace: Boolean
    ): JSONObject = Methods.edit.callSync(client, JSONObject()
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
            isCommentsDisabled: Boolean?
    ): JSONObject = Methods.editAlbum.callSync(client, JSONObject()
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
            attachments: List<String>?
    ): JSONObject = Methods.editComment.callSync(client, JSONObject()
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
            count: Int
    ): JSONObject = Methods.get.callSync(client, JSONObject()
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
            needPhotoSizes: Boolean
    ): JSONObject = Methods.getAlbums.callSync(client, JSONObject()
            .put("album_ids", albumIds?.joinToString(","))
            .put("owner_id", ownerId)
            .put("offset", offset)
            .put("count", count)
            .put("need_system", needSystem.asInt())
            .put("need_covers", needCovers.asInt())
            .put("photo_sizes", needPhotoSizes.asInt())
    )


    fun getAlbumsCountGroup(
            groupId: Int
    ): JSONObject = Methods.getAlbumsCount.callSync(client, JSONObject()
            .put("group_id", groupId)
    )


    fun getAlbumsCountUser(
            userId: Int?
    ): JSONObject = Methods.getAlbumsCount.callSync(client, JSONObject()
            .put("user_id", userId)
    )


    fun getAll(
            ownerId: Int?,
            extended: Boolean,
            offset: Int,
            count: Int,
            noServiceAlbums: Boolean,
            needHidden: Boolean,
            skipHidden: Boolean
    ): JSONObject = Methods.getAll.callSync(client, JSONObject()
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
            count: Int
    ): JSONObject = Methods.getAllComments.callSync(client, JSONObject()
            .put("owner_id", ownerId)
            .put("album_id", albumId)
            .put("need_likes", needLikes.asInt())
            .put("offset", offset)
            .put("count", count)
    )


    fun getById(
            photos: List<Media>,
            extended: Boolean
    ): JSONObject = Methods.getById.callSync(client, JSONObject()
            .put("photos", photos.joinToString(","))
            .put("extended", extended.asInt())
    )


    fun getChatUploadServer(
            chatId: Int,
            cropX: Int? = null,
            cropY: Int? = null,
            cropWidth: Int? = null
    ): JSONObject = Methods.getChatUploadServer.callSync(client, JSONObject()
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
            fields: List<ObjectField>
    ): JSONObject = Methods.getComments.callSync(client, JSONObject()
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
            groupId: Int
    ): JSONObject = Methods.getMarketAlbumUploadServer.callSync(client, JSONObject()
            .put("group_id", groupId)
    )


    fun getMarketUploadServer(
            groupId: Int,
            isMainPhoto: Boolean,
            cropX: Int?,
            cropY: Int?,
            cropWidth: Int?
    ): JSONObject = Methods.getMarketUploadServer.callSync(client, JSONObject()
            .put("group_id", groupId)
            .put("main_photo", isMainPhoto.asInt())
            .put("crop_x", cropX)
            .put("crop_y", cropY)
            .put("crop_width", cropWidth)
    )


    fun getMessagesUploadServer(
            peerId: Int
    ): JSONObject = Methods.getMessagesUploadServer.callSync(client, JSONObject()
            .put("peer_id", peerId)
    ).getJSONObject("response")


    fun getNewTags(
            offset: Int,
            count: Int
    ): JSONObject = Methods.getNewTags.callSync(client, JSONObject()
            .put("offset", offset)
            .put("count", count)
    )


    fun getOwnerCoverPhotoUploadServer(
            groupId: Int,
            cropX: Int,
            cropY: Int,
            cropX2: Int,
            cropY2: Int
    ): JSONObject = Methods.getOwnerCoverPhotoUploadServer.callSync(client, JSONObject()
            .put("group_id", groupId)
            .put("crop_x", cropX)
            .put("crop_y", cropY)
            .put("crop_x2", cropX2)
            .put("crop_y2", cropY2)
    )


    fun getOwnerPhotoUploadServer(
            ownerId: Int?
    ): JSONObject = Methods.getOwnerPhotoUploadServer.callSync(client, JSONObject()
            .put("owner_id", ownerId)
    )


    fun getTags(
            photoId: Int,
            ownerId: Int?,
            accessKey: String?
    ): JSONObject = Methods.getTags.callSync(client, JSONObject()
            .put("photo_id", photoId)
            .put("owner_id", ownerId)
            .put("access_key", accessKey)
    )


    fun getUploadServer(
            albumId: Int,
            groupId: Int? = null
    ): JSONObject = Methods.getUploadServer.callSync(client, JSONObject()
            .put("album_id", albumId)
            .put("group_id", groupId)
    )


    fun getUserPhotos(
            userId: Int?,
            offset: Int,
            count: Int,
            extended: Boolean,
            sortDescending: Boolean
    ): JSONObject = Methods.getUserPhotos.callSync(client, JSONObject()
            .put("user_id", userId)
            .put("offset", offset)
            .put("count", count)
            .put("extended", extended.asInt())
            .put("sort", sortDescending.not().asInt())
    )


    fun getWallUploadServer(
            groupId: Int?
    ): JSONObject = Methods.getWallUploadServer.callSync(client, JSONObject()
            .put("group_id", groupId)
    )


    fun makeCover(
            photoId: Int,
            albumId: Int,
            ownerId: Int?
    ): JSONObject = Methods.makeCover.callSync(client, JSONObject()
            .put("photo_id", photoId)
            .put("album_id", albumId)
            .put("owner_id", ownerId)
    )


    fun move(
            photoId: Int,
            targetAlbumId: Int,
            ownerId: Int?
    ): JSONObject = Methods.move.callSync(client, JSONObject()
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
            ownerId: Int?
    ): JSONObject = Methods.putTag.callSync(client, JSONObject()
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
            ownerId: Int?
    ): JSONObject = Methods.removeTag.callSync(client, JSONObject()
            .put("photo_id", photoId)
            .put("tag_id", tagId)
            .put("owner_id", ownerId)
    )


    fun reorderAlbums(
            albumId: Int,
            before: Int?,
            after: Int?,
            ownerId: Int?
    ): JSONObject = Methods.reorderAlbums.callSync(client, JSONObject()
            .put("album_id", albumId)
            .put("before", before)
            .put("after", after)
            .put("owner_id", ownerId)
    )


    fun reorderPhotos(
            photoId: Int,
            before: Int?,
            after: Int?,
            ownerId: Int?
    ): JSONObject = Methods.reorderPhotos.callSync(client, JSONObject()
            .put("photo_id", photoId)
            .put("before", before)
            .put("after", after)
            .put("owner_id", ownerId)
    )


    fun report(
            ownerId: Int,
            photoId: Int,
            reason: PostReportComplaintType
    ): JSONObject = Methods.report.callSync(client, JSONObject()
            .put("owner_id", ownerId)
            .put("photo_id", photoId)
            .put("reason", reason.value)
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
            photoId: Int,
            ownerId: Int?
    ): JSONObject = Methods.restore.callSync(client, JSONObject()
            .put("photo_id", photoId)
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
            albumId: Int,
            server: Int,
            photosList: String,
            hash: String,
            latitude: Double?,
            longitude: Double?,
            caption: String?,
            groupId: Int?
    ): JSONObject = Methods.save.callSync(client, JSONObject()
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
            hash: String
    ): JSONObject = Methods.saveMarketAlbumPhoto.callSync(client, JSONObject()
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
            groupId: Int?
    ): JSONObject = Methods.saveMarketPhoto.callSync(client, JSONObject()
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
            hash: String
    ): JSONArray = Methods.saveMessagesPhoto.callSync(client, JSONObject()
            .put("server", server)
            .put("photo", photo)
            .put("hash", hash)
    ).getJSONArray("response")


    fun saveOwnerCoverPhoto(
            photo: String,
            hash: String
    ): JSONObject = Methods.saveOwnerCoverPhoto.callSync(client, JSONObject()
            .put("photo", photo)
            .put("hash", hash)
    )


    fun saveOwnerPhoto(
            server: Int,
            photo: String,
            hash: String
    ): JSONObject = Methods.saveOwnerPhoto.callSync(client, JSONObject()
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
            caption: String?
    ): JSONObject = Methods.saveWallPhoto.callSync(client, JSONObject()
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
            radius: Int
    ): JSONObject = Methods.search.callSync(client, JSONObject()
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