package com.github.stormbit.sdk.vkapi.methods.docs

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.mediaString
import com.github.stormbit.sdk.objects.attachments.Document
import com.github.stormbit.sdk.objects.models.ListResponse
import com.github.stormbit.sdk.utils.parametersOf
import com.github.stormbit.sdk.utils.toInt
import com.github.stormbit.sdk.vkapi.methods.Media
import com.github.stormbit.sdk.vkapi.methods.MethodsContext
import com.github.stormbit.sdk.vkapi.methods.UploadServerResponse
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

@Suppress("unused")
class DocsApi(private val client: Client) : MethodsContext() {
    fun add(
            ownerId: Int,
            docId: Int,
            accessKey: String? = null
    ): Int? = Methods.add.callSync(
        client, Int.serializer(), parametersOf {
            append("owner_id", ownerId)
            append("doc_id", docId)
            append("access_key", accessKey)
        }
    )

    fun delete(
            ownerId: Int,
            docId: Int
    ): Int? = Methods.delete.callSync(
        client, Int.serializer(), parametersOf {
            append("owner_id", ownerId)
            append("doc_id", docId)
        }
    )

    fun edit(
            docId: Int,
            title: String,
            ownerId: Int? = null,
            tags: List<String>? = null
    ): Int? = Methods.edit.callSync(
        client, Int.serializer(), parametersOf {
            append("doc_id", docId)
            append("title", title)
            append("owner_id", ownerId)
            append("tags", tags?.joinToString(","))
        }
    )

    fun get(
            count: Int,
            offset: Int,
            ownerId: Int? = null,
            type: Document.Type? = null
    ): ListResponse<Document>? = Methods.get.callSync(
        client, ListResponse.serializer(Document.serializer()), parametersOf {
            append("owner_id", ownerId)
            append("count", count)
            append("offset", offset)
            append("type", type?.value)
        }
    )

    fun getById(
            docs: List<Media>,
            returnTags: Boolean
    ): List<Document>? = Methods.getById.callSync(
        client, ListSerializer(Document.serializer()), parametersOf {
            append("docs", docs.joinToString(",", transform = Media::mediaString))
            append("return_tags", returnTags.toInt())
        }
    )

    fun getMessagesUploadServer(
        peerId: Int,
        type: DocTypes? = null,
        forAudioMessage: Boolean = false
    ): UploadServerResponse? = Methods.getMessagesUploadServer.callSync(
        client, UploadServerResponse.serializer(), parametersOf {
            append("peer_id", peerId)
            append("type", if (forAudioMessage) "audio_message" else type?.type ?: DocTypes.DOC.type)
        }
    )

    fun getTypes(
            ownerId: Int?
    ): ListResponse<DocType>? = Methods.getTypes.callSync(
        client, ListResponse.serializer(DocType.serializer()), parametersOf {
            append("owner_id", ownerId)
        }
    )

    fun getUploadServer(
            groupId: Int?
    ): UploadServerResponse? = Methods.getUploadServer.callSync(
        client, UploadServerResponse.serializer(), parametersOf {
            append("group_id", groupId)
        }
    )

    fun getWallUploadServer(
            groupId: Int?
    ): UploadServerResponse? = Methods.getWallUploadServer.callSync(
        client, UploadServerResponse.serializer(), parametersOf {
            append("group_id", groupId)
        }
    )

    fun save(
            file: String,
            title: String? = null,
            tags: List<String>? = null
    ): DocumentSaveResponse? = Methods.save.callSync(
        client, DocumentSaveResponse.serializer(), parametersOf {
            append("file", file)
            append("title", title)
            append("tags", tags?.joinToString(","))
        }
    )

    fun search(
            query: String,
            withOwn: Boolean = false,
            count: Int = 1,
            offset: Int = 0
    ): ListResponse<Document>? = Methods.search.callSync(
        client, ListResponse.serializer(Document.serializer()), parametersOf {
            append("q", query)
            append("search_own", withOwn.toInt())
            append("count", count)
            append("offset", offset)
        }
    )

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