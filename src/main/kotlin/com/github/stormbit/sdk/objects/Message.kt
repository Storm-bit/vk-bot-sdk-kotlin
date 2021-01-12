package com.github.stormbit.sdk.objects

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.clients.Group
import com.github.stormbit.sdk.objects.attachments.*
import com.github.stormbit.sdk.objects.attachments.Voice
import com.github.stormbit.sdk.objects.models.Keyboard
import com.github.stormbit.sdk.objects.models.MessagePayload
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.vkapi.API
import com.github.stormbit.sdk.vkapi.methods.Attachment
import com.github.stormbit.sdk.objects.attachments.AttachmentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory
import java.util.concurrent.CopyOnWriteArrayList
import com.github.stormbit.sdk.objects.models.Message as MessageModel

@Suppress("unused", "MemberVisibilityCanBePrivate", "RegExpDuplicateCharacterInClass")
class Message {
    private val log = LoggerFactory.getLogger(Message::class.java)

    var messageId: Int? = null
        private set

    val from: MessageFrom?
        get() {
            return getSenderType(peerId)
        }

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

    var text: String = ""

    var textWithoutCommand: String = text
        get() {
            val words = text.split(" ")
            return if (words.size > 1) {
                words.subList(1, words.size).joinToString(" ")
            } else {
                ""
            }
        }
        private set

    private lateinit var api: API
    private lateinit var upload: Upload
    private lateinit var client: Client

