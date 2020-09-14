package com.github.stormbit.sdk.utils.vkapi.methods.messages

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.call
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
class MessagesApiAsync(private val client: Client) {
    fun addChatUser(
            chatId: Int,
            userId: Int,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.addChatUser, callback, "chat_id", chatId, "user_id", userId)

    fun allowMessagesFromGroup(
            groupId: Int,
            key: String?,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.allowMessagesFromGroup, callback,  "group_id", groupId, "key", key)

    fun createChat(
            userIds: List<Int>,
            title: String,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.createChat, callback, "user_ids", userIds.joinToString(","), "title", title)

    fun delete(
            messageIds: List<Int>,
            markAsSpam: Boolean,
            deleteForAll: Boolean,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.delete, callback, "message_ids", messageIds.joinToString(","), "spam", markAsSpam.asInt(), "delete_for_all", deleteForAll.asInt(), "group_id", groupId)

    fun delete(
            messageIds: List<Int>,
            markAsSpam: Boolean,
            deleteForAll: Boolean,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.delete, callback, "message_ids", messageIds.joinToString(","), "spam", markAsSpam.asInt(), "delete_for_all", deleteForAll.asInt())


    fun deleteChatPhoto(
            chatId: Int,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.deleteChatPhoto, callback, "chat_id", chatId, "group_id", groupId)


    fun deleteChatPhoto(
            chatId: Int,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.deleteChatPhoto, callback, "chat_id", chatId)

    fun deleteConversation(
            peerId: Int,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.deleteConversation, callback, "peer_id", peerId, "group_id", groupId)

    fun deleteConversation(
            peerId: Int,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.deleteConversation, callback, "peer_id", peerId)

    fun denyMessagesFromGroup(
            groupId: Int,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.denyMessagesFromGroup, callback, "group_id", groupId)

    fun edit(
            peerId: Int,
            messageId: Int,
            message: String?,
            latitude: Int?,
            longitude: Int?,
            attachments: List<String>?,
            keepForwardMessages: Boolean,
            keepSnippets: Boolean,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.edit, callback,
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
            keepSnippets: Boolean,
            callback: Callback<JsonObject?>
    ) = edit(
            peerId = peerId,
            messageId = messageId,
            message = message,
            latitude = latitude,
            longitude = longitude,
            attachments = attachments,
            keepForwardMessages = keepForwardMessages,
            keepSnippets = keepSnippets,
            groupId = null,
            callback
    )

    fun editChat(
            chatId: Int,
            title: String,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.editChat, callback,
            JsonObject()
                    .put("chat_id", chatId)
                    .put("title", title)
                    .put("group_id", groupId)
    )

    fun editChat(
            chatId: Int,
            title: String,
            callback: Callback<JsonObject?>
    ) = editChat(
            chatId = chatId,
            title = title,
            groupId = null,
            callback
    )

    fun getByConversationMessageId(
            peerId: Int,
            conversationMessageIds: List<Int>? = null,
            extended: Boolean? = null,
            fields: List<ObjectField>? = null,
            groupId: Int? = null,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.getByConversationMessageId, callback,
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
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.getById, callback,
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
            fields: List<ObjectField>? = null,
            callback: Callback<JsonObject?>
    ) = getById(
            messageIds = messageIds,
            previewLength = previewLength,
            extended = extended,
            fields = fields,
            groupId = null,
            callback
    )

    fun getChat(
            chatIds: List<Int>,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.getChat, callback,
            JsonObject().put("chat_ids", chatIds.joinToString(","))
    )

    fun getChatPreview(
            link: String,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.getChatPreview, callback,
            JsonObject()
                    .put("link", link)
                    .put("fields", fields.joinToString(",") { it.value })
    )

    fun getChatUsers(
            chatId: Int,
            callback: Callback<List<Int>>
    ) = getChat(listOf(chatId), callback = {callback.onResult(it!!.getAsJsonArray("users").toList() as List<Int>)})

    fun getConversationMembers(
            peerId: Int,
            fields: List<ObjectField>,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.getConversationMembers, callback,
            JsonObject()
                    .put("peer_id", peerId)
                    .put("fields", fields.joinToString(",") { it.value })
                    .put("group_id", groupId)
    )

