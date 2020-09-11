package com.github.stormbit.sdk.utils.vkapi.methods.likes

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.callSync
import com.github.stormbit.sdk.utils.vkapi.methods.LikeType
import com.github.stormbit.sdk.utils.vkapi.methods.LikesFilter
import org.json.JSONObject

@Suppress("unused")
class LikesApi(private val client: Client) {
    fun add(
            type: LikeType,
            itemId: Int,
            ownerId: Int?,
            accessKey: String?
    ): JSONObject = Methods.add.callSync(client, JSONObject()
            .put("type", type.value)
            .put("owner_id", ownerId)
            .put("item_id", itemId)
            .put("access_key", accessKey)
    )

    fun delete(
            type: LikeType,
            itemId: Int,
            ownerId: Int?
    ): JSONObject = Methods.delete.callSync(client, JSONObject()
            .put("type", type.value)
            .put("owner_id", ownerId)
            .put("item_id", itemId)
    )

    fun getList(
            type: LikeType,
            itemId: Int,
            ownerId: Int?,
            pageUrl: String?,
            filter: LikesFilter,
            onlyFriends: Boolean,
            offset: Int,
            count: Int,
            skipOwn: Boolean
    ): JSONObject = Methods.getList.callSync(client, JSONObject()
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
            ownerId: Int?,
            pageUrl: String?,
            filter: LikesFilter,
            onlyFriends: Boolean,
            offset: Int,
            count: Int,
            skipOwn: Boolean
    ): JSONObject = Methods.getList.callSync(client, JSONObject()
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
            ownerId: Int?,
            userId: Int?
    ): JSONObject = Methods.isLiked.callSync(client, JSONObject()
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