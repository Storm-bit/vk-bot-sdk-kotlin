package com.github.stormbit.vksdk.vkapi.methods.photos

import com.github.stormbit.vksdk.clients.Client
import com.github.stormbit.vksdk.objects.attachments.Photo
import com.github.stormbit.vksdk.objects.models.ListResponse
import com.github.stormbit.vksdk.objects.models.PrivacySettings
import com.github.stormbit.vksdk.objects.models.Tag
import com.github.stormbit.vksdk.objects.models.WallComment
import com.github.stormbit.vksdk.utils.append
import com.github.stormbit.vksdk.utils.toInt
import com.github.stormbit.vksdk.vkapi.VkApiRequest
import com.github.stormbit.vksdk.vkapi.methods.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonObject
import javax.print.attribute.standard.Media

@Suppress("unused")
class PhotosApi(client: Client) : MethodsContext(client) {

    fun confirmTag(
        photoId: Int,
        tagId: Int,
        ownerId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.confirmTag.call(JsonObject.serializer()) {
        append("photo_id", photoId)
        append("tag_id", tagId)
        append("owner_id", ownerId)
    }


    fun copy(
        ownerId: Int,
        photoId: Int,
        accessKey: String? = null
    ): VkApiRequest<JsonObject> = Methods.copy.call(JsonObject.serializer()) {
        append("owner_id", ownerId)
        append("photo_id", photoId)
        append("access_key", accessKey)
    }


    fun createAlbum(
        title: String,
        description: String? = null,
        privacyView: PrivacySettings,
        privacyComment: PrivacySettings,
        groupId: Int? = null,
        isUploadByAdminsOnly: Boolean,
        isCommentsDisabled: Boolean
    ): VkApiRequest<JsonObject> = Methods.createAlbum.call(JsonObject.serializer()) {
        append("title", title)
        append("description", description)
        append("privacy_view", privacyView.toRequestString())
        append("privacy_comment", privacyComment.toRequestString())
        append("group_id", groupId)
        append("upload_by_admins_only", isUploadByAdminsOnly.toInt())
        append("comments_disabled", isCommentsDisabled.toInt())
    }


    fun createComment(
        photoId: Int,
        ownerId: Int? = null,
        message: String? = null,
        attachments: List<String>? = null,
        stickerId: Int? = null,
        fromGroup: Boolean,
        accessKey: String? = null,
        guid: String? = null
    ): VkApiRequest<JsonObject> = Methods.createComment.call(JsonObject.serializer()) {
        append("photo_id", photoId)
        append("owner_id", ownerId)
        append("message", message)
        append("items", attachments?.joinToString(","))
        append("sticker_id", stickerId)
        append("from_group", fromGroup.toInt())
        append("access_key", accessKey)
        append("guid", guid)
    }


    fun delete(
        photoId: Int,
        ownerId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.delete.call(JsonObject.serializer()) {
        append("photo_id", photoId)
        append("owner_id", ownerId)
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
        photoId: Int,
        ownerId: Int? = null,
        caption: String,
        latitude: Double? = null,
        longitude: Double? = null,
        placeName: String? = null,
        foursquareId: String? = null,
        deletePlace: Boolean
    ): VkApiRequest<JsonObject> = Methods.edit.call(JsonObject.serializer()) {
        append("photo_id", photoId)
        append("owner_id", ownerId)
        append("caption", caption)
        append("latitude", latitude)
        append("longitude", longitude)
        append("place_str", placeName)
        append("foursquare_id", foursquareId)
        append("delete_place", deletePlace.toInt())
    }


    fun editAlbum(
        albumId: Int,
        ownerId: Int? = null,
        title: String? = null,
        description: String? = null,
        privacyView: PrivacySettings? = null,
        privacyComment: PrivacySettings? = null,
        isUploadByAdminsOnly: Boolean? = null,
        isCommentsDisabled: Boolean? = null
    ): VkApiRequest<JsonObject> = Methods.editAlbum.call(JsonObject.serializer()) {
        append("album_id", albumId)
        append("owner_id", ownerId)
        append("title", title)
        append("description", description)
        append("privacy_view", privacyView?.toRequestString())
        append("privacy_comment", privacyComment?.toRequestString())
        append("upload_by_admins_only", isUploadByAdminsOnly?.toInt())
        append("comments_disabled", isCommentsDisabled?.toInt())
    }


    fun editComment(
        commentId: Int,
        ownerId: Int? = null,
        message: String? = null,
        attachments: List<String>? = null
    ): VkApiRequest<JsonObject> = Methods.editComment.call(JsonObject.serializer()) {
        append("comment_id", commentId)
        append("owner_id", ownerId)
        append("message", message)
        append("items", attachments?.joinToString(","))
    }


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
    ): VkApiRequest<JsonObject> = Methods.get.call(JsonObject.serializer()) {
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


    fun getAlbums(
        albumIds: List<Int>? = null,
        ownerId: Int? = null,
        offset: Int,
        count: Int,
        needSystem: Boolean,
        needCovers: Boolean,
        needPhotoSizes: Boolean
    ): VkApiRequest<JsonObject> = Methods.getAlbums.call(JsonObject.serializer()) {
        append("album_ids", albumIds?.joinToString(","))
        append("owner_id", ownerId)
        append("offset", offset)
        append("count", count)
        append("need_system", needSystem.toInt())
        append("need_covers", needCovers.toInt())
        append("photo_sizes", needPhotoSizes.toInt())
    }


    fun getAlbumsCountGroup(
        groupId: Int
    ): VkApiRequest<JsonObject> = Methods.getAlbumsCount.call(JsonObject.serializer()) {
        append("group_id", groupId)
    }


    fun getAlbumsCountUser(
        userId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.getAlbumsCount.call(JsonObject.serializer()) {
        append("user_id", userId)
    }


    fun getAll(
        ownerId: Int? = null,
        extended: Boolean,
        offset: Int,
        count: Int,
        noServiceAlbums: Boolean,
        needHidden: Boolean,
        skipHidden: Boolean
    ): VkApiRequest<ListResponse<Photo>> = Methods.getAll.call(ListResponse.serializer(Photo.serializer())) {
        append("owner_id", ownerId)
        append("extended", extended.toInt())
        append("offset", offset)
        append("count", count)
        append("no_service_albums", noServiceAlbums.toInt())
        append("need_hidden", needHidden.toInt())
        append("skip_hidden", skipHidden.toInt())
    }


    fun getAllComments(
        ownerId: Int? = null,
        albumId: Int? = null,
        needLikes: Boolean,
        offset: Int,
        count: Int
    ): VkApiRequest<ListResponse<WallComment>> = Methods.getAllComments.call(ListResponse.serializer(WallComment.serializer())) {
        append("owner_id", ownerId)
        append("album_id", albumId)
        append("need_likes", needLikes.toInt())
        append("offset", offset)
        append("count", count)
    }


    fun getById(
        photos: List<Media>,
        extended: Boolean
    ): VkApiRequest<List<Photo>> = Methods.getById.call(ListSerializer(Photo.serializer())) {
        append("photos", photos.joinToString(","))
        append("extended", extended.toInt())
    }


    fun getChatUploadServer(
        chatId: Int,
        cropX: Int? = null,
        cropY: Int? = null,
        cropWidth: Int? = null
    ): VkApiRequest<UploadServerResponse> = Methods.getChatUploadServer.call(UploadServerResponse.serializer()) {
        append("chat_id", chatId)
        append("crop_x", cropX)
        append("crop_y", cropY)
        append("crop_width", cropWidth)
    }


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
    ): VkApiRequest<JsonObject> = Methods.getComments.call(JsonObject.serializer()) {
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


    fun getMarketAlbumUploadServer(
        groupId: Int
    ): VkApiRequest<UploadServerResponse> = Methods.getMarketAlbumUploadServer.call(UploadServerResponse.serializer()) {
        append("group_id", groupId)
    }


    fun getMarketUploadServer(
        groupId: Int,
        isMainPhoto: Boolean,
        cropX: Int? = null,
        cropY: Int? = null,
        cropWidth: Int? = null
    ): VkApiRequest<UploadServerResponse> = Methods.getMarketUploadServer.call(UploadServerResponse.serializer()) {
        append("group_id", groupId)
        append("main_photo", isMainPhoto.toInt())
        append("crop_x", cropX)
        append("crop_y", cropY)
        append("crop_width", cropWidth)
    }


    fun getMessagesUploadServer(
        peerId: Int
    ): VkApiRequest<PhotoUploadServerResponse> = Methods.getMessagesUploadServer.call(PhotoUploadServerResponse.serializer()) {
        append("peer_id", peerId)
    }


    fun getNewTags(
        offset: Int,
        count: Int
    ): VkApiRequest<JsonObject> = Methods.getNewTags.call(JsonObject.serializer()) {
        append("offset", offset)
        append("count", count)
    }


    fun getOwnerCoverPhotoUploadServer(
        groupId: Int,
        cropX: Int,
        cropY: Int,
        cropX2: Int,
        cropY2: Int
    ): VkApiRequest<UploadServerResponse> = Methods.getOwnerCoverPhotoUploadServer.call(UploadServerResponse.serializer()) {
        append("group_id", groupId)
        append("crop_x", cropX)
        append("crop_y", cropY)
        append("crop_x2", cropX2)
        append("crop_y2", cropY2)
    }


    fun getOwnerPhotoUploadServer(
        ownerId: Int? = null
    ): VkApiRequest<UploadServerResponse> = Methods.getOwnerPhotoUploadServer.call(UploadServerResponse.serializer()) {
        append("owner_id", ownerId)
    }


    fun getTags(
        photoId: Int,
        ownerId: Int? = null,
        accessKey: String? = null
    ): VkApiRequest<List<Tag>> = Methods.getTags.call(ListSerializer(Tag.serializer())) {
        append("photo_id", photoId)
        append("owner_id", ownerId)
        append("access_key", accessKey)
    }


    fun getUploadServer(
        albumId: Int,
        groupId: Int? = null
    ): VkApiRequest<PhotoUploadServerResponse> = Methods.getUploadServer.call(PhotoUploadServerResponse.serializer()) {
        append("album_id", albumId)
        append("group_id", groupId)
    }


    fun getUserPhotos(
        userId: Int? = null,
        offset: Int,
        count: Int,
        extended: Boolean,
        sortDescending: Boolean
    ): VkApiRequest<JsonObject> = Methods.getUserPhotos.call(JsonObject.serializer()) {
        append("user_id", userId)
        append("offset", offset)
        append("count", count)
        append("extended", extended.toInt())
        append("sort", sortDescending.not().toInt())
    }


    fun getWallUploadServer(
        groupId: Int? = null
    ): VkApiRequest<PhotoUploadServerResponse> = Methods.getWallUploadServer.call(PhotoUploadServerResponse.serializer()) {
        append("group_id", groupId)
    }


    fun makeCover(
        photoId: Int,
        albumId: Int,
        ownerId: Int? = null
    ): VkApiRequest<Int> = Methods.makeCover.call(Int.serializer()) {
        append("photo_id", photoId)
        append("album_id", albumId)
        append("owner_id", ownerId)
    }


    fun move(
        photoId: Int,
        targetAlbumId: Int,
        ownerId: Int? = null
    ): VkApiRequest<Int> = Methods.move.call(Int.serializer()) {
        append("photo_id", photoId)
        append("target_album_id", targetAlbumId)
        append("owner_id", ownerId)
    }


    fun appendTag(
        photoId: Int,
        userId: Int,
        x: Double,
        y: Double,
        x2: Double,
        y2: Double,
        ownerId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.appendTag.call(JsonObject.serializer()) {
        append("photo_id", photoId)
        append("user_id", userId)
        append("x", x)
        append("y", y)
        append("x2", x2)
        append("y2", y2)
        append("owner_id", ownerId)
    }


    fun removeTag(
        photoId: Int,
        tagId: Int,
        ownerId: Int? = null
    ): VkApiRequest<Int> = Methods.removeTag.call(Int.serializer()) {
        append("photo_id", photoId)
        append("tag_id", tagId)
        append("owner_id", ownerId)
    }


    fun reorderAlbums(
        albumId: Int,
        before: Int? = null,
        after: Int? = null,
        ownerId: Int? = null
    ): VkApiRequest<Int> = Methods.reorderAlbums.call(Int.serializer()) {
        append("album_id", albumId)
        append("before", before)
        append("after", after)
        append("owner_id", ownerId)
    }


    fun reorderPhotos(
        photoId: Int,
        before: Int? = null,
        after: Int? = null,
        ownerId: Int? = null
    ): VkApiRequest<Int> = Methods.reorderPhotos.call(Int.serializer()) {
        append("photo_id", photoId)
        append("before", before)
        append("after", after)
        append("owner_id", ownerId)
    }


    fun report(
        ownerId: Int,
        photoId: Int,
        reason: PostReportComplaintType
    ): VkApiRequest<Int> = Methods.report.call(Int.serializer()) {
        append("owner_id", ownerId)
        append("photo_id", photoId)
        append("reason", reason.value)
    }


    fun reportComment(
        ownerId: Int,
        commentId: Int,
        reason: PostReportComplaintType
    ): VkApiRequest<Int> = Methods.reportComment.call(Int.serializer()) {
        append("owner_id", ownerId)
        append("comment_id", commentId)
        append("reason", reason.value)
    }


    fun restore(
        photoId: Int,
        ownerId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.restore.call(JsonObject.serializer()) {
        append("photo_id", photoId)
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
        albumId: Int,
        server: Int,
        photosList: String,
        hash: String,
        latitude: Double? = null,
        longitude: Double? = null,
        caption: String? = null,
        groupId: Int? = null
    ): VkApiRequest<List<Photo>> = Methods.save.call(ListSerializer(Photo.serializer())) {
        append("album_id", albumId)
        append("server", server)
        append("photos_list", photosList)
        append("hash", hash)
        append("latitude", latitude)
        append("longitude", longitude)
        append("caption", caption)
        append("group_id", groupId)
    }


    fun saveMarketAlbumPhoto(
        groupId: Int,
        server: Int,
        photo: String,
        hash: String
    ): VkApiRequest<List<Photo>> = Methods.saveMarketAlbumPhoto.call(ListSerializer(Photo.serializer())) {
        append("group_id", groupId)
        append("server", server)
        append("photo", photo)
        append("hash", hash)
    }


    fun saveMarketPhoto(
        server: Int,
        photo: String,
        hash: String,
        cropData: String? = null,
        cropHash: String? = null,
        groupId: Int
    ): VkApiRequest<List<Photo>> = Methods.saveMarketPhoto.call(ListSerializer(Photo.serializer())) {
        append("server", server)
        append("photo", photo)
        append("hash", hash)
        append("crop_data", cropData)
        append("crop_hash", cropHash)
        append("group_id", groupId)
    }


    fun saveMessagesPhoto(
        server: Int,
        photo: String,
        hash: String
    ): VkApiRequest<List<Photo>> = Methods.saveMessagesPhoto.call(ListSerializer(Photo.serializer())) {
        append("server", server)
        append("photo", photo)
        append("hash", hash)
    }


    fun saveOwnerCoverPhoto(
        photo: String,
        hash: String
    ): VkApiRequest<List<OwnerCoverPhotoResponse>> = Methods.saveOwnerCoverPhoto.call(ListSerializer(OwnerCoverPhotoResponse.serializer())) {
        append("photo", photo)
        append("hash", hash)
    }


    fun saveOwnerPhoto(
        server: Int,
        photo: String,
        hash: String
    ): VkApiRequest<OwnerCoverPhotoResponse> = Methods.saveOwnerPhoto.call(OwnerCoverPhotoResponse.serializer()) {
        append("server", server)
        append("photo", photo)
        append("hash", hash)
    }


    fun saveWallPhoto(
        server: Int,
        photo: String,
        hash: String,
        userId: Int? = null,
        groupId: Int? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        caption: String? = null
    ): VkApiRequest<List<Photo>> = Methods.saveWallPhoto.call(ListSerializer(Photo.serializer())) {
        append("server", server)
        append("photo", photo)
        append("hash", hash)
        append("user_id", userId)
        append("group_id", groupId)
        append("latitude", latitude)
        append("longitude", longitude)
        append("caption", caption)
    }


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
    ): VkApiRequest<JsonObject> = Methods.search.call(JsonObject.serializer()) {
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