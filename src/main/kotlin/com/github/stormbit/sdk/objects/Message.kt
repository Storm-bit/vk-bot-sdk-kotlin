package com.github.stormbit.sdk.objects

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.clients.Group
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.utils.vkapi.API
import com.github.stormbit.sdk.utils.vkapi.Upload
import com.github.stormbit.sdk.utils.vkapi.docs.DocTypes
import com.github.stormbit.sdk.utils.vkapi.keyboard.Keyboard
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.slf4j.LoggerFactory
import java.util.concurrent.CopyOnWriteArrayList
import java.util.regex.Pattern

@Suppress("unused", "MemberVisibilityCanBePrivate")
class Message {
    private val log = LoggerFactory.getLogger(Message::class.java)
    private val gson = Gson()

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

    var keyboard: Keyboard? = null

    private var payload: JsonObject? = JsonObject()
        private set(value) {
            field = value ?: JsonObject()
        }

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

    var title: String = ""

    private lateinit var api: API
    private lateinit var upload: Upload
    private lateinit var client: Client

    /**
     * Attachments in format of received event from longpoll server
     *
     * More: [link](https://vk.com/dev/using_longpoll_2)
     */
    var attachmentsOfReceivedMessage: JsonObject? = JsonObject()
        private set(value) {
            field = value ?: JsonObject()
        }

    /**
     * Attachments in format [photo62802565_456241137, photo111_111, doc100_500]
     */
    private var attachments: CopyOnWriteArrayList<String> = CopyOnWriteArrayList()
    private var forwardedMessages: CopyOnWriteArrayList<String> = CopyOnWriteArrayList()
    private val photosToUpload = CopyOnWriteArrayList<String>()
    private val docsToUpload = CopyOnWriteArrayList<JsonObject>()

    var stringPhotos = ArrayList<String>()
    var stringDocs = ArrayList<String>()
    var stringVideos = ArrayList<String>()
    var stringAudios = ArrayList<String>()

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
     * @param attachments message attachments
     * @param randomId random id
     */
    constructor(client: Client, messageId: Int, peerId: Int, timestamp: Int, text: String, title: String, attachments: JsonObject?, randomId: Int, payload: JsonObject?) {
        this.messageId = messageId
        this.peerId = peerId
        this.timestamp = timestamp
        this.text = text
        this.attachmentsOfReceivedMessage = attachments
        this.randomId = randomId
        this.payload = payload
        this.title = title
        this.client = client

        this.api = client.api
        this.upload = Upload(client)
    }

