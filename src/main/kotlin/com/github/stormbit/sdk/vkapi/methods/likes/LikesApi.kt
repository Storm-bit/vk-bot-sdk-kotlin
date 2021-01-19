package com.github.stormbit.sdk.vkapi.methods.likes

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.models.ExtendedListResponse
import com.github.stormbit.sdk.objects.models.ListResponse
import com.github.stormbit.sdk.utils.append
import com.github.stormbit.sdk.utils.toInt
import com.github.stormbit.sdk.vkapi.VkApiRequest
import com.github.stormbit.sdk.vkapi.methods.LikeType
import com.github.stormbit.sdk.vkapi.methods.LikesFilter
import com.github.stormbit.sdk.vkapi.methods.MethodsContext
import kotlinx.serialization.builtins.serializer

@Suppress("unused")
class LikesApi(client: Client) : MethodsContext(client) {
    fun add(
        type: LikeType,
        itemId: Int,
        ownerId: Int? = null,
        accessKey: String? = null
    ): VkApiRequest<LikesResponse> = Methods.add.call(LikesResponse.serializer()) {
        append("type", type.value)
        append("owner_id", ownerId)
        append("item_id", itemId)
        append("access_key", accessKey)
    }


    fun delete(
        type: LikeType,
        itemId: Int,
        ownerId: Int? = null
    ): VkApiRequest<LikesResponse> = Methods.delete.call(LikesResponse.serializer()) {
        append("type", type.value)
        append("owner_id", ownerId)
        append("item_id", itemId)
    }


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
    ): VkApiRequest<ExtendedListResponse<Int>> = Methods.getList.call(ExtendedListResponse.serializer(Int.serializer())) {
        append("type", type.value)
        append("item_id", itemId)
        append("owner_id", ownerId)
        append("page_url", pageUrl)
        append("filter", filter.value)
        append("friends_only", onlyFriends.toInt())
        append("extended", 1)
        append("offset", offset)
        append("count", count)
        append("skip_own", skipOwn.toInt())
    }


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
    ): VkApiRequest<ListResponse<Int>> = Methods.getList.call(ListResponse.serializer(Int.serializer())) {
        append("type", type.value)
        append("item_id", itemId)
        append("owner_id", ownerId)
        append("page_url", pageUrl)
        append("filter", filter.value)
        append("friends_only", onlyFriends.toInt())
        append("offset", offset)
        append("count", count)
        append("skip_own", skipOwn.toInt())
    }


    fun isLiked(
        type: LikeType,
        itemId: Int,
        ownerId: Int? = null,
        userId: Int? = null
    ): VkApiRequest<IsLikedResponse> = Methods.isLiked.call(IsLikedResponse.serializer()) {
        append("type", type.value)
        append("owner_id", ownerId)
        append("item_id", itemId)
        append("user_id", userId)
    }


    private object Methods {
        private const val it = "likes."
        const val add = it + "add"
        const val delete = it + "delete"
        const val getList = it + "getList"
        const val isLiked = it + "isLiked"
    }
}