    /**
     * Attachments in format of received event from longpoll server
     *
     * @see: https://vk.com/dev/using_longpoll_2
     */
    var attachments = Attachments()

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
        this.upload = Upload(client)
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
                }

                if (it.video != null) {
                    attach = it.video.mediaString
                    type = it.type
                }

                if (it.audio != null) {
                    attach = it.audio.mediaString
                    type = it.type
                }

                if (it.document != null) {
                    attach = it.document.mediaString
                    type = it.type
                }

                if (it.voice != null) {
                    attach = it.voice.mediaString
                    type = it.type
                }

                if (it.sticker != null) {
                    attach = "sticker"
                    type = it.type
                }

                attachs.add(Attachments.Item(attach!!, type!!.value))
            }

            this.attachments = Attachments(attachs, message.forwardedMessages.isNotEmpty())
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
        upload = Upload(client)
        this.client = client
        return this
    }

    /**
     * ID of target dialog
     *
     * @param peerId target
     * @return this
     */
    fun to(peerId: Int): Message {
        this.peerId = peerId
        return this
    }

    /**
     * ID of sticker
     *
     * @param id sticker id
     * @return this
     */
    fun sticker(id: Int): Message {
        this.stickerId = id
        return this
    }

    /**
     * IDs of forwarded messages
     *
     * @param ids message ids
     * @return this
     */
    fun forwardedMessages(vararg ids: Int): Message {
        forwardedMessages.addAllAbsent(ids.toList())
        return this
    }

    /**
     * Message text
     *
     * @param text message content
     * @return this
     */
    fun text(text: String): Message {
        this.text = text
        return this
    }

    /**
     * Reply to message
     *
     * @param messageId Id of the message you want to reply to
     * @return this
     */
    fun replyTo(messageId: Int?): Message {
        this.replyTo = messageId
        return this
    }

    /**
     * Message keyboard
     *
     * @param keyboard keyboard
     * @return this
     */
    fun keyboard(keyboard: Keyboard): Message {
        this.keyboard = keyboard
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
     * Message random_id
     *
     * @param randomId random id
     * @return this
     */
    fun randomId(randomId: Int): Message {
        this.randomId = randomId
        return this
    }

    /**
     * Message sticker_id
     *
     * @param stickerId sticker id
     * @return this
     */
    fun stickerId(stickerId: Int): Message {
        this.stickerId = stickerId
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
    fun send(): Int? {
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
        )
    }

    /**
     * Send the message async
     */
    fun sendAsync(callback: Callback<Int?>? = null) {
        client.messagesAsync.send(
            peerId = peerId,
            randomId = randomId,
            text = text,
            stickerId = stickerId,
            payload = payload,
            attachments = attachmentsToUpload,
            keyboard = keyboard,
            replyToMessageId = replyTo,
            forwardedMessages = forwardedMessages,
            callback = callback
        )
    }

    /**
     * Get the type of message
     * @return type of message
     */
    fun messageType(): MessageType? {
        return when {
            isVoiceMessage -> MessageType.VOICE

            isStickerMessage -> MessageType.STICKER

            isAudioMessage -> MessageType.AUDIO

            isVideoMessage -> MessageType.VIDEO

            isDocMessage -> MessageType.DOC

            isWallMessage -> MessageType.WALL

            isPhotoMessage -> MessageType.PHOTO

            isLinkMessage -> MessageType.PHOTO

            isSimpleTextMessage -> MessageType.SIMPLE_TEXT

            else -> null
        }
    }

    /**
     * @return true if message has forwarded messages
     */
    val hasFwds: Boolean = attachments.hasFwd

    /**
     * @return array of forwarded messages or []
     */
    fun getForwardedMessages(): List<MessageModel.Forward> {
        if (hasFwds) {
            return client.messages.getById(listOf(messageId!!))!!.items[0].forwardedMessages
        }

        return emptyList()
    }

    /**
     * @return JsonObject with reply message or {}
     */
    fun getReplyMessage(): MessageModel.Forward? {
        if (hasFwds) {
            val response = client.messages.getById(listOf(messageId!!))

            return response!!.items[0].replyMessage!!
        }

        return null
    }

    /**
     * @param T Attachment type: Audio, Video, Photo, Voice, Document
     * @return List of T
     */
    @PublishedApi
    @Suppress("UNCHECKED_CAST")
    internal fun <T : Attachment> getAttachments(type: Class<T>): List<T> {
        val messages: List<MessageModel> = if (isMessageFromChat) {
            if (client is Group) {
                client.messages.getByConversationMessageId(chatIdLong, listOf(messageId!!), groupId = client.id)!!
            } else {
                client.messages.getByConversationMessageId(chatIdLong, listOf(messageId!!))!!
            }
        } else {
            client.messages.getById(listOf(messageId!!))!!.items
        }

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
     * Get items from message
     *
     * @return JSONArray items
     */
    inline fun <reified T : Attachment> getAttachments(): List<T> {
        return getAttachments(T::class.java)
    }

    /*
     * Priority: voice, sticker, gif, ... , simple text
     */
    val isPhotoMessage: Boolean get() = getCountOfAttachmentsByType()["photo"]!! > 0

    val isSimpleTextMessage: Boolean get() = getCountOfAttachmentsByType()["summary"] == 0

    val isVoiceMessage: Boolean get() = getCountOfAttachmentsByType()["voice"]!! > 0

    val isAudioMessage: Boolean get() = getCountOfAttachmentsByType()["audio"]!! > 0

    val isVideoMessage: Boolean get() = getCountOfAttachmentsByType()["video"]!! > 0

    val isDocMessage: Boolean get() = getCountOfAttachmentsByType()["doc"]!! > 0

    val isWallMessage: Boolean get() = getCountOfAttachmentsByType()["wall"]!! > 0

    val isStickerMessage: Boolean get() = getCountOfAttachmentsByType()["sticker"]!! > 0

    val isLinkMessage: Boolean get() = getCountOfAttachmentsByType()["link"]!! > 0

    /**
     * Method helps to identify kind of message
     *
     * @return Map: key=type of attachment, value=count of items, key=summary - value=count of all items.
     */
    fun getCountOfAttachmentsByType(): HashMap<String, Int> {
        var photo = 0
        var video = 0
        var audio = 0
        var doc = 0
        var wall = 0
        var link = 0
        var voice = 0
        var sticker = 0

        val answer = HashMap<String, Int>()

        answer["photo"] = 0
        answer["video"] = 0
        answer["audio"] = 0
        answer["doc"] = 0
        answer["wall"] = 0
        answer["sticker"] = 0
        answer["link"] = 0
        answer["voice"] = 0
        answer["summary"] = 0

        for (index in attachments.items.indices) {
            val attach = attachments.items[index]

            when (val value = attach.typeAttachment) {
                "photo"   -> answer[value] = ++photo

                "video"   -> answer[value] = ++video

                "audio"   -> answer[value] = ++audio

                "doc"     -> answer[value] = ++doc

                "wall"    -> answer[value] = ++wall

                "link"    -> answer[value] = ++link

                "voice"   -> answer[value] = ++voice

                "sticker" -> answer[value] = ++sticker

            }
        }

        var summary = 0

        for (key in answer.keys) {
            if (answer[key]!! > 0) summary++
        }

        answer["summary"] = summary

        return answer
    }

    val isMessageFromChat: Boolean
        get() = chatId > 0 || chatIdLong > 0

    private fun getForwardedMessagesIds(): List<Int> {
        val messages: List<MessageModel> = if (isMessageFromChat) {
            if (client is Group) {
                client.messages.getByConversationMessageId(chatIdLong, listOf(messageId!!), groupId = client.id)!!
            } else {
                client.messages.getByConversationMessageId(chatIdLong, listOf(messageId!!))!!
            }
        } else {
            client.messages.getById(listOf(messageId!!))!!.items
        }

        return messages[0].forwardedMessages.map { it.id!! }
    }

    private fun getSenderType(peerId: Int): MessageFrom? {
        return when {
            peerId.isGroupId -> MessageFrom.GROUP
            peerId.isUserPeerId -> MessageFrom.USER
            peerId.isChatPeerId -> MessageFrom.CHAT
            else -> null
        }
    }

    override fun toString(): String {
        return '{'.toString() +
                "\"message_id\":" + messageId +
                ",\"peer_id\":" + peerId +
                ",\"timestamp\":" + timestamp +
                ",\"random_id\":" + randomId +
                ",\"text\":\"" + text + '\"' +
                ",\"items\":" + attachments.toString() +
                ",\"payload\":" + payload.value +
                '}'
    }

    enum class MessageType {
        PHOTO,
        AUDIO,
        VIDEO,
        VOICE,
        STICKER,
        DOC,
        WALL,
        LINK,
        SIMPLE_TEXT
    }

    enum class MessageFrom {
        USER,
        GROUP,
        CHAT
    }

    @Serializable
    data class Attachments(
        @SerialName("items") val items: List<Item> = emptyList(),
        @SerialName("fwd") val hasFwd: Boolean = false,
        @SerialName("from") val from: Int? = null) {

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