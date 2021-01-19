package com.github.stormbit.sdk.longpoll.updateshandlers

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.events.EveryEvent
import com.github.stormbit.sdk.events.TypingEvent
import com.github.stormbit.sdk.events.chat.*
import com.github.stormbit.sdk.events.friend.FriendOfflineEvent
import com.github.stormbit.sdk.events.friend.FriendOnlineEvent
import com.github.stormbit.sdk.events.message.*
import com.github.stormbit.sdk.longpoll.events.Events
import com.github.stormbit.sdk.objects.Chat
import com.github.stormbit.sdk.objects.Message
import com.github.stormbit.sdk.objects.models.MessagePayload
import com.github.stormbit.sdk.objects.models.ServiceAction
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.vkapi.RoutePath
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*

class UpdatesHandlerUser(private val client: Client) : UpdatesHandler<JsonArray>(client) {

    override suspend fun handleCurrentUpdate(currentUpdate: JsonArray) {
        when (currentUpdate.getInt(0)) {
            4 -> {
                val messageFlags = currentUpdate.getInt(2)

                if ((messageFlags and 2) == 0) {
                    GlobalScope.launch { handleMessageUpdate(currentUpdate) }
                }
            }

            61 -> {
                handleTypingUpdate(currentUpdate)

                handleEveryLongPollUpdate(currentUpdate)
            }

            8 -> {
                handleOnline(currentUpdate)

                handleEveryLongPollUpdate(currentUpdate)
            }

            9 -> {
                handleOffline(currentUpdate)

                handleEveryLongPollUpdate(currentUpdate)
            }

            else -> handleEveryLongPollUpdate(currentUpdate)
        }
    }

    /**
     * Handle chat events
     */
    private suspend fun handleChatEvents(updateObject: JsonArray, message: Message): Boolean {
        try {
            val chatId = updateObject.getInt(3)

            val attachments =
                if (updateObject.size > 5)
                    if (updateObject[6].toString().startsWith("{"))
                        updateObject.getJsonObject(6)
                    else null
                else null ?: return false

            // Return if no items
            // Because there no events,
            // and because simple chat messages will be handled
            if (attachments!!.containsKey("source_act")) {
                val sourceAct = attachments.getString("source_act")!!
                val fromId = attachments.getString("senderType")!!.toInt()

                when (sourceAct) {
                    ServiceAction.Type.CHAT_CREATE.value -> {
                        val title = attachments.getString("source_text")!!
                        events[Events.CHAT_CREATE.value]?.invoke(ChatCreateEvent(title, fromId, chatId, Message()))
                    }

                    ServiceAction.Type.CHAT_TITLE_UPDATE.value -> {
                        val oldTitle = attachments.getString("source_old_text")!!
                        val newTitle = attachments.getString("source_text")!!

                        events[Events.CHAT_TITLE_CHANGE.value]?.invoke(
                            ChatTitleUpdateEvent(
                                oldTitle,
                                newTitle,
                                fromId,
                                chatId,
                                Message()
                            )
                        )
                    }

                    ServiceAction.Type.CHAT_PHOTO_UPDATE.value -> {
                        val attachmentsObject = findAttachments(updateObject.getJsonObject(7))

                        events[Events.CHAT_PHOTO_CHANGE.value]?.invoke(
                            ChatPhotoUpdateEvent(attachmentsObject.items[0].attach, fromId, chatId, message)
                        )
                    }

                    ServiceAction.Type.CHAT_INVITE_USER.value -> {
                        val user = attachments.getString("source_mid")!!.toInt()

                        events[Events.CHAT_JOIN.value]?.invoke(ChatJoinEvent(fromId, user, chatId, message))
                    }

                    ServiceAction.Type.CHAT_KICK_USER.value -> {
                        val user = attachments.getString("source_mid")!!.toInt()

                        events[Events.CHAT_LEAVE.value]?.invoke(ChatLeaveEvent(fromId, user, chatId, message))
                    }

                    ServiceAction.Type.CHAT_PHOTO_REMOVE.value -> {
                        events[Events.CHAT_PHOTO_REMOVE.value]?.invoke(ChatPhotoRemoveEvent(fromId, chatId, message))
                    }

                    ServiceAction.Type.CHAT_PIN_MESSAGE.value -> {
                        val messageId = attachments.getInt("source_chat_local_id")!!

                        events[Events.CHAT_PIN_MESSAGE.value]?.invoke(ChatPinMessageEvent(fromId, chatId, messageId, message))
                    }

                    ServiceAction.Type.CHAT_UNPIN_MESSAGE.value -> {
                        val messageId = attachments.getInt("source_chat_local_id")!!

                        events[Events.CHAT_UNPIN_MESSAGE.value]?.invoke(ChatUnpinMessageEvent(fromId, chatId, messageId, message))
                    }
                }
            }
        } catch (e: Exception) {
            log.error("Some error occurred when parsing chat event, error is: ", e)
        }

        return true
    }

