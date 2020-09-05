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

        val updateType = Events[currentUpdate.getString("type")]

        val obj = currentUpdate.getJSONObject("object")

        if (callbacks.containsKey(updateType.type)) {
            callbacks[updateType.type]?.onResult(obj)
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

                    if (chatCallbacks.containsKey("onChatCreatedCallback")) {
                        (chatCallbacks["onChatCreatedCallback"] as CallbackTriple<String?, Int?, Int?>).onEvent(title, from, chatId)
                    }
                }
                "chat_title_update" -> {
                    val oldTitle = attachments.getString("source_old_text")
                    val newTitle = attachments.getString("source_text")

                    if (chatCallbacks.containsKey("OnChatTitleChangedCallback")) {
                        (chatCallbacks["OnChatTitleChangedCallback"] as CallbackFourth<String?, String?, Int?, Int?>).onEvent(oldTitle, newTitle, from, chatId)
                    }
                }
                "chat_photo_update" -> {
                    val photo = client.messages.getById(listOf(updateObject.getInt("conversation_message_id"))).getJSONObject("response").getJSONArray("items").getJSONObject(0).getJSONArray("attachments").getJSONObject(0).getJSONObject("photo")

                    if (chatCallbacks.containsKey("onChatPhotoChangedCallback")) {
                        (chatCallbacks["onChatPhotoChangedCallback"] as CallbackTriple<JSONObject?, Int?, Int?>).onEvent(photo, from, chatId)
                    }
                }
                "chat_invite_user" -> {
                    val user = Integer.valueOf(attachments.getString("source_mid"))

                    if (chatCallbacks.containsKey("OnChatJoinCallback")) {
                        (chatCallbacks["OnChatJoinCallback"] as CallbackTriple<Int?, Int?, Int?>).onEvent(from, user, chatId)
                    }
                }
                "chat_kick_user" -> {
                    val user = Integer.valueOf(attachments.getString("source_mid"))

                    if (chatCallbacks.containsKey("OnChatLeaveCallback")) {
                        (chatCallbacks["OnChatLeaveCallback"] as CallbackTriple<Int?, Int?, Int?>).onEvent(from, user, chatId)
                    }
                }
                "chat_photo_remove" -> {
                    if (chatCallbacks.containsKey("onChatPhotoRemovedCallback")) {
                        (chatCallbacks["onChatPhotoRemovedCallback"] as CallbackDouble<Int?, Int?>).onEvent(from, chatId)
                    }
                }
            }
        }
    }

    /**
     * Handle every longpoll event
     */
    private fun handleEveryLongPollUpdate(updateObject: JSONObject) {
        if (callbacks.containsKey("OnEveryLongPollEventCallback")) {
            callbacks["OnEveryLongPollEventCallback"]!!.onResult(updateObject)
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
                attachments,
                randomId,
                payload
        )
        if (chatId > 0) {
            message.setChatId(chatId)
            message.setChatIdLong(Chat.CHAT_PREFIX + chatId)

            // chat events
            handleChatEvents(updateObject)
        }

        // check for commands
        if (client.commands.size > 0) {
            messageIsAlreadyHandled = handleCommands(message)
        }
        if (message.hasFwds()) {
            if (callbacks.containsKey("OnMessageWithFwdsCallback")) {
                callbacks["OnMessageWithFwdsCallback"]!!.onResult(message)
                messageIsAlreadyHandled = true
                handleSendTyping(message)
            }
        }
        if (!messageIsAlreadyHandled) {
            when (message.messageType()) {
                "voiceMessage" -> {
                    if (callbacks.containsKey("OnVoiceMessageCallback")) {
                        callbacks["OnVoiceMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                "stickerMessage" -> {
                    if (callbacks.containsKey("OnStickerMessageCallback")) {
                        callbacks["OnStickerMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                "gifMessage" -> {
                    if (callbacks.containsKey("OnGifMessageCallback")) {
                        callbacks["OnGifMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                "audioMessage" -> {
                    if (callbacks.containsKey("OnAudioMessageCallback")) {
                        callbacks["OnAudioMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                "videoMessage" -> {
                    if (callbacks.containsKey("OnVideoMessageCallback")) {
                        callbacks["OnVideoMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                "docMessage" -> {
                    if (callbacks.containsKey("OnDocMessageCallback")) {
                        callbacks["OnDocMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                "wallMessage" -> {
                    if (callbacks.containsKey("OnWallMessageCallback")) {
                        callbacks["OnWallMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                "photoMessage" -> {
                    if (callbacks.containsKey("OnPhotoMessageCallback")) {
                        callbacks["OnPhotoMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                "linkMessage" -> {
                    if (callbacks.containsKey("OnLinkMessageCallback")) {
                        callbacks["OnLinkMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                "simpleTextMessage" -> {
                    if (callbacks.containsKey("OnSimpleTextMessageCallback")) {
                        callbacks["OnSimpleTextMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
            }
        }
        if (callbacks.containsKey("OnMessageCallback") && !messageIsAlreadyHandled) {
            callbacks["OnMessageCallback"]!!.onResult(message)
            handleSendTyping(message)
        }
        if (callbacks.containsKey("OnChatMessageCallback") && !messageIsAlreadyHandled) {
            callbacks["OnChatMessageCallback"]!!.onResult(message)
        }
        if (callbacks.containsKey("OnEveryMessageCallback")) {
            callbacks["OnEveryMessageCallback"]!!.onResult(message)
            handleSendTyping(message)
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
        var `is` = false
        for (command in client.commands) {
            for (i in 0 until command.commands.size) {
                if (message.text.toLowerCase().contains(command.commands[i].toString().toLowerCase())) {
                    command.callback.onResult(message)
                    `is` = true
                    handleSendTyping(message)
                }
            }
        }
        return `is`
    }

    /**
     * Send typing
     */
    private fun handleSendTyping(message: Message) {

        // Send typing
        if (sendTyping) {
            if (!message.isMessageFromChat()) {
                client.messages.setActivity(message.authorId()!!, "typing")
            } else {
                client.messages.setActivity(message.chatIdLong!!, "typing")
            }
        }
    }

}