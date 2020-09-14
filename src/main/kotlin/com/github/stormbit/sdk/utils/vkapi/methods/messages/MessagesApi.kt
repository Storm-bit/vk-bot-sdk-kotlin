package com.github.stormbit.sdk.utils.vkapi.methods.messages

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.callSync
import com.github.stormbit.sdk.utils.Utils.Companion.toDMYString
import com.github.stormbit.sdk.utils.getInt
import com.github.stormbit.sdk.utils.gson
import com.github.stormbit.sdk.utils.put
import com.github.stormbit.sdk.utils.vkapi.keyboard.Keyboard
import com.github.stormbit.sdk.utils.vkapi.methods.AttachmentType
import com.github.stormbit.sdk.utils.vkapi.methods.ObjectField
import com.google.gson.JsonObject
import io.ktor.util.date.*

@Suppress("unused", "MemberVisibilityCanBePrivate")
class MessagesApi(private val client: Client) {
    fun addChatUser(
            chatId: Int,
            userId: Int
    ): JsonObject = client.api.callSync(Methods.addChatUser, "chat_id", chatId, "user_id", userId)

    fun allowMessagesFromGroup(
            groupId: Int,
            key: String?
    ): JsonObject = client.api.callSync(Methods.allowMessagesFromGroup, "group_id", groupId, "key", key)

    fun createChat(
            userIds: List<Int>,
            title: String
    ): JsonObject = client.api.callSync(Methods.createChat, "user_ids", userIds.joinToString(","), "title", title)

    fun delete(
            messageIds: List<Int>,
            markAsSpam: Boolean,
            deleteForAll: Boolean,
            groupId: Int?
    ): JsonObject = client.api.callSync(Methods.delete, "message_ids", messageIds.joinToString(","), "spam", markAsSpam.asInt(), "delete_for_all", deleteForAll.asInt(), "group_id", groupId)

    fun delete(
            messageIds: List<Int>,
            markAsSpam: Boolean,
            deleteForAll: Boolean
    ): JsonObject = client.api.callSync(Methods.delete, "message_ids", messageIds.joinToString(","), "spam", markAsSpam.asInt(), "delete_for_all", deleteForAll.asInt())


    fun deleteChatPhoto(
            chatId: Int,
            groupId: Int?
    ): JsonObject = client.api.callSync(Methods.deleteChatPhoto, "chat_id", chatId, "group_id", groupId)


    fun deleteChatPhoto(
            chatId: Int
    ): JsonObject = client.api.callSync(Methods.deleteChatPhoto, "chat_id", chatId)

    fun deleteConversation(
            peerId: Int,
            groupId: Int?
    ): JsonObject = client.api.callSync(Methods.deleteConversation, "peer_id", peerId, "group_id", groupId)

    fun deleteConversation(
            peerId: Int
    ): JsonObject = client.api.callSync(Methods.deleteConversation, "peer_id", peerId)

    fun denyMessagesFromGroup(
            groupId: Int
    ): JsonObject = client.api.callSync(Methods.denyMessagesFromGroup, "group_id", groupId)

    fun edit(
            peerId: Int,
            messageId: Int,
            message: String?,
            latitude: Int?,
            longitude: Int?,
            attachments: List<String>?,
            keepForwardMessages: Boolean,
            keepSnippets: Boolean,
            groupId: Int?
    ): JsonObject = client.api.callSync(Methods.edit,
            JsonObject()
                    .put("peer_id", peerId)
                    .put("message_id", messageId)
                    .put("message", message)
                    .put("lat", latitude)
                    .put("long", longitude)
                    .put("attachment", attachments?.joinToString(","))
                    .put("keep_forward_messages", keepForwardMessages.asInt())
                    .put("keep_snippets", keepSnippets.asInt())
                    .put("group_id", groupId)
    )

    fun edit(
            peerId: Int,
            messageId: Int,
            message: String?,
            latitude: Int?,
            longitude: Int?,
            attachments: List<String>?,
            keepForwardMessages: Boolean,
            keepSnippets: Boolean
    ): JsonObject = edit(
            peerId = peerId,
            messageId = messageId,
            message = message,
            latitude = latitude,
            longitude = longitude,
            attachments = attachments,
            keepForwardMessages = keepForwardMessages,
            keepSnippets = keepSnippets,
            groupId = null
    )

    fun editChat(
            chatId: Int,
            title: String,
            groupId: Int?
    ): JsonObject = client.api.callSync(Methods.editChat,
            JsonObject()
                    .put("chat_id", chatId)
                    .put("title", title)
                    .put("group_id", groupId)
    )