    constructor(client: Client, json: JsonObject) {
        this.client = client
        this.api = client.api
        this.upload = Upload(client)

        this.messageId = json.getInt("id")
        this.peerId = json.getInt("from_id")
        this.timestamp = json.getInt("date")
        this.text = json.getString("text")
        this.randomId = json.getInt("random_id")
        this.payload = if (json.has("payload")) gson.toJsonTree(json.getString("payload")).asJsonObject else JsonObject()
        this.title = " ... "
        val attachments = if (json.getAsJsonArray("attachments").size() > 0) json.getAsJsonArray("attachments").getJsonObject(0) else JsonObject()

        // Check for chat
        if (this.peerId > Chat.CHAT_PREFIX) {
            this.chatId = this.peerId - Chat.CHAT_PREFIX
            if (attachments.keySet().size > 0) {
                this.peerId = attachments.getString("from").toInt()
            }
            this.messageId = json.getInt("conversation_message_id")
        }

        this.attachmentsOfReceivedMessage = attachments

        if (chatId > 0) {
            this.chatIdLong = Chat.CHAT_PREFIX + chatId
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
    fun forwardedMessages(vararg ids: String): Message {
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
     * Message title (bold text)
     *
     * @param title message title
     * @return this
     */
    fun title(title: String): Message {
        this.title = title
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
     * Message attachments
     * @param attachments attachments
     * @return this
     */
    fun attachments(vararg attachments: String): Message {
        if (attachments.size > 10) log.error("Trying to send message with illegal count of attachments: {} (> 10)", attachments.size) else if (attachments.size == 1 && attachments[0].contains(",")) {
            this.attachments.addAllAbsent(listOf(*attachments[0].split(",".toRegex()).toTypedArray()))
        } else {
            this.attachments.addAllAbsent(listOf(*attachments))
        }
        return this
    }

    /**
     * Message random_id
     *
     * @param randomId random_id
     * @return this
     */
    fun randomId(randomId: Int): Message {
        this.randomId = randomId
        return this
    }

    /**
     * Message sticker_id
     *
     * @param stickerId sticker_id
     * @return this
     */
    fun stickerId(stickerId: Int): Message {
        this.stickerId = stickerId
        return this
    }

    /**
     * @param photo String URL, link to vk doc or path to file
     * @return this
     */
    fun photo(photo: String): Message {
        if (Regex("[https://vk.com]?photo-?\\d+_\\d+").matches(photo)) {
            attachments.add(photo.substring(photo.lastIndexOf("photo")))
            return this
        }

        attachments.add(upload.uploadPhoto(photo, peerId))
        return this
    }

    /**
     * Synchronous adding doc to the message
     *
     * @param doc String URL, link to vk doc or path to file
     * @return this
     */
    fun doc(doc: String): Message {
        val docAsAttach = upload.uploadDoc(doc, peerId, DocTypes.DOC)
        if (docAsAttach != null) attachments.add(docAsAttach)
        return this
    }

    /**
     * Attach photo to message
     *
     *
     * Works slower that sync photo adding, but will be called from execute
     *
     * @param photo Photo link: url, from disk or already uploaded to VK as photo{owner_id}_{id}
     * @return this
     */
    fun photoAsync(photo: String): Message {

        // Use already loaded photo
        if (Pattern.matches("[htps:/vk.com]?photo-?\\d+_\\d+", photo)) {
            attachments.add(photo.substring(photo.lastIndexOf("photo")))
            return this
        }

        // Use photo from url of disc
        photosToUpload.add(photo)
        return this
    }

    /**
     * Attach doc to message
     *
     * @param doc Doc link: url, from disk or already uploaded to VK as doc{owner_id}_{id}
     * @param type type
     * @return this
     */
    fun docAsync(doc: String, type: DocTypes): Message {

        // Use already loaded photo
        if (Pattern.matches("[htps:/vk.com]?doc-?\\d+_\\d+", doc)) {
            attachments.add(doc)
            return this
        }

        docsToUpload.add(JsonObject().put("doc", doc).put("type", type.type))
        return this
    }

    /**
     * Attach doc to message
     *
     * @param doc Doc link: url, from disk or already uploaded to VK as doc{owner_id}_{id}
     * @return this
     */
    fun docAsync(doc: String): Message {
        this.docAsync(doc, DocTypes.DOC)
        return this
    }

    /**
     * Send voice message
     *
     * @param doc      URL or path to file
     * @param callback response will returns to callback
     */
    fun sendVoiceMessage(doc: String, callback: Callback<JsonObject>?) {
        val docAsAttach = upload.uploadDoc(doc, peerId, DocTypes.AUDIO_MESSAGE)
        if (docAsAttach != null) attachments.add(docAsAttach)
        send(callback)
    }

    /**
     * Send the message
     *
     */
    fun sendAsync(): JsonObject? {
        if (photosToUpload.size > 0) {
            val photo = photosToUpload[0]
            photosToUpload.removeAt(0)

            val response = upload.uploadPhoto(photo, peerId)
            if (response != null) {
                attachments.addIfAbsent(response.toString())
                sendAsync()
            } else log.error("Some error occurred when uploading photo.")

            return null
        }

        if (docsToUpload.size > 0) {
            val doc = docsToUpload[0]
            docsToUpload.removeAt(0)

            val response = upload.uploadDoc(doc.getString("doc"), peerId)
            if (response != null) {
                attachments.addIfAbsent(response.toString())
                sendAsync()
            } else log.error("Some error occurred when uploading photo.")

            return null
        }

        return client.messages.send(
                peerId = peerId,
                randomId = randomId,
                text = text,
                stickerId = stickerId,
                payload = payload.toString(),
                attachments = attachments,
                keyboard = keyboard)
    }

    fun send(callback: Callback<JsonObject>? = null) {
        if (photosToUpload.size > 0) {
            val photo = photosToUpload[0]
            photosToUpload.removeAt(0)

            upload.uploadPhotoAsync(photo, peerId) { response: Any? ->
                if (response != null) {
                    attachments.addIfAbsent(response.toString())
                    send(callback)
                } else {
                    log.error("Some error occurred when uploading photo.")
                }
            }

            return
        }

        if (docsToUpload.size > 0) {
            val doc = docsToUpload[0]
            docsToUpload.removeAt(0)

            upload.uploadDocAsync(doc.getString("doc"), peerId) { response ->
                if (response != null) {
                    attachments.addIfAbsent(response.toString())
                    send(callback)
                } else {
                    log.error("Some error occurred when uploading doc.")
                }
            }

            return
        }

        val response = client.messages.send(
                peerId = peerId,
                randomId = randomId,
                text = text,
                stickerId = stickerId,
                payload = payload.toString(),
                attachments = attachments,
                keyboard = keyboard)

        callback?.onResult(response)
    }

    /**
     * Get the type of message
     * @return type of message
     */
    fun messageType(): MessageType? {
        return when {
            isVoiceMessage() -> MessageType.VOICE

            isStickerMessage() -> MessageType.STICKER

            isAudioMessage() -> MessageType.AUDIO

            isVideoMessage() -> MessageType.VIDEO

            isDocMessage() -> MessageType.DOC

            isWallMessage() -> MessageType.WALL

            isPhotoMessage() -> MessageType.PHOTO

            isLinkMessage() -> MessageType.PHOTO

            isSimpleTextMessage() -> MessageType.SIMPLE_TEXT

            else -> null
        }
    }

    /**
     * @return true if message has forwarded messages
     */
    fun hasFwds(): Boolean {
        var answer = false
        if (attachmentsOfReceivedMessage!!.has("fwd")) answer = true
        return answer
    }

    /**
     * @return array of forwarded messages or []
     */
    fun getForwardedMessages(): JsonArray {
        if (hasFwds()) {
            val response = client.messages.getById(listOf(messageId!!))

            if (response.has("response") && response.getAsJsonObject("response").getAsJsonArray("items").getJsonObject(0).has("fwd_messages")) {
                return response.getAsJsonObject("response").getAsJsonArray("items").getJsonObject(0).getAsJsonArray("fwd_messages")
            }
        }

        return JsonArray()
    }

    /**
     * @return JsonObject with reply message or {}
     */
    fun getReplyMessage(): JsonObject {
        if (hasFwds()) {
            val response = client.messages.getById(listOf(messageId!!))

            if (response.has("response") && response.getAsJsonObject("response").getAsJsonArray("items").getJsonObject(0).has("reply_message")) {
                return response.getAsJsonObject("response").getAsJsonArray("items").getJsonObject(0).getAsJsonObject("reply_message")
            }
        }
        return JsonObject()
    }

    /**
     * Get attachments from message
     * @return JSONArray attachments
     */
    fun getAttachments(): JsonArray {
        val response: JsonObject = if (isMessageFromChat()) {
            client.messages.getByConversationMessageId(chatIdLong, listOf(messageId!!), groupId = client.id)
        } else {
            client.messages.getById(listOf(messageId!!))
        }

        val stringPhotos = ArrayList<String>()
        val stringDocs = ArrayList<String>()
        val stringVideos = ArrayList<String>()
        val stringAudios = ArrayList<String>()

        if (response.has("response") && response.getAsJsonObject("response").getAsJsonArray("items").size() > 0) {
            val items = response.getAsJsonObject("response").getAsJsonArray("items")

            if (items.getJsonObject(0).has("attachments")) {
                val attachs = items.getJsonObject(0).getAsJsonArray("attachments")

                for (item in attachs) {
                    if (item is JsonObject) {
                        if (item.has("type")) {
                            when (item.getString("type")) {
                                "photo" -> {
                                    val photo = item.getAsJsonObject("photo")
                                    stringPhotos.add("photo${photo.getInt("owner_id")}_${photo.getInt("id")}")
                                }

                                "doc" -> {
                                    val doc = item.getAsJsonObject("doc")
                                    stringDocs.add("doc${doc.getInt("owner_id")}_${doc.getInt("id")}")
                                }

                                "video" -> {
                                    val video = item.getAsJsonObject("video")
                                    stringVideos.add("video${video.getInt("owner_id")}_${video.getInt("id")}")
                                }

                                "audio" -> {
                                    val audio = item.getAsJsonObject("audio")
                                    stringAudios.add("audio${audio.getInt("owner_id")}_${audio.getInt("id")}")
                                }
                            }
                        }
                    }
                }

                this.stringPhotos = stringPhotos
                this.stringDocs = stringDocs
                this.stringVideos = stringVideos
                this.stringAudios = stringAudios

                return attachs
            }
        }

        return JsonArray()
    }

    /*
     * Priority: voice, sticker, gif, ... , simple text
     */
    fun isPhotoMessage(): Boolean = getCountOfAttachmentsByType()["photo"]!! > 0

    fun isSimpleTextMessage(): Boolean = getCountOfAttachmentsByType()["summary"] == 0

    fun isVoiceMessage(): Boolean = getCountOfAttachmentsByType()["voice"]!! > 0

    fun isAudioMessage(): Boolean = getCountOfAttachmentsByType()["audio"]!! > 0

    fun isVideoMessage(): Boolean = getCountOfAttachmentsByType()["video"]!! > 0

    fun isDocMessage(): Boolean = getCountOfAttachmentsByType()["doc"]!! > 0

    fun isWallMessage(): Boolean = getCountOfAttachmentsByType()["wall"]!! > 0

    fun isStickerMessage(): Boolean = getCountOfAttachmentsByType()["sticker"]!! > 0

    fun isLinkMessage(): Boolean = getCountOfAttachmentsByType()["link"]!! > 0

    /**
     * Method helps to identify kind of message
     *
     * @return Map: key=type of attachment, value=count of attachments, key=summary - value=count of all attachments.
     */
    fun getCountOfAttachmentsByType(): HashMap<String, Int> {
        var photo = 0
        var video = 0
        var audio = 0
        var doc = 0
        var wall = 0
        var link = 0

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

        if (attachmentsOfReceivedMessage.toString().contains("sticker")) {
            answer["sticker"] = 1
            answer["summary"] = 1
            return answer
        } else {
            if (attachmentsOfReceivedMessage.toString().contains("audiomsg")) {
                answer["voice"] = 1
                answer["summary"] = 1
                return answer
            } else {
                if (client is Group) {
                    for (key in attachmentsOfReceivedMessage!!.keySet()) {
                        if (key == "type") {
                            when (val value = attachmentsOfReceivedMessage!!.getString(key)) {
                                "photo" -> {
                                    answer[value] = ++photo
                                }
                                "video" -> {
                                    answer[value] = ++video
                                }
                                "audio" -> {
                                    answer[value] = ++audio
                                }
                                "doc" -> {
                                    answer[value] = ++doc
                                }
                                "wall" -> {
                                    answer[value] = ++wall
                                }
                                "link" -> {
                                    answer[value] = ++link
                                }
                            }
                        }
                    }
                } else {
                    for (key in attachmentsOfReceivedMessage!!.keySet()) {
                        if (key.contains("type")) {
                            when (val value = attachmentsOfReceivedMessage!!.getString(key)) {
                                "photo" -> {
                                    answer[value] = ++photo
                                }
                                "video" -> {
                                    answer[value] = ++video
                                }
                                "audio" -> {
                                    answer[value] = ++audio
                                }
                                "doc" -> {
                                    answer[value] = ++doc
                                }
                                "wall" -> {
                                    answer[value] = ++wall
                                }
                                "link" -> {
                                    answer[value] = ++link
                                }
                            }
                        }
                    }
                }
            }
        }

        var summary = 0

        for (key in answer.keys) {
            if (answer[key]!! > 0) summary++
        }

        answer["summary"] = summary

        return answer
    }

    /* Public getters */

    fun getPhotos(): JsonArray? {
        val attachments = getAttachments()
        val answer = JsonArray()
        for (i in 0 until attachments.size()) {
            if (attachments.getJsonObject(i).getString("type").contains("photo")) answer.add(attachments.getJsonObject(i).getAsJsonObject("photo"))
        }
        return answer
    }

    /* Private setters */

    fun getVoiceMessage(): JsonObject? {
        val attachments = getAttachments()
        var answer: JsonObject? = JsonObject()
        for (i in 0 until attachments.size()) {
            if (attachments.getJsonObject(i).getString("type").contains("doc") && attachments.getJsonObject(i).getAsJsonObject("doc").toString().contains("waveform")) answer = attachments.getJsonObject(i).getAsJsonObject("doc")
        }
        return answer
    }

    fun isMessageFromChat(): Boolean {
        return chatId > 0 || chatIdLong > 0
    }

    private fun setAttachments(attachments: JsonObject?) {
        if (attachments == null) return
        attachmentsOfReceivedMessage = attachments
    }

    private fun getForwardedMessagesIds(): Array<String?>? {
        return if (attachmentsOfReceivedMessage!!.has("fwd")) {
            attachmentsOfReceivedMessage!!.getString("fwd").split(",".toRegex()).toTypedArray()
        } else arrayOf()
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
                ",\"attachments\":" + attachmentsOfReceivedMessage.toString() +
                ",\"payload\":" + payload.toString() +
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
}