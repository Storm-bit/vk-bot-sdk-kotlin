package com.github.stormbit.sdk.vkapi.methods.photos

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.attachments.Photo
import com.github.stormbit.sdk.objects.models.*
import com.github.stormbit.sdk.utils.parametersOf
import com.github.stormbit.sdk.utils.toInt
import kotlinx.serialization.json.JsonObject
import com.github.stormbit.sdk.vkapi.methods.*
import com.github.stormbit.sdk.vkapi.methods.UploadServerResponse
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import javax.print.attribute.standard.Media

@Suppress("unused")
class PhotosApi(private val client: Client) : MethodsContext() {

    fun confirmTag(
            photoId: Int,
            tagId: Int,
            ownerId: Int? = null
    ): JsonObject? = Methods.confirmTag.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("photo_id", photoId)
            append("tag_id", tagId)
            append("owner_id", ownerId)
        }
    )


    fun copy(
            ownerId: Int,
            photoId: Int,
            accessKey: String? = null
    ): JsonObject? = Methods.copy.callSync(
        client, JsonObject.serializer(), parametersOf {
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
        isCommentsDisabled: Boolean
    ): JsonObject? = Methods.createAlbum.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            guid: String? = null
    ): JsonObject? = Methods.createComment.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            ownerId: Int? = null
    ): JsonObject? = Methods.delete.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("photo_id", photoId)
            append("owner_id", ownerId)
        }
    )


    fun deleteAlbum(
            albumId: Int,
            groupId: Int? = null
    ): JsonObject? = Methods.deleteAlbum.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("album_id", albumId)
            append("group_id", groupId)
        }
    )


    fun deleteComment(
            commentId: Int,
            ownerId: Int? = null
    ): JsonObject? = Methods.deleteComment.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            deletePlace: Boolean
    ): JsonObject? = Methods.edit.callSync(
        client, JsonObject.serializer(), parametersOf {
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
        isCommentsDisabled: Boolean? = null
    ): JsonObject? = Methods.editAlbum.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            attachments: List<String>? = null
    ): JsonObject? = Methods.editComment.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            count: Int
    ): JsonObject? = Methods.get.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            needPhotoSizes: Boolean
    ): JsonObject? = Methods.getAlbums.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            groupId: Int
    ): JsonObject? = Methods.getAlbumsCount.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("group_id", groupId)
        }
    )


    fun getAlbumsCountUser(
            userId: Int? = null
    ): JsonObject? = Methods.getAlbumsCount.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            skipHidden: Boolean
    ): ListResponse<Photo>? = Methods.getAll.callSync(
        client, ListResponse.serializer(Photo.serializer()), parametersOf {
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
            count: Int
    ): ListResponse<WallComment>? = Methods.getAllComments.callSync(
        client, ListResponse.serializer(WallComment.serializer()), parametersOf {
            append("owner_id", ownerId)
            append("album_id", albumId)
            append("need_likes", needLikes.toInt())
            append("offset", offset)
            append("count", count)
        }
    )


    fun getById(
            photos: List<Media>,
            extended: Boolean
    ): List<Photo>? = Methods.getById.callSync(
        client, ListSerializer(Photo.serializer()), parametersOf {
            append("photos", photos.joinToString(","))
            append("extended", extended.toInt())
        }
    )


    fun getChatUploadServer(
            chatId: Int,
            cropX: Int? = null,
            cropY: Int? = null,
            cropWidth: Int? = null
    ): UploadServerResponse? = Methods.getChatUploadServer.callSync(
        client, UploadServerResponse.serializer(), parametersOf {
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
            fields: List<ObjectField>
    ): JsonObject? = Methods.getComments.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            groupId: Int
    ): JsonObject? = Methods.getMarketAlbumUploadServer.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("group_id", groupId)
        }
    )


    fun getMarketUploadServer(
            groupId: Int,
            isMainPhoto: Boolean,
            cropX: Int? = null,
            cropY: Int? = null,
            cropWidth: Int? = null
    ): JsonObject? = Methods.getMarketUploadServer.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("group_id", groupId)
            append("main_photo", isMainPhoto.toInt())
            append("crop_x", cropX)
            append("crop_y", cropY)
            append("crop_width", cropWidth)
        }
    )


    fun getMessagesUploadServer(
            peerId: Int
    ): PhotoUploadServerResponse? = Methods.getMessagesUploadServer.callSync(
        client, PhotoUploadServerResponse.serializer(), parametersOf {
            append("peer_id", peerId)
        }
    )


    fun getNewTags(
            offset: Int,
            count: Int
    ): JsonObject? = Methods.getNewTags.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("offset", offset)
            append("count", count)
        }
    )


    fun getOwnerCoverPhotoUploadServer(
            groupId: Int,
            cropX: Int,
            cropY: Int,
            cropX2: Int,
            cropY2: Int
    ): UploadServerResponse? = Methods.getOwnerCoverPhotoUploadServer.callSync(
        client, UploadServerResponse.serializer(), parametersOf {
            append("group_id", groupId)
            append("crop_x", cropX)
            append("crop_y", cropY)
            append("crop_x2", cropX2)
            append("crop_y2", cropY2)
        }
    )


    fun getOwnerPhotoUploadServer(
            ownerId: Int? = null
    ): UploadServerResponse? = Methods.getOwnerPhotoUploadServer.callSync(
        client, UploadServerResponse.serializer(), parametersOf {
            append("owner_id", ownerId)
        }
    )


    fun getTags(
            photoId: Int,
            ownerId: Int? = null,
            accessKey: String? = null
    ): List<Tag>? = Methods.getTags.callSync(
        client, ListSerializer(Tag.serializer()), parametersOf {
            append("photo_id", photoId)
            append("owner_id", ownerId)
            append("access_key", accessKey)
        }
    )


    fun getUploadServer(
            albumId: Int,
            groupId: Int? = null
    ): PhotoUploadServerResponse? = Methods.getUploadServer.callSync(
        client, PhotoUploadServerResponse.serializer(), parametersOf {
            append("album_id", albumId)
            append("group_id", groupId)
        }
    )


    fun getUserPhotos(
            userId: Int? = null,
            offset: Int,
            count: Int,
            extended: Boolean,
            sortDescending: Boolean
    ): JsonObject? = Methods.getUserPhotos.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("user_id", userId)
            append("offset", offset)
            append("count", count)
            append("extended", extended.toInt())
            append("sort", sortDescending.not().toInt())
        }
    )


    fun getWallUploadServer(
            groupId: Int? = null
    ): PhotoUploadServerResponse? = Methods.getWallUploadServer.callSync(
        client, PhotoUploadServerResponse.serializer(), parametersOf {
            append("group_id", groupId)
        }
    )


    fun makeCover(
            photoId: Int,
            albumId: Int,
            ownerId: Int? = null
    ): Int? = Methods.makeCover.callSync(
        client, Int.serializer(), parametersOf {
            append("photo_id", photoId)
            append("album_id", albumId)
            append("owner_id", ownerId)
        }
    )


    fun move(
            photoId: Int,
            targetAlbumId: Int,
            ownerId: Int? = null
    ): Int? = Methods.move.callSync(
        client, Int.serializer(), parametersOf {
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
            ownerId: Int? = null
    ): JsonObject? = Methods.appendTag.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            ownerId: Int? = null
    ): Int? = Methods.removeTag.callSync(
        client, Int.serializer(), parametersOf {
            append("photo_id", photoId)
            append("tag_id", tagId)
            append("owner_id", ownerId)
        }
    )


    fun reorderAlbums(
            albumId: Int,
            before: Int? = null,
            after: Int? = null,
            ownerId: Int? = null
    ): Int? = Methods.reorderAlbums.callSync(
        client, Int.serializer(), parametersOf {
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
            ownerId: Int? = null
    ): Int? = Methods.reorderPhotos.callSync(
        client, Int.serializer(), parametersOf {
            append("photo_id", photoId)
            append("before", before)
            append("after", after)
            append("owner_id", ownerId)
        }
    )


    fun report(
            ownerId: Int,
            photoId: Int,
            reason: PostReportComplaintType
    ): Int? = Methods.report.callSync(
        client, Int.serializer(), parametersOf {
            append("owner_id", ownerId)
            append("photo_id", photoId)
            append("reason", reason.value)
        }
    )


    fun reportComment(
            ownerId: Int,
            commentId: Int,
            reason: PostReportComplaintType
    ): Int? = Methods.reportComment.callSync(
        client, Int.serializer(), parametersOf {
            append("owner_id", ownerId)
            append("comment_id", commentId)
            append("reason", reason.value)
        }
    )


    fun restore(
            photoId: Int,
            ownerId: Int? = null
    ): JsonObject? = Methods.restore.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("photo_id", photoId)
            append("owner_id", ownerId)
        }
    )


    fun restoreComment(
            commentId: Int,
            ownerId: Int? = null
    ): JsonObject? = Methods.restoreComment.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            groupId: Int? = null
    ): List<Photo>? = Methods.save.callSync(
        client, ListSerializer(Photo.serializer()), parametersOf {
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
            hash: String
    ): JsonObject? = Methods.saveMarketAlbumPhoto.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            groupId: Int? = null
    ): JsonObject? = Methods.saveMarketPhoto.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            hash: String
    ): List<Photo>? = Methods.saveMessagesPhoto.callSync(
        client, ListSerializer(Photo.serializer()), parametersOf {
            append("server", server)
            append("photo", photo)
            append("hash", hash)
        }
    )


    fun saveOwnerCoverPhoto(
            photo: String,
            hash: String
    ): OwnerCoverPhotoResponse? = Methods.saveOwnerCoverPhoto.callSync(
        client, OwnerCoverPhotoResponse.serializer(), parametersOf {
            append("photo", photo)
            append("hash", hash)
        }
    )


    fun saveOwnerPhoto(
            server: Int,
            photo: String,
            hash: String
    ): JsonObject? = Methods.saveOwnerPhoto.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            caption: String? = null
    ): JsonObject? = Methods.saveWallPhoto.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            radius: Int
    ): JsonObject? = Methods.search.callSync(
        client, JsonObject.serializer(), parametersOf {
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