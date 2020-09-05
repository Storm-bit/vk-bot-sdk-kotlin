package com.github.stormbit.sdk.utils.vkapi.methods.friends

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.callSync
import com.github.stormbit.sdk.utils.vkapi.methods.FriendsOrder
import com.github.stormbit.sdk.utils.vkapi.methods.NameCase
import com.github.stormbit.sdk.utils.vkapi.methods.UserOptionalField
import org.json.JSONObject

@Suppress("unused")
class FriendsApi(private var client: Client) {
    fun add(
            userId: Int,
            text: String?,
            declineRequest: Boolean
    ): JSONObject = Methods.add.callSync(client, JSONObject()
            .put("user_id", userId)
            .put("text", text)
            .put("follow", declineRequest.asInt())
    )

    fun addList(
            name: String,
            userIds: List<Int>?
    ): JSONObject = Methods.addList.callSync(client, JSONObject()
            .put("name", name)
            .put("user_ids", userIds?.joinToString(","))
    )

    fun areFriends(
            userIds: List<Int>,
            needSign: Boolean
    ): JSONObject = Methods.areFriends.callSync(client, JSONObject()
            .put("user_ids", userIds.joinToString(","))
            .put("need_sign", needSign.asInt())
    )

    fun delete(
            userId: Int
    ): JSONObject = Methods.delete.callSync(client, JSONObject()
            .put("user_id", userId)
    )

    fun deleteAllRequests(): JSONObject = Methods.deleteAllRequests.callSync(client, JSONObject())

    fun deleteList(
            listId: Int
    ): JSONObject = Methods.deleteList.callSync(client, JSONObject()
            .put("list_id", listId)
    )

    fun edit(
            userId: Int,
            listIds: List<Int>?
    ): JSONObject = Methods.edit.callSync(client, JSONObject()
            .put("user_id", userId)
            .put("list_ids", listIds?.joinToString(","))
    )

    fun editList(
            listId: Int,
            userIds: List<Int>,
            name: String?
    ): JSONObject = Methods.editList.callSync(client, JSONObject()
            .put("list_id", listId)
            .put("user_ids", userIds.joinToString(","))
            .put("name", name)
    )

    fun editList(
            listId: Int,
            addUserIds: List<Int>?,
            deleteUserIds: List<Int>?,
            name: String?
    ): JSONObject = Methods.editList.callSync(client, JSONObject()
            .put("list_id", listId)
            .put("add_user_ids", addUserIds?.joinToString(","))
            .put("delete_user_ids", deleteUserIds?.joinToString(","))
            .put("name", name)
    )

    fun get(
            userId: Int?,
            order: FriendsOrder?,
            listId: Int?,
            count: Int,
            offset: Int,
            userFields: List<UserOptionalField>,
            nameCase: NameCase
    ): JSONObject = Methods.get.callSync(client, JSONObject()
            .put("user_id", userId)
            .put("order", order?.value)
            .put("list_id", listId)
            .put("count", count)
            .put("offset", offset)
            .put("fields", userFields.joinToString(",") { it.value })
            .put("name_case", nameCase.value)
    )

    fun getIds(
            userId: Int?,
            order: FriendsOrder?,
            listId: Int?,
            count: Int,
            offset: Int
    ): JSONObject = Methods.get.callSync(client, JSONObject()
            .put("user_id", userId)
            .put("order", order?.value)
            .put("list_id", listId)
            .put("count", count)
            .put("offset", offset)
    )

    fun getAppUsers(): JSONObject = Methods.getAppUsers.callSync(client, JSONObject())

    fun getByPhones(
            phones: List<String>,
            userFields: List<UserOptionalField>
    ): JSONObject = Methods.getByPhones.callSync(client, JSONObject()
            .put("phones", phones.joinToString(","))
            .put("fields", userFields.joinToString(",") { it.value })
    )

    fun getLists(
            userId: Int?,
            withSystem: Boolean
    ): JSONObject = Methods.getLists.callSync(client, JSONObject()
            .put("user_id", userId)
            .put("return_system", withSystem.asInt())
    )

    fun getMutual(
            targetUserId: Int,
            sourceUserId: Int?,
            sortRandomly: Boolean,
            count: Int?,
            offset: Int
    ): JSONObject = Methods.getMutual.callSync(client, JSONObject()
            .put("target_uid", targetUserId)
            .put("source_uid", sourceUserId)
            .put("order", if (sortRandomly) "random" else null)
            .put("count", count)
            .put("offset", offset)
    )

