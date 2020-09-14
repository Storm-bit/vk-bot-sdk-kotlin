package com.github.stormbit.sdk.longpoll

import com.github.stormbit.sdk.callbacks.CallbackDouble
import com.github.stormbit.sdk.callbacks.CallbackFourth
import com.github.stormbit.sdk.callbacks.CallbackTriple
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.Chat
import com.github.stormbit.sdk.objects.Message
import com.github.stormbit.sdk.utils.Utils.Companion.shift
import com.github.stormbit.sdk.utils.Utils.Companion.toJsonObject
import com.github.stormbit.sdk.utils.getInt
import com.github.stormbit.sdk.utils.getJsonObject
import com.github.stormbit.sdk.utils.getString
import com.github.stormbit.sdk.utils.gson
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

            else -> {
                handleEveryLongPollUpdate(currentUpdate)
            }
        }
    }

    /**
     * Handle chat events
     */
    private fun handleChatEvents(updateObject: JsonArray) {
        val chatId = updateObject.getInt(3)

        val attachments = if (updateObject.size() > 6) if (updateObject[6].toString().startsWith("{")) gson.toJsonTree(updateObject[6].toString()).asJsonObject else null else null
                ?: return

        // Return if no attachments
        // Because there no events,
        // and because simple chat messages will be handled
        if (attachments!!.has("source_act")) {
            val sourceAct = attachments.getString("source_act")
            val from = attachments.getString("from").toInt()
            when (sourceAct) {
                "chat_create" -> {
                    val title = attachments.getString("source_text")
                    if (chatCallbacks.containsKey(Events.CHAT_CREATE.value)) {
                        (chatCallbacks[Events.CHAT_CREATE.value] as CallbackTriple<String?, Int?, Int?>).onEvent(title, from, chatId)
                    }
                }
                "chat_title_update" -> {
                    val oldTitle = attachments.getString("source_old_text")
                    val newTitle = attachments.getString("source_text")
                    if (chatCallbacks.containsKey(Events.CHAT_TITLE_CHANGE.value)) {
                        (chatCallbacks[Events.CHAT_TITLE_CHANGE.value] as CallbackFourth<String?, String?, Int?, Int?>).onEvent(oldTitle, newTitle, from, chatId)
                    }
                }
                "chat_photo_update" -> {
                    val photo = gson.toJsonTree(client.messages.getById(listOf(updateObject.getInt(1))).getAsJsonObject("response").getAsJsonArray("items").getJsonObject(0).getAsJsonArray("attachments").getJsonObject(0).getAsJsonObject("photo")).asJsonObject

                    if (chatCallbacks.containsKey(Events.CHAT_PHOTO_UPDATE.value)) {
                        (chatCallbacks[Events.CHAT_PHOTO_UPDATE.value] as CallbackTriple<JsonObject?, Int?, Int?>).onEvent(photo, from, chatId)
                    }
                }
                "chat_invite_user" -> {
                    val user = Integer.valueOf(attachments.getString("source_mid"))
                    if (chatCallbacks.containsKey(Events.CHAT_JOIN.value)) {
                        (chatCallbacks[Events.CHAT_JOIN.value] as CallbackTriple<Int?, Int?, Int?>).onEvent(from, user, chatId)
                    }
                }
                "chat_kick_user" -> {
                    val user = Integer.valueOf(attachments.getString("source_mid"))
                    if (chatCallbacks.containsKey(Events.CHAT_LEAVE.value)) {
                        (chatCallbacks[Events.CHAT_LEAVE.value] as CallbackTriple<Int?, Int?, Int?>).onEvent(from, user, chatId)
                    }
                }
                "chat_photo_remove" -> {
                    if (chatCallbacks.containsKey(Events.CHAT_PHOTO_REMOVE.value)) {
                        (chatCallbacks[Events.CHAT_PHOTO_REMOVE.value] as CallbackDouble<Int?, Int?>).onEvent(from, chatId)
                    }
                }
            }
        }
    }

    /**
     * Handle every longpoll event
     */
    private fun handleEveryLongPollUpdate(updateObject: JsonArray) {
        if (callbacks.containsKey(Events.EVERY.value)) {
            callbacks[Events.EVERY.value]!!.onResult(updateObject)
        }
    }

    private fun handleMessageUpdate(updateObject: JsonArray) {
        var messageIsAlreadyHandled = false

        val messageId = updateObject.getInt(1)
        var peerId = updateObject.getInt(3)
        val timestamp = updateObject.getInt(4)
        var chatId = 0

        val messageText = updateObject.getString(5)

        val payload = JsonObject()

        val title = updateObject.getJsonObject(6).getString("title")

        val attachments = if (updateObject.size() > 0) if (updateObject.get(7).toString().startsWith("{")) toJsonObject(updateObject[7].toString()) else null else null

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
            handleChatEvents(updateObject)
        }

        // check for commands
        if (client.commands.size > 0) {
            messageIsAlreadyHandled = handleCommands(message)
        }

        if (message.hasFwds()) {
            if (callbacks.containsKey(MessageEvents.MESSAGE_WITH_FORWARDS.value)) {
                callbacks[MessageEvents.MESSAGE_WITH_FORWARDS.value]!!.onResult(message)
            }
            messageIsAlreadyHandled = true

            handleSendTyping(message)
        }

        if (!messageIsAlreadyHandled) {
            when (message.messageType()) {
                Message.MessageType.VOICE -> {
                    if (callbacks.containsKey(MessageEvents.VOICE_MESSAGE.value)) {
                        callbacks[MessageEvents.VOICE_MESSAGE.value]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.STICKER -> {
                    if (callbacks.containsKey(MessageEvents.STICKER_MESSAGE.value)) {
                        callbacks[MessageEvents.STICKER_MESSAGE.value]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.AUDIO -> {
                    if (callbacks.containsKey(MessageEvents.AUDIO_MESSAGE.value)) {
                        callbacks[MessageEvents.AUDIO_MESSAGE.value]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.VIDEO -> {
                    if (callbacks.containsKey(MessageEvents.VIDEO_MESSAGE.value)) {
                        callbacks[MessageEvents.VIDEO_MESSAGE.value]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.DOC -> {
                    if (callbacks.containsKey(MessageEvents.DOC_MESSAGE.value)) {
                        callbacks[MessageEvents.DOC_MESSAGE.value]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.WALL -> {
                    if (callbacks.containsKey(MessageEvents.WALL_MESSAGE.value)) {
                        callbacks[MessageEvents.WALL_MESSAGE.value]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.PHOTO -> {
                    if (callbacks.containsKey(MessageEvents.PHOTO_MESSAGE.value)) {
                        callbacks[MessageEvents.PHOTO_MESSAGE.value]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.LINK -> {
                    if (callbacks.containsKey(MessageEvents.LINK_MESSAGE.value)) {
                        callbacks[MessageEvents.LINK_MESSAGE.value]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.SIMPLE_TEXT -> {
                    if (callbacks.containsKey(MessageEvents.SIMPLE_TEXT_MESSAGE.value)) {
                        callbacks[MessageEvents.SIMPLE_TEXT_MESSAGE.value]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
            }
        }

        if (callbacks.containsKey(MessageEvents.MESSAGE.value) && !messageIsAlreadyHandled) {
            callbacks[MessageEvents.MESSAGE.value]!!.onResult(message)
            handleSendTyping(message)
        }

        if (callbacks.containsKey(MessageEvents.CHAT_MESSAGE.value) && !messageIsAlreadyHandled) {
            callbacks[MessageEvents.CHAT_MESSAGE.value]!!.onResult(message)
        }
    }

    private fun handleOnline(updateObject: JsonArray) {
        val targetId = updateObject.getInt(1)
        val timestamp = updateObject.getInt(3)


        if (callbacks.containsKey(Events.FRIEND_ONLINE.value)) {
            (abstractCallbacks[Events.FRIEND_ONLINE.value] as CallbackDouble<Int, Int>).onEvent(targetId, timestamp)
        }
    }

    private fun handleOffline(updateObject: JsonArray) {
        val targetId = updateObject.getInt(1)
        val timestamp = updateObject.getInt(3)

        if (callbacks.containsKey(Events.FRIEND_OFFLINE.value)) {
            (abstractCallbacks[Events.FRIEND_OFFLINE.value] as CallbackDouble<Int, Int>).onEvent(targetId, timestamp)
        }
    }

    /**
     * Handle dialog with typing user
     */
    private fun handleTypingUpdate(updateObject: JsonArray) {
        if (callbacks.containsKey(Events.TYPING.value)) {
            callbacks[Events.TYPING.value]!!.onResult(updateObject.getInt(1))
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
                    command.callback.onResult(message)
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
            if (!message.isMessageFromChat()) {
                client.messages.setActivity(message.peerId, "typing")
            } else {
                client.messages.setActivity(message.chatIdLong, "typing")
            }
        }
    }
}