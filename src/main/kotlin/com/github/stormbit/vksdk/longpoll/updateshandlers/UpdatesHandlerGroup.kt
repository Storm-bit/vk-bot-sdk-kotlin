package com.github.stormbit.vksdk.longpoll.updateshandlers

import com.github.stormbit.vksdk.clients.Client
import com.github.stormbit.vksdk.events.*
import com.github.stormbit.vksdk.events.chat.*
import com.github.stormbit.vksdk.events.message.ChatMessageEvent
import com.github.stormbit.vksdk.events.message.CommunityMessageEvent
import com.github.stormbit.vksdk.events.message.UserMessageEvent
import com.github.stormbit.vksdk.longpoll.events.Events
import com.github.stormbit.vksdk.objects.Message
import com.github.stormbit.vksdk.objects.models.MessageEvent
import com.github.stormbit.vksdk.objects.models.ServiceAction
import com.github.stormbit.vksdk.utils.*
import com.github.stormbit.vksdk.utils.json
import com.github.stormbit.vksdk.vkapi.RoutePath
import kotlinx.serialization.json.JsonObject
import com.github.stormbit.vksdk.objects.models.Message as MessageModel

class UpdatesHandlerGroup(private val client: Client) : UpdatesHandler<JsonObject>() {

    override suspend fun handleCurrentUpdate(currentUpdate: JsonObject) {
        val event = currentUpdate.getString("type")!!

        val updateType = Events.fromString(event)

        val obj = currentUpdate.getJsonObject("object")!!

        when (updateType) {
            Events.MESSAGE_NEW -> {
                // check if message is received
                handleMessageUpdate(obj)

                // handle every
                handleEveryLongPollUpdate(obj)
            }

            Events.MESSAGE_EVENT -> {
                handleMessageEvent(obj)

                // handle every
                handleEveryLongPollUpdate(currentUpdate)
            }

            else -> handleEveryLongPollUpdate(currentUpdate)
        }
    }

    private suspend fun handleEveryLongPollUpdate(obj: JsonObject) {
        events[Events.EVERY.value]?.invoke(EveryEvent(obj))
    }

    /**
     * Handle chat events
     */
    private suspend fun handleChatEvents(messageModel: MessageModel, message: Message) {
        if (!messageModel.isServiceAction) return

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
    }

    /**
     * Handle new message
     */
    private suspend fun handleMessageUpdate(updateObject: JsonObject) {
        try {
            val messageObject = json.decodeFromJsonElement(MessageModel.serializer(), updateObject)

            val message = Message(client, messageObject)

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

        events[Events.MESSAGE_EVENT.value]?.invoke(messageEvent)
    }
}
