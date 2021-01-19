package com.github.stormbit.sdk.longpoll.updateshandlers

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.events.*
import com.github.stormbit.sdk.events.chat.*
import com.github.stormbit.sdk.events.message.ChatMessageEvent
import com.github.stormbit.sdk.events.message.CommunityMessageEvent
import com.github.stormbit.sdk.events.message.UserMessageEvent
import com.github.stormbit.sdk.longpoll.events.Events
import com.github.stormbit.sdk.objects.Message
import com.github.stormbit.sdk.objects.models.MessageEvent
import com.github.stormbit.sdk.objects.models.ServiceAction
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.utils.json
import com.github.stormbit.sdk.vkapi.RoutePath
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import com.github.stormbit.sdk.objects.models.Message as MessageModel

class UpdatesHandlerGroup(private val client: Client) : UpdatesHandler<JsonObject>(client) {

    override suspend fun handleCurrentUpdate(currentUpdate: JsonObject) {
        val event = currentUpdate.getString("type")!!

        val updateType = Events.fromString(event)

        val obj = currentUpdate.getJsonObject("object")!!

        when (updateType) {
            Events.MESSAGE_NEW -> {
                // check if message is received
                GlobalScope.launch { handleMessageUpdate(obj) }

                // handle every
                handleEveryLongPollUpdate(obj)
            }

            Events.MESSAGE_TYPING_STATE -> {
                GlobalScope.launch  { handleTypingUpdate(obj) }

                // handle every
                handleEveryLongPollUpdate(currentUpdate)
            }

            Events.MESSAGE_EVENT -> {
                GlobalScope.launch  { handleMessageEvent(obj) }

                handleEveryLongPollUpdate(currentUpdate)
            }

            else -> GlobalScope.launch { handleEveryLongPollUpdate(currentUpdate) }
        }
    }

    private suspend fun handleEveryLongPollUpdate(obj: JsonObject) {
        events[Events.EVERY.value]?.invoke(EveryEvent(obj))
    }

    /**
     * Handle chat events
     */
    private suspend fun handleChatEvents(messageModel: MessageModel, message: Message) : Boolean {
        if (!messageModel.isServiceAction) return false

        val action = messageModel.serviceAction!!
        val fromId = messageModel.fromId
        val chatId = messageModel.peerId

        try {
            when (action.type) {
                ServiceAction.Type.CHAT_CREATE -> {
                    client.messageHandler?.pass(
                        ChatCreateEvent(
                            "",
                            fromId,
                            chatId,
                            message
                        ),
                        RoutePath(String(), message.text)
                    )
                }

                ServiceAction.Type.CHAT_TITLE_UPDATE -> {
                    client.messageHandler?.pass(
                        ChatTitleUpdateEvent(
                            "",
                            action.conversationTitle!!,
                            fromId,
                            chatId,
                            message
                        ),
                        RoutePath(String(), message.text)
                    )
                }

                ServiceAction.Type.CHAT_PHOTO_UPDATE -> {
                    val photo = messageModel.attachments[0].photo!!

                    client.messageHandler?.pass(
                        ChatPhotoUpdateEvent(
                            photo.attachmentString,
                            fromId,
                            chatId,
                            message
                        ),
                        RoutePath(String(), message.text)
                    )
                }

                ServiceAction.Type.CHAT_INVITE_USER -> {
                    client.messageHandler?.pass(
                        ChatJoinEvent(
                            fromId,
                            action.memberId!!,
                            chatId,
                            message
                        ),
                        RoutePath(String(), message.text)
                    )
                }

                ServiceAction.Type.CHAT_KICK_USER -> {
                    client.messageHandler?.pass(
                        ChatLeaveEvent(
                            fromId,
                            action.memberId!!,
                            chatId,
                            message
                        ),
                        RoutePath(String(), message.text)
                    )
                }

                ServiceAction.Type.CHAT_PHOTO_REMOVE -> {
                    client.messageHandler?.pass(
                        ChatPhotoRemoveEvent(
                            fromId,
                            chatId,
                            message
                        ),
                        RoutePath(String(), message.text)
                    )
                }

                ServiceAction.Type.CHAT_PIN_MESSAGE -> {
                    client.messageHandler?.pass(
                        ChatPinMessageEvent(
                            fromId,
                            chatId,
                            messageModel.conversationMessageId,
                            message
                        ),
                        RoutePath(String(), message.text)
                    )
                }

                ServiceAction.Type.CHAT_UNPIN_MESSAGE -> {
                    client.messageHandler?.pass(
                        ChatUnpinMessageEvent(
                            fromId,
                            chatId,
                            messageModel.conversationMessageId,
                            message
                        ),
                        RoutePath(String(), message.text)
                    )
                }

                ServiceAction.Type.CHAT_INVITE_USER_BY_LINK -> {
                    client.messageHandler?.pass(
                        ChatInviteUserByLinkEvent(
                            fromId,
                            message
                        ),
                        RoutePath(String(), message.text)
                    )
                }

                ServiceAction.Type.CHAT_SCREENSHOT -> {
                    client.messageHandler?.pass(
                        ChatScreenshotEvent(
                            fromId,
                            message
                        ),
                        RoutePath(String(), message.text)
                    )
                }

                ServiceAction.Type.CHAT_GROUP_CALL_IN_PROGRESS -> {
                    client.messageHandler?.pass(
                        ChatGroupCallInProgressEvent(
                            message
                        ),
                        RoutePath(String(), message.text)
                    )
                }

                ServiceAction.Type.CHAT_INVITE_USER_BY_CALL -> {
                    client.messageHandler?.pass(
                        ChatInviteUserByCallEvent(
                            fromId,
                            message
                        ),
                        RoutePath(String(), message.text)
                    )
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
    private suspend fun handleMessageUpdate(updateObject: JsonObject) {
        try {
            val messageObject = json.decodeFromJsonElement(MessageModel.serializer(), updateObject)

            val message = Message(messageObject)

            if (messageObject.serviceAction != null) {
                handleChatEvents(messageObject, message)
            } else {
                when (message.senderType) {
                    Message.SenderType.COMMUNITY -> {
                        client.messageHandler?.pass(CommunityMessageEvent(message), RoutePath(String(), message.text))
                    }

                    Message.SenderType.USER -> {
                        client.messageHandler?.pass(UserMessageEvent(message), RoutePath(String(), message.text))
                    }

                    Message.SenderType.CHAT -> {
                        client.messageHandler?.pass(ChatMessageEvent(message), RoutePath(String(), message.text))
                    }
                }
            }
        } catch (e: Exception) {
            log.error("Some error occurred when parsing message event, error is: ", e)
        }
    }

    /**
     * Handle message event
     */
    private suspend fun handleMessageEvent(updateObject: JsonObject) {
        val messageEvent = json.decodeFromJsonElement(MessageEvent.serializer(), updateObject)

        GlobalScope.launch { events[Events.MESSAGE_EVENT.value]?.invoke(messageEvent) }
    }

    /**
     * Handle dialog with typing user
     */
    private suspend fun handleTypingUpdate(updateObject: JsonObject) {
        events[Events.TYPING.value]?.invoke(TypingEvent(updateObject.getString("from_id")!!.toInt()))
    }
}