    fun editChat(
            chatId: Int,
            title: String
    ): JsonObject = editChat(
            chatId = chatId,
            title = title,
            groupId = null
    )

    fun getByConversationMessageId(
            peerId: Int,
            conversationMessageIds: List<Int>? = null,
            extended: Boolean? = null,
            fields: List<ObjectField>? = null,
            groupId: Int? = null
    ): JsonObject = client.api.callSync(Methods.getByConversationMessageId,
            JsonObject()
                    .put("peer_id", peerId)
                    .put("conversation_message_ids", conversationMessageIds?.joinToString(","))
                    .put("extended", extended?.asInt())
                    .put("fields", fields?.joinToString(",") { it.value })
                    .put("group_id", groupId)
    )

    fun getById(
            messageIds: List<Int>,
            previewLength: Int?,
            extended: Boolean?,
            fields: List<ObjectField>?,
            groupId: Int?
    ): JsonObject = client.api.callSync(Methods.getById,
            JsonObject()
                    .put("message_ids", messageIds.joinToString(","))
                    .put("preview_length", previewLength)
                    .put("extended", extended?.asInt())
                    .put("fields", fields?.joinToString(",") { it.value })
                    .put("group_id", groupId)
    )

    fun getById(
            messageIds: List<Int>,
            previewLength: Int? = null,
            extended: Boolean? = null,
            fields: List<ObjectField>? = null
    ): JsonObject = getById(
            messageIds = messageIds,
            previewLength = previewLength,
            extended = extended,
            fields = fields,
            groupId = null
    )

    fun getChat(
            chatIds: List<Int>
    ): JsonObject = client.api.callSync(Methods.getChat,
            JsonObject().put("chat_ids", chatIds.joinToString(","))
    )

    fun getChatPreview(
            link: String,
            fields: List<ObjectField>
    ): JsonObject = client.api.callSync(Methods.getChatPreview,
            JsonObject()
                    .put("link", link)
                    .put("fields", fields.joinToString(",") { it.value })
    )

    fun getChatUsers(
            chatId: Int
    ): List<Int> = getChat(listOf(chatId)).getAsJsonArray("users").toList() as List<Int>

    fun getConversationMembers(
            peerId: Int,
            fields: List<ObjectField>,
            groupId: Int?
    ): JsonObject = client.api.callSync(Methods.getConversationMembers,
            JsonObject()
                    .put("peer_id", peerId)
                    .put("fields", fields.joinToString(",") { it.value })
                    .put("group_id", groupId)
    )

    fun getConversationMembers(
            peerId: Int,
            fields: List<ObjectField>
    ): JsonObject =
            getConversationMembers(
                    peerId = peerId,
                    fields = fields,
                    groupId = null
            )

    fun getConversations(
            offset: Int,
            count: Int,
            filter: ConversationFilter,
            startMessageId: Int?,
            extended: Boolean,
            fields: List<ObjectField>,
            groupId: Int?
    ): JsonObject = client.api.callSync(Methods.getConversations,
            JsonObject()
                    .put("offset", offset)
                    .put("count", count)
                    .put("filter", filter.value)
                    .put("start_message_id", startMessageId)
                    .put("extended", extended.asInt())
                    .put("fields", fields.joinToString(",") { it.value })
                    .put("group_id", groupId)
    )

    fun getConversations(
            offset: Int,
            count: Int,
            filter: ConversationFilter,
            startMessageId: Int?,
            extended: Boolean,
            fields: List<ObjectField>
    ): JsonObject =
            getConversations(
                    offset = offset,
                    count = count,
                    filter = filter,
                    startMessageId = startMessageId,
                    extended = extended,
                    fields = fields,
                    groupId = null
            )

    fun getConversationsById(
            peerIds: List<Int>,
            extended: Boolean,
            fields: List<ObjectField>,
            groupId: Int?
    ): JsonObject = Methods.getConversationsById.callSync(client,
            JsonObject()
                    .put("peer_ids", peerIds.joinToString(","))
                    .put("extended", extended.asInt())
                    .put("fields", fields.joinToString(",") { it.value })
                    .put("group_id", groupId)
    )

    fun getConversationsById(
            peerIds: List<Int>,
            extended: Boolean,
            fields: List<ObjectField>
    ): JsonObject =
            getConversationsById(
                    peerIds = peerIds,
                    extended = extended,
                    fields = fields,
                    groupId = null
            )

