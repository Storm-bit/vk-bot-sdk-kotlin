package com.github.stormbit.vksdk.objects.upload

import com.github.stormbit.vksdk.clients.Client
import com.github.stormbit.vksdk.clients.GroupClient
import com.github.stormbit.vksdk.objects.attachments.Photo
import com.github.stormbit.vksdk.vkapi.FileContent
import com.github.stormbit.vksdk.vkapi.execute
import com.github.stormbit.vksdk.vkapi.methods.docs.DocumentSaveResponse
import com.github.stormbit.vksdk.vkapi.methods.photos.OwnerCoverPhotoResponse
import com.github.stormbit.vksdk.vkapi.methods.upload.UploadApi
import com.github.stormbit.vksdk.vkapi.methods.upload.UploadVideoResponse

@Suppress("unused")
class Upload(private val client: Client) {
    private val uploadApi = UploadApi(client)

    val groupId = if (client is GroupClient) client.id else null
    val ownerId = if (client !is GroupClient) client.id else null

    suspend fun document(file: FileContent): DocumentSaveResponse {
        val uploadUrl = client.docs.getUploadServer(groupId).execute().uploadUrl
        val uploadResponse = uploadApi.document(uploadUrl, file).execute()

        return client.docs.save(uploadResponse.file, file.filename).execute()
    }

    suspend fun photoForAlbum(files: List<FileContent>, albumId: Int): List<Photo> {
        val uploadUrl = client.photos.getUploadServer(albumId, groupId).execute().uploadUrl
        val (server, photosList, aid, hash) = uploadApi.photoForAlbum(uploadUrl, files).execute()

        return client.photos.save(aid, server, photosList, hash).execute()
    }

    suspend fun photoForChat(file: FileContent, chatId: Int): String {
        val uploadUrl = client.photos.getChatUploadServer(chatId).execute().uploadUrl

        return uploadApi.photoForChat(uploadUrl, file).execute()
    }

    suspend fun photoForMarketAlbum(file: FileContent): List<Photo> {
        val uploadUrl = client.photos.getMarketAlbumUploadServer(groupId!!).execute().uploadUrl
        val (server, photo, gid, hash) = uploadApi.photoForMarketAlbum(uploadUrl, file).execute()

        return client.photos.saveMarketAlbumPhoto(gid, server, photo, hash).execute()
    }

    suspend fun photoForMarket(files: List<FileContent>, isMainPhoto: Boolean): List<Photo> {
        val uploadUrl = client.photos.getMarketUploadServer(groupId!!, isMainPhoto).execute().uploadUrl
        val (server, photo, hash, cropData, cropHash) = uploadApi.photoForMarket(uploadUrl, files).execute()

        return client.photos.saveMarketPhoto(server, photo, hash, cropData, cropHash, groupId).execute()
    }

    suspend fun photoForMessage(files: List<FileContent>, peerId: Int): List<Photo> {
        val (_, uploadUrl) = client.photos.getMessagesUploadServer(peerId).execute()
        val (server, photo, hash) = uploadApi.photoForMessage(uploadUrl, files).execute()

        return client.photos.saveMessagesPhoto(server, photo, hash).execute()
    }

    suspend fun photoForOwnerCover(file: FileContent, cropX: Int, cropY: Int, cropX2: Int, cropY2: Int): List<OwnerCoverPhotoResponse> {
        val uploadUrl = client.photos.getOwnerCoverPhotoUploadServer(groupId!!, cropX, cropY, cropX2, cropY2)
            .execute().uploadUrl
        val (photo, hash) = uploadApi.photoForOwnerCover(uploadUrl, file).execute()

        return client.photos.saveOwnerCoverPhoto(photo, hash).execute()
    }

    suspend fun photoForOwner(file: FileContent, previewX: Int? = null, previewY: Int? = null, previewWidth: Int? = null): OwnerCoverPhotoResponse {
        val uploadUrl = client.photos.getOwnerPhotoUploadServer(ownerId).execute().uploadUrl
        val (server, photo, hash) = uploadApi.photoForOwner(uploadUrl, file, previewX, previewY, previewWidth).execute()

        return client.photos.saveOwnerPhoto(server, photo, hash).execute()
    }

    suspend fun photoForWall(files: List<FileContent>, groupId: Int? = null, userId: Int? = null): List<Photo> {
        val uploadUrl = client.photos.getWallUploadServer(groupId).execute().uploadUrl
        val (server, photo, hash) = uploadApi.photoForWall(uploadUrl, files).execute()

        return client.photos.saveWallPhoto(server, photo, hash, userId, groupId).execute()
    }

    suspend fun videoByLink(name: String?, isPrivate: Boolean, publishOnWall: Boolean, description: String? = null): Boolean {
        val saveResponse = client.video.save(name, description, isPrivate, publishOnWall).execute()
        return uploadApi.videoByLink(saveResponse.uploadUrl).execute().value
    }

    suspend fun video(file: FileContent, name: String?, isPrivate: Boolean, publishOnWall: Boolean, description: String? = null): UploadVideoResponse {
        val uploadUrl = client.video.save(name, description, isPrivate, publishOnWall).execute().uploadUrl
        return uploadApi.video(uploadUrl, file).execute()
    }
}