package com.github.stormbit.sdk.longpoll.updateshandlers

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.events.CommandEvent
import com.github.stormbit.sdk.events.EveryEvent
import com.github.stormbit.sdk.events.TypingEvent
import com.github.stormbit.sdk.events.chat.*
import com.github.stormbit.sdk.events.friend.FriendOfflineEvent
import com.github.stormbit.sdk.events.friend.FriendOnlineEvent
import com.github.stormbit.sdk.events.message.*
import com.github.stormbit.sdk.longpoll.Events
import com.github.stormbit.sdk.longpoll.MessageEvents
import com.github.stormbit.sdk.objects.Chat
import com.github.stormbit.sdk.objects.Message
import com.github.stormbit.sdk.objects.models.MessagePayload
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.objects.models.Message.ServiceAction.Type
import kotlinx.serialization.json.*

class UpdatesHandlerUser(private val client: Client) : UpdatesHandler(client) {

    override fun handleCurrentUpdate() {
        val currentUpdate = if (this.queue.updatesUser.isEmpty()) {
            return
        } else {
            this.queue.updatesUser.shift()!!
        }

        when (currentUpdate.getInt(0)) {
            4 -> {
                val messageFlags = currentUpdate.getInt(2)

                if ((messageFlags and 2) == 0) {
                    Client.service.submit { handleMessageUpdate(currentUpdate) }
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
    private fun handleChatEvents(updateObject: JsonArray): Boolean {
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
                val fromId = attachments.getString("from")!!.toInt()

                when (sourceAct) {
                    Type.CHAT_CREATE.value -> {
                        val title = attachments.getString("source_text")!!
                        events[Events.CHAT_CREATE.value]?.invoke(ChatCreateEvent(title, fromId, chatId))
                    }

                    Type.CHAT_TITLE_UPDATE.value -> {
                        val oldTitle = attachments.getString("source_old_text")!!
                        val newTitle = attachments.getString("source_text")!!

                        events[Events.CHAT_TITLE_CHANGE.value]?.invoke(
                            ChatTitleChangeEvent(
                                oldTitle,
                                newTitle,
                                fromId,
                                chatId
                            )
                        )
                    }

                    Type.CHAT_PHOTO_UPDATE.value -> {
                        val attachmentsObject = findAttachments(updateObject.getJsonObject(7))

                        events[Events.CHAT_PHOTO_UPDATE.value]?.invoke(
                            ChatPhotoUpdateEvent(attachmentsObject.items[0].attach, fromId, chatId)
                        )
                    }

                    Type.CHAT_INVITE_USER.value -> {
                        val user = attachments.getString("source_mid")!!.toInt()

                        events[Events.CHAT_JOIN.value]?.invoke(ChatJoinEvent(fromId, user, chatId))
                    }

                    Type.CHAT_KICK_USER.value -> {
                        val user = attachments.getString("source_mid")!!.toInt()

                        events[Events.CHAT_LEAVE.value]?.invoke(ChatLeaveEvent(fromId, user, chatId))
                    }

                    Type.CHAT_PHOTO_REMOVE.value -> {
                        events[Events.CHAT_PHOTO_REMOVE.value]?.invoke(ChatPhotoRemoveEvent(fromId, chatId))
                    }

                    Type.CHAT_PIN_MESSAGE.value -> {
                        val messageId = attachments.getInt("source_chat_local_id")!!

                        events[Events.CHAT_PIN_MESSAGE.value]?.invoke(ChatPinMessageEvent(fromId, chatId, messageId))
                    }

                    Type.CHAT_UNPIN_MESSAGE.value -> {
                        val messageId = attachments.getInt("source_chat_local_id")!!

                        events[Events.CHAT_UNPIN_MESSAGE.value]?.invoke(ChatUnpinMessageEvent(fromId, chatId, messageId))
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
    private fun handleEveryLongPollUpdate(updateObject: JsonArray) {
        events[Events.EVERY.value]?.invoke(EveryEvent(
            buildJsonObject {
                put("response", updateObject)
            }
        ))
    }

    private fun handleMessageUpdate(updateObject: JsonArray) {
        try {
            var messageIsAlreadyHandled = false

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

                peerId = updateObject.getJsonObject(6).getInt("from")!!
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
                messageIsAlreadyHandled = handleChatEvents(updateObject)
            }

            // check for commands
            if (client.commands.size > 0) {
                messageIsAlreadyHandled = handleCommands(message)
            }

            if (message.hasFwds) {
                events[MessageEvents.MESSAGE_WITH_FORWARDS.value]?.invoke(MessageWithForwardsEvent(message))
                messageIsAlreadyHandled = true

                handleSendTyping(message)
            }

            if (!messageIsAlreadyHandled) {
                when (message.messageType()) {
                    Message.MessageType.VOICE -> {
                        if (events.containsKey(MessageEvents.VOICE_MESSAGE.value)) {
                            events[MessageEvents.VOICE_MESSAGE.value]?.invoke(VoiceMessageEvent(message))
                            messageIsAlreadyHandled = true
                            handleSendTyping(message)
                        }
                    }

                    Message.MessageType.STICKER -> {
                        if (events.containsKey(MessageEvents.STICKER_MESSAGE.value)) {
                            events[MessageEvents.STICKER_MESSAGE.value]?.invoke(StickerMessageEvent(message))
                            messageIsAlreadyHandled = true
                            handleSendTyping(message)
                        }
                    }

                    Message.MessageType.AUDIO -> {
                        if (events.containsKey(MessageEvents.AUDIO_MESSAGE.value)) {
                            events[MessageEvents.AUDIO_MESSAGE.value]?.invoke(AudioMessageEvent(message))
                            messageIsAlreadyHandled = true
                            handleSendTyping(message)
                        }
                    }

                    Message.MessageType.VIDEO -> {
                        if (events.containsKey(MessageEvents.VIDEO_MESSAGE.value)) {
                            events[MessageEvents.VIDEO_MESSAGE.value]?.invoke(VideoMessageEvent(message))
                            messageIsAlreadyHandled = true
                            handleSendTyping(message)
                        }
                    }

                    Message.MessageType.DOC -> {
                        if (events.containsKey(MessageEvents.DOC_MESSAGE.value)) {
                            events[MessageEvents.DOC_MESSAGE.value]?.invoke(DocMessageEvent(message))
                            messageIsAlreadyHandled = true
                            handleSendTyping(message)
                        }
                    }

                    Message.MessageType.WALL -> {
                        if (events.containsKey(MessageEvents.WALL_MESSAGE.value)) {
                            events[MessageEvents.WALL_MESSAGE.value]?.invoke(WallMessageEvent(message))
                            messageIsAlreadyHandled = true
                            handleSendTyping(message)
                        }
                    }

                    Message.MessageType.PHOTO -> {
                        if (events.containsKey(MessageEvents.PHOTO_MESSAGE.value)) {
                            events[MessageEvents.PHOTO_MESSAGE.value]?.invoke(PhotoMessageEvent(message))
                            messageIsAlreadyHandled = true
                            handleSendTyping(message)
                        }
                    }

                    Message.MessageType.LINK -> {
                        if (events.containsKey(MessageEvents.LINK_MESSAGE.value)) {
                            events[MessageEvents.LINK_MESSAGE.value]?.invoke(LinkMessageEvent(message))
                            messageIsAlreadyHandled = true
                            handleSendTyping(message)
                        }
                    }

                    Message.MessageType.SIMPLE_TEXT -> {
                        if (events.containsKey(MessageEvents.SIMPLE_TEXT_MESSAGE.value)) {
                            events[MessageEvents.SIMPLE_TEXT_MESSAGE.value]?.invoke(SimpleTextMessage(message))
                            messageIsAlreadyHandled = true
                            handleSendTyping(message)
                        }
                    }
                }
            }

            if (events.containsKey(MessageEvents.MESSAGE.value) && !messageIsAlreadyHandled) {
                events[MessageEvents.MESSAGE.value]?.invoke(MessageNewEvent(message))
                handleSendTyping(message)
            }

            if (message.isMessageFromChat) {
                if (events.containsKey(MessageEvents.CHAT_MESSAGE.value) && !messageIsAlreadyHandled) {
                    events[MessageEvents.CHAT_MESSAGE.value]?.invoke(ChatMessageEvent(message))
                    handleSendTyping(message)
                }
            }
        } catch (e: Exception) {
            log.error("Some error occurred when parsing message, error is: ", e)
        }
    }

    private fun handleOnline(updateObject: JsonArray) {
        val userId = updateObject.getInt(1)
        val timestamp = updateObject.getInt(3)


        if (events.containsKey(Events.FRIEND_ONLINE.value)) {
            events[Events.FRIEND_ONLINE.value]?.invoke(FriendOnlineEvent(userId, timestamp))
        }
    }

    private fun handleOffline(updateObject: JsonArray) {
        val userId = updateObject.getInt(1)
        val timestamp = updateObject.getInt(3)

        if (events.containsKey(Events.FRIEND_OFFLINE.value)) {
            events[Events.FRIEND_OFFLINE.value]?.invoke(FriendOfflineEvent(userId, timestamp))
        }
    }

    /**
     * Handle dialog with typing user
     */
    private fun handleTypingUpdate(updateObject: JsonArray) {
        if (events.containsKey(Events.TYPING.value)) {
            events[Events.TYPING.value]?.invoke(TypingEvent(updateObject.getInt(1)))
        }
    }

    /**
     * Handle message and call back if it contains any command
     *
     * @param message received message
     */
    private fun handleCommands(message: Message): Boolean {
        var isDone = false

        val words = message.text.split(" ")

        for (command in client.commands) {
            for (element in command.commands) {
                if (words[0].toLowerCase().contains(element.toLowerCase())) {
                    command.callback.invoke(CommandEvent(words.subList(1, words.size), message))
                    isDone = true
                    handleSendTyping(message)
                }
            }
        }

        return isDone
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