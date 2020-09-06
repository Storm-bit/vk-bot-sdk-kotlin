package com.github.stormbit.sdk.objects

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.vkapi.API
import com.github.stormbit.sdk.utils.vkapi.Upload
import com.github.stormbit.sdk.utils.vkapi.docs.DocTypes
import com.github.stormbit.sdk.utils.vkapi.keyboard.Keyboard
import org.json.JSONArray
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Suppress("unused", "MemberVisibilityCanBePrivate")
class Message {
    private val log = LoggerFactory.getLogger(Message::class.java)

    var messageId: Int? = null
    val authorId get() = peerId
    var timestamp: Int? = null
    var chatId: Int? = null
    var chatIdLong: Int? = null

    private var peerId: Int? = null
    private var randomId: Int = 0
    private var stickerId: Int? = null
    private var keyboard: Keyboard? = null
    private var payload = JSONObject()

    var text: String = ""
        private set
    var title: String = ""
        private set

    private lateinit var api: API
    private lateinit var upload: Upload
    private lateinit var client: Client

    /**
     * Attachments in format of received event from longpoll server
     * More: [link](https://vk.com/dev/using_longpoll_2)
     */
    private var attachmentsOfReceivedMessage = JSONObject()

    /**
     * Attachments in format [photo62802565_456241137, photo111_111, doc100_500]
     */
    private var attachments: CopyOnWriteArrayList<String> = CopyOnWriteArrayList()
    private var forwardedMessages: CopyOnWriteArrayList<String> = CopyOnWriteArrayList()
    private val photosToUpload = CopyOnWriteArrayList<String>()
    private val docsToUpload = CopyOnWriteArrayList<JSONObject>()

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
     * @param client client
     * @param messageId message id
     * @param peerId peer id
     * @param timestamp timestamp
     * @param text message text
     * @param attachments message attachments
     * @param randomId random id
     */
    constructor(client: Client, messageId: Int, peerId: Int, timestamp: Int, text: String, attachments: JSONObject?, randomId: Int, payload: JSONObject?) {
        setMessageId(messageId)
        setPeerId(peerId)
        setTimestamp(timestamp)
        setText(text)
        setAttachments(attachments)
        setRandomId(randomId)
        setPayload(payload)
        setTitle(if (attachments != null && attachments.has("title")) attachments.getString("title") else " ... ")
        this.client = client

        api = client.api
        upload = Upload(client)
    }

    constructor(block: Message.() -> Message) {
        block()
    }

    /**
     * Your client with id
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
     * @param peerId target
     * @return this
     */
    fun to(peerId: Int): Message {
        this.peerId = peerId
        return this
    }

    /**
     * ID of sticker
     * @param id sticker id
     * @return this
     */
    fun sticker(id: Int): Message {
        this.stickerId = id
        return this
    }

    /**
     * IDs of forwarded messages
     * @param ids message ids
     * @return this
     */
    fun forwardedMessages(vararg ids: String): Message {
        for (id in ids) {
            forwardedMessages.add(id)
        }

        return this
    }

    /**
     * Message text
     * @param text message content
     * @return this
     */
    fun text(text: String): Message {
        this.text = text
        return this
    }

    /**
     * Message title (bold text)
     * @param title message title
     * @return this
     */
    fun title(title: String): Message {
        this.title = title
        return this
    }

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
     * @param randomId random
     * @return this
     */
    fun randomId(randomId: Int): Message? {
        this.randomId = randomId
        return this
    }

    /**
     * @param photo String URL, link to vk doc or path to file
     * @return this
     */
    fun photo(photo: String): Message {
        if (Pattern.matches("[htps:/vk.com]?photo-?\\d+_\\d+", photo)) {
            attachments.add(photo.substring(photo.lastIndexOf("photo")))
            return this
        }

        attachments.add(upload.uploadPhoto(photo, peerId!!))
        return this
    }

