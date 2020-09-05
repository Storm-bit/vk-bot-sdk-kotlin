package com.github.stormbit.sdk.utils.vkapi.methods.docs

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.callSync
import com.github.stormbit.sdk.utils.mediaString
import com.github.stormbit.sdk.utils.vkapi.docs.DocTypes
import com.github.stormbit.sdk.utils.vkapi.methods.Document
import com.github.stormbit.sdk.utils.vkapi.methods.Media
import org.json.JSONObject

@Suppress("unused")
class DocsApi(private val client: Client) {
    fun add(
            ownerId: Int,
            docId: Int,
            accessKey: String?
    ): JSONObject = Methods.add.callSync(client, JSONObject()
            .put("owner_id", ownerId)
            .put("doc_id", docId)
            .put("access_key", accessKey)
    )

    fun delete(
            ownerId: Int,
            docId: Int
    ): JSONObject = Methods.delete.callSync(client, JSONObject()
            .put("owner_id", ownerId)
            .put("doc_id", docId)
    )

    fun edit(
            docId: Int,
            title: String,
            ownerId: Int?,
            tags: List<String>?
    ): JSONObject = Methods.edit.callSync(client, JSONObject()
            .put("doc_id", docId)
            .put("title", title)
            .put("owner_id", ownerId)
            .put("tags", tags?.joinToString(","))
    )

    fun get(
            ownerId: Int?,
            count: Int,
            offset: Int,
            type: Document.Type?
    ): JSONObject = Methods.get.callSync(client, JSONObject()
            .put("owner_id", ownerId)
            .put("count", count)
            .put("offset", offset)
            .put("type", type?.value)
    )

    fun getById(
            docs: List<Media>,
            returnTags: Boolean
    ): JSONObject = Methods.getById.callSync(client, JSONObject()
            .put("docs", docs.joinToString(",", transform = Media::mediaString))
            .put("return_tags", returnTags.asInt())
    )

    fun getMessagesUploadServer(
            peerId: Int,
            type: DocTypes? = DocTypes.DOC,
            forAudioMessage: Boolean = false
    ): JSONObject = Methods.getMessagesUploadServer.callSync(client, JSONObject()
            .put("peer_id", peerId)
            .put("type", if (forAudioMessage) "audio_message" else type?.type ?: "doc")
    )

    fun getTypes(
            ownerId: Int?
    ): JSONObject = Methods.getTypes.callSync(client, JSONObject()
            .put("owner_id", ownerId)
    )

    fun getUploadServer(
            groupId: Int?
    ): JSONObject = Methods.getUploadServer.callSync(client, JSONObject()
            .put("group_id", groupId)
    )

    fun getWallUploadServer(
            groupId: Int?
    ): JSONObject = Methods.getWallUploadServer.callSync(client, JSONObject()
            .put("group_id", groupId)
    )

    fun save(
            file: String,
            title: String? = null,
            tags: List<String>? = null
    ): JSONObject = Methods.save.callSync(client, JSONObject()
            .put("file", file)
            .put("title", title)
            .put("tags", tags?.joinToString(","))
    )

    fun search(
            query: String,
            withOwn: Boolean,
            count: Int,
            offset: Int
    ): JSONObject = Methods.search.callSync(client, JSONObject()
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