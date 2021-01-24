package com.github.stormbit.vksdk.vkapi

import com.github.stormbit.vksdk.events.AttachmentEvent
import com.github.stormbit.vksdk.events.MessageEvent
import com.github.stormbit.vksdk.events.ServiceActionEvent
import com.github.stormbit.vksdk.events.chat.*
import com.github.stormbit.vksdk.events.chat.ChatScreenshotEvent
import com.github.stormbit.vksdk.events.message.*
import com.github.stormbit.vksdk.objects.Message
import com.github.stormbit.vksdk.objects.attachments.AttachmentType
import com.github.stormbit.vksdk.objects.models.ServiceAction

data class RoutePath(val passedPath: String, val restOfMessage: CharSequence)

@ContextDsl
open class MessageRoute<E : MessageEvent>(
    open val context: ApiContext,
    open val parent: MessageRoute<*>?,
    open val senderType: Message.SenderType?,
    open val attachmentType: AttachmentType?,
    open val serviceActionType: ServiceAction.Type?,
    open val withForwards: Boolean,
    open val withReply: Boolean,
    open val phrases: List<String>) {

    private var handler: (suspend ApiContext.(E) -> Unit)? = null
    private var interceptor: (suspend ApiContext.(MessageEvent) -> Unit)? = null

    @PublishedApi
    internal val children: MutableSet<MessageRoute<*>> = mutableSetOf()

    @Suppress("unchecked_cast")
    internal suspend fun pass(event: MessageEvent, routePath: RoutePath) {
        handler?.let { context.it(event as E) }

        val iterator = children.iterator()

        while (iterator.hasNext()) {
            val messageRoute = iterator.next()
            val overlapPhrase = messageRoute.phrases.find {
                routePath.restOfMessage.startsWith(it, true)
            } ?: String()

            if (messageRoute.phrases.isNotEmpty() && overlapPhrase.isBlank()) continue
            if (messageRoute.senderType != null && messageRoute.senderType != event.message.senderType) continue
            if (messageRoute.attachmentType != null && !event.message.attachmentTypes.contains(messageRoute.attachmentType)) continue
            if (messageRoute.serviceActionType != null && messageRoute.serviceActionType != event.message.serviceActionType) continue
            if (messageRoute.withForwards && !event.message.hasForwards) continue
            if (messageRoute.withReply && !event.message.hasReply) continue

            var restOfMessage = routePath.restOfMessage.substring(overlapPhrase.length)
            if (overlapPhrase.isNotBlank()) {
                restOfMessage = restOfMessage.trim()
            }

            val newRoute = routePath.passedPath
                .let { if (it.isNotBlank()) "$it " else it }
                .let { it + routePath.restOfMessage.substring(0..overlapPhrase.lastIndex) }
                .let { RoutePath(it, restOfMessage) }

            when {
                messageRoute.senderType != null -> {
                    when (event.message.senderType) {
                        Message.SenderType.USER -> (messageRoute as MessageRoute<UserMessageEvent>).pass(event as UserMessageEvent, newRoute)
                        Message.SenderType.COMMUNITY -> (messageRoute as MessageRoute<CommunityMessageEvent>).pass(event as CommunityMessageEvent, newRoute)
                        Message.SenderType.CHAT -> (messageRoute as MessageRoute<ChatMessageEvent>).pass(event as ChatMessageEvent, newRoute)
                    }
                }

                messageRoute.attachmentType != null -> {
                    when {
                        event.message.isAudioMessage -> (messageRoute as MessageRoute<Audio>).pass(event as Audio, newRoute)
                        event.message.isVideoMessage -> (messageRoute as MessageRoute<Video>).pass(event as Video, newRoute)
                        event.message.isVoiceMessage -> (messageRoute as MessageRoute<Voice>).pass(event as Voice, newRoute)
                        event.message.isPhotoMessage -> (messageRoute as MessageRoute<Photo>).pass(event as Photo, newRoute)
                        event.message.isDocMessage -> (messageRoute as MessageRoute<Document>).pass(event as Document, newRoute)
                        event.message.isStickerMessage -> (messageRoute as MessageRoute<Sticker>).pass(event as Sticker, newRoute)
                    }
                }

                messageRoute.serviceActionType != null -> {
                    when (event.message.serviceActionType) {
                        ServiceAction.Type.CHAT_CREATE -> (messageRoute as MessageRoute<ChatCreate>).pass(event as ChatCreate, newRoute)
                        ServiceAction.Type.CHAT_TITLE_UPDATE -> (messageRoute as MessageRoute<ChatTitleUpdate>).pass(event as ChatTitleUpdate, newRoute)
                        ServiceAction.Type.CHAT_INVITE_USER -> (messageRoute as MessageRoute<ChatJoin>).pass(event as ChatJoin, newRoute)
                        ServiceAction.Type.CHAT_KICK_USER -> (messageRoute as MessageRoute<ChatLeave>).pass(event as ChatLeave, newRoute)
                        ServiceAction.Type.CHAT_PHOTO_UPDATE -> (messageRoute as MessageRoute<ChatPhotoUpdate>).pass(event as ChatPhotoUpdate, newRoute)
                        ServiceAction.Type.CHAT_PHOTO_REMOVE -> (messageRoute as MessageRoute<ChatPhotoRemove>).pass(event as ChatPhotoRemove, newRoute)
                        ServiceAction.Type.CHAT_PIN_MESSAGE -> (messageRoute as MessageRoute<ChatPinMessage>).pass(event as ChatPinMessage, newRoute)
                        ServiceAction.Type.CHAT_UNPIN_MESSAGE -> (messageRoute as MessageRoute<ChatUnpinMessage>).pass(event as ChatUnpinMessage, newRoute)
                        ServiceAction.Type.CHAT_INVITE_USER_BY_LINK -> (messageRoute as MessageRoute<ChatInviteUserByLink>).pass(event as ChatInviteUserByLink, newRoute)
                        ServiceAction.Type.CHAT_SCREENSHOT -> (messageRoute as MessageRoute<ChatInviteUserByLink>).pass(event as ChatScreenshot, newRoute)
                        ServiceAction.Type.CHAT_GROUP_CALL_IN_PROGRESS -> (messageRoute as MessageRoute<ChatGroupCallInProgress>).pass(event as ChatGroupCallInProgress, routePath)
                        ServiceAction.Type.CHAT_INVITE_USER_BY_CALL -> (messageRoute as MessageRoute<ChatInviteUserByLink>).pass(event as ChatInviteUserByCall, routePath)
                    }
                }

                messageRoute.phrases.isNotEmpty() -> {
                    if (messageRoute.phrases.contains(overlapPhrase)) {
                        (messageRoute as MessageRoute<CommandMessageEvent>).pass(CommandMessageEvent(overlapPhrase, restOfMessage.split(" "), event.message), newRoute)
                    }
                }

                else -> messageRoute.pass(event, newRoute)
            }

            return
        }

        interceptor?.let { context.it(event as E) }
    }

    @Suppress("unused")
    fun handle(block: suspend ApiContext.(E) -> Unit) {
        if (handler != null)
            throw Exception("You can set only one handler")
        else handler = block
    }

    @Suppress("unused")
    fun intercept(block: suspend ApiContext.(MessageEvent) -> Unit) {
        if (interceptor != null)
            throw Exception("You can set only one interceptor")
        else interceptor = block
    }
}

