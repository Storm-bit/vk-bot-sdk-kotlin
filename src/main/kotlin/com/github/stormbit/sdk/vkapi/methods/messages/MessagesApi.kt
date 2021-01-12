package com.github.stormbit.sdk.vkapi.methods.messages

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.attachments.AttachmentType
import com.github.stormbit.sdk.objects.models.*
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.vkapi.methods.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonObject

@Suppress("unused", "MemberVisibilityCanBePrivate")
class MessagesApi(private val client: Client) : MethodsContext() {
    fun addChatUser(
            chatId: Int,
            userId: Int
    ): Int? = Methods.addChatUser.callSync(
        client, Int.serializer(), parametersOf {
            append("chat_id", chatId)
            append("user_id", userId)
        }
    )

    fun allowMessagesFromGroup(
            groupId: Int,
            key: String? = null
    ): Int? = Methods.allowMessagesFromGroup.callSync(
        client, Int.serializer(), parametersOf {
            append("group_id", groupId)
            append("key", key)
        }
    )

    fun createChat(
            userIds: List<Int>,
            title: String
    ): Int? = Methods.createChat.callSync(
        client, Int.serializer(), parametersOf {
            append("user_ids", userIds.joinToString(","))
            append("title", title)
        }
    )

    fun delete(
            messageIds: List<Int>,
            deleteForAll: Boolean,
            markAsSpam: Boolean = false,
            groupId: Int? = null
    ): Map<Int, Int>? = Methods.delete.callSync(
        client, MapSerializer(Int.serializer(), Int.serializer()), parametersOf {
            append("message_ids", messageIds.joinToString(","))
            append("spam", markAsSpam.toInt())
            append("delete_for_all", deleteForAll.toInt())
            append("group_id", groupId)
        }
    )

    fun deleteChatPhoto(
            chatId: Int,
            groupId: Int? = null
    ): ChatChangePhotoResponse? = Methods.deleteChatPhoto.callSync(
        client, ChatChangePhotoResponse.serializer(), parametersOf {
            append("chat_id", chatId)
            append("group_id", groupId)
        }
    )

    fun deleteChatPhoto(
            chatId: Int
    ): ChatChangePhotoResponse? = Methods.deleteChatPhoto.callSync(
        client, ChatChangePhotoResponse.serializer(), parametersOf {
            append("chat_id", chatId)
        }
    )

    fun deleteConversation(
            peerId: Int,
            groupId: Int? = null
    ): Map<String, Int>? = Methods.deleteConversation.callSync(
        client, MapSerializer(String.serializer(), Int.serializer()), parametersOf {
            append("peer_id", peerId)
            append("group_id", groupId)
        }
    )

    fun deleteConversation(
            peerId: Int
    ): Map<String, Int>? = Methods.deleteConversation.callSync(
        client, MapSerializer(String.serializer(), Int.serializer()), parametersOf {
            append("peer_id", peerId)
        }
    )

