package com.github.stormbit.sdk.utils.vkapi.methods.messages

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.callSync
import com.github.stormbit.sdk.utils.Utils.Companion.toDMYString
import com.github.stormbit.sdk.utils.vkapi.keyboard.Keyboard
import com.github.stormbit.sdk.utils.vkapi.methods.AttachmentType
import com.github.stormbit.sdk.utils.vkapi.methods.ObjectField
import io.ktor.util.date.*
import org.json.JSONObject

@Suppress("unused", "MemberVisibilityCanBePrivate")
class MessagesApi(private val client: Client) {
    fun addChatUser(
            chatId: Int,
            userId: Int
    ): JSONObject = client.api.callSync(Methods.addChatUser, "chat_id", chatId, "user_id", userId)

    fun allowMessagesFromGroup(
            groupId: Int,
            key: String?
    ): JSONObject = client.api.callSync(Methods.allowMessagesFromGroup, "group_id", groupId, "key", key)

    fun createChat(
            userIds: List<Int>,
            title: String
    ): JSONObject = client.api.callSync(Methods.createChat, "user_ids", userIds.joinToString(","), "title", title)

    fun delete(
            messageIds: List<Int>,
            markAsSpam: Boolean,
            deleteForAll: Boolean,
            groupId: Int?
    ): JSONObject = client.api.callSync(Methods.delete, "message_ids", messageIds.joinToString(","), "spam", markAsSpam.asInt(), "delete_for_all", deleteForAll.asInt(), "group_id", groupId)

    fun delete(
            messageIds: List<Int>,
            markAsSpam: Boolean,
            deleteForAll: Boolean
    ): JSONObject = client.api.callSync(Methods.delete, "message_ids", messageIds.joinToString(","), "spam", markAsSpam.asInt(), "delete_for_all", deleteForAll.asInt())


    fun deleteChatPhoto(
            chatId: Int,
            groupId: Int?
    ): JSONObject = client.api.callSync(Methods.deleteChatPhoto, "chat_id", chatId, "group_id", groupId)


    fun deleteChatPhoto(
            chatId: Int
    ): JSONObject = client.api.callSync(Methods.deleteChatPhoto, "chat_id", chatId)

    fun deleteConversation(
            peerId: Int,
            groupId: Int?
    ): JSONObject = client.api.callSync(Methods.deleteConversation, "peer_id", peerId, "group_id", groupId)

    fun deleteConversation(
            peerId: Int
    ): JSONObject = client.api.callSync(Methods.deleteConversation, "peer_id", peerId)