    fun getHistory(
            peerId: Int,
            offset: Int,
            count: Int,
            startMessageId: Int? = null,
            reverse: Boolean,
            extended: Boolean,
            fields: List<ObjectField>,
            groupId: Int?
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

    fun getHistory(
            peerId: Int,
            offset: Int = 0,
            count: Int = 1,
            startMessageId: Int? = null,
            reverse: Boolean = false,
            extended: Boolean = false,
            fields: List<ObjectField> = ArrayList()
    ): JsonObject =
            getHistory(
                    peerId = peerId,
                    offset = offset,
                    count = count,
                    startMessageId = startMessageId,
                    reverse = reverse,
                    extended = extended,
                    fields = fields,
                    groupId = null
            )

    fun getHistoryAttachments(
            peerId: Int,
            mediaType: AttachmentType,
            startFrom: Int?,
            count: Int,
            withPhotoSizes: Boolean,
            fields: List<ObjectField>,
            groupId: Int?
    ): JsonObject =
            Methods.getHistoryAttachments.callSync(client, JsonObject()
                    .put("peer_id", peerId)
                    .put("media_type", mediaType.value)
                    .put("start_from", startFrom)
                    .put("count", count)
                    .put("photo_sizes", withPhotoSizes.asInt())
                    .put("fields", fields.joinToString(",") { it.value })
                    .put("group_id", groupId)
            )

    fun getHistoryAttachments(
            peerId: Int,
            mediaType: AttachmentType,
            startFrom: Int?,
            count: Int,
            withPhotoSizes: Boolean,
            fields: List<ObjectField>
    ): JsonObject =
            getHistoryAttachments(
                    peerId = peerId,
                    mediaType = mediaType,
                    startFrom = startFrom,
                    count = count,
                    withPhotoSizes = withPhotoSizes,
                    fields = fields,
                    groupId = null
            )

    fun getImportantMessages(
            count: Int,
            offset: Int,
            startMessageId: Int?,
            previewLength: Int,
            extended: Boolean,
            fields: List<ObjectField>,
            groupId: Int?
    ): JsonObject = Methods.getImportantMessages.callSync(client, JsonObject()
            .put("count", count)
            .put("offset", offset)
            .put("start_message_id", startMessageId)
            .put("preview_length", previewLength)
            .put("extended", extended.asInt())
            .put("fields", fields.joinToString(",") { it.value })
            .put("group_id", groupId)
    )

    fun getImportantMessages(
            count: Int,
            offset: Int,
            startMessageId: Int?,
            previewLength: Int,
            extended: Boolean,
            fields: List<ObjectField>
    ): JsonObject =
            getImportantMessages(
                    count = count,
                    offset = offset,
                    startMessageId = startMessageId,
                    previewLength = previewLength,
                    extended = extended,
                    fields = fields,
                    groupId = null
            )

    fun getInviteLink(
            peerId: Int,
            generateNewLink: Boolean,
            groupId: Int?
    ): JsonObject = Methods.getInviteLink.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("reset", generateNewLink.asInt())
            .put("group_id", groupId)
    )

    fun getInviteLink(
            peerId: Int,
            generateNewLink: Boolean
    ): JsonObject =
            getInviteLink(
                    peerId = peerId,
                    generateNewLink = generateNewLink,
                    groupId = null
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
            groupId: Int?
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

    fun getLongPollHistory(
            ts: Long,
            pts: Long,
            previewLength: Int,
            withOnlineStatuses: Boolean,
            userFields: List<ObjectField>,
            eventsLimit: Int,
            messagesLimit: Int,
            maxMessageId: Int?,
            longPollVersion: Int
    ): JsonObject =
            getLongPollHistory(
                    ts = ts,
                    pts = pts,
                    previewLength = previewLength,
                    withOnlineStatuses = withOnlineStatuses,
                    userFields = userFields,
                    eventsLimit = eventsLimit,
                    messagesLimit = messagesLimit,
                    maxMessageId = maxMessageId,
                    longPollVersion = longPollVersion,
                    groupId = null
            )

    fun getLongPollServer(
            needPts: Boolean,
            longPollVersion: Int,
            groupId: Int?
    ): JsonObject = Methods.getLongPollServer.callSync(client, JsonObject()
            .put("need_pts", needPts.asInt())
            .put("lp_version", longPollVersion)
            .put("group_id", groupId)
    )

    fun getLongPollServer(
            needPts: Boolean,
            longPollVersion: Int
    ): JsonObject = getLongPollServer(
            needPts = needPts,
            longPollVersion = longPollVersion,
            groupId = null
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
            groupId: Int?
    ): JsonObject = Methods.markAsRead.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("start_message_id", startMessageId)
            .put("group_id", groupId)
    )

