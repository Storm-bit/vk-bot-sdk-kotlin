package com.github.stormbit.vksdk.objects

import com.github.stormbit.vksdk.clients.Client
import com.github.stormbit.vksdk.objects.attachments.AttachmentType
import com.github.stormbit.vksdk.objects.attachments.Audio
import com.github.stormbit.vksdk.objects.attachments.Document
import com.github.stormbit.vksdk.objects.attachments.Photo
import com.github.stormbit.vksdk.objects.attachments.Video
import com.github.stormbit.vksdk.objects.attachments.Voice
import com.github.stormbit.vksdk.objects.models.Keyboard
import com.github.stormbit.vksdk.objects.models.MessagePayload
import com.github.stormbit.vksdk.objects.models.ServiceAction
import com.github.stormbit.vksdk.utils.isGroupId
import com.github.stormbit.vksdk.utils.isUserPeerId
import com.github.stormbit.vksdk.utils.mediaString
import com.github.stormbit.vksdk.vkapi.execute
import com.github.stormbit.vksdk.vkapi.methods.Attachment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.properties.Delegates
import com.github.stormbit.vksdk.objects.models.Message as MessageModel

@Suppress("unused", "MemberVisibilityCanBePrivate", "RegExpDuplicateCharacterInClass")
class Message {
    private val log = LoggerFactory.getLogger(Message::class.java)

    var messageId: Int = 0
        private set

    val senderType: SenderType? get() = getSenderType(peerId)
    var serviceActionType: ServiceAction.Type? = null

    var timestamp: Int = 0
        private set

    var chatId: Int = 0
    var chatIdLong: Int = 0

    var peerId: Int = 0
    var randomId: Int = 0
    var stickerId: Int = 0
    var replyTo: Int? = null

    var keyboard: Keyboard? = null

    var payload: MessagePayload = MessagePayload("{}")

    var text: CharSequence = ""

    var client: Client by Delegates.notNull()


    /**
     * Attachments in format of received event senderType LongPoll server
     *
     * @see: https://vk.com/dev/using_longpoll_2
     */
    internal var receivedAttachments = Attachments()
    internal var receivedForwards: List<MessageModel.Forward> = emptyList()
    internal var receivedReplyMessage: MessageModel.Forward? = null

    internal val attachmentTypes = CopyOnWriteArrayList<AttachmentType>()

    /**
     * Attachments in format [photo62802565_456241137, photo111_111, doc100_500]
     */
    internal val attachmentsToSend = CopyOnWriteArrayList<String>()
    internal val forwardedMessagesToSend = CopyOnWriteArrayList<Int>()

    /**
     * Constructor for sent message
     */
    constructor()

    /**
     * Constructor for received message
     *
     * @param client client
     * @param messageId message id
     * @param peerId peer id
     * @param timestamp timestamp
     * @param text message text
     * @param attachments message items
     * @param randomId random id
     */
    constructor(client: Client, messageId: Int, peerId: Int, timestamp: Int, text: String, attachments: Attachments, randomId: Int, payload: MessagePayload) {
        this.messageId = messageId
        this.peerId = peerId
        this.timestamp = timestamp
        this.text = text
        this.receivedAttachments = attachments
        this.stickerId = attachments.getStickerId()
        this.randomId = randomId
        this.payload = payload
        this.client = client
    }

    constructor(client: Client, message: MessageModel) {
        this.client = client
        this.messageId = message.id
        this.peerId = message.peerId
        this.timestamp = message.date
        this.text = message.text
        this.randomId = message.randomId ?: 0
        this.receivedForwards = message.forwardedMessages
        this.receivedReplyMessage = message.replyMessage

        if (message.attachments.isNotEmpty()) {
            val attachs = ArrayList<Attachments.Item>()

            message.attachments.forEach {
                var attach: String? = null
                var type: AttachmentType? = null

                if (it.photo != null) {
                    attach = it.photo.mediaString
                    type = it.type
                    attachmentTypes.add(AttachmentType.PHOTO)
                }

                if (it.video != null) {
                    attach = it.video.mediaString
                    type = it.type
                    attachmentTypes.add(AttachmentType.VIDEO)
                }

                if (it.audio != null) {
                    attach = it.audio.mediaString
                    type = it.type
                    attachmentTypes.add(AttachmentType.AUDIO)
                }

                if (it.document != null) {
                    attach = it.document.mediaString
                    type = it.type
                    attachmentTypes.add(AttachmentType.DOC)
                }

                if (it.voice != null) {
                    attach = it.voice.mediaString
                    type = it.type
                    attachmentTypes.add(AttachmentType.VOICE)
                }

                if (it.sticker != null) {
                    attach = "sticker"
                    type = it.type
                    attachmentTypes.add(AttachmentType.STICKER)
                }

                attachs.add(Attachments.Item(attach!!, type!!))
            }

            this.receivedAttachments = Attachments(attachs, message.forwardedMessages.isNotEmpty())
        }

        if (message.serviceAction != null) {
            this.serviceActionType = message.serviceAction.type
        }

        if (message.payload != null) this.payload = message.payload

        if (this.peerId > Chat.CHAT_PREFIX) {
            this.chatId = this.peerId - Chat.CHAT_PREFIX
            this.peerId = message.fromId

            this.messageId = message.conversationMessageId
        }

        if (this.chatId > 0) {
            this.chatIdLong = this.chatId + Chat.CHAT_PREFIX
        }
    }