@ContextDsl
@Suppress("unused")
open class TypedMessageRoute<E : MessageEvent>(
    context: ApiContext,
    parent: MessageRoute<*>?,
    senderType: Message.SenderType?,
    attachmentType: AttachmentType?,
    phrases: List<String>
) : MessageRoute<E>(context, parent, senderType, attachmentType, null, false, false, phrases) {

    suspend fun onCommand(vararg words: String, block: suspend MessageRoute<CommandMessageEvent>.() -> Unit) {
        onCommand(words.toList(), block)
    }

    suspend fun onCommand(words: List<String>, block: suspend MessageRoute<CommandMessageEvent>.() -> Unit) {
        MessageRoute<CommandMessageEvent>(
            context, this, null, null, null, withForwards = false, withReply = false, words
        ).also { children.add(it) }.block()
    }

    suspend inline fun <reified T : AttachmentEvent> onMessageWith(noinline block: suspend TypedMessageRoute<T>.() -> Unit) {
        val attachmentType = when (T::class) {
            Audio::class -> AttachmentType.AUDIO
            Video::class -> AttachmentType.VIDEO
            Voice::class -> AttachmentType.VOICE
            Photo::class -> AttachmentType.PHOTO
            Document::class -> AttachmentType.DOC
            Sticker::class -> AttachmentType.STICKER
            else -> throw IllegalArgumentException("Unknown attachment type class")
        }

        TypedMessageRoute<T>(context, this, senderType, attachmentType, emptyList())
            .also { children.add(it) }.block()
    }

    suspend fun onMessageWithForwards(block: suspend MessageRoute<MessageEvent>.() -> Unit) {
        MessageRoute<MessageEvent>(context, this, null, null, null,
            withForwards = true,
            withReply = false,
            emptyList()
        ).also { children.add(it) }.block()
    }

    suspend fun onMessageWithReply(block: suspend MessageRoute<MessageEvent>.() -> Unit) {
        MessageRoute<MessageEvent>(context, this, null, null, null,
            withForwards = false,
            withReply = true,
            emptyList()
        ).also { children.add(it) }.block()
    }
}