    /**
     * Synchronous adding doc to the message
     *
     * @param doc String URL, link to vk doc or path to file
     * @return this
     */
    fun doc(doc: String?): Message {
        val docAsAttach = upload.uploadDoc(doc!!, peerId!!, DocTypes.DOC)
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

        docsToUpload.add(JSONObject().put("doc", doc).put("type", type.type))
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
    fun sendVoiceMessage(doc: String, callback: Callback<JSONObject>?) {
        val docAsAttach = upload.uploadDoc(doc, peerId!!, DocTypes.AUDIO_MESSAGE)
        if (docAsAttach != null) attachments.add(docAsAttach)
        send(callback)
    }

    /**
     * Send the message
     *
     * @param callback will be called with response object
     */
    fun send(callback: Callback<JSONObject>? = null) {
        if (photosToUpload.size > 0) {
            val photo = photosToUpload[0]
            photosToUpload.removeAt(0)

            upload.uploadPhotoAsync(photo, peerId!!) { response: Any? ->
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

            upload.uploadDocAsync(doc, peerId!!) { response ->
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
                peerId = peerId!!,
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

            isGifMessage() -> MessageType.GIF

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
        if (attachmentsOfReceivedMessage.has("fwd")) answer = true
        return answer
    }

    /**
     * @return array of forwarded messages or []
     */
    fun getForwardedMessages(): JSONArray {
        if (hasFwds()) {
            val response = client.messages.getById(listOf(messageId!!))

            if (response.has("response") && response.getJSONObject("response").getJSONArray("items").getJSONObject(0).has("fwd_messages")) {
                return response.getJSONObject("response").getJSONArray("items").getJSONObject(0).getJSONArray("fwd_messages")
            }
        }

        return JSONArray()
    }

    /**
     * @return JSONObject with reply message or {}
     */
    fun getReplyMessage(): JSONObject {
        if (hasFwds()) {
            val response = client.messages.getById(listOf(messageId!!))

            if (response.has("response") && response.getJSONObject("response").getJSONArray("items").getJSONObject(0).has("reply_message")) {
                return response.getJSONObject("response").getJSONArray("items").getJSONObject(0).getJSONObject("reply_message")
            }
        }
        return JSONObject()
    }

    /**
     * Get attachments from message
     * @return JSONArray attachments
     */
    fun getAttachments(): JSONArray {
        val response: JSONObject = if (isMessageFromChat()) {
            client.messages.getByConversationMessageId(chatIdLong!!, listOf(messageId!!), groupId = client.id)
        } else {
            client.messages.getById(listOf(messageId!!))
        }

        val stringPhotos = ArrayList<String>()
        val stringDocs = ArrayList<String>()
        val stringVideos = ArrayList<String>()
        val stringAudios = ArrayList<String>()

        if (response.has("response") && response.getJSONObject("response").getJSONArray("items").length() > 0) {
            val items = response.getJSONObject("response").getJSONArray("items")

            if (items.getJSONObject(0).has("attachments")) {
                val attachs = items.getJSONObject(0).getJSONArray("attachments")

                for (item in attachs) {
                    if (item is JSONObject) {
                        if (item.has("type")) {
                            when (item.getString("type")) {
                                "photo" -> {
                                    val photo = item.getJSONObject("photo")
                                    stringPhotos.add("photo${photo.getInt("owner_id")}_${photo.getInt("id")}")
                                }

                                "doc" -> {
                                    val doc = item.getJSONObject("doc")
                                    stringDocs.add("doc${doc.getInt("owner_id")}_${doc.getInt("id")}")
                                }

                                "video" -> {
                                    val video = item.getJSONObject("video")
                                    stringVideos.add("video${video.getInt("owner_id")}_${video.getInt("id")}")
                                }

                                "audio" -> {
                                    val audio = item.getJSONObject("audio")
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

        return JSONArray()
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


    fun isGifMessage(): Boolean {
        val attachments = getAttachments()
        for (attachment in attachments) {
            if (attachment is JSONObject) {
                if (attachment.has("type") && attachment.getJSONObject(attachment.getString("type")).has("type") && attachment.getJSONObject(attachment.getString("type")).getInt("type") == 3) return true
            }
        }
        return false
    }

    // Getters and setters for handling new message
    /**
     * Method helps to identify kind of message
     *
     * @return Map: key=type of attachment, value=count of attachments, key=summary - value=count of all attachments.
     */
    fun getCountOfAttachmentsByType(): Map<String, Int> {
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
                for (key in attachmentsOfReceivedMessage.keySet()) {
                    if (key == "type") {
                        when (val value = attachmentsOfReceivedMessage.getString(key)) {
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

        var summary = 0

        for (key in answer.keys) {
            if (answer[key]!! > 0) summary++
        }

        answer["summary"] = summary

        return answer
    }

    /* Public getters */
    fun authorId(): Int? = peerId


    fun getPhotos(): JSONArray? {
        val attachments = getAttachments()
        val answer = JSONArray()
        for (i in 0 until attachments.length()) {
            if (attachments.getJSONObject(i).getString("type").contains("photo")) answer.put(attachments.getJSONObject(i).getJSONObject("photo"))
        }
        return answer
    }

    /* Private setters */
    private fun setMessageId(messageId: Int) {
        this.messageId = messageId
    }

    private fun setPeerId(peerId: Int) {
        this.peerId = peerId
    }

    private fun setTimestamp(timestamp: Int) {
        this.timestamp = timestamp
    }

    fun setChatIdLong(chatIdLong: Int) {
        this.chatIdLong = chatIdLong
    }

    /**
     * @param photos JSONArray with photo objects
     * @return URL of biggest image file
     */
    fun getBiggestPhotoUrl(photos: JSONArray): String {
        val currentBiggestPhoto: String

        val sizes: MutableMap<Int, String> = HashMap()

        for (obj in photos) {
            if (obj is JSONObject) {
                val width = obj.getInt("width")
                val url = obj.getString("url")
                sizes[width] = url
            }
        }

        currentBiggestPhoto = sizes.getValue(Collections.max(sizes.keys))
        return currentBiggestPhoto
    }

    fun getVoiceMessage(): JSONObject? {
        val attachments = getAttachments()
        var answer: JSONObject? = JSONObject()
        for (i in 0 until attachments.length()) {
            if (attachments.getJSONObject(i).getString("type").contains("doc") && attachments.getJSONObject(i).getJSONObject("doc").toString().contains("waveform")) answer = attachments.getJSONObject(i).getJSONObject("doc")
        }
        return answer
    }

    fun isMessageFromChat(): Boolean {
        return chatId != null && chatId!! > 0 || chatIdLong != null && chatIdLong!! > 0
    }

    fun chatId(): Int? = chatId


    fun setChatId(chatId: Int) {
        this.chatId = chatId
    }

    private fun setAttachments(attachments: JSONObject?) {
        if (attachments == null) return
        attachmentsOfReceivedMessage = attachments
    }

    fun getRandomId(): Int = randomId

    private fun setRandomId(randomId: Int) {
        this.randomId = randomId
    }

    fun getKeyboard(): Keyboard? = keyboard

    fun setKeyboard(keyboard: Keyboard) {
        this.keyboard = keyboard
    }

    fun getPayload(): JSONObject = payload

    fun setPayload(payload: JSONObject?) {
        if (payload == null) return

        this.payload = payload
    }

    fun setText(text: String) {
        this.text = text
    }

    fun setTitle(title: String) {
        this.title = title
    }

    private fun getForwardedMessagesIds(): Array<String?>? {
        return if (attachmentsOfReceivedMessage.has("fwd")) {
            attachmentsOfReceivedMessage.getString("fwd").split(",".toRegex()).toTypedArray()
        } else arrayOf()
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
        GIF,
        WALL,
        LINK,
        SIMPLE_TEXT
    }
}