    fun getConversationMembers(
            peerId: Int,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = getConversationMembers(
            peerId = peerId,
            fields = fields,
            groupId = null,
            callback
    )

    fun getConversations(
            offset: Int,
            count: Int,
            filter: ConversationFilter,
            startMessageId: Int?,
            extended: Boolean,
            fields: List<ObjectField>,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = client.api.call(Methods.getConversations, callback,
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
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = getConversations(
            offset = offset,
            count = count,
            filter = filter,
            startMessageId = startMessageId,
            extended = extended,
            fields = fields,
            groupId = null,
            callback
    )

    fun getConversationsById(
            peerIds: List<Int>,
            extended: Boolean,
            fields: List<ObjectField>,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.getConversationsById.call(client, callback,
            JsonObject()
                    .put("peer_ids", peerIds.joinToString(","))
                    .put("extended", extended.asInt())
                    .put("fields", fields.joinToString(",") { it.value })
                    .put("group_id", groupId)
    )

    fun getConversationsById(
            peerIds: List<Int>,
            extended: Boolean,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = getConversationsById(
            peerIds = peerIds,
            extended = extended,
            fields = fields,
            groupId = null,
            callback
    )

    fun getHistory(
            peerId: Int,
            offset: Int,
            count: Int,
            startMessageId: Int? = null,
            reverse: Boolean,
            extended: Boolean,
            fields: List<ObjectField>,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.getHistory.call(client, callback, JsonObject()
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
            fields: List<ObjectField> = ArrayList(),
            callback: Callback<JsonObject?>
    ) = getHistory(
            peerId = peerId,
            offset = offset,
            count = count,
            startMessageId = startMessageId,
            reverse = reverse,
            extended = extended,
            fields = fields,
            groupId = null,
            callback
    )

    fun getHistoryAttachments(
            peerId: Int,
            mediaType: AttachmentType,
            startFrom: Int?,
            count: Int,
            withPhotoSizes: Boolean,
            fields: List<ObjectField>,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.getHistoryAttachments.call(client, callback, JsonObject()
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
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = getHistoryAttachments(
            peerId = peerId,
            mediaType = mediaType,
            startFrom = startFrom,
            count = count,
            withPhotoSizes = withPhotoSizes,
            fields = fields,
            groupId = null,
            callback
    )

    fun getImportantMessages(
            count: Int,
            offset: Int,
            startMessageId: Int?,
            previewLength: Int,
            extended: Boolean,
            fields: List<ObjectField>,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.getImportantMessages.call(client, callback, JsonObject()
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
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = getImportantMessages(
            count = count,
            offset = offset,
            startMessageId = startMessageId,
            previewLength = previewLength,
            extended = extended,
            fields = fields,
            groupId = null,
            callback
    )

    fun getInviteLink(
            peerId: Int,
            generateNewLink: Boolean,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.getInviteLink.call(client, callback, JsonObject()
            .put("peer_id", peerId)
            .put("reset", generateNewLink.asInt())
            .put("group_id", groupId)
    )

    fun getInviteLink(
            peerId: Int,
            generateNewLink: Boolean,
            callback: Callback<JsonObject?>
    ) = getInviteLink(
            peerId = peerId,
            generateNewLink = generateNewLink,
            groupId = null,
            callback
    )

    fun getLastActivity(
            userId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getLastActivity.call(client, callback, JsonObject().put("user_id", userId))

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
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.getLongPollHistory.call(client, callback, JsonObject()
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
            peerId: Int,
            callback: Callback<Int?>
    ) = getHistory(peerId, count = 0, callback = { callback.onResult(it!!.getAsJsonObject("response").getInt("count")) })

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
            callback: Callback<JsonObject?>
    ) = getLongPollHistory(
            ts = ts,
            pts = pts,
            previewLength = previewLength,
            withOnlineStatuses = withOnlineStatuses,
            userFields = userFields,
            eventsLimit = eventsLimit,
            messagesLimit = messagesLimit,
            maxMessageId = maxMessageId,
            longPollVersion = longPollVersion,
            groupId = null,
            callback
    )

    fun getLongPollServer(
            needPts: Boolean,
            longPollVersion: Int,
            groupId: Int?,
            callback: Callback<JsonObject?>,
    ) = Methods.getLongPollServer.call(client, callback, JsonObject()
            .put("need_pts", needPts.asInt())
            .put("lp_version", longPollVersion)
            .put("group_id", groupId)
    )

    fun getLongPollServer(
            needPts: Boolean,
            longPollVersion: Int,
            callback: Callback<JsonObject?>
    ) = getLongPollServer(
            needPts = needPts,
            longPollVersion = longPollVersion,
            groupId = null,
            callback
    )

    fun getRecentCalls(
            count: Int,
            startMessageId: Int?,
            fields: List<ObjectField>,
            extended: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getRecentCalls.call(client, callback, JsonObject()
            .put("count", count)
            .put("start_message_id", startMessageId)
            .put("fields", fields.joinToString(",") { it.value })
            .put("extended", extended.asInt())
    )

    fun isMessagesFromGroupAllowed(
            groupId: Int,
            userId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.isMessagesFromGroupAllowed.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("user_id", userId)
    )

    fun joinChatByInviteLink(
            link: String,
            callback: Callback<JsonObject?>
    ) = Methods.joinChatByInviteLink.call(client, callback, JsonObject().put("link", link))

    fun markAsAnsweredConversation(
            peerId: Int,
            isAnswered: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.markAsAnsweredConversation.call(client, callback, JsonObject()
            .put("peer_id", peerId)
            .put("answered", isAnswered.asInt())
    )

    fun markAsAnsweredConversation(
            peerId: Int,
            isAnswered: Boolean,
            groupId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.markAsAnsweredConversation.call(client, callback, JsonObject()
            .put("peer_id", peerId)
            .put("answered", isAnswered.asInt())
            .put("group_id", groupId)
    )

    fun markAsImportant(
            messageIds: List<Int>,
            markAsImportant: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.markAsImportant.call(client, callback, JsonObject()
            .put("message_ids", messageIds.joinToString(","))
            .put("important", markAsImportant.asInt())
    )

    fun markAsImportantConversation(
            peerId: Int,
            isImportant: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.markAsImportantConversation.call(client, callback, JsonObject()
            .put("peer_id", peerId)
            .put("important", isImportant.asInt())
    )

    fun markAsImportantConversation(
            peerId: Int,
            isImportant: Boolean,
            groupId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.markAsImportantConversation.call(client, callback, JsonObject()
            .put("peer_id", peerId)
            .put("important", isImportant.asInt())
            .put("group_id", groupId)
    )

    fun markAsRead(
            peerId: Int,
            startMessageId: Int?,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.markAsRead.call(client, callback, JsonObject()
            .put("peer_id", peerId)
            .put("start_message_id", startMessageId)
            .put("group_id", groupId)
    )

    fun markAsRead(
            peerId: Int,
            startMessageId: Int?,
            callback: Callback<JsonObject?>
    ) = markAsRead(
            peerId = peerId,
            startMessageId = startMessageId,
            groupId = null,
            callback
    )

    fun pin(
            peerId: Int,
            messageId: Int,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.pin.call(client, callback, JsonObject()
            .put("peer_id", peerId)
            .put("message_id", messageId)
            .put("group_id", groupId)
    )

    fun pin(
            peerId: Int,
            messageId: Int,
            callback: Callback<JsonObject?>
    ) = pin(
            peerId = peerId,
            messageId = messageId,
            groupId = null,
            callback
    )

    fun removeChatUser(
            chatId: Int,
            memberId: Int,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.removeChatUser.call(client, callback, JsonObject()
            .put("chat_id", chatId)
            .put("member_id", memberId)
            .put("group_id", groupId)
    )

    fun removeChatUser(
            chatId: Int,
            memberId: Int,
            callback: Callback<JsonObject?>
    ) = removeChatUser(
            chatId = chatId,
            memberId = memberId,
            groupId = null,
            callback
    )

    fun restore(
            messageId: Int,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.restore.call(client, callback, JsonObject()
            .put("message_id", messageId)
            .put("group_id", groupId)
    )

    fun restore(
            messageId: Int,
            callback: Callback<JsonObject?>
    ) = restore(
            messageId = messageId,
            groupId = null,
            callback
    )

    fun search(
            query: String,
            peerId: Int?,
            maxDate: GMTDate?,
            previewLength: Int,
            offset: Int,
            count: Int,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.search.call(client, callback, JsonObject()
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
            count: Int,
            callback: Callback<JsonObject?>
    ) = search(
            query = query,
            peerId = peerId,
            maxDate = maxDate,
            previewLength = previewLength,
            offset = offset,
            count = count,
            groupId = null,
            callback
    )

    fun searchExtended(
            query: String,
            peerId: Int?,
            maxDate: GMTDate?,
            previewLength: Int,
            offset: Int,
            count: Int,
            groupId: Int?,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = Methods.search.call(client, callback, JsonObject()
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
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = searchExtended(
            query = query,
            peerId = peerId,
            maxDate = maxDate,
            previewLength = previewLength,
            offset = offset,
            count = count,
            fields = fields,
            groupId = null,
            callback = callback
    )

    fun searchConversations(
            query: String,
            count: Int,
            extended: Boolean,
            fields: List<ObjectField>,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.searchConversations.call(client, callback, JsonObject()
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
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = searchConversations(
            query = query,
            count = count,
            extended = extended,
            fields = fields,
            groupId = null,
            callback
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
            callback: Callback<JsonObject?>
    ) = Methods.send.call(client, callback, JsonObject()
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
            groupId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.send.call(client, callback, JsonObject()
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
            dontParseLink: Boolean? = null,
            callback: Callback<JsonObject?>
    ) = Methods.send.call(client, callback, JsonObject()
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
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.setActivity.call(client, callback, JsonObject()
            .put("peer_id", peerId)
            .put("group_id", groupId)
            .put("type", type)
    )

    fun setActivity(
            peerId: Int,
            type: String,
            callback: Callback<JsonObject?>
    ) = setActivity(
            peerId = peerId,
            type = type,
            groupId = null,
            callback
    )

    fun setChatPhoto(
            file: String,
            groupId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.setChatPhoto.call(client, callback, JsonObject()
            .put("file", file)
            .put("group_id", groupId)
    )

    fun unpin(
            peerId: Int,
            groupId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.unpin.call(client, callback, JsonObject()
            .put("peer_id", peerId)
            .put("group_id", groupId)
    )

    fun unpin(
            peerId: Int,
            callback: Callback<JsonObject?>
    ) = unpin(
            peerId = peerId,
            groupId = null,
            callback
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