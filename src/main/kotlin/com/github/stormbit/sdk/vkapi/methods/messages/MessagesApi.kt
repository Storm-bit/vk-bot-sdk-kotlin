package com.github.stormbit.sdk.vkapi.methods.messages

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.attachments.AttachmentType
import com.github.stormbit.sdk.objects.models.*
import com.github.stormbit.sdk.objects.models.enums.ConversationFilter
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.utils.append
import com.github.stormbit.sdk.vkapi.VkApiRequest
import com.github.stormbit.sdk.vkapi.execute
import com.github.stormbit.sdk.vkapi.methods.MethodsContext
import com.github.stormbit.sdk.vkapi.methods.NameCase
import com.github.stormbit.sdk.vkapi.methods.ObjectField
import io.ktor.util.date.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonObject

@Suppress("unused", "MemberVisibilityCanBePrivate")
class MessagesApi(client: Client) : MethodsContext(client) {
    fun addChatUser(
        chatId: Int,
        userId: Int
    ): VkApiRequest<Int> = Methods.addChatUser.call(Int.serializer()) {
        append("chat_id", chatId)
        append("user_id", userId)
    }


    fun allowMessagesFromGroup(
        groupId: Int,
        key: String? = null
    ): VkApiRequest<Int> = Methods.allowMessagesFromGroup.call(Int.serializer()) {
        append("group_id", groupId)
        append("key", key)
    }


    fun createChat(
        userIds: List<Int>,
        title: String
    ): VkApiRequest<Int> = Methods.createChat.call(Int.serializer()) {
        append("user_ids", userIds.joinToString(","))
        append("title", title)
    }


    fun delete(
        messageIds: List<Int>,
        deleteForAll: Boolean,
        markAsSpam: Boolean = false,
        groupId: Int? = null
    ): VkApiRequest<Map<Int, Int>> = Methods.delete.call(MapSerializer(Int.serializer(), Int.serializer())) {
        append("message_ids", messageIds.joinToString(","))
        append("spam", markAsSpam.toInt())
        append("delete_for_all", deleteForAll.toInt())
        append("group_id", groupId)
    }


    fun deleteChatPhoto(
        chatId: Int,
        groupId: Int? = null
    ): VkApiRequest<ChatChangePhotoResponse> = Methods.deleteChatPhoto.call(ChatChangePhotoResponse.serializer()) {
        append("chat_id", chatId)
        append("group_id", groupId)
    }


    fun deleteChatPhoto(
        chatId: Int
    ): VkApiRequest<ChatChangePhotoResponse> = Methods.deleteChatPhoto.call(ChatChangePhotoResponse.serializer()) {
        append("chat_id", chatId)
    }


    fun deleteConversation(
        peerId: Int,
        groupId: Int? = null
    ): VkApiRequest<Map<String, Int>> = Methods.deleteConversation.call(MapSerializer(String.serializer(), Int.serializer())) {
        append("peer_id", peerId)
        append("group_id", groupId)
    }


    fun deleteConversation(
        peerId: Int
    ): VkApiRequest<Map<String, Int>> = Methods.deleteConversation.call(MapSerializer(String.serializer(), Int.serializer())) {
        append("peer_id", peerId)
    }


