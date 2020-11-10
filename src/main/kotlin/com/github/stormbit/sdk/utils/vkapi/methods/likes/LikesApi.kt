package com.github.stormbit.sdk.utils.vkapi.methods.likes

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.callSync
import com.github.stormbit.sdk.utils.put
import com.github.stormbit.sdk.utils.vkapi.methods.LikeType
import com.github.stormbit.sdk.utils.vkapi.methods.LikesFilter
import com.google.gson.JsonObject

@Suppress("unused")
class LikesApi(private val client: Client) {
    fun add(
            type: LikeType,
            itemId: Int,
            ownerId: Int? = null,
            accessKey: String? = null
    ): JsonObject = Methods.add.callSync(client, JsonObject()
            .put("type", type.value)
            .put("owner_id", ownerId)
            .put("item_id", itemId)
            .put("access_key", accessKey)
    )

    fun delete(
            type: LikeType,
            itemId: Int,
            ownerId: Int? = null
    ): JsonObject = Methods.delete.callSync(client, JsonObject()
            .put("type", type.value)
            .put("owner_id", ownerId)
            .put("item_id", itemId)
    )

    fun getList(
            type: LikeType,
            itemId: Int,
            ownerId: Int? = null,
            pageUrl: String? = null,
            filter: LikesFilter,
            onlyFriends: Boolean,
            offset: Int,
            count: Int,
            skipOwn: Boolean
    ): JsonObject = Methods.getList.callSync(client, JsonObject()
            .put("type", type.value)
            .put("item_id", itemId)
            .put("owner_id", ownerId)
            .put("page_url", pageUrl)
            .put("filter", filter.value)
            .put("friends_only", onlyFriends.asInt())
            .put("extended", 1)
            .put("offset", offset)
            .put("count", count)
            .put("skip_own", skipOwn.asInt())
    )

    fun getListIds(
            type: LikeType,
            itemId: Int,
            ownerId: Int? = null,
            pageUrl: String? = null,
            filter: LikesFilter,
            onlyFriends: Boolean,
            offset: Int,
            count: Int,
            skipOwn: Boolean
    ): JsonObject = Methods.getList.callSync(client, JsonObject()
            .put("type", type.value)
            .put("item_id", itemId)
            .put("owner_id", ownerId)
            .put("page_url", pageUrl)
            .put("filter", filter.value)
            .put("friends_only", onlyFriends.asInt())
            .put("offset", offset)
            .put("count", count)
            .put("skip_own", skipOwn.asInt())
    )

    fun isLiked(
            type: LikeType,
            itemId: Int,
            ownerId: Int? = null,
            userId: Int? = null
    ): JsonObject = Methods.isLiked.callSync(client, JsonObject()
            .put("type", type.value)
            .put("owner_id", ownerId)
            .put("item_id", itemId)
            .put("user_id", userId)
    )

    private object Methods {
        private const val it = "likes."
        const val add = it + "add"
        const val delete = it + "delete"
        const val getList = it + "getList"
        const val isLiked = it + "isLiked"
    }
}