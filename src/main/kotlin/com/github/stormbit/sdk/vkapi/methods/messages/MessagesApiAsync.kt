package com.github.stormbit.sdk.vkapi.methods.messages

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.Chat.Companion.CHAT_PREFIX
import com.github.stormbit.sdk.objects.models.*
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.objects.attachments.AttachmentType
import com.github.stormbit.sdk.vkapi.methods.GMTDate
import com.github.stormbit.sdk.vkapi.methods.MethodsContext
import com.github.stormbit.sdk.vkapi.methods.ObjectField
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonObject

@Suppress("unused", "MemberVisibilityCanBePrivate")
class MessagesApiAsync(private val client: Client) : MethodsContext() {
    fun addChatUser(
            chatId: Int,
            userId: Int,
            callback: Callback<Int?>?
    ) = Methods.addChatUser.call(
        client, Int.serializer(), callback, parametersOf {
            append("chat_id", chatId)
            append("user_id", userId)
        }
    )

    fun allowMessagesFromGroup(
            groupId: Int,
            key: String? = null,
            callback: Callback<JsonObject?>?
    ) = Methods.allowMessagesFromGroup.call(
        client, JsonObject.serializer(), callback,  parametersOf { 
            append("group_id", groupId)
            append("key", key)
        }
    )

    fun createChat(
            userIds: List<Int>,
            title: String,
            callback: Callback<JsonObject?>?
    ) = Methods.createChat.call(
        client, JsonObject.serializer(), callback, parametersOf { 
            append("user_ids", userIds.joinToString(","))
            append("title", title)
        }
    )

    fun delete(
            messageIds: List<Int>,
            markAsSpam: Boolean,
            deleteForAll: Boolean,
            groupId: Int? = null,
            callback: Callback<JsonObject?>?
    ) = Methods.delete.call(
        client, JsonObject.serializer(), callback, parametersOf { 
            append("message_ids", messageIds.joinToString(","))
            append("spam", markAsSpam.toInt())
            append("group_id", groupId)
            append("delete_for_all", deleteForAll.toInt())
        }
    )

    fun delete(
            messageIds: List<Int>,
            markAsSpam: Boolean,
            deleteForAll: Boolean,
            callback: Callback<JsonObject?>?
    ) = Methods.delete.call(
        client, JsonObject.serializer(), callback, parametersOf { 
            append("message_ids", messageIds.joinToString(","))
            append("spam", markAsSpam.toInt())
            append("delete_for_all", deleteForAll.toInt())
        }
    )


    fun deleteChatPhoto(
            chatId: Int,
            groupId: Int? = null,
            callback: Callback<JsonObject?>?
    ) = Methods.deleteChatPhoto.call(
        client, JsonObject.serializer(), callback, parametersOf { 
            append("chat_id", chatId)
            append("group_id", groupId)
        }
    )


    fun deleteChatPhoto(
            chatId: Int,
            callback: Callback<ChatChangePhotoResponse?>?
    ) = Methods.deleteChatPhoto.call(
        client, ChatChangePhotoResponse.serializer(), callback, parametersOf {
            append("chat_id", chatId)
        }
    )

    fun deleteConversation(
            peerId: Int,
            groupId: Int? = null,
            callback: Callback<JsonObject?>?
    ) = Methods.deleteConversation.call(
        client, JsonObject.serializer(), callback, parametersOf { 
            append("peer_id", peerId)
            append("group_id", groupId)
        }
    )

    fun deleteConversation(
            peerId: Int,
            callback: Callback<JsonObject?>?
    ) = Methods.deleteConversation.call(
        client, JsonObject.serializer(), callback, parametersOf { 
            append("peer_id", peerId)
        }
    )

    fun denyMessagesFromGroup(
            groupId: Int,
            callback: Callback<JsonObject?>?
    ) = Methods.denyMessagesFromGroup.call(
        client, JsonObject.serializer(), callback, parametersOf { 
            append("group_id", groupId)
        }
    )

