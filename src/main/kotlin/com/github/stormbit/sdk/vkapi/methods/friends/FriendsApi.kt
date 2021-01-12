package com.github.stormbit.sdk.vkapi.methods.friends

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.models.FriendRequest
import com.github.stormbit.sdk.objects.models.ListResponse
import com.github.stormbit.sdk.objects.models.User
import com.github.stormbit.sdk.utils.parametersOf
import com.github.stormbit.sdk.utils.toInt
import com.github.stormbit.sdk.vkapi.methods.FriendsOrder
import com.github.stormbit.sdk.vkapi.methods.MethodsContext
import com.github.stormbit.sdk.vkapi.methods.NameCase
import com.github.stormbit.sdk.vkapi.methods.UserOptionalField
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonObject

@Suppress("unused")
class FriendsApi(private var client: Client) : MethodsContext() {
    fun add(
            userId: Int,
            declineRequest: Boolean,
            text: String? = null,
    ): JsonObject? = Methods.add.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("user_id", userId)
            append("text", text)
            append("follow", declineRequest.toInt())
        }
    )

    fun addList(
            name: String,
            userIds: List<Int>? = null
    ): JsonObject? = Methods.addList.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("name", name)
            append("user_ids", userIds?.joinToString(","))
        }
    )

    fun areFriends(
            userIds: List<Int>,
            needSign: Boolean
    ): JsonObject? = Methods.areFriends.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("user_ids", userIds.joinToString(","))
            append("need_sign", needSign.toInt())
        }
    )

    fun delete(
            userId: Int
    ): JsonObject? = Methods.delete.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("user_id", userId)
        }
    )

    fun deleteAllRequests(): JsonObject? = Methods.deleteAllRequests.callSync(client, JsonObject.serializer(), mapOf())

    fun deleteList(
            listId: Int
    ): JsonObject? = Methods.deleteList.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("list_id", listId)
        }
    )

    fun edit(
            userId: Int,
            listIds: List<Int>? = null
    ): JsonObject? = Methods.edit.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("user_id", userId)
            append("list_ids", listIds?.joinToString(","))
        }
    )

    fun editList(
            listId: Int,
            userIds: List<Int>,
            name: String? = null
    ): JsonObject? = Methods.editList.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("list_id", listId)
            append("user_ids", userIds.joinToString(","))
            append("name", name)
        }
    )

    fun editList(
            listId: Int,
            addUserIds: List<Int>? = null,
            deleteUserIds: List<Int>? = null,
            name: String? = null
    ): JsonObject? = Methods.editList.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("list_id", listId)
            append("add_user_ids", addUserIds?.joinToString(","))
            append("delete_user_ids", deleteUserIds?.joinToString(","))
            append("name", name)
        }
    )

    fun get(
            userId: Int? = null,
            order: FriendsOrder? = null,
            listId: Int? = null,
            count: Int,
            offset: Int,
            userFields: List<UserOptionalField>,
            nameCase: NameCase
    ): JsonObject? = Methods.get.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("user_id", userId)
            append("order", order?.value)
            append("list_id", listId)
            append("count", count)
            append("offset", offset)
            append("fields", userFields.joinToString(",") { it.value })
            append("name_case", nameCase.value)
        }
    )

    fun getIds(
            userId: Int? = null,
            order: FriendsOrder? = null,
            listId: Int? = null,
            count: Int,
            offset: Int
    ): JsonObject? = Methods.get.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("user_id", userId)
            append("order", order?.value)
            append("list_id", listId)
            append("count", count)
            append("offset", offset)
        }
    )

    fun getAppUsers(): JsonObject? = Methods.getAppUsers.callSync(client, JsonObject.serializer(), mapOf())

    fun getByPhones(
            phones: List<String>,
            userFields: List<UserOptionalField>
    ): JsonObject? = Methods.getByPhones.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("phones", phones.joinToString(","))
            append("fields", userFields.joinToString(",") { it.value })
        }
    )

    fun getLists(
            userId: Int? = null,
            withSystem: Boolean
    ): ListResponse<FriendsListItem>? = Methods.getLists.callSync(
        client, ListResponse.serializer(FriendsListItem.serializer()), parametersOf {
            append("user_id", userId)
            append("return_system", withSystem.toInt())
        }
    )

    fun getMutual(
            targetUserId: Int,
            sourceUserId: Int? = null,
            sortRandomly: Boolean,
            count: Int? = null,
            offset: Int
    ): JsonObject? = Methods.getMutual.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("target_uid", targetUserId)
            append("source_uid", sourceUserId)
            append("order", if (sortRandomly) "random" else null)
            append("count", count)
            append("offset", offset)
        }
    )

    fun getMutual(
            targetUserIds: List<Int>,
            sourceUserId: Int? = null,
            sortRandomly: Boolean,
            count: Int? = null,
            offset: Int
    ): JsonObject? = Methods.getMutual.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("target_uids", targetUserIds.joinToString(","))
            append("source_uid", sourceUserId)
            append("order", if (sortRandomly) "random" else null)
            append("count", count)
            append("offset", offset)
        }
    )

    fun getOnline(
            userId: Int? = null,
            listId: Int? = null,
            sortRandomly: Boolean,
            count: Int? = null,
            offset: Int
    ): JsonObject? = Methods.getOnline.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("user_id", userId)
            append("list_id", listId)
            append("order", if (sortRandomly) "random" else null)
            append("count", count)
            append("offset", offset)
        }
    )

    fun getOnlineWithOnlineFromMobile(
            userId: Int? = null,
            listId: Int? = null,
            sortRandomly: Boolean,
            count: Int? = null,
            offset: Int
    ): JsonObject? = Methods.getOnline.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("user_id", userId)
            append("list_id", listId)
            append("online_mobile", 1)
            append("order", if (sortRandomly) "random" else null)
            append("count", count)
            append("offset", offset)
        }
    )

    fun getRecent(
            count: Int
    ): JsonObject? = Methods.getRecent.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("count", count)
        }
    )

    fun getOutgoingRequests(
            count: Int,
            offset: Int
    ): JsonObject? = Methods.getRequests.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("count", count)
            append("offset", offset)
            append("out", 1)
        }
    )

    fun getOutgoingRequestsWithMutual(
            offset: Int
    ): JsonObject? = Methods.getRequests.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("offset", offset)
            append("need_mutual", 1)
            append("out", 1)
        }
    )

    fun getRequests(
            count: Int,
            offset: Int,
            sortByMutual: Boolean,
            needViewed: Boolean
    ): ListResponse<Int>? = Methods.getRequests.callSync(
        client, ListResponse.serializer(Int.serializer()), parametersOf {
            append("count", count)
            append("offset", offset)
            append("sort", sortByMutual.toInt())
            append("need_viewed", needViewed.toInt())
        }
    )

    fun getRequestsWithMutual(
            offset: Int,
            sortByMutual: Boolean,
            needViewed: Boolean
    ): ListResponse<Int>? = Methods.getRequests.callSync(
        client, ListResponse.serializer(Int.serializer()), parametersOf {
            append("offset", offset)
            append("sort", sortByMutual.toInt())
            append("need_viewed", needViewed.toInt())
            append("need_mutual", 1)
        }
    )

    fun getSuggestedRequests(
            count: Int,
            offset: Int,
            sortByMutual: Boolean
    ): ListResponse<Int>? = Methods.getRequests.callSync(
        client, ListResponse.serializer(Int.serializer()), parametersOf {
            append("count", count)
            append("offset", offset)
            append("sort", sortByMutual.toInt())
            append("suggested", 1)
        }
    )

    fun getSuggestedRequestsExtended(
            count: Int,
            offset: Int,
            sortByMutual: Boolean
    ): ListResponse<FriendRequest>? = Methods.getRequests.callSync(
        client, ListResponse.serializer(FriendRequest.serializer()), parametersOf {
            append("count", count)
            append("offset", offset)
            append("sort", sortByMutual.toInt())
            append("extended", 1)
            append("suggested", 1)
        }
    )

    fun getSuggestedRequestsWithMutual(
            offset: Int,
            sortByMutual: Boolean
    ): ListResponse<FriendRequest>? = Methods.getRequests.callSync(
        client, ListResponse.serializer(FriendRequest.serializer()), parametersOf {
            append("offset", offset)
            append("sort", sortByMutual.toInt())
            append("extended", 1)
            append("need_mutual", 1)
            append("suggested", 1)
        }
    )

    fun getSuggestions(
            count: Int,
            offset: Int,
            onlyWithMutual: Boolean,
            userFields: List<UserOptionalField>,
            nameCase: NameCase
    ): ListResponse<User>? = Methods.getSuggestions.callSync(
        client, ListResponse.serializer(User.serializer()), parametersOf {
            append("count", count)
            append("offset", offset)
            append("filter", if (onlyWithMutual) "mutual" else null)
            append("fields", userFields.joinToString(",") { it.value })
            append("name_case", nameCase.value)
        }
    )

    fun search(
            query: String,
            userId: Int? = null,
            count: Int,
            offset: Int,
            userFields: List<UserOptionalField>,
            nameCase: NameCase
    ): ListResponse<User>? = Methods.search.callSync(
        client, ListResponse.serializer(User.serializer()), parametersOf {
            append("q", query)
            append("user_id", userId)
            append("count", count)
            append("offset", offset)
            append("fields", userFields.joinToString(",") { it.value })
            append("name_case", nameCase.value)
        }
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