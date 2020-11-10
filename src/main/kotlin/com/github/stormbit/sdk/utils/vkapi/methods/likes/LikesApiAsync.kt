package com.github.stormbit.sdk.utils.vkapi.methods.likes

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.call
import com.github.stormbit.sdk.utils.put
import com.github.stormbit.sdk.utils.vkapi.methods.LikeType
import com.github.stormbit.sdk.utils.vkapi.methods.LikesFilter
import com.google.gson.JsonObject

@Suppress("unused")
class LikesApiAsync(private val client: Client) {
    fun add(
            type: LikeType,
            itemId: Int,
            ownerId: Int? = null,
            accessKey: String? = null,
            callback: Callback<JsonObject?>
    ) = Methods.add.call(client, callback, JsonObject()
            .put("type", type.value)
            .put("owner_id", ownerId)
            .put("item_id", itemId)
            .put("access_key", accessKey)
    )

    fun delete(
            type: LikeType,
            itemId: Int,
            ownerId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.delete.call(client, callback, JsonObject()
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
            skipOwn: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getList.call(client, callback, JsonObject()
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
            skipOwn: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getList.call(client, callback, JsonObject()
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
            userId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.isLiked.call(client, callback, JsonObject()
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