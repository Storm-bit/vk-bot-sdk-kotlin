package com.github.stormbit.sdk.objects

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.clients.GroupClient
import com.github.stormbit.sdk.objects.attachments.AttachmentType
import com.github.stormbit.sdk.objects.attachments.Audio
import com.github.stormbit.sdk.objects.attachments.Document
import com.github.stormbit.sdk.objects.attachments.Photo
import com.github.stormbit.sdk.objects.attachments.Video
import com.github.stormbit.sdk.objects.attachments.Voice
import com.github.stormbit.sdk.objects.models.ExtendedListResponse
import com.github.stormbit.sdk.objects.models.Keyboard
import com.github.stormbit.sdk.objects.models.MessagePayload
import com.github.stormbit.sdk.objects.models.ServiceAction
import com.github.stormbit.sdk.utils.isGroupId
import com.github.stormbit.sdk.utils.isUserPeerId
import com.github.stormbit.sdk.utils.mediaString
import com.github.stormbit.sdk.vkapi.API
import com.github.stormbit.sdk.vkapi.VkApiRequest
import com.github.stormbit.sdk.vkapi.execute
import com.github.stormbit.sdk.vkapi.methods.Attachment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory
import java.util.concurrent.CopyOnWriteArrayList
import com.github.stormbit.sdk.objects.models.Message as MessageModel

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

    private lateinit var api: API
    private lateinit var client: Client

    /**
     * Attachments in format of received event senderType longpoll server
     *
     * @see: https://vk.com/dev/using_longpoll_2
     */
    var attachments = Attachments()

    val attachmentTypes = CopyOnWriteArrayList<AttachmentType>()

    /**
     * Attachments in format [photo62802565_456241137, photo111_111, doc100_500]
     */
    private val attachmentsToUpload = CopyOnWriteArrayList<String>()
    private val forwardedMessages = CopyOnWriteArrayList<Int>()

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
        this.attachments = attachments
        this.stickerId = attachments.getStickerId()
        this.randomId = randomId
        this.payload = payload
        this.client = client
        this.api = client.api
    }

    constructor(message: MessageModel) {
        this.messageId = message.id
        this.peerId = message.peerId
        this.timestamp = message.date
        this.text = message.text
        this.randomId = message.randomId ?: 0

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

                attachs.add(Attachments.Item(attach!!, type!!.value))
            }

            this.attachments = Attachments(attachs, message.forwardedMessages.isNotEmpty())
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

    constructor(block: Message.() -> Message) {
        block()
    }

    /**
     * Your client with id
     *
     * @param client client
     * @return this
     */
    fun from(client: Client): Message {
        api = client.api
        this.client = client
        return this
    }

    /**
     * Message items
     * @param attachments items
     * @return this
     */
    fun attachments(vararg attachments: String): Message {
        if (attachments.size > 10) {
            log.error("Trying to send message with illegal count of items: ${attachments.size} (> 10)")
        } else if (attachments.size == 1 && attachments[0].contains(",")) {
            this.attachmentsToUpload.addAllAbsent(listOf(*attachments[0].split(",".toRegex()).toTypedArray()))
        } else {
            this.attachmentsToUpload.addAllAbsent(listOf(*attachments))
        }
        return this
    }

    /**
     * @param photo Photo link
     * @return this
     */
    fun photo(photo: String): Message {
        if (Regex("[https://vk.com]?photo-?\\d+_\\d+").matches(photo)) {
            attachmentsToUpload.add(photo.substring(photo.lastIndexOf("photo")))
            return this
        }

        return this
    }

    /**
     * Attach doc to message
     *
     * @param doc Doc link
     * @return this
     */
    fun doc(doc: String): Message {
        if (Regex("[https:/vk.com]?doc-?\\d+_\\d+").matches(doc)) {
            attachmentsToUpload.add(doc)
        }

        return this
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
            attachments = attachmentsToUpload,
            keyboard = keyboard,
            replyToMessageId = replyTo,
            forwardedMessages = forwardedMessages
        ).execute()
    }

    /**
     * @return true if message has forwarded messages
     */
    val hasFwds: Boolean = attachments.hasFwd

    /**
     * @return array of forwarded messages or []
     */
    suspend fun getForwardedMessages(): List<MessageModel.Forward> {
        if (hasFwds) {
            return client.messages.getById(listOf(messageId)).execute().items[0].forwardedMessages
        }

        return emptyList()
    }

    /**
     * @return JsonObject with reply message or {}
     */
    suspend fun getReplyMessage(): MessageModel.Forward? {
        if (hasFwds) {
            val (_, items) = client.messages.getById(listOf(messageId)).execute()

            return items[0].replyMessage!!
        }

        return null
    }

    /**
     * @param T Attachment type: Audio, Video, Photo, Voice, Document
     * @return List of T
     */
    @PublishedApi
    @Suppress("UNCHECKED_CAST")
    internal suspend fun <T : Attachment> getAttachments(type: Class<T>): List<T> {
        val messagesRequest: VkApiRequest<ExtendedListResponse<MessageModel>> = if (isMessageFromChat) {
            if (client is GroupClient) {
                client.messages.getByConversationMessageId(chatIdLong, listOf(messageId), groupId = client.id)
            } else {
                client.messages.getByConversationMessageId(chatIdLong, listOf(messageId))
            }
        } else {
            client.messages.getById(listOf(messageId))
        }

        val messages = messagesRequest.execute().items

        if (messages.isNotEmpty()) {

            if (messages[0].attachments.isNotEmpty()) {
                val attachs = messages[0].attachments

                return when (type) {
                    Audio::class.java -> {
                        attachs
                            .filter { it.type == AttachmentType.AUDIO }
                            .map { it.audio!! } as List<T>
                    }

                    Photo::class.java -> {
                        attachs
                            .filter { it.type == AttachmentType.PHOTO }
                            .map { it.photo!! } as List<T>
                    }

                    Document::class.java -> {
                        attachs
                            .filter { it.type == AttachmentType.DOC }
                            .map { it.document!! } as List<T>
                    }

                    Voice::class.java -> {
                        attachs
                            .filter { it.type == AttachmentType.VOICE }
                            .map { it.voice!! } as List<T>
                    }

                    Video::class.java -> {
                        attachs
                            .filter { it.type == AttachmentType.VIDEO }
                            .map { it.video!! } as List<T>
                    }

                    else -> emptyList()
                }
            }
        }

        return emptyList()
    }

    /**
     * Get items senderType message
     *
     * @return JSONArray items
     */
    suspend inline fun <reified T : Attachment> getAttachments(): List<T> {
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

    private suspend fun getForwardedMessagesIds(): List<Int> {
        val messages: VkApiRequest<ExtendedListResponse<MessageModel>> =
            if (client is GroupClient) {
                client.messages.getByConversationMessageId(chatIdLong, listOf(messageId), groupId = client.id)
            } else {
                client.messages.getByConversationMessageId(chatIdLong, listOf(messageId))
            }

        return messages.execute().items[0].forwardedMessages.map { it.id!! }
    }

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
            append("\"items\": $attachments,")
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
            @SerialName("attach_type") val typeAttachment: String,
            @SerialName("attach_kind") val kind: String? = null
        )

        fun toArray(): Array<String> {
            return items.filter { it.typeAttachment != "sticker" }.map { it.typeAttachment + it.attach }.toTypedArray()
        }

        fun getStickerId(): Int {
            val list = items.filter { it.typeAttachment == "sticker" }

            return if (list.isNotEmpty()) return list[0].attach.toInt() else 0
        }
    }
}