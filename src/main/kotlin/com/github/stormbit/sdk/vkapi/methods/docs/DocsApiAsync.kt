package com.github.stormbit.sdk.vkapi.methods.docs

import com.github.stormbit.sdk.callbacks.Callback
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
import kotlinx.serialization.json.JsonObject

@Suppress("unused")
class DocsApiAsync(private val client: Client) : MethodsContext() {
    fun add(
            ownerId: Int,
            docId: Int,
            accessKey: String? = null,
            callback: Callback<Int?>
    ) = Methods.add.call(
        client, Int.serializer(), callback, parametersOf {
            append("owner_id", ownerId)
            append("doc_id", docId)
            append("access_key", accessKey)
        }
    )

    fun delete(
            ownerId: Int,
            docId: Int,
            callback: Callback<Int?>
    ) = Methods.delete.call(
        client, Int.serializer(), callback, parametersOf {
            append("owner_id", ownerId)
            append("doc_id", docId)
        }
    )

    fun edit(
            docId: Int,
            title: String,
            ownerId: Int? = null,
            tags: List<String>? = null,
            callback: Callback<Int?>
    ) = Methods.edit.call(
        client, Int.serializer(), callback, parametersOf {
            append("doc_id", docId)
            append("title", title)
            append("owner_id", ownerId)
            append("tags", tags?.joinToString(","))
        }
    )

    fun get(
        ownerId: Int? = null,
        count: Int,
        offset: Int,
        type: Document.Type? = null,
        callback: Callback<JsonObject?>
    ) = Methods.get.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("owner_id", ownerId)
            append("count", count)
            append("offset", offset)
            append("type", type?.value)
        }
    )

    fun getById(
            docs: List<Media>,
            returnTags: Boolean,
            callback: Callback<List<Document>?>
    ) = Methods.getById.call(
        client, ListSerializer(Document.serializer()), callback, parametersOf {
            append("docs", docs.joinToString(",", transform = Media::mediaString))
            append("return_tags", returnTags.toInt())
        }
    )

    fun getMessagesUploadServer(
        peerId: Int,
        type: DocTypes? = DocTypes.DOC,
        forAudioMessage: Boolean = false,
        callback: Callback<UploadServerResponse?>
    ) = Methods.getMessagesUploadServer.call(
        client, UploadServerResponse.serializer(), callback, parametersOf {
            append("peer_id", peerId)
            append("type", if (forAudioMessage) "audio_message" else type?.type ?: "doc")
        }
    )

    fun getTypes(
            ownerId: Int? = null,
            callback: Callback<ListResponse<DocType>?>
    ) = Methods.getTypes.call(
        client, ListResponse.serializer(DocType.serializer()), callback, parametersOf {
            append("owner_id", ownerId)
        }
    )

    fun getUploadServer(
            groupId: Int? = null,
            callback: Callback<UploadServerResponse?>
    ) = Methods.getUploadServer.call(
        client, UploadServerResponse.serializer(), callback, parametersOf {
            append("group_id", groupId)
        }
    )

    fun getWallUploadServer(
            groupId: Int? = null,
            callback: Callback<UploadServerResponse?>
    ) = Methods.getWallUploadServer.call(
        client, UploadServerResponse.serializer(), callback, parametersOf {
            append("group_id", groupId)
        }
    )

    fun save(
            file: String,
            title: String? = null,
            tags: List<String>? = null,
            callback: Callback<Document?>
    ) = Methods.save.call(
        client, Document.serializer(), callback, parametersOf {
            append("file", file)
            append("title", title)
            append("tags", tags?.joinToString(","))
        }
    )

    fun search(
            query: String,
            withOwn: Boolean,
            count: Int,
            offset: Int,
            callback: Callback<ListResponse<Document>?>
    ) = Methods.search.call(
        client, ListResponse.serializer(Document.serializer()), callback, parametersOf {
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