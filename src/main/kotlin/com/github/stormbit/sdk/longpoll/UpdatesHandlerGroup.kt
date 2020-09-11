package com.github.stormbit.sdk.longpoll

import com.github.stormbit.sdk.callbacks.CallbackDouble
import com.github.stormbit.sdk.callbacks.CallbackFourth
import com.github.stormbit.sdk.callbacks.CallbackTriple
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.Chat
import com.github.stormbit.sdk.objects.Message
import com.github.stormbit.sdk.utils.Utils.Companion.shift
import org.json.JSONObject

class UpdatesHandlerGroup(private val client: Client) : UpdatesHandler(client) {
    override fun handleCurrentUpdate() {
        val currentUpdate: JSONObject

        if (queue.updatesGroup.isEmpty()) {
            return
        } else {
            currentUpdate = queue.updatesGroup.shift()!!
        }

        val updateType = Events[currentUpdate.getString("type")]!!

        val obj = currentUpdate.getJSONObject("object")

        if (callbacks.containsKey(updateType.value)) {
            callbacks[updateType.value]?.onResult(obj)
        }

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

            else -> {
                handleEveryLongPollUpdate(currentUpdate)
            }
        }
    }

    /**
     * Handle chat events
     */
    private fun handleChatEvents(updateObject: JSONObject) {
        val chatId = updateObject.getInt("peer_id")
        val attachments = (if (updateObject.getJSONArray("attachments").length() > 0) updateObject.getJSONArray("attachments").getJSONObject(0) else null)
                ?: return

        // Return if no attachments
        // Because there no events,
        // and because simple chat messages will be handled
        if (attachments.has("source_act")) {
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
                    val photo = client.messages.getById(listOf(updateObject.getInt("conversation_message_id"))).getJSONObject("response").getJSONArray("items").getJSONObject(0).getJSONArray("attachments").getJSONObject(0).getJSONObject("photo")

                    if (chatCallbacks.containsKey(Events.CHAT_PHOTO_UPDATE.value)) {
                        (chatCallbacks[Events.CHAT_PHOTO_UPDATE.value] as CallbackTriple<JSONObject?, Int?, Int?>).onEvent(photo, from, chatId)
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
    private fun handleEveryLongPollUpdate(updateObject: JSONObject) {
        if (callbacks.containsKey(Events.EVERY.value)) {
            callbacks[Events.EVERY.value]!!.onResult(updateObject)
        }
    }

    /**
     * Handle new message
     */
    private fun handleMessageUpdate(updateObject: JSONObject) {

        // Flag
        var messageIsAlreadyHandled = false

        // All necessary data
        var messageId = updateObject.getInt("id")
        var peerId = updateObject.getInt("peer_id")
        var chatId = 0
        val timestamp = updateObject.getInt("date")
        val messageText = updateObject.getString("text")
        val payload = if (updateObject.has("payload")) JSONObject(updateObject.getString("payload")) else JSONObject()
        val attachments = if (updateObject.getJSONArray("attachments").length() > 0) updateObject.getJSONArray("attachments").getJSONObject(0) else null
        val randomId = updateObject.getInt("random_id")

        // Check for chat
        if (peerId > Chat.CHAT_PREFIX) {
            chatId = peerId - Chat.CHAT_PREFIX
            if (attachments != null) {
                peerId = attachments.getString("from").toInt()
            }
            messageId = updateObject.getInt("conversation_message_id")
        }
        val message = Message(
                client,
                messageId,
                peerId,
                timestamp,
                messageText,
                " ... ",
                attachments,
                randomId,
                payload
        )
        if (chatId > 0) {
            message.chatId = chatId
            message.chatIdLong = Chat.CHAT_PREFIX + chatId

            // chat events
            handleChatEvents(updateObject)
        }

        // check for commands
        if (client.commands.size > 0) {
            messageIsAlreadyHandled = handleCommands(message)
        }
        if (message.hasFwds()) {
            if (callbacks.containsKey(MessageEvents.MESSAGE_WITH_FORWARDS.value)) {
                callbacks[MessageEvents.MESSAGE_WITH_FORWARDS.value]!!.onResult(message)
                messageIsAlreadyHandled = true
                handleSendTyping(message)
            }
        }
        if (!messageIsAlreadyHandled) {
            when (message.messageType()) {
                Message.MessageType.VOICE -> {
                    if (callbacks.containsKey(MessageEvents.VOICE_MESSAGE.value)) {
                        callbacks[MessageEvents.VIDEO_MESSAGE.value]!!.onResult(message)
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

    /**
     * Handle dialog with typing user
     */
    private fun handleTypingUpdate(updateObject: JSONObject) {
        if (callbacks.containsKey("OnTypingCallback")) {
            callbacks["OnTypingCallback"]!!.onResult(updateObject.getString("from_id"))
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