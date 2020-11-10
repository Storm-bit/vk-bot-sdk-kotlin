package com.github.stormbit.sdk.utils.vkapi.methods.friends

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.call
import com.github.stormbit.sdk.utils.put
import com.github.stormbit.sdk.utils.vkapi.methods.FriendsOrder
import com.github.stormbit.sdk.utils.vkapi.methods.NameCase
import com.github.stormbit.sdk.utils.vkapi.methods.UserOptionalField
import com.google.gson.JsonObject

@Suppress("unused")
class FriendsApiAsync(private var client: Client) {
    fun add(
            userId: Int,
            text: String? = null,
            declineRequest: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.add.call(client, callback, JsonObject()
            .put("user_id", userId)
            .put("text", text)
            .put("follow", declineRequest.asInt())
    )

    fun addList(
            name: String,
            userIds: List<Int>? = null,
            callback: Callback<JsonObject?>
    ) = Methods.addList.call(client, callback, JsonObject()
            .put("name", name)
            .put("user_ids", userIds?.joinToString(","))
    )

    fun areFriends(
            userIds: List<Int>,
            needSign: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.areFriends.call(client, callback, JsonObject()
            .put("user_ids", userIds.joinToString(","))
            .put("need_sign", needSign.asInt())
    )

    fun delete(
            userId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.delete.call(client, callback, JsonObject()
            .put("user_id", userId)
    )

    fun deleteAllRequests(callback: Callback<JsonObject?>) = Methods.deleteAllRequests.call(client, callback, JsonObject())

    fun deleteList(
            listId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.deleteList.call(client, callback, JsonObject()
            .put("list_id", listId)
    )

    fun edit(
            userId: Int,
            listIds: List<Int>? = null,
            callback: Callback<JsonObject?>
    ) = Methods.edit.call(client, callback, JsonObject()
            .put("user_id", userId)
            .put("list_ids", listIds?.joinToString(","))
    )

    fun editList(
            listId: Int,
            userIds: List<Int>,
            name: String? = null,
            callback: Callback<JsonObject?>
    ) = Methods.editList.call(client, callback, JsonObject()
            .put("list_id", listId)
            .put("user_ids", userIds.joinToString(","))
            .put("name", name)
    )

    fun editList(
            listId: Int,
            addUserIds: List<Int>? = null,
            deleteUserIds: List<Int>? = null,
            name: String? = null,
            callback: Callback<JsonObject?>
    ) = Methods.editList.call(client, callback, JsonObject()
            .put("list_id", listId)
            .put("add_user_ids", addUserIds?.joinToString(","))
            .put("delete_user_ids", deleteUserIds?.joinToString(","))
            .put("name", name)
    )

    fun get(
            userId: Int? = null,
            order: FriendsOrder? = null,
            listId: Int? = null,
            count: Int,
            offset: Int,
            userFields: List<UserOptionalField>,
            nameCase: NameCase,
            callback: Callback<JsonObject?>
    ) = Methods.get.call(client, callback, JsonObject()
            .put("user_id", userId)
            .put("order", order?.value)
            .put("list_id", listId)
            .put("count", count)
            .put("offset", offset)
            .put("fields", userFields.joinToString(",") { it.value })
            .put("name_case", nameCase.value)
    )

    fun getIds(
            userId: Int? = null,
            order: FriendsOrder? = null,
            listId: Int? = null,
            count: Int,
            offset: Int,
            callback: Callback<JsonObject?>
    ) = Methods.get.call(client, callback, JsonObject()
            .put("user_id", userId)
            .put("order", order?.value)
            .put("list_id", listId)
            .put("count", count)
            .put("offset", offset)
    )

    fun getAppUsers(callback: Callback<JsonObject?>) = Methods.getAppUsers.call(client, callback, JsonObject())

    fun getByPhones(
            phones: List<String>,
            userFields: List<UserOptionalField>,
            callback: Callback<JsonObject?>
    ) = Methods.getByPhones.call(client, callback, JsonObject()
            .put("phones", phones.joinToString(","))
            .put("fields", userFields.joinToString(",") { it.value })
    )

    fun getLists(
            userId: Int? = null,
            withSystem: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getLists.call(client, callback, JsonObject()
            .put("user_id", userId)
            .put("return_system", withSystem.asInt())
    )

    fun getMutual(
            targetUserId: Int,
            sourceUserId: Int? = null,
            sortRandomly: Boolean,
            count: Int? = null,
            offset: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getMutual.call(client, callback, JsonObject()
            .put("target_uid", targetUserId)
            .put("source_uid", sourceUserId)
            .put("order", if (sortRandomly) "random" else null)
            .put("count", count)
            .put("offset", offset)
    )

    fun getMutual(
            targetUserIds: List<Int>,
            sourceUserId: Int? = null,
            sortRandomly: Boolean,
            count: Int? = null,
            offset: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getMutual.call(client, callback, JsonObject()
            .put("target_uids", targetUserIds.joinToString(","))
            .put("source_uid", sourceUserId)
            .put("order", if (sortRandomly) "random" else null)
            .put("count", count)
            .put("offset", offset)
    )

    fun getOnline(
            userId: Int? = null,
            listId: Int? = null,
            sortRandomly: Boolean,
            count: Int? = null,
            offset: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getOnline.call(client, callback, JsonObject()
            .put("user_id", userId)
            .put("list_id", listId)
            .put("order", if (sortRandomly) "random" else null)
            .put("count", count)
            .put("offset", offset)
    )

    fun getOnlineWithOnlineFromMobile(
            userId: Int? = null,
            listId: Int? = null,
            sortRandomly: Boolean,
            count: Int? = null,
            offset: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getOnline.call(client, callback, JsonObject()
            .put("user_id", userId)
            .put("list_id", listId)
            .put("online_mobile", 1)
            .put("order", if (sortRandomly) "random" else null)
            .put("count", count)
            .put("offset", offset)
    )

    fun getRecent(
            count: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getRecent.call(client, callback, JsonObject()
            .put("count", count)
    )

    fun getOutgoingRequests(
            count: Int,
            offset: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getRequests.call(client, callback, JsonObject()
            .put("count", count)
            .put("offset", offset)
            .put("out", 1)
    )

    fun getOutgoingRequestsWithMutual(
            offset: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getRequests.call(client, callback, JsonObject()
            .put("offset", offset)
            .put("need_mutual", 1)
            .put("out", 1)
    )

    fun getRequests(
            count: Int,
            offset: Int,
            sortByMutual: Boolean,
            needViewed: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getRequests.call(client, callback, JsonObject()
            .put("count", count)
            .put("offset", offset)
            .put("sort", sortByMutual.asInt())
            .put("need_viewed", needViewed.asInt())
    )

    fun getRequestsWithMutual(
            offset: Int,
            sortByMutual: Boolean,
            needViewed: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getRequests.call(client, callback, JsonObject()
            .put("offset", offset)
            .put("sort", sortByMutual.asInt())
            .put("need_viewed", needViewed.asInt())
            .put("need_mutual", 1)
    )

    fun getSuggestedRequests(
            count: Int,
            offset: Int,
            sortByMutual: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getRequests.call(client, callback, JsonObject()
            .put("count", count)
            .put("offset", offset)
            .put("sort", sortByMutual.asInt())
            .put("suggested", 1)
    )

    fun getSuggestedRequestsExtended(
            count: Int,
            offset: Int,
            sortByMutual: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getRequests.call(client, callback, JsonObject()
            .put("count", count)
            .put("offset", offset)
            .put("sort", sortByMutual.asInt())
            .put("extended", 1)
            .put("suggested", 1)
    )

    fun getSuggestedRequestsWithMutual(
            offset: Int,
            sortByMutual: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getRequests.call(client, callback, JsonObject()
            .put("offset", offset)
            .put("sort", sortByMutual.asInt())
            .put("extended", 1)
            .put("need_mutual", 1)
            .put("suggested", 1)
    )

    fun getSuggestions(
            count: Int,
            offset: Int,
            onlyWithMutual: Boolean,
            userFields: List<UserOptionalField>,
            nameCase: NameCase,
            callback: Callback<JsonObject?>
    ) = Methods.getSuggestions.call(client, callback, JsonObject()
            .put("count", count)
            .put("offset", offset)
            .put("filter", if (onlyWithMutual) "mutual" else null)
            .put("fields", userFields.joinToString(",") { it.value })
            .put("name_case", nameCase.value)
    )

    fun search(
            query: String,
            userId: Int? = null,
            count: Int,
            offset: Int,
            userFields: List<UserOptionalField>,
            nameCase: NameCase,
            callback: Callback<JsonObject?>
    ) = Methods.search.call(client, callback, JsonObject()
            .put("q", query)
            .put("user_id", userId)
            .put("count", count)
            .put("offset", offset)
            .put("fields", userFields.joinToString(",") { it.value })
            .put("name_case", nameCase.value)
    )

    private object Methods {
        private const val it = "friends."
        const val add = it + "add"
        const val addList = it + "addList"
        const val areFriends = it + "areFriends"
        const val delete = it + "delete"
        const val deleteAllRequests = it + "deleteAllRequests"
        const val deleteList = it + "deleteList"
        const val edit = it + "edit"
        const val editList = it + "editList"
        const val get = it + "get"
        const val getAppUsers = it + "getAppUsers"
        const val getByPhones = it + "getByPhones"
        const val getLists = it + "getLists"
        const val getMutual = it + "getMutual"
        const val getOnline = it + "getOnline"
        const val getRecent = it + "getRecent"
        const val getRequests = it + "getRequests"
        const val getSuggestions = it + "getSuggestions"
        const val search = it + "search"
    }
}