@ContextDsl
@Suppress("unused")
class DefaultMessageRoute(
    context: ApiContext,
    parent: MessageRoute<*>?,
    phrases: List<String>
) : MessageRoute<MessageEvent>(context, parent, null, null, null, false, false, phrases) {

    @Suppress("unused")
    suspend inline fun <reified T : MessageRoute<*>> onMessageFrom(noinline block: suspend T.() -> Unit) {
        when (T::class) {
            User::class -> (UserMessageRoute(context, parent, phrases).also { children.add(it) } as T).block()
            Chat::class -> (ChatMessageRoute(context, parent, phrases).also { children.add(it) } as T).block()
            Community::class -> (ChatMessageRoute(context, parent, phrases).also { children.add(it) } as T).block()
            else -> throw IllegalArgumentException("Unknown sender type class")
        }
    }

    suspend fun onCommand(vararg words: String, block: suspend MessageRoute<CommandMessageEvent>.() -> Unit) {
        onCommand(words.toList(), block)
    }

    suspend fun onCommand(words: List<String>, block: suspend MessageRoute<CommandMessageEvent>.() -> Unit) {
        MessageRoute<CommandMessageEvent>(
            context, this, null, null, null, withForwards = false, withReply = false, words
        ).also { children.add(it) }.block()
    }

    suspend inline fun <reified T : AttachmentEvent> onMessageWith(noinline block: suspend TypedMessageRoute<T>.() -> Unit) {
        val attachmentType = when (T::class) {
            Audio::class -> AttachmentType.AUDIO
            Video::class -> AttachmentType.VIDEO
            Voice::class -> AttachmentType.VOICE
            Photo::class -> AttachmentType.PHOTO
            Document::class -> AttachmentType.DOC
            Sticker::class -> AttachmentType.STICKER
            else -> throw IllegalArgumentException("Unknown attachment type class")
        }

        TypedMessageRoute<T>(context, this, null, attachmentType, emptyList())
            .also { children.add(it) }.block()
    }

    suspend fun onMessageWithForwards(block: suspend MessageRoute<MessageEvent>.() -> Unit) {
        MessageRoute<MessageEvent>(context, this, null, null, null,
            withForwards = true,
            withReply = false,
            emptyList()
        ).also { children.add(it) }.block()
    }

    suspend fun onMessageWithReply(block: suspend MessageRoute<MessageEvent>.() -> Unit) {
        MessageRoute<MessageEvent>(context, this, null, null, null,
            withForwards = false,
            withReply = true,
            emptyList()
        ).also { children.add(it) }.block()
    }
}