    fun edit(
            peerId: Int,
            messageId: Int,
            message: String? = null,
            latitude: Int? = null,
            longitude: Int? = null,
            attachments: List<String>? = null,
            keepForwardMessages: Boolean,
            keepSnippets: Boolean,
            groupId: Int? = null,
            callback: Callback<JsonObject?>?
    ) = Methods.edit.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("peer_id", peerId)
            append("message_id", messageId)
            append("message", message)
            append("lat", latitude)
            append("long", longitude)
            append("attachment", attachments?.joinToString(","))
            append("keep_forward_messages", keepForwardMessages.toInt())
            append("keep_snippets", keepSnippets.toInt())
            append("group_id", groupId)
        }
    )

    fun edit(
            peerId: Int,
            messageId: Int,
            message: String? = null,
            latitude: Int? = null,
            longitude: Int? = null,
            attachments: List<String>? = null,
            keepForwardMessages: Boolean,
            keepSnippets: Boolean,
            callback: Callback<JsonObject?>?
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
            groupId: Int? = null,
            callback: Callback<Int?>?
    ) = Methods.editChat.call(
        client, Int.serializer(), callback, parametersOf {
            append("chat_id", chatId)
            append("title", title)
            append("group_id", groupId)
        }
    )

    fun getByConversationMessageId(
            peerId: Int,
            conversationMessageIds: List<Int>? = null,
            extended: Boolean? = null,
            fields: List<ObjectField>? = null,
            groupId: Int? = null,
            callback: Callback<JsonObject?>?
    ) = Methods.getByConversationMessageId.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("peer_id", peerId)
            append("conversation_message_ids", conversationMessageIds?.joinToString(","))
            append("extended", extended?.toInt())
            append("fields", fields?.joinToString(",") { it.value })
            append("group_id", groupId)
        }
    )

    fun getById(
            messageIds: List<Int>,
            previewLength: Int? = null,
            extended: Boolean? = null,
            fields: List<ObjectField>? = null,
            groupId: Int? = null,
            callback: Callback<JsonObject?>?
    ) = Methods.getById.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("message_ids", messageIds.joinToString(","))
            append("preview_length", previewLength)
            append("extended", extended?.toInt())
            append("fields", fields?.joinToString(",") { it.value })
            append("group_id", groupId)
        }
    )

    fun getById(
            messageIds: List<Int>,
            previewLength: Int? = null,
            extended: Boolean? = null,
            fields: List<ObjectField>? = null,
            callback: Callback<JsonObject?>?
    ) = getById(
            messageIds = messageIds,
            previewLength = previewLength,
            extended = extended,
            fields = fields,
            groupId = null,
            callback
    )

    fun getChatTitle(
        chatId: Int
    ): String = Methods.getChat.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("chat_ids", if (chatId.isChatPeerId) chatId - CHAT_PREFIX else chatId)
        }
    )!!.getJsonArray("response")!!.getJsonObject(0).getString("title")!!

    fun getChat(
            chatIds: List<Int>,
            callback: Callback<List<Chat>?>?
    ) = Methods.getChat.call(
        client, ListSerializer(Chat.serializer()), callback, parametersOf {
            append("chat_ids", chatIds.joinToString(","))
        }
    )

    fun getChatPreview(
            link: String,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>?
    ) = Methods.getChatPreview.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("link", link)
            append("fields", fields.joinToString(",") { it.value })
        }
    )

    fun getChatUsers(
            chatId: Int,
            callback: Callback<List<Int>?>?
    ) = getChat(listOf(chatId)) {
        callback?.onResult(it?.get(0)?.users)
    }

    fun getConversationMembers(
            peerId: Int,
            fields: List<ObjectField>,
            groupId: Int? = null,
            callback: Callback<ExtendedListResponse<Member>?>?
    ) = Methods.getConversationMembers.call(
        client, ExtendedListResponse.serializer(Member.serializer()), callback, parametersOf {
            append("peer_id", peerId)
            append("fields", fields.joinToString(",") { it.value })
            append("group_id", groupId)
        }
    )

    fun getConversationMembers(
            peerId: Int,
            fields: List<ObjectField>,
            callback: Callback<ExtendedListResponse<Member>?>?
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
            startMessageId: Int? = null,
            extended: Boolean,
            fields: List<ObjectField>,
            groupId: Int? = null,
            callback: Callback<ConversationsListResponse?>?
    ) = Methods.getConversations.call(
        client, ConversationsListResponse.serializer(), callback, parametersOf {
            append("offset", offset)
            append("count", count)
            append("filter", filter.value)
            append("start_message_id", startMessageId)
            append("extended", extended.toInt())
            append("fields", fields.joinToString(",") { it.value })
            append("group_id", groupId)
        }
    )

    fun getConversations(
            offset: Int,
            count: Int,
            filter: ConversationFilter,
            startMessageId: Int? = null,
            extended: Boolean,
            fields: List<ObjectField>,
            callback: Callback<ConversationsListResponse?>?
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
            groupId: Int? = null,
            callback: Callback<ConversationsListResponse?>?
    ) = Methods.getConversationsById.call(
        client, ConversationsListResponse.serializer(), callback, parametersOf {
            append("peer_ids", peerIds.joinToString(","))
            append("extended", extended.toInt())
            append("fields", fields.joinToString(",") { it.value })
            append("group_id", groupId)
        }
    )

    fun getConversationsById(
            peerIds: List<Int>,
            extended: Boolean,
            fields: List<ObjectField>,
            callback: Callback<ConversationsListResponse?>?
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
            groupId: Int? = null,
            callback: Callback<ExtendedListResponse<Message>?>?
    ) = Methods.getHistory.call(
        client, ExtendedListResponse.serializer(Message.serializer()), callback, parametersOf {
            append("peer_id", peerId)
            append("offset", offset)
            append("count", count)
            append("start_message_id", startMessageId)
            append("rev", reverse.toInt())
            append("extended", extended.toInt())
            append("fields", fields.joinToString(",") { it.value })
            append("group_id", groupId)
        }
    )

    fun getHistory(
            peerId: Int,
            offset: Int = 0,
            count: Int = 1,
            startMessageId: Int? = null,
            reverse: Boolean = false,
            extended: Boolean = false,
            fields: List<ObjectField> = emptyList(),
            callback: Callback<ExtendedListResponse<Message>?>?
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
        startFrom: Int? = null,
        count: Int,
        withPhotoSizes: Boolean,
        fields: List<ObjectField>,
        groupId: Int? = null,
        callback: Callback<JsonObject?>?
    ) = Methods.getHistoryAttachments.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("peer_id", peerId)
            append("media_type", mediaType.value)
            append("start_from", startFrom)
            append("count", count)
            append("photo_sizes", withPhotoSizes.toInt())
            append("fields", fields.joinToString(",") { it.value })
            append("group_id", groupId)
        }
    )

    fun getHistoryAttachments(
        peerId: Int,
        mediaType: AttachmentType,
        startFrom: Int? = null,
        count: Int,
        withPhotoSizes: Boolean,
        fields: List<ObjectField>,
        callback: Callback<JsonObject?>?
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
            startMessageId: Int? = null,
            previewLength: Int,
            extended: Boolean,
            fields: List<ObjectField>,
            groupId: Int? = null,
            callback: Callback<ExtendedListResponse<Message>?>?
    ) = Methods.getImportantMessages.call(
        client, ExtendedListResponse.serializer(Message.serializer()), callback, parametersOf {
            append("count", count)
            append("offset", offset)
            append("start_message_id", startMessageId)
            append("preview_length", previewLength)
            append("extended", extended.toInt())
            append("fields", fields.joinToString(",") { it.value })
            append("group_id", groupId)
        }
    )

    fun getImportantMessages(
            count: Int,
            offset: Int,
            startMessageId: Int? = null,
            previewLength: Int,
            extended: Boolean,
            fields: List<ObjectField>,
            callback: Callback<ExtendedListResponse<Message>?>?
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
            groupId: Int? = null,
            callback: Callback<LinkResponse?>?
    ) = Methods.getInviteLink.call(
        client, LinkResponse.serializer(), callback, parametersOf {
            append("peer_id", peerId)
            append("reset", generateNewLink.toInt())
            append("group_id", groupId)
        }
    )

    fun getInviteLink(
            peerId: Int,
            generateNewLink: Boolean,
            callback: Callback<LinkResponse?>?
    ) = getInviteLink(
            peerId = peerId,
            generateNewLink = generateNewLink,
            groupId = null,
            callback
    )

    fun getLastActivity(
            userId: Int,
            callback: Callback<JsonObject?>?
    ) = Methods.getLastActivity.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("user_id", userId)
        }
    )

    fun getLongPollHistory(
            ts: Long,
            pts: Long,
            previewLength: Int,
            withOnlineStatuses: Boolean,
            userFields: List<ObjectField>,
            eventsLimit: Int,
            messagesLimit: Int,
            maxMessageId: Int? = null,
            longPollVersion: Int,
            groupId: Int? = null,
            callback: Callback<JsonObject?>?
    ) = Methods.getLongPollHistory.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("ts", ts)
            append("pts", pts)
            append("preview_length", previewLength)
            append("onlines", withOnlineStatuses.toInt())
            append("fields", userFields.joinToString(",") { it.value })
            append("events_limit", eventsLimit)
            append("msgs_limit", messagesLimit)
            append("max_msg_id", maxMessageId)
            append("lp_version", longPollVersion)
            append("group_id", groupId)
        }
    )


    fun getMessagesCount(
            peerId: Int,
            callback: Callback<Int?>?
    ) = getHistory(peerId, count = 0, callback = { callback?.onResult(it!!.count) })

    fun getLongPollHistory(
            ts: Long,
            pts: Long,
            previewLength: Int,
            withOnlineStatuses: Boolean,
            userFields: List<ObjectField>,
            eventsLimit: Int,
            messagesLimit: Int,
            maxMessageId: Int? = null,
            longPollVersion: Int,
            callback: Callback<JsonObject?>?
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
            groupId: Int? = null,
            callback: Callback<LongPollServerResponse?>?,
    ) = Methods.getLongPollServer.call(
        client, LongPollServerResponse.serializer(), callback, parametersOf {
            append("need_pts", needPts.toInt())
            append("lp_version", longPollVersion)
            append("group_id", groupId)
        }
    )

    fun getLongPollServer(
            needPts: Boolean,
            longPollVersion: Int,
            callback: Callback<LongPollServerResponse?>?
    ) = getLongPollServer(
            needPts = needPts,
            longPollVersion = longPollVersion,
            groupId = null,
            callback
    )

    fun getRecentCalls(
            count: Int,
            startMessageId: Int? = null,
            fields: List<ObjectField>,
            extended: Boolean,
            callback: Callback<ExtendedListResponse<RecentCall>?>?
    ) = Methods.getRecentCalls.call(
        client, ExtendedListResponse.serializer(RecentCall.serializer()), callback, parametersOf {
            append("count", count)
            append("start_message_id", startMessageId)
            append("fields", fields.joinToString(",") { it.value })
            append("extended", extended.toInt())
        }
    )

    fun isMessagesFromGroupAllowed(
            groupId: Int,
            userId: Int,
            callback: Callback<Boolean?>?
    ) = Methods.isMessagesFromGroupAllowed.call(
        client, Boolean.serializer(), callback, parametersOf {
            append("group_id", groupId)
            append("user_id", userId)
        }
    )

    fun joinChatByInviteLink(
            link: String,
            callback: Callback<Int?>?
    ) = Methods.joinChatByInviteLink.call(
        client, JsonObject.serializer(), callback = { callback?.onResult(it!!.getInt("chat_id")) }, parametersOf {
            append("link", link)
        }
    )

    fun markAsAnsweredConversation(
            peerId: Int,
            isAnswered: Boolean,
            callback: Callback<Int?>?
    ) = Methods.markAsAnsweredConversation.call(
        client, Int.serializer(), callback, parametersOf {
            append("peer_id", peerId)
            append("answered", isAnswered.toInt())
        }
    )

    fun markAsAnsweredConversation(
            peerId: Int,
            isAnswered: Boolean,
            groupId: Int,
            callback: Callback<Int?>?
    ) = Methods.markAsAnsweredConversation.call(
        client, Int.serializer(), callback, parametersOf {
            append("peer_id", peerId)
            append("answered", isAnswered.toInt())
            append("group_id", groupId)
        }
    )

    fun markAsImportant(
            messageIds: List<Int>,
            markAsImportant: Boolean,
            callback: Callback<Int?>?
    ) = Methods.markAsImportant.call(
        client, Int.serializer(), callback, parametersOf {
            append("message_ids", messageIds.joinToString(","))
            append("important", markAsImportant.toInt())
        }
    )

    fun markAsImportantConversation(
            peerId: Int,
            isImportant: Boolean,
            callback: Callback<Int?>?
    ) = Methods.markAsImportantConversation.call(
        client, Int.serializer(), callback, parametersOf {
            append("peer_id", peerId)
            append("important", isImportant.toInt())
        }
    )

    fun markAsImportantConversation(
            peerId: Int,
            isImportant: Boolean,
            groupId: Int,
            callback: Callback<Int?>?
    ) = Methods.markAsImportantConversation.call(
        client, Int.serializer(), callback, parametersOf {
            append("peer_id", peerId)
            append("important", isImportant.toInt())
            append("group_id", groupId)
        }
    )

    fun markAsRead(
            peerId: Int,
            startMessageId: Int? = null,
            groupId: Int? = null,
            callback: Callback<Int?>?
    ) = Methods.markAsRead.call(
        client, Int.serializer(), callback, parametersOf {
            append("peer_id", peerId)
            append("start_message_id", startMessageId)
            append("group_id", groupId)
        }
    )

    fun markAsRead(
            peerId: Int,
            startMessageId: Int? = null,
            callback: Callback<Int?>?
    ) = markAsRead(
            peerId = peerId,
            startMessageId = startMessageId,
            groupId = null,
            callback
    )

    fun pin(
            peerId: Int,
            messageId: Int,
            groupId: Int? = null,
            callback: Callback<Message?>?
    ) = Methods.pin.call(
        client, Message.serializer(), callback, parametersOf {
            append("peer_id", peerId)
            append("message_id", messageId)
            append("group_id", groupId)
        }
    )

    fun pin(
            peerId: Int,
            messageId: Int,
            callback: Callback<Message?>?
    ) = pin(
            peerId = peerId,
            messageId = messageId,
            groupId = null,
            callback
    )

    fun removeChatUser(
            chatId: Int,
            memberId: Int,
            groupId: Int? = null,
            callback: Callback<Int?>?
    ) = Methods.removeChatUser.call(
        client, Int.serializer(), callback, parametersOf {
            append("chat_id", chatId)
            append("member_id", memberId)
            append("group_id", groupId)
        }
    )

    fun removeChatUser(
            chatId: Int,
            memberId: Int,
            callback: Callback<Int?>?
    ) = removeChatUser(
            chatId = chatId,
            memberId = memberId,
            groupId = null,
            callback
    )

    fun restore(
            messageId: Int,
            groupId: Int? = null,
            callback: Callback<JsonObject?>?
    ) = Methods.restore.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("message_id", messageId)
            append("group_id", groupId)
        }
    )

    fun restore(
            messageId: Int,
            callback: Callback<JsonObject?>?
    ) = restore(
            messageId = messageId,
            groupId = null,
            callback
    )

    fun search(
            query: String,
            peerId: Int? = null,
            maxDate: GMTDate? = null,
            previewLength: Int,
            offset: Int,
            count: Int,
            groupId: Int? = null,
            callback: Callback<JsonObject?>?
    ) = Methods.search.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("q", query)
            append("peer_id", peerId)
            append("date", maxDate?.toDMYString())
            append("preview_length", previewLength)
            append("offset", offset)
            append("count", count)
            append("group_id", groupId)
        }
    )

    fun search(
            query: String,
            peerId: Int? = null,
            maxDate: GMTDate? = null,
            previewLength: Int,
            offset: Int,
            count: Int,
            callback: Callback<JsonObject?>?
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
            peerId: Int? = null,
            maxDate: GMTDate? = null,
            previewLength: Int,
            offset: Int,
            count: Int,
            groupId: Int? = null,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>?
    ) = Methods.search.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("q", query)
            append("peer_id", peerId)
            append("date", maxDate?.toDMYString())
            append("preview_length", previewLength)
            append("offset", offset)
            append("count", count)
            append("extended", 1)
            append("fields", fields.joinToString(",") { it.value })
            append("group_id", groupId)
        }
    )

    fun searchExtended(
            query: String,
            peerId: Int? = null,
            maxDate: GMTDate? = null,
            previewLength: Int,
            offset: Int,
            count: Int,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>?
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
            groupId: Int? = null,
            callback: Callback<JsonObject?>?
    ) = Methods.searchConversations.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("q", query)
            append("count", count)
            append("extended", extended.toInt())
            append("fields", fields.joinToString(",") { it.value })
            append("group_id", groupId)
        }
    )

    fun searchConversations(
            query: String,
            count: Int,
            extended: Boolean,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>?
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
            payload: MessagePayload? = null,
            dontParseLink: Boolean? = null,
            disableMentions: Boolean? = null,
            callback: Callback<Int?>?
    ) = Methods.send.call(
        client, Int.serializer(), callback, parametersOf {
            append("peer_id", peerId)
            append("random_id", randomId)
            append("message", text)
            append("lat", latitude)
            append("long", longitude)
            append("attachment", attachments?.joinToString(","))
            append("reply_to", replyToMessageId)
            append("forward_messages", forwardedMessages?.joinToString(","))
            append("sticker_id", stickerId)
            append("keyboard", keyboard?.serialize())
            append("payload", payload?.value)
            append("dont_parse_links", dontParseLink?.toInt())
            append("disable_mentions", disableMentions?.toInt())
        }
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
            callback: Callback<Int?>?
    ) = Methods.send.call(
        client, Int.serializer(), callback, parametersOf {
            append("peer_id", peerId)
            append("random_id", randomId)
            append("message", text)
            append("lat", latitude)
            append("long", longitude)
            append("attachment", attachments?.joinToString(","))
            append("reply_to", replyToMessageId)
            append("forward_messages", forwardedMessages?.joinToString(","))
            append("sticker_id", stickerId)
            append("dont_parse_links", dontParseLink?.toInt())
            append("disable_mentions", disableMentions?.toInt())
            append("group_id", groupId)
        }
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
            payload: MessagePayload? = null,
            dontParseLink: Boolean? = null,
            callback: Callback<Int?>?
    ) = Methods.send.call(
        client, Int.serializer(), callback, parametersOf {
            append("user_ids", userIds.joinToString(","))
            append("random_id", randomId)
            append("message", text)
            append("lat", latitude)
            append("long", longitude)
            append("attachment", attachments?.joinToString(","))
            append("forward_messages", forwardedMessages?.joinToString(","))
            append("sticker_id", stickerId)
            append("keyboard", keyboard?.serialize())
            append("payload", payload?.value)
            append("dont_parse_links", dontParseLink?.toInt())
        }
    )

    fun setActivity(
            peerId: Int,
            type: String,
            groupId: Int? = null,
            callback: Callback<JsonObject?>?
    ) = Methods.setActivity.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("peer_id", peerId)
            append("group_id", groupId)
            append("type", type)
        }
    )

    fun setActivity(
            peerId: Int,
            type: String,
            callback: Callback<JsonObject?>?
    ) = setActivity(
            peerId = peerId,
            type = type,
            groupId = null,
            callback
    )

    fun setChatPhoto(
            file: String,
            groupId: Int? = null,
            callback: Callback<JsonObject?>?
    ) = Methods.setChatPhoto.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("file", file)
            append("group_id", groupId)
        }
    )

    fun unpin(
            peerId: Int,
            groupId: Int? = null,
            callback: Callback<JsonObject?>?
    ) = Methods.unpin.call(
        client, JsonObject.serializer(), callback, parametersOf {
            append("peer_id", peerId)
            append("group_id", groupId)
        }
    )

    fun unpin(
            peerId: Int,
            callback: Callback<JsonObject?>?
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