package com.github.stormbit.sdk.utils.vkapi.methods.docs

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.callSync
import com.github.stormbit.sdk.utils.mediaString
import com.github.stormbit.sdk.utils.put
import com.github.stormbit.sdk.utils.vkapi.docs.DocTypes
import com.github.stormbit.sdk.utils.vkapi.methods.Document
import com.github.stormbit.sdk.utils.vkapi.methods.Media
import com.google.gson.JsonObject

@Suppress("unused")
class DocsApi(private val client: Client) {
    fun add(
            ownerId: Int,
            docId: Int,
            accessKey: String? = null
    ): JsonObject = Methods.add.callSync(client, JsonObject()
            .put("owner_id", ownerId)
            .put("doc_id", docId)
            .put("access_key", accessKey)
    )

    fun delete(
            ownerId: Int,
            docId: Int
    ): JsonObject = Methods.delete.callSync(client, JsonObject()
            .put("owner_id", ownerId)
            .put("doc_id", docId)
    )

    fun edit(
            docId: Int,
            title: String,
            ownerId: Int?,
            tags: List<String>? = null
    ): JsonObject = Methods.edit.callSync(client, JsonObject()
            .put("doc_id", docId)
            .put("title", title)
            .put("owner_id", ownerId)
            .put("tags", tags?.joinToString(","))
    )

    fun get(
            ownerId: Int?,
            count: Int,
            offset: Int,
            type: Document.Type? = null
    ): JsonObject = Methods.get.callSync(client, JsonObject()
            .put("owner_id", ownerId)
            .put("count", count)
            .put("offset", offset)
            .put("type", type?.value)
    )

    fun getById(
            docs: List<Media>,
            returnTags: Boolean
    ): JsonObject = Methods.getById.callSync(client, JsonObject()
            .put("docs", docs.joinToString(",", transform = Media::mediaString))
            .put("return_tags", returnTags.asInt())
    )

    fun getMessagesUploadServer(
            peerId: Int,
            type: DocTypes? = null,
            forAudioMessage: Boolean = false
    ): JsonObject = Methods.getMessagesUploadServer.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("type", if (forAudioMessage) "audio_message" else type?.type ?: DocTypes.DOC.type)
    )

    fun getTypes(
            ownerId: Int?
    ): JsonObject = Methods.getTypes.callSync(client, JsonObject()
            .put("owner_id", ownerId)
    )

    fun getUploadServer(
            groupId: Int?
    ): JsonObject = Methods.getUploadServer.callSync(client, JsonObject()
            .put("group_id", groupId)
    )

    fun getWallUploadServer(
            groupId: Int?
    ): JsonObject = Methods.getWallUploadServer.callSync(client, JsonObject()
            .put("group_id", groupId)
    )

    fun save(
            file: String,
            title: String? = null,
            tags: List<String>? = null
    ): JsonObject = Methods.save.callSync(client, JsonObject()
            .put("file", file)
            .put("title", title)
            .put("tags", tags?.joinToString(","))
    )

    fun search(
            query: String,
            withOwn: Boolean,
            count: Int,
            offset: Int
    ): JsonObject = Methods.search.callSync(client, JsonObject()
            .put("q", query)
            .put("search_own", withOwn.asInt())
            .put("count", count)
            .put("offset", offset)
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