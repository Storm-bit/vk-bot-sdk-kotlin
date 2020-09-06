package com.github.stormbit.sdk.longpoll

import com.github.stormbit.sdk.callbacks.CallbackDouble
import com.github.stormbit.sdk.callbacks.CallbackFourth
import com.github.stormbit.sdk.callbacks.CallbackTriple
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.Chat
import com.github.stormbit.sdk.objects.Message
import com.github.stormbit.sdk.utils.Utils.Companion.shift
import org.json.JSONArray
import org.json.JSONObject

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
    private fun handleChatEvents(updateObject: JSONArray) {
        val chatId = updateObject.getInt(3)

        val attachments = if (updateObject.length() > 6) if (updateObject[6].toString().startsWith("{")) JSONObject(updateObject[6].toString()) else null else null ?: return

        // Return if no attachments
        // Because there no events,
        // and because simple chat messages will be handled
        if (attachments!!.has("source_act")) {
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
                    val photo = JSONObject(client.messages.getById(listOf(updateObject.getInt(1))).getJSONObject("response").getJSONArray("items").getJSONObject(0).getJSONArray("attachments").getJSONObject(0).getJSONObject("photo"))

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
    private fun handleEveryLongPollUpdate(updateObject: JSONArray) {
        if (callbacks.containsKey("OnEveryLongPollEventCallback")) {
            callbacks["OnEveryLongPollEventCallback"]!!.onResult(updateObject)
        }
    }

    private fun handleMessageUpdate(updateObject: JSONArray) {
        var messageIsAlreadyHandled = false

        val messageId = updateObject.getInt(1)
        var peerId = updateObject.getInt(3)
        val timestamp = updateObject.getInt(4)
        var chatId = 0

        val messageText = updateObject.getString(5)

        val payload = JSONObject()

        val attachments = if (updateObject.length() > 0) if (updateObject.get(6).toString().startsWith("{")) JSONObject(updateObject[6].toString()) else null else null

        val randomId = if (updateObject.length() > 7) updateObject.getInt(8) else 0

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
                attachments,
                randomId,
                payload
        )

        if (chatId > 0) {
            message.setChatId(chatId)
            message.setChatIdLong(Chat.CHAT_PREFIX + chatId)

            // chat
            handleChatEvents(updateObject)
        }

        // check for commands
        if (client.commands.size > 0) {
            messageIsAlreadyHandled = handleCommands(message)
        }

        if (message.hasFwds()) {
            if (callbacks.containsKey("OnMessageWithFwdsCallback")) {
                callbacks["OnMessageWithFwdsCallback"]!!.onResult(message)
            }
            messageIsAlreadyHandled = true

            handleSendTyping(message)
        }

        if (!messageIsAlreadyHandled) {
            when (message.messageType()) {
                Message.MessageType.VOICE -> {
                    if (callbacks.containsKey("OnVoiceMessageCallback")) {
                        callbacks["OnVoiceMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.STICKER -> {
                    if (callbacks.containsKey("OnStickerMessageCallback")) {
                        callbacks["OnStickerMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.GIF -> {
                    if (callbacks.containsKey("OnGifMessageCallback")) {
                        callbacks["OnGifMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.AUDIO -> {
                    if (callbacks.containsKey("OnAudioMessageCallback")) {
                        callbacks["OnAudioMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.VIDEO -> {
                    if (callbacks.containsKey("OnVideoMessageCallback")) {
                        callbacks["OnVideoMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.DOC -> {
                    if (callbacks.containsKey("OnDocMessageCallback")) {
                        callbacks["OnDocMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.WALL -> {
                    if (callbacks.containsKey("OnWallMessageCallback")) {
                        callbacks["OnWallMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.PHOTO -> {
                    if (callbacks.containsKey("OnPhotoMessageCallback")) {
                        callbacks["OnPhotoMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.LINK -> {
                    if (callbacks.containsKey("OnLinkMessageCallback")) {
                        callbacks["OnLinkMessageCallback"]!!.onResult(message)
                        messageIsAlreadyHandled = true
                        handleSendTyping(message)
                    }
                }
                Message.MessageType.SIMPLE_TEXT -> {
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

    private fun handleOnline(updateObject: JSONArray) {
        val targetId = updateObject.getInt(1)
        val timestamp = updateObject.getInt(3)


        if (callbacks.containsKey("OnFriendOnlineCallback")) {
            (abstractCallbacks["OnFriendOnlineCallback"] as CallbackDouble<Int, Int>).onEvent(targetId, timestamp)
        }
    }

    private fun handleOffline(updateObject: JSONArray) {
        val targetId = updateObject.getInt(1)
        val timestamp = updateObject.getInt(3)

        if (callbacks.containsKey("OnFriendOfflineCallback")) {
            (abstractCallbacks["OnFriendOfflineCallback"] as CallbackDouble<Int, Int>).onEvent(targetId, timestamp)
        }
    }

    /**
     * Handle dialog with typing user
     */
    private fun handleTypingUpdate(updateObject: JSONArray) {
        if (callbacks.containsKey("OnTypingCallback")) {
            callbacks["OnTypingCallback"]!!.onResult(updateObject.getInt(1))
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
                if (message.text.toLowerCase().contains(element.toString().toLowerCase())) {
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
                client.messages.setActivity(message.authorId!!, "typing")
            } else {
                client.messages.setActivity(message.chatIdLong!!, "typing")
            }
        }
    }
}