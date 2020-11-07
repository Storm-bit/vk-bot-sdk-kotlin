package com.github.stormbit.sdk.utils.vkapi.methods.messages

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.Chat
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.callSync
import com.github.stormbit.sdk.utils.vkapi.keyboard.Keyboard
import com.github.stormbit.sdk.utils.vkapi.methods.AttachmentType
import com.github.stormbit.sdk.utils.vkapi.methods.GMTDate
import com.github.stormbit.sdk.utils.vkapi.methods.ObjectField
import com.google.gson.JsonObject

@Suppress("unused", "MemberVisibilityCanBePrivate")
class MessagesApi(private val client: Client) {
    fun addChatUser(
            chatId: Int,
            userId: Int
    ): JsonObject = Methods.addChatUser.callSync(client, "chat_id", chatId, "user_id", userId)

    fun allowMessagesFromGroup(
            groupId: Int,
            key: String?
    ): JsonObject = Methods.allowMessagesFromGroup.callSync(client, "group_id", groupId, "key", key)

    fun createChat(
            userIds: List<Int>,
            title: String
    ): JsonObject = Methods.createChat.callSync(client, "user_ids", userIds.joinToString(","), "title", title)

    fun delete(
            messageIds: List<Int>,
            deleteForAll: Boolean,
            markAsSpam: Boolean = false,
            groupId: Int? = null
    ): JsonObject = Methods.delete.callSync(client, JsonObject()
            .put("message_ids", messageIds.joinToString(","))
            .put("spam", markAsSpam.asInt())
            .put("delete_for_all", deleteForAll.asInt())
            .put("group_id", groupId)
    )

    fun deleteChatPhoto(
            chatId: Int,
            groupId: Int? = null
    ): JsonObject = Methods.deleteChatPhoto.callSync(client, JsonObject()
            .put("chat_id", chatId)
            .put("group_id", groupId)
    )

    fun deleteChatPhoto(
            chatId: Int
    ): JsonObject = Methods.deleteChatPhoto.callSync(client, "chat_id", chatId)

    fun deleteConversation(
            peerId: Int,
            groupId: Int?
    ): JsonObject = Methods.deleteConversation.callSync(client, "peer_id", peerId, "group_id", groupId)

    fun deleteConversation(
            peerId: Int
    ): JsonObject = Methods.deleteConversation.callSync(client, "peer_id", peerId)

    fun denyMessagesFromGroup(
            groupId: Int
    ): JsonObject = Methods.denyMessagesFromGroup.callSync(client, "group_id", groupId)

