package com.github.stormbit.sdk.longpoll.updateshandlers

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.events.*
import com.github.stormbit.sdk.events.chat.*
import com.github.stormbit.sdk.events.message.ChatMessageEvent
import com.github.stormbit.sdk.events.message.MessageNewEvent
import com.github.stormbit.sdk.longpoll.Events
import com.github.stormbit.sdk.longpoll.MessageEvents
import com.github.stormbit.sdk.objects.Message
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.utils.json
import kotlinx.serialization.json.JsonObject
import com.github.stormbit.sdk.objects.models.Message as MessageModel

class UpdatesHandlerGroup(private val client: Client) : UpdatesHandler(client) {

    override fun handleCurrentUpdate() {
        val currentUpdate: JsonObject

        if (queue.updatesGroup.isEmpty()) {
            return
        } else {
            currentUpdate = queue.updatesGroup.shift()!!
        }

        val updateType = Events[currentUpdate.getString("type")!!]!!

        val obj = currentUpdate.getJsonObject("object")!!

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
    private fun handleChatEvents(message: MessageModel) : Boolean {
        if (!message.isServiceAction) return false

        val action = message.serviceAction
        val fromId = message.fromId
        val chatId = message.peerId

        try {
            when (action!!.type) {
                MessageModel.ServiceAction.Type.CHAT_CREATE -> {
                    events[Events.CHAT_CREATE.value]?.invoke(ChatCreateEvent(action.conversationTitle!!, fromId, chatId))
                }

                MessageModel.ServiceAction.Type.CHAT_TITLE_UPDATE -> {
                    events[Events.CHAT_TITLE_CHANGE.value]?.invoke(
                        ChatTitleChangeEvent(
                            "",
                            action.conversationTitle!!,
                            fromId,
                            chatId
                        )
                    )
                }

                MessageModel.ServiceAction.Type.CHAT_PHOTO_UPDATE -> {
                    val photo = message.attachments[0].photo!!

                    events[Events.CHAT_PHOTO_UPDATE.value]?.invoke(
                        ChatPhotoUpdateEvent(
                            photo.attachmentString,
                            fromId,
                            chatId
                        )
                    )
                }

                MessageModel.ServiceAction.Type.CHAT_INVITE_USER -> {
                    events[Events.CHAT_JOIN.value]?.invoke(ChatJoinEvent(fromId, action.memberId!!, chatId))
                }

                MessageModel.ServiceAction.Type.CHAT_KICK_USER -> {
                    events[Events.CHAT_LEAVE.value]?.invoke(ChatLeaveEvent(fromId, action.memberId!!, chatId))
                }

                MessageModel.ServiceAction.Type.CHAT_PHOTO_REMOVE -> {
                    events[Events.CHAT_PHOTO_REMOVE.value]?.invoke(ChatPhotoRemoveEvent(fromId, chatId))
                }

                MessageModel.ServiceAction.Type.CHAT_PIN_MESSAGE -> {
                    events[Events.CHAT_PIN_MESSAGE.value]?.invoke(ChatPinMessageEvent(fromId, chatId, message.conversationMessageId))
                }

                MessageModel.ServiceAction.Type.CHAT_UNPIN_MESSAGE -> {
                    events[Events.CHAT_UNPIN_MESSAGE.value]?.invoke(ChatUnpinMessageEvent(fromId, chatId, message.conversationMessageId))
                }

            }
        } catch (e: Exception) {
            log.error("Some error occurred when parsing chat event, error is: ", e)
        }

        return true
    }

    /**
     * Handle new message
     */
    private fun handleMessageUpdate(updateObject: JsonObject) {
        try {
            // Flag
            var messageIsAlreadyHandled = false

            val messageObject = json.decodeFromJsonElement(MessageModel.serializer(), updateObject)

            val message = Message(messageObject)

            if (message.chatId > 0) {
                messageIsAlreadyHandled = handleChatEvents(messageObject)
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
        } catch (e: Exception) {
            log.error("Some error occurred when parsing chat event, error is: ", e)
        }
    }

    /**
     * Handle dialog with typing user
     */
    private fun handleTypingUpdate(updateObject: JsonObject) {
        events[Events.TYPING.value]?.invoke(TypingEvent(updateObject.getString("from_id")!!.toInt()))
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
}