    fun denyMessagesFromGroup(
            groupId: Int
    ): JSONObject = client.api.callSync(Methods.denyMessagesFromGroup, "group_id", groupId)

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
    ): JSONObject = client.api.callSync(Methods.edit,
            JSONObject()
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
    ): JSONObject = edit(
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
    ): JSONObject = client.api.callSync(Methods.editChat,
            JSONObject()
                    .put("chat_id", chatId)
                    .put("title", title)
                    .put("group_id", groupId)
    )

    fun editChat(
            chatId: Int,
            title: String
    ): JSONObject = editChat(
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
    ): JSONObject = client.api.callSync(Methods.getByConversationMessageId,
            JSONObject()
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
    ): JSONObject = client.api.callSync(Methods.getById,
            JSONObject()
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
    ): JSONObject = getById(
            messageIds = messageIds,
            previewLength = previewLength,
            extended = extended,
            fields = fields,
            groupId = null
    )

    fun getChat(
            chatIds: List<Int>
    ): JSONObject = client.api.callSync(Methods.getChat,
            JSONObject().put("chat_ids", chatIds.joinToString(","))
    )

    fun getChatPreview(
            link: String,
            fields: List<ObjectField>
    ): JSONObject = client.api.callSync(Methods.getChatPreview,
            JSONObject()
                    .put("link", link)
                    .put("fields", fields.joinToString(",") { it.value })
    )

    fun getChatUsers(
            chatId: Int
    ): List<Int> = getChat(listOf(chatId)).getJSONArray("users").toList() as List<Int>

    fun getConversationMembers(
            peerId: Int,
            fields: List<ObjectField>,
            groupId: Int?
    ): JSONObject = client.api.callSync(Methods.getConversationMembers,
            JSONObject()
                    .put("peer_id", peerId)
                    .put("fields", fields.joinToString(",") { it.value })
                    .put("group_id", groupId)
    )

    fun getConversationMembers(
            peerId: Int,
            fields: List<ObjectField>
    ): JSONObject =
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
    ): JSONObject = client.api.callSync(Methods.getConversations,
            JSONObject()
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
    ): JSONObject =
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
    ): JSONObject = Methods.getConversationsById.callSync(client,
            JSONObject()
                    .put("peer_ids", peerIds.joinToString(","))
                    .put("extended", extended.asInt())
                    .put("fields", fields.joinToString(",") { it.value })
                    .put("group_id", groupId)
    )

    fun getConversationsById(
            peerIds: List<Int>,
            extended: Boolean,
            fields: List<ObjectField>
    ): JSONObject =
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
    ): JSONObject = Methods.getHistory.callSync(client, JSONObject()
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
    ): JSONObject =
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
    ): JSONObject =
            Methods.getHistoryAttachments.callSync(client, JSONObject()
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
    ): JSONObject =
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
    ): JSONObject = Methods.getImportantMessages.callSync(client, JSONObject()
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
    ): JSONObject =
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
    ): JSONObject = Methods.getInviteLink.callSync(client, JSONObject()
            .put("peer_id", peerId)
            .put("reset", generateNewLink.asInt())
            .put("group_id", groupId)
    )

    fun getInviteLink(
            peerId: Int,
            generateNewLink: Boolean
    ): JSONObject =
            getInviteLink(
                    peerId = peerId,
                    generateNewLink = generateNewLink,
                    groupId = null
            )

    fun getLastActivity(
            userId: Int
    ): JSONObject = Methods.getLastActivity.callSync(client, JSONObject().put("user_id", userId))

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
    ): JSONObject = Methods.getLongPollHistory.callSync(client, JSONObject()
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
    ): Int = getHistory(peerId, count = 0).getJSONObject("response").getInt("count")

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
    ): JSONObject =
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
    ): JSONObject = Methods.getLongPollServer.callSync(client, JSONObject()
            .put("need_pts", needPts.asInt())
            .put("lp_version", longPollVersion)
            .put("group_id", groupId)
    )

    fun getLongPollServer(
            needPts: Boolean,
            longPollVersion: Int
    ): JSONObject = getLongPollServer(
            needPts = needPts,
            longPollVersion = longPollVersion,
            groupId = null
    )

    fun getRecentCalls(
            count: Int,
            startMessageId: Int?,
            fields: List<ObjectField>,
            extended: Boolean
    ): JSONObject = Methods.getRecentCalls.callSync(client, JSONObject()
            .put("count", count)
            .put("start_message_id", startMessageId)
            .put("fields", fields.joinToString(",") { it.value })
            .put("extended", extended.asInt())
    )

    fun isMessagesFromGroupAllowed(
            groupId: Int,
            userId: Int
    ): JSONObject = Methods.isMessagesFromGroupAllowed.callSync(client, JSONObject()
            .put("group_id", groupId)
            .put("user_id", userId)
    )

    fun joinChatByInviteLink(
            link: String
    ): JSONObject = Methods.joinChatByInviteLink.callSync(client, JSONObject().put("link", link))

    fun markAsAnsweredConversation(
            peerId: Int,
            isAnswered: Boolean
    ): JSONObject = Methods.markAsAnsweredConversation.callSync(client, JSONObject()
            .put("peer_id", peerId)
            .put("answered", isAnswered.asInt())
    )

    fun markAsAnsweredConversation(
            peerId: Int,
            isAnswered: Boolean,
            groupId: Int
    ): JSONObject = Methods.markAsAnsweredConversation.callSync(client, JSONObject()
            .put("peer_id", peerId)
            .put("answered", isAnswered.asInt())
            .put("group_id", groupId)
    )

    fun markAsImportant(
            messageIds: List<Int>,
            markAsImportant: Boolean
    ): JSONObject = Methods.markAsImportant.callSync(client, JSONObject()
            .put("message_ids", messageIds.joinToString(","))
            .put("important", markAsImportant.asInt())
    )

    fun markAsImportantConversation(
            peerId: Int,
            isImportant: Boolean
    ): JSONObject = Methods.markAsImportantConversation.callSync(client, JSONObject()
            .put("peer_id", peerId)
            .put("important", isImportant.asInt())
    )

    fun markAsImportantConversation(
            peerId: Int,
            isImportant: Boolean,
            groupId: Int
    ): JSONObject = Methods.markAsImportantConversation.callSync(client, JSONObject()
            .put("peer_id", peerId)
            .put("important", isImportant.asInt())
            .put("group_id", groupId)
    )

    fun markAsRead(
            peerId: Int,
            startMessageId: Int?,
            groupId: Int?
    ): JSONObject = Methods.markAsRead.callSync(client, JSONObject()
            .put("peer_id", peerId)
            .put("start_message_id", startMessageId)
            .put("group_id", groupId)
    )

    fun markAsRead(
            peerId: Int,
            startMessageId: Int?
    ): JSONObject = markAsRead(
            peerId = peerId,
            startMessageId = startMessageId,
            groupId = null
    )

    fun pin(
            peerId: Int,
            messageId: Int,
            groupId: Int?
    ): JSONObject = Methods.pin.callSync(client, JSONObject()
            .put("peer_id", peerId)
            .put("message_id", messageId)
            .put("group_id", groupId)
    )

    fun pin(
            peerId: Int,
            messageId: Int
    ): JSONObject = pin(
            peerId = peerId,
            messageId = messageId,
            groupId = null
    )

    fun removeChatUser(
            chatId: Int,
            memberId: Int,
            groupId: Int?
    ): JSONObject = Methods.removeChatUser.callSync(client, JSONObject()
            .put("chat_id", chatId)
            .put("member_id", memberId)
            .put("group_id", groupId)
    )

    fun removeChatUser(
            chatId: Int,
            memberId: Int
    ): JSONObject = removeChatUser(
            chatId = chatId,
            memberId = memberId,
            groupId = null
    )

    fun restore(
            messageId: Int,
            groupId: Int?
    ): JSONObject = Methods.restore.callSync(client, JSONObject()
            .put("message_id", messageId)
            .put("group_id", groupId)
    )

    fun restore(
            messageId: Int
    ): JSONObject = restore(
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
    ): JSONObject = Methods.search.callSync(client, JSONObject()
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
    ): JSONObject =
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
    ): JSONObject = Methods.search.callSync(client, JSONObject()
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
    ): JSONObject = searchExtended(
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
    ): JSONObject = Methods.searchConversations.callSync(client, JSONObject()
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
    ): JSONObject = searchConversations(
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
    ): JSONObject = Methods.send.callSync(client, JSONObject()
            .put("peer_id", peerId)
            .put("random_id", randomId)
            .put("message", text)
            .put("lat", latitude)
            .put("long", longitude)
            .put("attachment", attachments?.joinToString(","))
            .put("reply_to", replyToMessageId)
            .put("forward_messages", forwardedMessages?.joinToString(","))
            .put("sticker_id", stickerId)
            .put("keyboard", if (keyboard != null) JSONObject(keyboard) else null)
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
    ): JSONObject = Methods.send.callSync(client, JSONObject()
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
    ): JSONObject = Methods.send.callSync(client, JSONObject()
            .put("user_ids", userIds.joinToString(","))
            .put("random_id", randomId)
            .put("message", text)
            .put("lat", latitude)
            .put("long", longitude)
            .put("attachment", attachments?.joinToString(","))
            .put("forward_messages", forwardedMessages?.joinToString(","))
            .put("sticker_id", stickerId)
            .put("keyboard", JSONObject(keyboard))
            .put("payload", payload)
            .put("dont_parse_links", dontParseLink?.asInt())
    )

    fun setActivity(
            peerId: Int,
            type: String,
            groupId: Int?
    ): JSONObject = Methods.setActivity.callSync(client, JSONObject()
            .put("peer_id", peerId)
            .put("group_id", groupId)
            .put("type", type)
    )

    fun setActivity(
            peerId: Int,
            type: String
    ): JSONObject = setActivity(
            peerId = peerId,
            type = type,
            groupId = null
    )

    fun setChatPhoto(
            file: String,
            groupId: Int? = null
    ): JSONObject = Methods.setChatPhoto.callSync(client, JSONObject()
            .put("file", file)
            .put("group_id", groupId)
    )

    fun unpin(
            peerId: Int,
            groupId: Int?
    ): JSONObject = Methods.unpin.callSync(client, JSONObject()
            .put("peer_id", peerId)
            .put("group_id", groupId)
    )

    fun unpin(
            peerId: Int
    ): JSONObject = unpin(
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