    fun denyMessagesFromGroup(
        groupId: Int
    ): VkApiRequest<Int> = Methods.denyMessagesFromGroup.call(Int.serializer()) {
        append("group_id", groupId)
    }


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
    ): VkApiRequest<Int> = Methods.edit.call(Int.serializer()) {
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


    fun editChat(
        chatId: Int,
        title: String,
        groupId: Int? = null
    ): VkApiRequest<Int> = Methods.editChat.call(Int.serializer()) {
        append("chat_id", chatId)
        append("title", title)
        append("group_id", groupId)
    }


    fun getByConversationMessageId(
        peerId: Int,
        conversationMessageIds: List<Int>? = null,
        extended: Boolean? = null,
        fields: List<ObjectField>? = null,
        groupId: Int? = null
    ): VkApiRequest<ExtendedListResponse<Message>> = Methods.getByConversationMessageId.call(ExtendedListResponse.serializer(Message.serializer())) {
        append("peer_id", peerId)
        append("conversation_message_ids", conversationMessageIds?.joinToString(","))
        append("extended", extended?.toInt())
        append("fields", fields?.joinToString(",") { it.value })
        append("group_id", groupId)
    }


    fun getById(
        messageIds: List<Int>,
        previewLength: Int? = null,
        extended: Boolean? = null,
        fields: List<ObjectField>? = null,
        groupId: Int? = null
    ): VkApiRequest<ExtendedListResponse<Message>> = Methods.getById.call(ExtendedListResponse.serializer(Message.serializer())) {
        append("message_ids", messageIds.joinToString(","))
        append("preview_length", previewLength)
        append("extended", extended?.toInt())
        append("fields", fields?.joinToString(",") { it.value })
        append("group_id", groupId)
    }


    fun getChat(
        chatIds: List<Int>,
        nameCase: NameCase? = null
    ): VkApiRequest<List<Chat>> = Methods.getChat.call(ListSerializer(Chat.serializer())) {
        append("chat_ids", chatIds.joinToString(","))
        append("name_case", nameCase?.value)
    }


    suspend fun getChatTitle(chatId: Int): String = getChat(listOf(chatId)).execute()[0].title

    fun getChatPreview(
        link: String,
        fields: List<ObjectField> = emptyList()
    ): VkApiRequest<JsonObject> = Methods.getChatPreview.call(JsonObject.serializer()) {
        append("link", link)
        append("fields", fields.joinToString(",") { it.value })
    }


    suspend fun getChatUsers(
        chatId: Int
    ): List<Int> = getChat(listOf(chatId)).execute()[0].users

    fun getConversationMembers(
        peerId: Int,
        fields: List<ObjectField> = emptyList(),
        groupId: Int? = null
    ): VkApiRequest<ExtendedListResponse<Member>> = Methods.getConversationMembers.call(ExtendedListResponse.serializer(Member.serializer())) {
        append("peer_id", peerId)
        append("fields", fields.joinToString(",") { it.value })
        append("group_id", groupId)
    }


    fun getConversations(
        count: Int = 1,
        offset: Int = 0,
        filter: ConversationFilter = ConversationFilter.ALL,
        startMessageId: Int? = null,
        extended: Boolean = false,
        fields: List<ObjectField> = emptyList(),
        groupId: Int? = null
    ): VkApiRequest<ConversationsListResponse> = Methods.getConversations.call(ConversationsListResponse.serializer()) {
        append("offset", offset)
        append("count", count)
        append("filter", filter.value)
        append("start_message_id", startMessageId)
        append("extended", extended.toInt())
        append("fields", fields.joinToString(",") { it.value })
        append("group_id", groupId)
    }


    fun getConversationsById(
        peerIds: List<Int>,
        extended: Boolean = false,
        fields: List<ObjectField> = emptyList(),
        groupId: Int? = null
    ): VkApiRequest<ConversationsListResponse> = Methods.getConversationsById.call(ConversationsListResponse.serializer()) {
        append("peer_ids", peerIds.joinToString(","))
        append("extended", extended.toInt())
        append("fields", fields.joinToString(",") { it.value })
        append("group_id", groupId)
    }


    fun getHistory(
        peerId: Int,
        count: Int = 1,
        offset: Int = 0,
        startMessageId: Int? = null,
        reverse: Boolean = false,
        extended: Boolean = false,
        fields: List<ObjectField> = emptyList(),
        groupId: Int? = null
    ): VkApiRequest<ExtendedListResponse<Message>> = Methods.getHistory.call(ExtendedListResponse.serializer(Message.serializer())) {
        append("peer_id", peerId)
        append("offset", offset)
        append("count", count)
        append("start_message_id", startMessageId)
        append("rev", reverse.toInt())
        append("extended", extended.toInt())
        append("fields", fields.joinToString(",") { it.value })
        append("group_id", groupId)
    }


    fun getHistoryAttachments(
        peerId: Int,
        mediaType: AttachmentType,
        startFrom: Int? = null,
        count: Int = 1,
        withPhotoSizes: Boolean = true,
        fields: List<ObjectField> = emptyList(),
        groupId: Int? = null
    ): VkApiRequest<HistoryAttachmentsResponse> = Methods.getHistoryAttachments.call(HistoryAttachmentsResponse.serializer()) {
        append("peer_id", peerId)
        append("media_type", mediaType.value)
        append("start_from", startFrom)
        append("count", count)
        append("photo_sizes", withPhotoSizes.toInt())
        append("fields", fields.joinToString(",") { it.value })
        append("group_id", groupId)
    }


    fun getImportantMessages(
        count: Int = 1,
        offset: Int = 0,
        startMessageId: Int? = null,
        previewLength: Int = 0,
        extended: Boolean = false,
        fields: List<ObjectField> = emptyList(),
        groupId: Int? = null
    ): VkApiRequest<ExtendedListResponse<Message>> = Methods.getImportantMessages.call(ExtendedListResponse.serializer(Message.serializer())) {
        append("count", count)
        append("offset", offset)
        append("start_message_id", startMessageId)
        append("preview_length", previewLength)
        append("extended", extended.toInt())
        append("fields", fields.joinToString(",") { it.value })
        append("group_id", groupId)
    }


    fun getInviteLink(
        peerId: Int,
        generateNewLink: Boolean,
        groupId: Int? = null
    ): VkApiRequest<LinkResponse> = Methods.getInviteLink.call(LinkResponse.serializer()) {
        append("peer_id", peerId)
        append("reset", generateNewLink.toInt())
        append("group_id", groupId)
    }


    fun getLastActivity(
        userId: Int
    ): VkApiRequest<JsonObject> = Methods.getLastActivity.call(JsonObject.serializer()) {
        append("user_id", userId)
    }


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
    ): VkApiRequest<JsonObject> = Methods.getLongPollHistory.call(JsonObject.serializer()) {
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


    suspend fun getMessagesCount(
        peerId: Int
    ): Int = getHistory(peerId, count = 0).execute().count

    fun getLongPollServer(
        needPts: Boolean,
        longPollVersion: Int,
        groupId: Int? = null
    ): VkApiRequest<LongPollServerResponse> = Methods.getLongPollServer.call(LongPollServerResponse.serializer()) {
        append("need_pts", needPts.toInt())
        append("lp_version", longPollVersion)
        append("group_id", groupId)
    }


    fun getRecentCalls(
        count: Int,
        startMessageId: Int? = null,
        fields: List<ObjectField>,
        extended: Boolean
    ): VkApiRequest<ExtendedListResponse<RecentCall>> = Methods.getRecentCalls.call(ExtendedListResponse.serializer(RecentCall.serializer())) {
        append("count", count)
        append("start_message_id", startMessageId)
        append("fields", fields.joinToString(",") { it.value })
        append("extended", extended.toInt())
    }


    suspend fun isMessagesFromGroupAllowed(
        groupId: Int,
        userId: Int
    ): Boolean = Methods.isMessagesFromGroupAllowed.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("user_id", userId)
    }.execute().getInt("is_allowed")!!.toBoolean()

    suspend fun joinChatByInviteLink(
        link: String
    ): Int = Methods.joinChatByInviteLink.call(JsonObject.serializer()) {
        append("link", link)
    }.execute().getInt("chat_id")!!

    fun markAsAnsweredConversation(
        peerId: Int,
        isAnswered: Boolean
    ): VkApiRequest<Int> = Methods.markAsAnsweredConversation.call(Int.serializer()) {
        append("peer_id", peerId)
        append("answered", isAnswered.toInt())
    }


    fun markAsAnsweredConversation(
        peerId: Int,
        isAnswered: Boolean,
        groupId: Int
    ): VkApiRequest<Int> = Methods.markAsAnsweredConversation.call(Int.serializer()) {
        append("peer_id", peerId)
        append("answered", isAnswered.toInt())
        append("group_id", groupId)
    }


    fun markAsImportant(
        messageIds: List<Int>,
        markAsImportant: Boolean
    ): VkApiRequest<List<Int>> = Methods.markAsImportant.call(ListSerializer(Int.serializer())) {
        append("message_ids", messageIds.joinToString(","))
        append("important", markAsImportant.toInt())
    }


    fun markAsImportantConversation(
        peerId: Int,
        isImportant: Boolean
    ): VkApiRequest<Int> = Methods.markAsImportantConversation.call(Int.serializer()) {
        append("peer_id", peerId)
        append("important", isImportant.toInt())
    }


    fun markAsImportantConversation(
        peerId: Int,
        isImportant: Boolean,
        groupId: Int
    ): VkApiRequest<Int> = Methods.markAsImportantConversation.call(Int.serializer()) {
        append("peer_id", peerId)
        append("important", isImportant.toInt())
        append("group_id", groupId)
    }


    fun markAsRead(
        peerId: Int,
        startMessageId: Int? = null,
        groupId: Int? = null
    ): VkApiRequest<Int> = Methods.markAsRead.call(Int.serializer()) {
        append("peer_id", peerId)
        append("start_message_id", startMessageId)
        append("group_id", groupId)
    }


    fun pin(
        peerId: Int,
        messageId: Int,
        groupId: Int? = null
    ): VkApiRequest<Message> = Methods.pin.call(Message.serializer()) {
        append("peer_id", peerId)
        append("message_id", messageId)
        append("group_id", groupId)
    }


    fun removeChatUser(
        chatId: Int,
        memberId: Int,
        groupId: Int? = null
    ): VkApiRequest<Int> = Methods.removeChatUser.call(Int.serializer()) {
        append("chat_id", chatId)
        append("member_id", memberId)
        append("group_id", groupId)
    }


    fun restore(
        messageId: Int,
        groupId: Int? = null
    ): VkApiRequest<Int> = Methods.restore.call(Int.serializer()) {
        append("message_id", messageId)
        append("group_id", groupId)
    }


    fun search(
        query: String,
        peerId: Int? = null,
        maxDate: GMTDate? = null,
        previewLength: Int? = 0,
        count: Int = 1,
        offset: Int = 0,
        groupId: Int? = null
    ): VkApiRequest<ExtendedListResponse<Message>> = Methods.search.call(ExtendedListResponse.serializer(Message.serializer())) {
        append("q", query)
        append("peer_id", peerId)
        append("date", maxDate?.toDMYString())
        append("preview_length", previewLength)
        append("offset", offset)
        append("count", count)
        append("group_id", groupId)
    }


    fun searchExtended(
        query: String,
        peerId: Int? = null,
        maxDate: GMTDate? = null,
        previewLength: Int? = 0,
        count: Int = 1,
        offset: Int = 0,
        fields: List<ObjectField> = emptyList(),
        groupId: Int? = null,
    ): VkApiRequest<ExtendedListResponse<Message>> = Methods.search.call(ExtendedListResponse.serializer(Message.serializer())) {
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


    fun searchConversations(
        query: String,
        count: Int,
        extended: Boolean,
        fields: List<ObjectField>,
        groupId: Int? = null
    ): VkApiRequest<ExtendedListResponse<Conversation>> = Methods.searchConversations.call(ExtendedListResponse.serializer(Conversation.serializer())) {
        append("q", query)
        append("count", count)
        append("extended", extended.toInt())
        append("fields", fields.joinToString(",") { it.value })
        append("group_id", groupId)
    }


    fun send(
        peerId: Int,
        randomId: Int,
        text: CharSequence,
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
    ): VkApiRequest<Int> = Methods.send.call(Int.serializer()) {
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
    ): VkApiRequest<Int> = Methods.send.call(Int.serializer()) {
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


    fun sendMessageEventAnswer(
        eventId: String,
        userId: Int,
        peerId: Int,
        eventData: MessagePayload = MessagePayload("")
    ): VkApiRequest<Int> = Methods.sendMessageEventAnswer.call(Int.serializer()) {
        append("event_id", eventId)
        append("user_id", userId)
        append("peer_id", peerId)
        append("event_data", eventData.value)
    }

    fun setActivity(
        peerId: Int,
        type: String,
        groupId: Int? = null
    ): VkApiRequest<Int> = Methods.setActivity.call(Int.serializer()) {
        append("peer_id", peerId)
        append("group_id", groupId)
        append("type", type)
    }


    fun setChatPhoto(
        file: String,
        groupId: Int? = null
    ): VkApiRequest<ChatChangePhotoResponse> = Methods.setChatPhoto.call(ChatChangePhotoResponse.serializer()) {
        append("file", file)
        append("group_id", groupId)
    }


    fun unpin(
        peerId: Int,
        groupId: Int? = null
    ): VkApiRequest<Int> = Methods.unpin.call(Int.serializer()) {
        append("peer_id", peerId)
        append("group_id", groupId)
    }


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
            const val sendMessageEventAnswer = it + "sendMessageEventAnswer"
            const val setActivity = it + "setActivity"
            const val setChatPhoto = it + "setChatPhoto"
            const val unpin = it + "unpin"
        }
    }
}