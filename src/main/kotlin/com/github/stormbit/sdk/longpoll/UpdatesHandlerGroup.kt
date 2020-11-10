package com.github.stormbit.sdk.longpoll

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.events.*
import com.github.stormbit.sdk.events.chat.*
import com.github.stormbit.sdk.events.message.ChatMessageEvent
import com.github.stormbit.sdk.events.message.MessageNewEvent
import com.github.stormbit.sdk.objects.Message
import com.github.stormbit.sdk.utils.Utils.Companion.shift
import com.github.stormbit.sdk.utils.getInt
import com.github.stormbit.sdk.utils.getJsonObject
import com.github.stormbit.sdk.utils.getString
import com.google.gson.JsonObject

class UpdatesHandlerGroup(private val client: Client) : UpdatesHandler(client) {

    override fun handleCurrentUpdate() {
        val currentUpdate: JsonObject

        if (queue.updatesGroup.isEmpty()) {
            return
        } else {
            currentUpdate = queue.updatesGroup.shift()!!
        }

        val updateType = Events[currentUpdate["type"].asString]!!

        val obj = currentUpdate["object"].asJsonObject

        when (updateType) {
            Events.MESSAGE_NEW -> {
                // check if message is received
                Client.service.submit { handleMessageUpdate(obj) }

                // handle every
                handleEveryLongPollUpdate(obj)
            }

            Events.MESSAGE_TYPING_STATE -> {
                handleTypingUpdate(obj)

                // handle every
                handleEveryLongPollUpdate(currentUpdate)
            }

            else -> handleEveryLongPollUpdate(currentUpdate)
        }
    }

    private fun handleEveryLongPollUpdate(obj: JsonObject) {
        events[Events.EVERY.value]?.invoke(EveryEvent(obj))
    }

    /**
     * Handle chat events
     */
    private fun handleChatEvents(updateObject: JsonObject) {
        val chatId = updateObject.getInt("peer_id")
        val attachments = (if (updateObject.getAsJsonArray("attachments").size() > 0)
            updateObject.getAsJsonArray("attachments").getJsonObject(0) else null) ?: return

        // Return if no attachments
        // Because there no events,
        // and because simple chat messages will be handled
        if (attachments.has("source_act")) {
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
                    val photo = client.messages.getById(listOf(updateObject.getInt("conversation_message_id"))).getAsJsonObject("response").getAsJsonArray("items").getJsonObject(0).getAsJsonArray("attachments").getJsonObject(0).getAsJsonObject("photo")

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
        }
    }

    /**
     * Handle new message
     */
    private fun handleMessageUpdate(updateObject: JsonObject) {

        // Flag
        var messageIsAlreadyHandled = false

        val message = Message(client, updateObject)

        if (message.chatId > 0) {
            // chat events
            handleChatEvents(updateObject)
        }

        // check for commands
        if (client.commands.size > 0) {
            messageIsAlreadyHandled = handleCommands(message)
        }

        if (message.hasFwds) {
            if (events.containsKey(MessageEvents.MESSAGE_WITH_FORWARDS.value)) {
                events[MessageEvents.MESSAGE_WITH_FORWARDS.value]?.invoke(MessageNewEvent(message))
                messageIsAlreadyHandled = true
                handleSendTyping(message)
            }
        }

        if (!messageIsAlreadyHandled) {
            when (message.messageType()) {
                Message.MessageType.VOICE -> {
                    if (events.containsKey(MessageEvents.VOICE_MESSAGE.value)) {
                        events[MessageEvents.VIDEO_MESSAGE.value]?.invoke(MessageNewEvent(message))
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.STICKER -> {
                    if (events.containsKey(MessageEvents.STICKER_MESSAGE.value)) {
                        events[MessageEvents.STICKER_MESSAGE.value]?.invoke(MessageNewEvent(message))
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.AUDIO -> {
                    if (events.containsKey(MessageEvents.AUDIO_MESSAGE.value)) {
                        events[MessageEvents.AUDIO_MESSAGE.value]?.invoke(MessageNewEvent(message))
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.VIDEO -> {
                    if (events.containsKey(MessageEvents.VIDEO_MESSAGE.value)) {
                        events[MessageEvents.VIDEO_MESSAGE.value]?.invoke(MessageNewEvent(message))
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.DOC -> {
                    if (events.containsKey(MessageEvents.DOC_MESSAGE.value)) {
                        events[MessageEvents.DOC_MESSAGE.value]?.invoke(MessageNewEvent(message))
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.WALL -> {
                    if (events.containsKey(MessageEvents.WALL_MESSAGE.value)) {
                        events[MessageEvents.WALL_MESSAGE.value]?.invoke(MessageNewEvent(message))
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.PHOTO -> {
                    if (events.containsKey(MessageEvents.PHOTO_MESSAGE.value)) {
                        events[MessageEvents.PHOTO_MESSAGE.value]?.invoke(MessageNewEvent(message))
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.LINK -> {
                    if (events.containsKey(MessageEvents.LINK_MESSAGE.value)) {
                        events[MessageEvents.LINK_MESSAGE.value]?.invoke(MessageNewEvent(message))
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.SIMPLE_TEXT -> {
                    if (events.containsKey(MessageEvents.SIMPLE_TEXT_MESSAGE.value)) {
                        events[MessageEvents.SIMPLE_TEXT_MESSAGE.value]?.invoke(MessageNewEvent(message))
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

    /**
     * Handle dialog with typing user
     */
    private fun handleTypingUpdate(updateObject: JsonObject) {
        events[Events.TYPING.value]?.invoke(TypingEvent(updateObject.getString("from_id").toInt()))
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