    constructor(client: Client, block: Message.() -> Unit) {
        this.client = client
        block()
    }

    /**
     * Send the message
     */
    suspend fun send(): Int {
        return client.messages.send(
            peerId = peerId,
            randomId = randomId,
            text = text,
            stickerId = stickerId,
            payload = payload,
            attachments = attachmentsToSend,
            keyboard = keyboard,
            replyToMessageId = replyTo,
            forwardedMessages = forwardedMessagesToSend
        ).execute()
    }

    /**
     * @return true if message has forwarded messages
     */
    val hasForwards: Boolean get() = receivedAttachments.hasFwd || receivedForwards.isNotEmpty()

    /**
     * @return true if message has reply message
     */
    val hasReply: Boolean get() = receivedReplyMessage != null


    val replyMessage get() = receivedReplyMessage
    val forwardedMessages get() = forwardedMessagesToSend
    val attachments get() = attachmentsToSend

    /**
     * @param T Attachment type: Audio, Video, Photo, Voice, Document
     * @return List of T
     */
    @PublishedApi
    internal fun <T : Attachment> getAttachments(type: Class<T>): List<String> {
        val attachs = receivedAttachments.items

        if (attachs.isNotEmpty()) {
            return when (type) {
                Audio::class.java -> {
                    attachs
                        .filter { it.typeAttachment == AttachmentType.AUDIO }
                        .map { it.attach }
                }

                Photo::class.java -> {
                    attachs
                        .filter { it.typeAttachment == AttachmentType.PHOTO }
                        .map { it.attach }
                }

                Document::class.java -> {
                    attachs
                        .filter { it.typeAttachment == AttachmentType.DOC }
                        .map { it.attach }
                }

                Voice::class.java -> {
                    attachs
                        .filter { it.typeAttachment == AttachmentType.VOICE }
                        .map { it.attach }
                }

                Video::class.java -> {
                    attachs
                        .filter { it.typeAttachment == AttachmentType.VIDEO }
                        .map { it.attach }
                }

                else -> emptyList()
            }
        }

        return emptyList()
    }

    /**
     * Get items senderType message
     *
     * @return JSONArray items
     */
    inline fun <reified T : Attachment> getAttachments(): List<String> {
        return getAttachments(T::class.java)
    }

    val isPhotoMessage: Boolean get() = attachmentTypes.contains(AttachmentType.PHOTO)

    val isSimpleTextMessage: Boolean get() = attachmentTypes.isEmpty()

    val isVoiceMessage: Boolean get() = attachmentTypes.contains(AttachmentType.VOICE)

    val isAudioMessage: Boolean get() = attachmentTypes.contains(AttachmentType.AUDIO)

    val isVideoMessage: Boolean get() = attachmentTypes.contains(AttachmentType.VIDEO)

    val isDocMessage: Boolean get() = attachmentTypes.contains(AttachmentType.DOC)

    val isWallMessage: Boolean get() = attachmentTypes.contains(AttachmentType.WALL)

    val isStickerMessage: Boolean get() = attachmentTypes.contains(AttachmentType.STICKER)

    val isLinkMessage: Boolean get() = attachmentTypes.contains(AttachmentType.LINK)


    val isMessageFromChat: Boolean
        get() = chatId > 0 || chatIdLong > 0

    private fun getSenderType(peerId: Int): SenderType? {
        return when {
            peerId.isGroupId -> SenderType.COMMUNITY
            peerId.isUserPeerId && (chatIdLong == 0 || chatId == 0) -> SenderType.USER
            chatIdLong != 0 || chatId != 0 -> SenderType.CHAT
            else -> null
        }
    }

    override fun toString(): String {
        return buildString {
            append("{")
            append("\"message_id\": $messageId,")
            append("\"peer_id\": $peerId,")
            append("\"timestamp\": $timestamp,")
            append("\"random_id\": $randomId,")
            append("\"text\": \"$text\",")
            append("\"items\": $receivedAttachments,")
            append("\"payload\": \"${payload.value}\"")
            append("}")
        }
    }

    enum class SenderType {
        USER,
        COMMUNITY,
        CHAT
    }

    @Serializable
    data class Attachments(
        @SerialName("items") val items: List<Item> = emptyList(),
        @SerialName("fwd") val hasFwd: Boolean = false,
        @SerialName("senderType") val from: Int? = null) {

        @Serializable
        data class Item(
            @SerialName("attach") val attach: String,
            @SerialName("attach_type") val typeAttachment: AttachmentType,
            @SerialName("attach_kind") val kind: String? = null
        )

        fun toArray(): Array<String> {
            return items.filter { it.typeAttachment != AttachmentType.STICKER }.map { it.typeAttachment.value + it.attach }.toTypedArray()
        }

        fun getStickerId(): Int {
            val list = items.filter { it.typeAttachment == AttachmentType.STICKER }

            return if (list.isNotEmpty()) return list[0].attach.toInt() else 0
        }
    }
}