    fun denyMessagesFromGroup(
            groupId: Int
    ): Int? = Methods.denyMessagesFromGroup.callSync(
        client, Int.serializer(), parametersOf {
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
            keepForwardMessages: Boolean? = null,
            keepSnippets: Boolean? = null,
            groupId: Int? = null
    ): Int? = Methods.edit.callSync(
        client, Int.serializer(), parametersOf {
            append("peer_id", peerId)
            append("message_id", messageId)
            append("message", message)
            append("lat", latitude)
            append("long", longitude)
            append("attachment", attachments?.joinToString(","))
            append("keep_forward_messages", keepForwardMessages?.toInt())
            append("keep_snippets", keepSnippets?.toInt())
            append("group_id", groupId)
        }
    )

    fun editChat(
            chatId: Int,
            title: String,
            groupId: Int? = null
    ): Int? = Methods.editChat.callSync(
        client, Int.serializer(), parametersOf {
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
            groupId: Int? = null
    ): List<Message>? = Methods.getByConversationMessageId.callSync(
        client, ListSerializer(Message.serializer()), parametersOf {
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
            groupId: Int? = null
    ): ExtendedListResponse<Message>? = Methods.getById.callSync(
        client, ExtendedListResponse.serializer(Message.serializer()), parametersOf {
            append("message_ids", messageIds.joinToString(","))
            append("preview_length", previewLength)
            append("extended", extended?.toInt())
            append("fields", fields?.joinToString(",") { it.value })
            append("group_id", groupId)
        }
    )

    fun getChat(
            chatIds: List<Int>,
            nameCase: NameCase? = null
    ): List<Chat>? = Methods.getChat.callSync(
        client, ListSerializer(Chat.serializer()), parametersOf {
            append("chat_ids", chatIds.joinToString(","))
            append("name_case", nameCase?.value)
        }
    )

    fun getChatTitle(chatId: Int): String = getChat(listOf(chatId))!![0].title

    fun getChatPreview(
            link: String,
            fields: List<ObjectField> = emptyList()
    ): JsonObject? = Methods.getChatPreview.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("link", link)
            append("fields", fields.joinToString(",") { it.value })
        }
    )

    fun getChatUsers(
            chatId: Int
    ): List<Int>? = getChat(listOf(chatId))?.get(0)?.users

    fun getConversationMembers(
            peerId: Int,
            fields: List<ObjectField> = emptyList(),
            groupId: Int? = null
    ): ExtendedListResponse<Member>? = Methods.getConversationMembers.callSync(
        client, ExtendedListResponse.serializer(Member.serializer()), parametersOf {
            append("peer_id", peerId)
            append("fields", fields.joinToString(",") { it.value })
            append("group_id", groupId)
        }
    )

    fun getConversations(
            count: Int = 1,
            offset: Int = 0,
            filter: ConversationFilter = ConversationFilter.ALL,
            startMessageId: Int? = null,
            extended: Boolean = false,
            fields: List<ObjectField> = emptyList(),
            groupId: Int? = null
    ): ConversationsListResponse? = Methods.getConversations.callSync(
        client, ConversationsListResponse.serializer(), parametersOf {
            append("offset", offset)
            append("count", count)
            append("filter", filter.value)
            append("start_message_id", startMessageId)
            append("extended", extended.toInt())
            append("fields", fields.joinToString(",") { it.value })
            append("group_id", groupId)
        }
    )

    fun getConversationsById(
            peerIds: List<Int>,
            extended: Boolean = false,
            fields: List<ObjectField> = emptyList(),
            groupId: Int? = null
    ): ConversationsListResponse? = Methods.getConversationsById.callSync(
        client, ConversationsListResponse.serializer(), parametersOf {
            append("peer_ids", peerIds.joinToString(","))
            append("extended", extended.toInt())
            append("fields", fields.joinToString(",") { it.value })
            append("group_id", groupId)
        }
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
    ): ExtendedListResponse<Message>? = Methods.getHistory.callSync(
        client, ExtendedListResponse.serializer(Message.serializer()), parametersOf {
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

    fun getHistoryAttachments(
        peerId: Int,
        mediaType: AttachmentType,
        startFrom: Int? = null,
        count: Int = 1,
        withPhotoSizes: Boolean = true,
        fields: List<ObjectField> = emptyList(),
        groupId: Int? = null
    ): HistoryAttachmentsResponse? = Methods.getHistoryAttachments.callSync(
        client, HistoryAttachmentsResponse.serializer(), parametersOf {
            append("peer_id", peerId)
            append("media_type", mediaType.value)
            append("start_from", startFrom)
            append("count", count)
            append("photo_sizes", withPhotoSizes.toInt())
            append("fields", fields.joinToString(",") { it.value })
            append("group_id", groupId)
        }
    )

    fun getImportantMessages(
            count: Int = 1,
            offset: Int = 0,
            startMessageId: Int? = null,
            previewLength: Int = 0,
            extended: Boolean = false,
            fields: List<ObjectField> = emptyList(),
            groupId: Int? = null
    ): ExtendedListResponse<Message>? = Methods.getImportantMessages.callSync(
        client, ExtendedListResponse.serializer(Message.serializer()), parametersOf {
            append("count", count)
            append("offset", offset)
            append("start_message_id", startMessageId)
            append("preview_length", previewLength)
            append("extended", extended.toInt())
            append("fields", fields.joinToString(",") { it.value })
            append("group_id", groupId)
        }
    )

    fun getInviteLink(
            peerId: Int,
            generateNewLink: Boolean,
            groupId: Int? = null
    ): LinkResponse? = Methods.getInviteLink.callSync(
        client, LinkResponse.serializer(), parametersOf {
            append("peer_id", peerId)
            append("reset", generateNewLink.toInt())
            append("group_id", groupId)
        }
    )

    fun getLastActivity(
            userId: Int
    ): JsonObject? = Methods.getLastActivity.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            groupId: Int? = null
    ): JsonObject? = Methods.getLongPollHistory.callSync(
        client, JsonObject.serializer(), parametersOf {
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
            peerId: Int
    ): Int = getHistory(peerId, count = 0)!!.count

    fun getLongPollServer(
            needPts: Boolean,
            longPollVersion: Int,
            groupId: Int? = null
    ): LongPollServerResponse? = Methods.getLongPollServer.callSync(
        client, LongPollServerResponse.serializer(), parametersOf {
            append("need_pts", needPts.toInt())
            append("lp_version", longPollVersion)
            append("group_id", groupId)
        }
    )

    fun getRecentCalls(
            count: Int,
            startMessageId: Int? = null,
            fields: List<ObjectField>,
            extended: Boolean
    ): ExtendedListResponse<RecentCall>? = Methods.getRecentCalls.callSync(
        client, ExtendedListResponse.serializer(RecentCall.serializer()), parametersOf {
            append("count", count)
            append("start_message_id", startMessageId)
            append("fields", fields.joinToString(",") { it.value })
            append("extended", extended.toInt())
        }
    )

    fun isMessagesFromGroupAllowed(
            groupId: Int,
            userId: Int
    ): Boolean = Methods.isMessagesFromGroupAllowed.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("group_id", groupId)
            append("user_id", userId)
        }
    )!!.getInt("is_allowed")!!.toBoolean()

    fun joinChatByInviteLink(
            link: String
    ): Int = Methods.joinChatByInviteLink.callSync(
        client, JsonObject.serializer(), parametersOf {
            append("link", link)
        }
    )!!.getInt("chat_id")!!

    fun markAsAnsweredConversation(
            peerId: Int,
            isAnswered: Boolean
    ): Int? = Methods.markAsAnsweredConversation.callSync(
        client, Int.serializer(), parametersOf {
            append("peer_id", peerId)
            append("answered", isAnswered.toInt())
        }
    )

    fun markAsAnsweredConversation(
            peerId: Int,
            isAnswered: Boolean,
            groupId: Int
    ): Int? = Methods.markAsAnsweredConversation.callSync(
        client, Int.serializer(), parametersOf {
            append("peer_id", peerId)
            append("answered", isAnswered.toInt())
            append("group_id", groupId)
        }
    )

    fun markAsImportant(
            messageIds: List<Int>,
            markAsImportant: Boolean
    ): List<Int>? = Methods.markAsImportant.callSync(
        client, ListSerializer(Int.serializer()), parametersOf {
            append("message_ids", messageIds.joinToString(","))
            append("important", markAsImportant.toInt())
        }
    )

    fun markAsImportantConversation(
            peerId: Int,
            isImportant: Boolean
    ): Int? = Methods.markAsImportantConversation.callSync(
        client, Int.serializer(), parametersOf {
            append("peer_id", peerId)
            append("important", isImportant.toInt())
        }
    )

    fun markAsImportantConversation(
            peerId: Int,
            isImportant: Boolean,
            groupId: Int
    ): Int? = Methods.markAsImportantConversation.callSync(
        client, Int.serializer(), parametersOf {
            append("peer_id", peerId)
            append("important", isImportant.toInt())
            append("group_id", groupId)
        }
    )

    fun markAsRead(
            peerId: Int,
            startMessageId: Int? = null,
            groupId: Int? = null
    ): Int? = Methods.markAsRead.callSync(
        client, Int.serializer(), parametersOf {
            append("peer_id", peerId)
            append("start_message_id", startMessageId)
            append("group_id", groupId)
        }
    )

    fun pin(
            peerId: Int,
            messageId: Int,
            groupId: Int? = null
    ): Message? = Methods.pin.callSync(
        client, Message.serializer(), parametersOf {
            append("peer_id", peerId)
            append("message_id", messageId)
            append("group_id", groupId)
        }
    )

    fun removeChatUser(
            chatId: Int,
            memberId: Int,
            groupId: Int? = null
    ): Int? = Methods.removeChatUser.callSync(
        client, Int.serializer(), parametersOf {
            append("chat_id", chatId)
            append("member_id", memberId)
            append("group_id", groupId)
        }
    )

    fun restore(
            messageId: Int,
            groupId: Int? = null
    ): Int? = Methods.restore.callSync(
        client, Int.serializer(), parametersOf {
            append("message_id", messageId)
            append("group_id", groupId)
        }
    )

    fun search(
            query: String,
            peerId: Int? = null,
            maxDate: GMTDate? = null,
            previewLength: Int? = 0,
            count: Int = 1,
            offset: Int = 0,
            groupId: Int? = null
    ): ExtendedListResponse<Message>? = Methods.search.callSync(
        client, ExtendedListResponse.serializer(Message.serializer()), parametersOf {
            append("q", query)
            append("peer_id", peerId)
            append("date", maxDate?.toDMYString())
            append("preview_length", previewLength)
            append("offset", offset)
            append("count", count)
            append("group_id", groupId)
        }
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
    ): ExtendedListResponse<Message>? = Methods.search.callSync(
        client, ExtendedListResponse.serializer(Message.serializer()), parametersOf {
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

    fun searchConversations(
            query: String,
            count: Int,
            extended: Boolean,
            fields: List<ObjectField>,
            groupId: Int? = null
    ): ExtendedListResponse<Conversation>? = Methods.searchConversations.callSync(
        client, ExtendedListResponse.serializer(Conversation.serializer()), parametersOf {
            append("q", query)
            append("count", count)
            append("extended", extended.toInt())
            append("fields", fields.joinToString(",") { it.value })
            append("group_id", groupId)
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
            keyboard: Keyboard? = null,
            payload: MessagePayload? = null,
            dontParseLink: Boolean? = null,
            disableMentions: Boolean? = null,
            groupId: Int? = null
    ): Int? = Methods.send.callSync(
        client, Int.serializer(), parametersOf {
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
            dontParseLink: Boolean? = null
    ): Int? = Methods.send.callSync(
        client, Int.serializer(), parametersOf {
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
            groupId: Int? = null
    ): Int? = Methods.setActivity.callSync(
        client, Int.serializer(), parametersOf {
            append("peer_id", peerId)
            append("group_id", groupId)
            append("type", type)
        }
    )

    fun setChatPhoto(
            file: String,
            groupId: Int? = null
    ): ChatChangePhotoResponse? = Methods.setChatPhoto.callSync(
        client, ChatChangePhotoResponse.serializer(), parametersOf {
            append("file", file)
            append("group_id", groupId)
        }
    )

    fun unpin(
            peerId: Int,
            groupId: Int? = null
    ): Int? = Methods.unpin.callSync(
        client, Int.serializer(), parametersOf {
            append("peer_id", peerId)
            append("group_id", groupId)
        }
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