    /**
     * Handle every longpoll event
     */
    private suspend fun handleEveryLongPollUpdate(updateObject: JsonArray) {
        events[Events.EVERY.value]?.invoke(EveryEvent(
            buildJsonObject {
                put("response", updateObject)
            }
        ))
    }

    private suspend fun handleMessageUpdate(updateObject: JsonArray) {
        try {
            val messageId = updateObject.getInt(1)
            var peerId = updateObject.getInt(3)
            val timestamp = updateObject.getInt(4)
            var chatId = 0

            val messageText = updateObject.getString(5)

            val payload = MessagePayload("{}")

            val attachmentsObject = if (updateObject.size > 6) updateObject.getJsonObject(7) else null

            val attachments = findAttachments(attachmentsObject)

            val randomId = if (updateObject.size > 7) updateObject.getInt(8) else 0

            // Check for chat
            if (peerId > Chat.CHAT_PREFIX) {
                chatId = peerId - Chat.CHAT_PREFIX

                peerId = updateObject.getJsonObject(6).getInt("senderType")!!
            }

            val message = Message(
                client,
                messageId,
                peerId,
                timestamp,
                messageText,
                attachments,
                randomId,
                payload
            )

            if (chatId > 0) {
                message.chatId = chatId
                message.chatIdLong = Chat.CHAT_PREFIX + chatId

                // handle chat events
                if (handleChatEvents(updateObject, message)) return
            }

            when (message.senderType) {
                Message.SenderType.COMMUNITY ->
                    client.messageHandler?.pass(CommunityMessageEvent(message), RoutePath(String(), message.text))

                Message.SenderType.USER ->
                    client.messageHandler?.pass(UserMessageEvent(message), RoutePath(String(), message.text))

                Message.SenderType.CHAT ->
                    client.messageHandler?.pass(ChatMessageEvent(message), RoutePath(String(), message.text))
            }

            handleSendTyping(message)
        } catch (e: Exception) {
            log.error("Some error occurred when parsing message, error is: ", e)
        }
    }

    private suspend fun handleOnline(updateObject: JsonArray) {
        val userId = updateObject.getInt(1)
        val timestamp = updateObject.getInt(3)


        if (events.containsKey(Events.FRIEND_ONLINE.value)) {
            events[Events.FRIEND_ONLINE.value]?.invoke(FriendOnlineEvent(userId, timestamp))
        }
    }

    private suspend fun handleOffline(updateObject: JsonArray) {
        val userId = updateObject.getInt(1)
        val timestamp = updateObject.getInt(3)

        if (events.containsKey(Events.FRIEND_OFFLINE.value)) {
            events[Events.FRIEND_OFFLINE.value]?.invoke(FriendOfflineEvent(userId, timestamp))
        }
    }

    /**
     * Handle dialog with typing user
     */
    private suspend fun handleTypingUpdate(updateObject: JsonArray) {
        if (events.containsKey(Events.TYPING.value)) {
            events[Events.TYPING.value]?.invoke(TypingEvent(updateObject.getInt(1)))
        }
    }

    /**
     * Send typing
     */
    private fun handleSendTyping(message: Message) {

        // Send typing
        if (sendTyping) {
            if (!message.isMessageFromChat) {
                client.messages.setActivity(message.peerId, "typing")
            } else {
                client.messages.setActivity(message.chatIdLong, "typing")
            }
        }
    }

    private fun findAttachments(jsonObject: JsonObject?): Message.Attachments {
        if (jsonObject == null) return Message.Attachments()

        val result = ArrayList<Message.Attachments.Item>()
        var hasFwd = false

        try {
            val attachObjects: ArrayList<Map<String, String>> = ArrayList()

            val string = jsonObject.keys.joinToString("")
            val numbers = string.replace("[^0-9]".toRegex(), "").toCharArray().distinct()

            numbers.forEach { number ->
                val index = number.toString()

                val keys = jsonObject.keys.filter { it.contains(index) }

                val map = HashMap<String, String>()

                for (key in keys) {
                    if (key == "fwd") hasFwd = true
                    map[key.replace("[0-9]".toRegex(), "")] = jsonObject.getString(key)!!
                }

                attachObjects.add(map)
            }

            for (map in attachObjects) {
                val item = json.decodeFromJsonElement(Message.Attachments.Item.serializer(), map.toJsonObject())

                result.add(item)
            }
        } catch (e: Exception) {
            log.error("Some error occurred when parsing attachments, error is: ", e)
        }

        return Message.Attachments(result, hasFwd)
    }
}