    fun getMutual(
            targetUserIds: List<Int>,
            sourceUserId: Int?,
            sortRandomly: Boolean,
            count: Int?,
            offset: Int
    ): JSONObject = Methods.getMutual.callSync(client, JSONObject()
            .put("target_uids", targetUserIds.joinToString(","))
            .put("source_uid", sourceUserId)
            .put("order", if (sortRandomly) "random" else null)
            .put("count", count)
            .put("offset", offset)
    )

    fun getOnline(
            userId: Int?,
            listId: Int?,
            sortRandomly: Boolean,
            count: Int?,
            offset: Int
    ): JSONObject = Methods.getOnline.callSync(client, JSONObject()
            .put("user_id", userId)
            .put("list_id", listId)
            .put("order", if (sortRandomly) "random" else null)
            .put("count", count)
            .put("offset", offset)
    )

    fun getOnlineWithOnlineFromMobile(
            userId: Int?,
            listId: Int?,
            sortRandomly: Boolean,
            count: Int?,
            offset: Int
    ): JSONObject = Methods.getOnline.callSync(client, JSONObject()
            .put("user_id", userId)
            .put("list_id", listId)
            .put("online_mobile", 1)
            .put("order", if (sortRandomly) "random" else null)
            .put("count", count)
            .put("offset", offset)
    )

    fun getRecent(
            count: Int
    ): JSONObject = Methods.getRecent.callSync(client, JSONObject()
            .put("count", count)
    )

    fun getOutgoingRequests(
            count: Int,
            offset: Int
    ): JSONObject = Methods.getRequests.callSync(client, JSONObject()
            .put("count", count)
            .put("offset", offset)
            .put("out", 1)
    )

    fun getOutgoingRequestsWithMutual(
            offset: Int
    ): JSONObject = Methods.getRequests.callSync(client, JSONObject()
            .put("offset", offset)
            .put("need_mutual", 1)
            .put("out", 1)
    )

    fun getRequests(
            count: Int,
            offset: Int,
            sortByMutual: Boolean,
            needViewed: Boolean
    ): JSONObject = Methods.getRequests.callSync(client, JSONObject()
            .put("count", count)
            .put("offset", offset)
            .put("sort", sortByMutual.asInt())
            .put("need_viewed", needViewed.asInt())
    )

    fun getRequestsWithMutual(
            offset: Int,
            sortByMutual: Boolean,
            needViewed: Boolean
    ): JSONObject = Methods.getRequests.callSync(client, JSONObject()
            .put("offset", offset)
            .put("sort", sortByMutual.asInt())
            .put("need_viewed", needViewed.asInt())
            .put("need_mutual", 1)
    )

    fun getSuggestedRequests(
            count: Int,
            offset: Int,
            sortByMutual: Boolean
    ): JSONObject = Methods.getRequests.callSync(client, JSONObject()
            .put("count", count)
            .put("offset", offset)
            .put("sort", sortByMutual.asInt())
            .put("suggested", 1)
    )

    fun getSuggestedRequestsExtended(
            count: Int,
            offset: Int,
            sortByMutual: Boolean
    ): JSONObject = Methods.getRequests.callSync(client, JSONObject()
            .put("count", count)
            .put("offset", offset)
            .put("sort", sortByMutual.asInt())
            .put("extended", 1)
            .put("suggested", 1)
    )

    fun getSuggestedRequestsWithMutual(
            offset: Int,
            sortByMutual: Boolean
    ): JSONObject = Methods.getRequests.callSync(client, JSONObject()
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
            nameCase: NameCase
    ): JSONObject = Methods.getSuggestions.callSync(client, JSONObject()
            .put("count", count)
            .put("offset", offset)
            .put("filter", if (onlyWithMutual) "mutual" else null)
            .put("fields", userFields.joinToString(",") { it.value })
            .put("name_case", nameCase.value)
    )

    fun search(
            query: String,
            userId: Int?,
            count: Int,
            offset: Int,
            userFields: List<UserOptionalField>,
            nameCase: NameCase
    ): JSONObject = Methods.search.callSync(client, JSONObject()
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