    fun edit(
            peerId: Int,
            messageId: Int,
            message: String?,
            latitude: Int? = null,
            longitude: Int? = null,
            attachments: List<String>? = null,
            keepForwardMessages: Boolean? = null,
            keepSnippets: Boolean? = null,
            groupId: Int? = null
    ): JsonObject = Methods.edit.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("message_id", messageId)
            .put("message", message)
            .put("lat", latitude)
            .put("long", longitude)
            .put("attachment", attachments?.joinToString(","))
            .put("keep_forward_messages", keepForwardMessages?.asInt())
            .put("keep_snippets", keepSnippets?.asInt())
            .put("group_id", groupId)
    )

    fun editChat(
            chatId: Int,
            title: String,
            groupId: Int? = null
    ): JsonObject = Methods.editChat.callSync(client, JsonObject()
            .put("chat_id", chatId)
            .put("title", title)
            .put("group_id", groupId)
    )

    fun getByConversationMessageId(
            peerId: Int,
            conversationMessageIds: List<Int>? = null,
            extended: Boolean? = null,
            fields: List<ObjectField>? = null,
            groupId: Int? = null
    ): JsonObject = Methods.getByConversationMessageId.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("conversation_message_ids", conversationMessageIds?.joinToString(","))
            .put("extended", extended?.asInt())
            .put("fields", fields?.joinToString(",") { it.value })
            .put("group_id", groupId)
    )

    fun getById(
            messageIds: List<Int>,
            previewLength: Int? = null,
            extended: Boolean? = null,
            fields: List<ObjectField>? = null,
            groupId: Int? = null
    ): JsonObject = Methods.getById.callSync(client, JsonObject()
            .put("message_ids", messageIds.joinToString(","))
            .put("preview_length", previewLength)
            .put("extended", extended?.asInt())
            .put("fields", fields?.joinToString(",") { it.value })
            .put("group_id", groupId)
    )

    fun getChat(
            chatIds: List<Int>
    ): JsonObject = Methods.getChat.callSync(client, "chat_ids", chatIds.joinToString(","))

    fun getChatTitle(chatId: Int): String = Methods.getChat.callSync(client, "chat_ids", if (chatId.isChatPeerId) chatId - Chat.CHAT_PREFIX else chatId).getAsJsonArray("response").getJsonObject(0).getString("title")

    fun getChatPreview(
            link: String,
            fields: List<ObjectField> = emptyList()
    ): JsonObject = Methods.getChatPreview.callSync(client, JsonObject()
            .put("link", link)
            .put("fields", fields.joinToString(",") { it.value })
    )

    fun getChatUsers(
            chatId: Int
    ): List<Int> = getChat(listOf(chatId)).getAsJsonArray("users").toList() as List<Int>

    fun getConversationMembers(
            peerId: Int,
            fields: List<ObjectField> = emptyList(),
            groupId: Int? = null
    ): JsonObject = Methods.getConversationMembers.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("fields", fields.joinToString(",") { it.value })
            .put("group_id", groupId)
    )

    fun getConversations(
            count: Int = 1,
            offset: Int = 0,
            filter: ConversationFilter = ConversationFilter.ALL,
            startMessageId: Int? = null,
            extended: Boolean = false,
            fields: List<ObjectField> = emptyList(),
            groupId: Int? = null
    ): JsonObject = Methods.getConversations.callSync(client, JsonObject()
            .put("offset", offset)
            .put("count", count)
            .put("filter", filter.value)
            .put("start_message_id", startMessageId)
            .put("extended", extended.asInt())
            .put("fields", fields.joinToString(",") { it.value })
            .put("group_id", groupId)
    )

    fun getConversationsById(
            peerIds: List<Int>,
            extended: Boolean = false,
            fields: List<ObjectField> = emptyList(),
            groupId: Int? = null
    ): JsonObject = Methods.getConversationsById.callSync(client, JsonObject()
            .put("peer_ids", peerIds.joinToString(","))
            .put("extended", extended.asInt())
            .put("fields", fields.joinToString(",") { it.value })
            .put("group_id", groupId)
    )

    fun getHistory(
            peerId: Int,
            count: Int = 1,
            offset: Int = 0,
            startMessageId: Int? = null,
            reverse: Boolean = false,
            extended: Boolean = false,
            fields: List<ObjectField> = emptyList(),
            groupId: Int? = null
    ): JsonObject = Methods.getHistory.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("offset", offset)
            .put("count", count)
            .put("start_message_id", startMessageId)
            .put("rev", reverse.asInt())
            .put("extended", extended.asInt())
            .put("fields", fields.joinToString(",") { it.value })
            .put("group_id", groupId)
    )

    fun getHistoryAttachments(
            peerId: Int,
            mediaType: AttachmentType,
            startFrom: Int? = null,
            count: Int = 1,
            withPhotoSizes: Boolean = true,
            fields: List<ObjectField> = emptyList(),
            groupId: Int? = null
    ): JsonObject = Methods.getHistoryAttachments.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("media_type", mediaType.value)
            .put("start_from", startFrom)
            .put("count", count)
            .put("photo_sizes", withPhotoSizes.asInt())
            .put("fields", fields.joinToString(",") { it.value })
            .put("group_id", groupId)
    )

    fun getImportantMessages(
            count: Int = 1,
            offset: Int = 0,
            startMessageId: Int? = null,
            previewLength: Int = 0,
            extended: Boolean = false,
            fields: List<ObjectField> = emptyList(),
            groupId: Int? = null
    ): JsonObject = Methods.getImportantMessages.callSync(client, JsonObject()
            .put("count", count)
            .put("offset", offset)
            .put("start_message_id", startMessageId)
            .put("preview_length", previewLength)
            .put("extended", extended.asInt())
            .put("fields", fields.joinToString(",") { it.value })
            .put("group_id", groupId)
    )

    fun getInviteLink(
            peerId: Int,
            generateNewLink: Boolean,
            groupId: Int? = null
    ): JsonObject = Methods.getInviteLink.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("reset", generateNewLink.asInt())
            .put("group_id", groupId)
    )

    fun getLastActivity(
            userId: Int
    ): JsonObject = Methods.getLastActivity.callSync(client, JsonObject().put("user_id", userId))

    fun getLongPollHistory(
            ts: Long,
            pts: Long,
            previewLength: Int,
            withOnlineStatuses: Boolean,
            userFields: List<ObjectField>,
            eventsLimit: Int,
            messagesLimit: Int,
            maxMessageId: Int?,
            longPollVersion: Int,
            groupId: Int? = null
    ): JsonObject = Methods.getLongPollHistory.callSync(client, JsonObject()
            .put("ts", ts)
            .put("pts", pts)
            .put("preview_length", previewLength)
            .put("onlines", withOnlineStatuses.asInt())
            .put("fields", userFields.joinToString(",") { it.value })
            .put("events_limit", eventsLimit)
            .put("msgs_limit", messagesLimit)
            .put("max_msg_id", maxMessageId)
            .put("lp_version", longPollVersion)
            .put("group_id", groupId)
    )


    fun getMessagesCount(
            peerId: Int
    ): Int = getHistory(peerId, count = 0).getAsJsonObject("response").getInt("count")

    fun getLongPollServer(
            needPts: Boolean,
            longPollVersion: Int,
            groupId: Int? = null
    ): JsonObject = Methods.getLongPollServer.callSync(client, JsonObject()
            .put("need_pts", needPts.asInt())
            .put("lp_version", longPollVersion)
            .put("group_id", groupId)
    )

    fun getRecentCalls(
            count: Int,
            startMessageId: Int?,
            fields: List<ObjectField>,
            extended: Boolean
    ): JsonObject = Methods.getRecentCalls.callSync(client, JsonObject()
            .put("count", count)
            .put("start_message_id", startMessageId)
            .put("fields", fields.joinToString(",") { it.value })
            .put("extended", extended.asInt())
    )

    fun isMessagesFromGroupAllowed(
            groupId: Int,
            userId: Int
    ): JsonObject = Methods.isMessagesFromGroupAllowed.callSync(client, JsonObject()
            .put("group_id", groupId)
            .put("user_id", userId)
    )

    fun joinChatByInviteLink(
            link: String
    ): JsonObject = Methods.joinChatByInviteLink.callSync(client, JsonObject().put("link", link))

    fun markAsAnsweredConversation(
            peerId: Int,
            isAnswered: Boolean
    ): JsonObject = Methods.markAsAnsweredConversation.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("answered", isAnswered.asInt())
    )

    fun markAsAnsweredConversation(
            peerId: Int,
            isAnswered: Boolean,
            groupId: Int
    ): JsonObject = Methods.markAsAnsweredConversation.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("answered", isAnswered.asInt())
            .put("group_id", groupId)
    )

    fun markAsImportant(
            messageIds: List<Int>,
            markAsImportant: Boolean
    ): JsonObject = Methods.markAsImportant.callSync(client, JsonObject()
            .put("message_ids", messageIds.joinToString(","))
            .put("important", markAsImportant.asInt())
    )

    fun markAsImportantConversation(
            peerId: Int,
            isImportant: Boolean
    ): JsonObject = Methods.markAsImportantConversation.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("important", isImportant.asInt())
    )

    fun markAsImportantConversation(
            peerId: Int,
            isImportant: Boolean,
            groupId: Int
    ): JsonObject = Methods.markAsImportantConversation.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("important", isImportant.asInt())
            .put("group_id", groupId)
    )

    fun markAsRead(
            peerId: Int,
            startMessageId: Int?,
            groupId: Int? = null
    ): JsonObject = Methods.markAsRead.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("start_message_id", startMessageId)
            .put("group_id", groupId)
    )

    fun pin(
            peerId: Int,
            messageId: Int,
            groupId: Int? = null
    ): JsonObject = Methods.pin.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("message_id", messageId)
            .put("group_id", groupId)
    )

    fun removeChatUser(
            chatId: Int,
            memberId: Int,
            groupId: Int? = null
    ): JsonObject = Methods.removeChatUser.callSync(client, JsonObject()
            .put("chat_id", chatId)
            .put("member_id", memberId)
            .put("group_id", groupId)
    )

    fun restore(
            messageId: Int,
            groupId: Int? = null
    ): JsonObject = Methods.restore.callSync(client, JsonObject()
            .put("message_id", messageId)
            .put("group_id", groupId)
    )

    fun search(
            query: String,
            peerId: Int? = null,
            maxDate: GMTDate? = null,
            previewLength: Int? = 0,
            count: Int = 1,
            offset: Int = 0,
            groupId: Int? = null
    ): JsonObject = Methods.search.callSync(client, JsonObject()
            .put("q", query)
            .put("peer_id", peerId)
            .put("date", maxDate?.toDMYString())
            .put("preview_length", previewLength)
            .put("offset", offset)
            .put("count", count)
            .put("group_id", groupId)
    )

    fun searchExtended(
            query: String,
            peerId: Int? = null,
            maxDate: GMTDate? = null,
            previewLength: Int? = 0,
            count: Int = 1,
            offset: Int = 0,
            fields: List<ObjectField> = emptyList(),
            groupId: Int? = null,
    ): JsonObject = Methods.search.callSync(client, JsonObject()
            .put("q", query)
            .put("peer_id", peerId)
            .put("date", maxDate?.toDMYString())
            .put("preview_length", previewLength)
            .put("offset", offset)
            .put("count", count)
            .put("extended", 1)
            .put("fields", fields.joinToString(",") { it.value })
            .put("group_id", groupId)
    )

    fun searchConversations(
            query: String,
            count: Int,
            extended: Boolean,
            fields: List<ObjectField>,
            groupId: Int? = null
    ): JsonObject = Methods.searchConversations.callSync(client, JsonObject()
            .put("q", query)
            .put("count", count)
            .put("extended", extended.asInt())
            .put("fields", fields.joinToString(",") { it.value })
            .put("group_id", groupId)
    )

    fun send(
            peerId: Int,
            randomId: Int,
            text: String,
            latitude: Int? = null,
            longitude: Int? = null,
            attachments: List<String>? = null,
            replyToMessageId: Int? = null,
            forwardedMessages: List<Int>? = null,
            stickerId: Int? = null,
            keyboard: Keyboard? = null,
            payload: String? = null,
            dontParseLink: Boolean? = null,
            disableMentions: Boolean? = null,
            groupId: Int? = null
    ): JsonObject = Methods.send.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("random_id", randomId)
            .put("message", text)
            .put("lat", latitude)
            .put("long", longitude)
            .put("attachment", attachments?.joinToString(","))
            .put("reply_to", replyToMessageId)
            .put("forward_messages", forwardedMessages?.joinToString(","))
            .put("sticker_id", stickerId)
            .put("keyboard", if (keyboard != null) gson.toJsonTree(keyboard).asJsonObject else null)
            .put("payload", payload)
            .put("dont_parse_links", dontParseLink?.asInt())
            .put("disable_mentions", disableMentions?.asInt())
            .put("group_id", groupId)
    )

    fun sendBulk(
            userIds: List<Int>,
            randomId: Int,
            text: String,
            latitude: Int? = null,
            longitude: Int? = null,
            attachments: List<String>? = null,
            forwardedMessages: List<Int>? = null,
            stickerId: Int? = null,
            keyboard: Keyboard? = null,
            payload: String? = null,
            dontParseLink: Boolean? = null
    ): JsonObject = Methods.send.callSync(client, JsonObject()
            .put("user_ids", userIds.joinToString(","))
            .put("random_id", randomId)
            .put("message", text)
            .put("lat", latitude)
            .put("long", longitude)
            .put("attachment", attachments?.joinToString(","))
            .put("forward_messages", forwardedMessages?.joinToString(","))
            .put("sticker_id", stickerId)
            .put("keyboard", gson.toJsonTree(keyboard).asJsonObject)
            .put("payload", payload)
            .put("dont_parse_links", dontParseLink?.asInt())
    )

    fun setActivity(
            peerId: Int,
            type: String,
            groupId: Int? = null
    ): JsonObject = Methods.setActivity.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("group_id", groupId)
            .put("type", type)
    )

    fun setChatPhoto(
            file: String,
            groupId: Int? = null
    ): JsonObject = Methods.setChatPhoto.callSync(client, JsonObject()
            .put("file", file)
            .put("group_id", groupId)
    )

    fun unpin(
            peerId: Int,
            groupId: Int? = null
    ): JsonObject = Methods.unpin.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("group_id", groupId)
    )

    companion object {
        object Methods {
            private const val it = "messages."
            const val addChatUser = it + "addChatUser"
            const val allowMessagesFromGroup = it + "allowMessagesFromGroup"
            const val createChat = it + "createChat"
            const val delete = it + "delete"
            const val deleteChatPhoto = it + "deleteChatPhoto"
            const val deleteConversation = it + "deleteConversation"
            const val denyMessagesFromGroup = it + "denyMessagesFromGroup"
            const val edit = it + "edit"
            const val editChat = it + "editChat"
            const val getByConversationMessageId = it + "getByConversationMessageId"
            const val getById = it + "getById"
            const val getChat = it + "getChat"
            const val getChatPreview = it + "getChatPreview"
            const val getConversationMembers = it + "getConversationMembers"
            const val getConversations = it + "getConversations"
            const val getConversationsById = it + "getConversationsById"
            const val getHistory = it + "getHistory"
            const val getHistoryAttachments = it + "getHistoryAttachments"
            const val getImportantMessages = it + "getImportantMessages"
            const val getInviteLink = it + "getInviteLink"
            const val getLastActivity = it + "getLastActivity"
            const val getLongPollHistory = it + "getLongPollHistory"
            const val getLongPollServer = it + "getLongPollServer"
            const val getRecentCalls = it + "getRecentCalls"
            const val isMessagesFromGroupAllowed = it + "isMessagesFromGroupAllowed"
            const val joinChatByInviteLink = it + "joinChatByInviteLink"
            const val markAsAnsweredConversation = it + "markAsAnsweredConversation"
            const val markAsImportant = it + "markAsImportant"
            const val markAsImportantConversation = it + "markAsImportantConversation"
            const val markAsRead = it + "markAsRead"
            const val pin = it + "pin"
            const val removeChatUser = it + "removeChatUser"
            const val restore = it + "restore"
            const val search = it + "search"
            const val searchConversations = it + "searchConversations"
            const val send = it + "send"
            const val setActivity = it + "setActivity"
            const val setChatPhoto = it + "setChatPhoto"
            const val unpin = it + "unpin"
        }
    }
}