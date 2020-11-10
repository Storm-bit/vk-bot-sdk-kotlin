package com.github.stormbit.sdk.longpoll

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.events.EveryEvent
import com.github.stormbit.sdk.events.TypingEvent
import com.github.stormbit.sdk.events.chat.*
import com.github.stormbit.sdk.events.friend.FriendOfflineEvent
import com.github.stormbit.sdk.events.friend.FriendOnlineEvent
import com.github.stormbit.sdk.events.message.*
import com.github.stormbit.sdk.objects.Chat
import com.github.stormbit.sdk.objects.Message
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.utils.Utils.Companion.shift
import com.github.stormbit.sdk.utils.Utils.Companion.toJsonObject
import com.google.gson.JsonArray
import com.google.gson.JsonObject

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
        val chatId = updateObject.getInt(3)

        val attachments = if (updateObject.size() > 5) if (updateObject[6].toString().startsWith("{")) updateObject[6].asJsonObject else null else null
                ?: return false

        // Return if no attachments
        // Because there no events,
        // and because simple chat messages will be handled
        if (attachments!!.has("source_act")) {
            val sourceAct = attachments.getString("source_act")
            val from = attachments.getString("from").toInt()
            when (sourceAct) {
                "chat_create" -> {
                    val title = attachments.getString("source_text")
                    events[Events.CHAT_CREATE.value]?.invoke(ChatCreateEvent(title, from, chatId))
                }
                "chat_title_update" -> {
                    val oldTitle = attachments.getString("source_old_text")
                    val newTitle = attachments.getString("source_text")

                    events[Events.CHAT_TITLE_CHANGE.value]?.invoke(ChatTitleChangeEvent(oldTitle, newTitle, from, chatId))
                }
                "chat_photo_update" -> {
                    val photo = gson.toJsonTree(client.messages.getById(listOf(updateObject.getInt(1))).getAsJsonObject("response").getAsJsonArray("items").getJsonObject(0).getAsJsonArray("attachments").getJsonObject(0).getAsJsonObject("photo")).asJsonObject

                    events[Events.CHAT_PHOTO_UPDATE.value]?.invoke(ChatPhotoUpdateEvent(photo, from, chatId))
                }
                "chat_invite_user" -> {
                    val user = attachments.getString("source_mid").toInt()

                    events[Events.CHAT_JOIN.value]?.invoke(ChatJoinEvent(from, user, chatId))

                }
                "chat_kick_user" -> {
                    val user = attachments.getString("source_mid").toInt()


                    events[Events.CHAT_LEAVE.value]?.invoke(ChatLeaveEvent(from, user, chatId))
                }
                "chat_photo_remove" -> {
                    events[Events.CHAT_PHOTO_REMOVE.value]?.invoke(ChatPhotoRemoveEvent(from, chatId))
                }
            }

            return true
        }

        return false
    }

    /**
     * Handle every longpoll event
     */
    private fun handleEveryLongPollUpdate(updateObject: JsonArray) {
        events[Events.EVERY.value]?.invoke(EveryEvent(JsonObject().put("response", updateObject)))
    }

    private fun handleMessageUpdate(updateObject: JsonArray) {
        var messageIsAlreadyHandled = false

        val messageId = updateObject.getInt(1)
        var peerId = updateObject.getInt(3)
        val timestamp = updateObject.getInt(4)
        var chatId = 0

        val messageText = updateObject.getString(5)

        val payload = JsonObject()

//        val title = updateObject.getJsonObject(6).getString("title")
        val title = "..."

        val attachments = if (updateObject.size() > 0) if (updateObject.get(6).toString().startsWith("{")) toJsonObject(updateObject[6].toString()) else null else null

        val randomId = if (updateObject.size() > 7) updateObject.getInt(8) else 0

        // Check for chat
        if (peerId > Chat.CHAT_PREFIX) {
            chatId = peerId - Chat.CHAT_PREFIX
            if (attachments != null) {
                peerId = attachments.getString("from").toInt()
            }
        }

        val message = Message(
                client,
                messageId,
                peerId,
                timestamp,
                messageText,
                title,
                attachments,
                randomId,
                payload
        )

        if (chatId > 0) {
            message.chatId = chatId
            message.chatIdLong = Chat.CHAT_PREFIX + chatId

            // chat
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
        var done = false

        for (command in client.commands) {
            for (element in command.commands) {
                if (message.text.split(" ")[0].toLowerCase().contains(element.toLowerCase())) {
                    command.callback.invoke(MessageNewEvent(message))
                    done = true
                    handleSendTyping(message)
                }
            }
        }

        return done
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
}