package com.github.stormbit.vksdk.vkapi.methods.upload

import com.github.stormbit.vksdk.clients.Client
import com.github.stormbit.vksdk.utils.BooleanInt
import com.github.stormbit.vksdk.vkapi.FileContent
import com.github.stormbit.vksdk.vkapi.UploadFilesRequest
import com.github.stormbit.vksdk.vkapi.UploadableFile
import io.ktor.http.*
import kotlinx.serialization.builtins.serializer

@Suppress("unused")
class UploadApi(private val client: Client) {
    fun document(
        uploadUrl: String,
        file: FileContent
    ): UploadFilesRequest<UploadDocumentResponse> =
        UploadFilesRequest(
            client = client,
            uploadUrl = uploadUrl,
            files = listOf(UploadableFile(DEFAULT_FILE_FIELD, file)),
            parameters = Parameters.Empty,
            serializer = UploadDocumentResponse.serializer()
        )

    fun photoForAlbum(
        uploadUrl: String,
        files: List<FileContent>
    ): UploadFilesRequest<UploadAlbumPhotoResponse> =
        UploadFilesRequest(
            client = client,
            uploadUrl = uploadUrl,
            files = prepareFiles(files),
            parameters = Parameters.Empty,
            serializer = UploadAlbumPhotoResponse.serializer()
        )

    fun photoForChat(
        uploadUrl: String,
        file: FileContent
    ): UploadFilesRequest<String> =
        UploadFilesRequest(
            client = client,
            uploadUrl = uploadUrl,
            files = listOf(UploadableFile(DEFAULT_FILE_FIELD, file)),
            parameters = Parameters.Empty,
            serializer = String.serializer()
        )

    fun photoForMarketAlbum(
        uploadUrl: String,
        file: FileContent
    ): UploadFilesRequest<UploadMarketAlbumPhotoResponse> =
        UploadFilesRequest(
            client = client,
            uploadUrl = uploadUrl,
            files = listOf(UploadableFile(DEFAULT_FILE_FIELD, file)),
            parameters = Parameters.Empty,
            serializer = UploadMarketAlbumPhotoResponse.serializer()
        )

    fun photoForMarket(
        uploadUrl: String,
        files: List<FileContent>
    ): UploadFilesRequest<UploadMarketPhotoResponse> =
        UploadFilesRequest(
            client = client,
            uploadUrl = uploadUrl,
            files = prepareFiles(files),
            parameters = Parameters.Empty,
            serializer = UploadMarketPhotoResponse.serializer()
        )

    fun photoForMessage(
        uploadUrl: String,
        files: List<FileContent>
    ): UploadFilesRequest<UploadPhotoResponse> =
        UploadFilesRequest(
            client = client,
            uploadUrl = uploadUrl,
            files = prepareFiles(files),
            parameters = Parameters.Empty,
            serializer = UploadPhotoResponse.serializer()
        )

    fun photoForOwnerCover(
        uploadUrl: String,
        file: FileContent
    ): UploadFilesRequest<UploadOwnerCoverPhotoResponse> =
        UploadFilesRequest(
            client = client,
            uploadUrl = uploadUrl,
            files = listOf(UploadableFile(DEFAULT_FILE_FIELD, file)),
            parameters = Parameters.Empty,
            serializer = UploadOwnerCoverPhotoResponse.serializer()
        )

    fun photoForOwner(
        uploadUrl: String,
        file: FileContent,
        previewX: Int?,
        previewY: Int?,
        previewWidth: Int?
    ): UploadFilesRequest<UploadOwnerPhotoResponse> =
        UploadFilesRequest(
            client = client,
            uploadUrl = uploadUrl,
            files = listOf(UploadableFile(DEFAULT_FILE_FIELD, file)),
            parameters = parametersOf("_square_crop", "$previewX,$previewY,$previewWidth"),
            serializer = UploadOwnerPhotoResponse.serializer()
        )

    fun photoForWall(
        uploadUrl: String,
        files: List<FileContent>
    ): UploadFilesRequest<UploadPhotoResponse> =
        UploadFilesRequest(
            client = client,
            uploadUrl = uploadUrl,
            files = prepareFiles(files),
            parameters = Parameters.Empty,
            serializer = UploadPhotoResponse.serializer()
        )

    fun videoByLink(
        uploadUrl: String
    ): UploadFilesRequest<BooleanInt> =
        UploadFilesRequest(
            client = client,
            uploadUrl = uploadUrl,
            files = emptyList(),
            parameters = Parameters.Empty,
            serializer = BooleanInt.serializer()
        )

    fun video(
        uploadUrl: String,
        file: FileContent
    ): UploadFilesRequest<UploadVideoResponse> =
        UploadFilesRequest(
            client = client,
            uploadUrl = uploadUrl,
            files = listOf(UploadableFile(DEFAULT_FILE_FIELD, file)),
            parameters = Parameters.Empty,
            serializer = UploadVideoResponse.serializer()
        )

    companion object {

        private const val DEFAULT_FILE_FIELD = "file"

        private fun prepareFiles(files: List<FileContent>): List<UploadableFile> =
            files.mapIndexed { i, file -> UploadableFile("$DEFAULT_FILE_FIELD$i", file) }

    }
}