@ContextDsl
class ServiceActionMessageRoute<E : ServiceActionEvent>(
    context: ApiContext,
    parent: MessageRoute<*>?,
    serviceActionType: ServiceAction.Type
) : MessageRoute<E>(context, parent, null, null, serviceActionType, false, false, emptyList())


@ContextDsl
class ChatMessageRoute(
    context: ApiContext,
    parent: MessageRoute<*>?,
    phrases: List<String>,
    override val senderType: Message.SenderType = Message.SenderType.CHAT
) : TypedMessageRoute<ChatMessageEvent>(context, parent, senderType, null, phrases) {

    @Suppress("unchecked_cast")
    suspend inline fun <reified T : ServiceActionEvent> onServiceAction(noinline block: suspend ServiceActionMessageRoute<T>.() -> Unit) {
        val serviceActionType = when (T::class) {
            ChatCreate::class -> ServiceAction.Type.CHAT_CREATE
            ChatTitleUpdate::class -> ServiceAction.Type.CHAT_TITLE_UPDATE
            ChatJoin::class -> ServiceAction.Type.CHAT_INVITE_USER
            ChatLeave::class -> ServiceAction.Type.CHAT_KICK_USER
            ChatPhotoUpdate::class -> ServiceAction.Type.CHAT_PHOTO_UPDATE
            ChatPhotoRemove::class -> ServiceAction.Type.CHAT_PHOTO_REMOVE
            ChatPinMessage::class -> ServiceAction.Type.CHAT_PIN_MESSAGE
            ChatUnpinMessage::class -> ServiceAction.Type.CHAT_UNPIN_MESSAGE
            else -> throw IllegalArgumentException("Unknown service action type class")
        }

        ServiceActionMessageRoute<T>(context, this, serviceActionType)
            .also { children.add(it) }.block()
    }
}

@ContextDsl
class UserMessageRoute(
    context: ApiContext,
    parent: MessageRoute<*>?,
    phrases: List<String>,
    override val senderType: Message.SenderType = Message.SenderType.USER
) : TypedMessageRoute<UserMessageEvent>(context, parent, null, null, phrases)

@ContextDsl
class CommunityMessageRoute(
    context: ApiContext,
    parent: MessageRoute<*>?,
    phrases: List<String>,
    override val senderType: Message.SenderType = Message.SenderType.COMMUNITY
) : TypedMessageRoute<UserMessageEvent>(context, parent, null, null, phrases)


typealias User = UserMessageRoute
typealias Chat = ChatMessageRoute
typealias Community = CommunityMessageRoute

typealias Audio = AudioMessageEvent
typealias Video = VideoMessageEvent
typealias Voice = VoiceMessageEvent
typealias Photo = PhotoMessageEvent
typealias Document = DocumentMessageEvent
typealias Sticker = StickerMessageEvent

typealias ChatCreate = ChatCreateEvent
typealias ChatTitleUpdate = ChatTitleUpdateEvent
typealias ChatJoin = ChatJoinEvent
typealias ChatLeave = ChatLeaveEvent
typealias ChatPhotoUpdate = ChatPhotoUpdateEvent
typealias ChatPhotoRemove = ChatPhotoRemoveEvent
typealias ChatPinMessage = ChatPinMessageEvent
typealias ChatUnpinMessage = ChatUnpinMessageEvent
typealias ChatInviteUserByLink = ChatInviteUserByLinkEvent
typealias ChatScreenshot = ChatScreenshotEvent
typealias ChatInviteUserByCall = ChatInviteUserByCallEvent
typealias ChatGroupCallInProgress = ChatGroupCallInProgressEvent
