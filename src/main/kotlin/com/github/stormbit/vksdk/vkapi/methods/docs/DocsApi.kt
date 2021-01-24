package com.github.stormbit.vksdk.vkapi.methods.docs

import com.github.stormbit.vksdk.clients.Client
import com.github.stormbit.vksdk.objects.attachments.Document
import com.github.stormbit.vksdk.objects.models.ListResponse
import com.github.stormbit.vksdk.objects.models.enums.DocTypes
import com.github.stormbit.vksdk.utils.append
import com.github.stormbit.vksdk.utils.mediaString
import com.github.stormbit.vksdk.utils.toInt
import com.github.stormbit.vksdk.vkapi.VkApiRequest
import com.github.stormbit.vksdk.vkapi.methods.Media
import com.github.stormbit.vksdk.vkapi.methods.MethodsContext
import com.github.stormbit.vksdk.vkapi.methods.UploadServerResponse
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

@Suppress("unused")
class DocsApi(client: Client) : MethodsContext(client) {
    fun add(
        ownerId: Int,
        docId: Int,
        accessKey: String? = null
    ): VkApiRequest<Int> = Methods.add.call(Int.serializer()) {
        append("owner_id", ownerId)
        append("doc_id", docId)
        append("access_key", accessKey)
    }

    fun delete(
        ownerId: Int,
        docId: Int
    ): VkApiRequest<Int> = Methods.delete.call(Int.serializer()) {
        append("owner_id", ownerId)
        append("doc_id", docId)
    }

    fun edit(
        docId: Int,
        title: String,
        ownerId: Int? = null,
        tags: List<String>? = null
    ): VkApiRequest<Int> = Methods.edit.call(Int.serializer()) {
        append("doc_id", docId)
        append("title", title)
        append("owner_id", ownerId)
        append("tags", tags?.joinToString(","))
    }

    fun get(
        count: Int,
        offset: Int,
        ownerId: Int? = null,
        type: Document.Type? = null
    ): VkApiRequest<ListResponse<Document>> = Methods.get.call(ListResponse.serializer(Document.serializer())) {
        append("owner_id", ownerId)
        append("count", count)
        append("offset", offset)
        append("type", type?.value)
    }

    fun getById(
        docs: List<Media>,
        returnTags: Boolean
    ): VkApiRequest<List<Document>> = Methods.getById.call(ListSerializer(Document.serializer())) {
        append("docs", docs.joinToString(",", transform = Media::mediaString))
        append("return_tags", returnTags.toInt())
    }

    fun getMessagesUploadServer(
        peerId: Int,
        type: DocTypes? = null,
        forAudioMessage: Boolean = false
    ): VkApiRequest<UploadServerResponse> = Methods.getMessagesUploadServer.call(UploadServerResponse.serializer()) {
        append("peer_id", peerId)
        append("type", if (forAudioMessage) "audio_message" else type?.type ?: DocTypes.DOC.type)
    }

    fun getTypes(
        ownerId: Int?
    ): VkApiRequest<ListResponse<DocType>> = Methods.getTypes.call(ListResponse.serializer(DocType.serializer())) {
        append("owner_id", ownerId)
    }

    fun getUploadServer(
        groupId: Int? = null
    ): VkApiRequest<UploadServerResponse> = Methods.getUploadServer.call(UploadServerResponse.serializer()) {
        append("group_id", groupId)
    }

    fun getWallUploadServer(
        groupId: Int?
    ): VkApiRequest<UploadServerResponse> = Methods.getWallUploadServer.call(UploadServerResponse.serializer()) {
        append("group_id", groupId)
    }

    fun save(
        file: String,
        title: String? = null,
        tags: List<String>? = null
    ): VkApiRequest<DocumentSaveResponse> = Methods.save.call(DocumentSaveResponse.serializer()) {
        append("file", file)
        append("title", title)
        append("tags", tags?.joinToString(","))
    }

    fun search(
        query: String,
        withOwn: Boolean = false,
        count: Int = 1,
        offset: Int = 0
    ): VkApiRequest<ListResponse<Document>> = Methods.search.call(ListResponse.serializer(Document.serializer())) {
        append("q", query)
        append("search_own", withOwn.toInt())
        append("count", count)
        append("offset", offset)
    }

    private object Methods {
        private const val it = "docs."
        const val add = it + "add"
        const val delete = it + "delete"
        const val edit = it + "edit"
        const val get = it + "get"
        const val getById = it + "getById"
        const val getMessagesUploadServer = it + "getMessagesUploadServer"
        const val getTypes = it + "getTypes"
        const val getUploadServer = it + "getUploadServer"
        const val getWallUploadServer = it + "getWallUploadServer"
        const val save = it + "save"
        const val search = it + "search"
    }
}