    fun markAsRead(
            peerId: Int,
            startMessageId: Int?
    ): JsonObject = markAsRead(
            peerId = peerId,
            startMessageId = startMessageId,
            groupId = null
    )

    fun pin(
            peerId: Int,
            messageId: Int,
            groupId: Int?
    ): JsonObject = Methods.pin.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("message_id", messageId)
            .put("group_id", groupId)
    )

    fun pin(
            peerId: Int,
            messageId: Int
    ): JsonObject = pin(
            peerId = peerId,
            messageId = messageId,
            groupId = null
    )

    fun removeChatUser(
            chatId: Int,
            memberId: Int,
            groupId: Int?
    ): JsonObject = Methods.removeChatUser.callSync(client, JsonObject()
            .put("chat_id", chatId)
            .put("member_id", memberId)
            .put("group_id", groupId)
    )

    fun removeChatUser(
            chatId: Int,
            memberId: Int
    ): JsonObject = removeChatUser(
            chatId = chatId,
            memberId = memberId,
            groupId = null
    )

    fun restore(
            messageId: Int,
            groupId: Int?
    ): JsonObject = Methods.restore.callSync(client, JsonObject()
            .put("message_id", messageId)
            .put("group_id", groupId)
    )

    fun restore(
            messageId: Int
    ): JsonObject = restore(
            messageId = messageId,
            groupId = null
    )

    fun search(
            query: String,
            peerId: Int?,
            maxDate: GMTDate?,
            previewLength: Int,
            offset: Int,
            count: Int,
            groupId: Int?
    ): JsonObject = Methods.search.callSync(client, JsonObject()
            .put("q", query)
            .put("peer_id", peerId)
            .put("date", maxDate?.toDMYString())
            .put("preview_length", previewLength)
            .put("offset", offset)
            .put("count", count)
            .put("group_id", groupId)
    )

    fun search(
            query: String,
            peerId: Int?,
            maxDate: GMTDate?,
            previewLength: Int,
            offset: Int,
            count: Int
    ): JsonObject =
            search(
                    query = query,
                    peerId = peerId,
                    maxDate = maxDate,
                    previewLength = previewLength,
                    offset = offset,
                    count = count,
                    groupId = null
            )

    fun searchExtended(
            query: String,
            peerId: Int?,
            maxDate: GMTDate?,
            previewLength: Int,
            offset: Int,
            count: Int,
            groupId: Int?,
            fields: List<ObjectField>
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

    fun searchExtended(
            query: String,
            peerId: Int?,
            maxDate: GMTDate?,
            previewLength: Int,
            offset: Int,
            count: Int,
            fields: List<ObjectField>
    ): JsonObject = searchExtended(
            query = query,
            peerId = peerId,
            maxDate = maxDate,
            previewLength = previewLength,
            offset = offset,
            count = count,
            fields = fields,
            groupId = null
    )

    fun searchConversations(
            query: String,
            count: Int,
            extended: Boolean,
            fields: List<ObjectField>,
            groupId: Int?
    ): JsonObject = Methods.searchConversations.callSync(client, JsonObject()
            .put("q", query)
            .put("count", count)
            .put("extended", extended.asInt())
            .put("fields", fields.joinToString(",") { it.value })
            .put("group_id", groupId)
    )

    fun searchConversations(
            query: String,
            count: Int,
            extended: Boolean,
            fields: List<ObjectField>
    ): JsonObject = searchConversations(
            query = query,
            count = count,
            extended = extended,
            fields = fields,
            groupId = null
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
            disableMentions: Boolean? = null
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
            groupId: Int?
    ): JsonObject = Methods.setActivity.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("group_id", groupId)
            .put("type", type)
    )

    fun setActivity(
            peerId: Int,
            type: String
    ): JsonObject = setActivity(
            peerId = peerId,
            type = type,
            groupId = null
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
            groupId: Int?
    ): JsonObject = Methods.unpin.callSync(client, JsonObject()
            .put("peer_id", peerId)
            .put("group_id", groupId)
    )

    fun unpin(
            peerId: Int
    ): JsonObject = unpin(
            peerId = peerId,
            groupId = null
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