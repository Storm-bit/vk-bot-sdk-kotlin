package com.github.stormbit.sdk.vkapi.methods.likes

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.models.ExtendedListResponse
import com.github.stormbit.sdk.objects.models.ListResponse
import com.github.stormbit.sdk.utils.parametersOf
import com.github.stormbit.sdk.utils.toInt
import com.github.stormbit.sdk.vkapi.methods.LikeType
import com.github.stormbit.sdk.vkapi.methods.LikesFilter
import com.github.stormbit.sdk.vkapi.methods.MethodsContext
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonPrimitive

@Suppress("unused")
class LikesApi(private val client: Client) : MethodsContext() {
    fun add(
            type: LikeType,
            itemId: Int,
            ownerId: Int? = null,
            accessKey: String? = null
    ): LikesResponse? = Methods.add.callSync(
        client, LikesResponse.serializer(), parametersOf {
            append("type", JsonPrimitive(type.value))
            append("owner_id", JsonPrimitive(ownerId))
            append("item_id", JsonPrimitive(itemId))
            append("access_key", JsonPrimitive(accessKey))
        }
    )

    fun delete(
            type: LikeType,
            itemId: Int,
            ownerId: Int? = null
    ): LikesResponse? = Methods.delete.callSync(
        client, LikesResponse.serializer(), parametersOf {
            append("type", JsonPrimitive(type.value))
            append("owner_id", JsonPrimitive(ownerId))
            append("item_id", JsonPrimitive(itemId))
        }
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
    ): ExtendedListResponse<Int>? = Methods.getList.callSync(
        client, ExtendedListResponse.serializer(Int.serializer()), parametersOf {
            append("type", JsonPrimitive(type.value))
            append("item_id", JsonPrimitive(itemId))
            append("owner_id", JsonPrimitive(ownerId))
            append("page_url", JsonPrimitive(pageUrl))
            append("filter", JsonPrimitive(filter.value))
            append("friends_only", JsonPrimitive(onlyFriends.toInt()))
            append("extended", JsonPrimitive(1))
            append("offset", JsonPrimitive(offset))
            append("count", JsonPrimitive(count))
            append("skip_own", JsonPrimitive(skipOwn.toInt()))
        }
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
    ): ListResponse<Int>? = Methods.getList.callSync(
        client, ListResponse.serializer(Int.serializer()), parametersOf {
            append("type", JsonPrimitive(type.value))
            append("item_id", JsonPrimitive(itemId))
            append("owner_id", JsonPrimitive(ownerId))
            append("page_url", JsonPrimitive(pageUrl))
            append("filter", JsonPrimitive(filter.value))
            append("friends_only", JsonPrimitive(onlyFriends.toInt()))
            append("offset", JsonPrimitive(offset))
            append("count", JsonPrimitive(count))
            append("skip_own", JsonPrimitive(skipOwn.toInt()))
        }
    )

    fun isLiked(
            type: LikeType,
            itemId: Int,
            ownerId: Int? = null,
            userId: Int? = null
    ): IsLikedResponse? = Methods.isLiked.callSync(
        client, IsLikedResponse.serializer(), parametersOf {
            append("type", JsonPrimitive(type.value))
            append("owner_id", JsonPrimitive(ownerId))
            append("item_id", JsonPrimitive(itemId))
            append("user_id", JsonPrimitive(userId))
        }
    )

    private object Methods {
        private const val it = "likes."
        const val add = it + "add"
        const val delete = it + "delete"
        const val getList = it + "getList"
        const val isLiked = it + "isLiked"
    }
}