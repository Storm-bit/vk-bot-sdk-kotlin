package com.github.stormbit.sdk.clients

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.callbacks.CallbackDouble
import com.github.stormbit.sdk.callbacks.CallbackFourth
import com.github.stormbit.sdk.callbacks.CallbackTriple
import com.github.stormbit.sdk.longpoll.Events
import com.github.stormbit.sdk.longpoll.LongPoll
import com.github.stormbit.sdk.longpoll.MessageEvents
import com.github.stormbit.sdk.objects.Chat
import com.github.stormbit.sdk.objects.Message
import com.github.stormbit.sdk.utils.Utils
import com.github.stormbit.sdk.utils.vkapi.API
import com.github.stormbit.sdk.utils.vkapi.Auth
import com.github.stormbit.sdk.utils.vkapi.Session
import com.github.stormbit.sdk.utils.vkapi.apis.APIGroup
import com.github.stormbit.sdk.utils.vkapi.apis.APIUser
import com.github.stormbit.sdk.utils.vkapi.methods.likes.LikesApi
import com.github.stormbit.sdk.utils.vkapi.methods.docs.DocsApi
import com.github.stormbit.sdk.utils.vkapi.methods.docs.DocsApiAsync
import com.github.stormbit.sdk.utils.vkapi.methods.friends.FriendsApi
import com.github.stormbit.sdk.utils.vkapi.methods.friends.FriendsApiAsync
import com.github.stormbit.sdk.utils.vkapi.methods.groups.GroupsApi
import com.github.stormbit.sdk.utils.vkapi.methods.groups.GroupsApiAsync
import com.github.stormbit.sdk.utils.vkapi.methods.likes.LikesApiAsync
import com.github.stormbit.sdk.utils.vkapi.methods.messages.MessagesApi
import com.github.stormbit.sdk.utils.vkapi.methods.messages.MessagesApiAsync
import com.github.stormbit.sdk.utils.vkapi.methods.photos.PhotosApi
import com.github.stormbit.sdk.utils.vkapi.methods.photos.PhotosApiAsync
import com.github.stormbit.sdk.utils.vkapi.methods.users.UsersApi
import com.github.stormbit.sdk.utils.vkapi.methods.users.UsersApiAsync
import com.github.stormbit.sdk.utils.vkapi.methods.video.VideoApi
import com.github.stormbit.sdk.utils.vkapi.methods.video.VideoApiAsync
import com.github.stormbit.sdk.utils.vkapi.methods.wall.WallApi
import com.github.stormbit.sdk.utils.vkapi.methods.wall.WallApiAsync
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.*

@Suppress("unused", "LeakingThis")
abstract class Client {

    /**
     * Main params
     */
    var id: Int
    val api: API
    val http: Session
    val longPoll: LongPoll
    val auth: Auth
    var token: String? = null

    val commands = CopyOnWriteArrayList<Command>()
    private val chats = ConcurrentHashMap<String, Chat>()

    /**
     * Api methods
     */
    val messages = MessagesApi(this)
    val groups = GroupsApi(this)
    val users = UsersApi(this)
    val photos = PhotosApi(this)
    val videos = VideoApi(this)
    val docs = DocsApi(this)
    val friends = FriendsApi(this)
    val likes = LikesApi(this)
    val wall = WallApi(this)

    val messagesAsync = MessagesApiAsync(this)
    val groupsAsync = GroupsApiAsync(this)
    val usersAsync = UsersApiAsync(this)
    val photosAsync = PhotosApiAsync(this)
    val videosAsync = VideoApiAsync(this)
    val docsAsync = DocsApiAsync(this)
    val friendsAsync = FriendsApiAsync(this)
    val likesAsync = LikesApiAsync(this)
    val wallAsync = WallApiAsync(this)

    companion object {
        /**
         * Executor services for threadsafing and fast work
         */
        val service: ExecutorService = Executors.newCachedThreadPool()
        val scheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    }

    constructor(login: String, password: String, saveCookie: Boolean = false, loadFromCookie: Boolean = false, twoFactorListener: Auth.TwoFactorListener? = null, captchaListener: Auth.CaptchaListener? = null) {
        this.auth = Auth(login, password, saveCookie, loadFromCookie, twoFactorListener, captchaListener)
        this.http = this.auth.session

        this.api = APIUser(this)
        this.id = Utils.getId(this)
        this.longPoll = LongPoll(this)
    }

    constructor(accessToken: String, id: Int) {
        this.auth = Auth()
        this.http = this.auth.session

        this.id = id
        this.token = accessToken
        this.api = APIGroup(this)
        this.longPoll = LongPoll(this)
    }

    fun sendMessage(block: Message.() -> Unit): JSONObject? {
        val message = Message()
        message.from(this)

        block(message)

        return message.sendAsync()
    }

    /**
     * If the client need to start typing
     * after receiving message
     * and until client's message is sent
     *
     * @param enable true or false
     */
    fun enableTyping(enable: Boolean) = this.longPoll.enableTyping(enable)

    /* On every event */
    fun onLongPollEvent(callback: Callback<JSONArray>) = this.longPoll.registerCallback(Events.EVERY.value, callback)

    /* Chats */
    fun onChatJoin(callback: CallbackTriple<Int, Int, Int>) = this.longPoll.registerChatCallback(Events.CHAT_JOIN.value, callback)

    fun onChatLeave(callback: CallbackTriple<Int, Int, Int>) = this.longPoll.registerChatCallback(Events.CHAT_LEAVE.value, callback)

    fun onChatTitleChanged(callback: CallbackFourth<String, String, Int, Int>) = this.longPoll.registerChatCallback(Events.CHAT_TITLE_CHANGE.value, callback)

    fun onChatPhotoChanged(callback: CallbackTriple<JSONObject, Int, Int>) = this.longPoll.registerChatCallback(Events.CHAT_PHOTO_UPDATE.value, callback)

    fun onChatPhotoRemoved(callback: CallbackDouble<Int?, Int?>) = this.longPoll.registerChatCallback(Events.CHAT_PHOTO_REMOVE.value, callback)

    fun onChatCreated(callback: CallbackTriple<String?, Int?, Int?>) = this.longPoll.registerChatCallback(Events.CHAT_CREATE.value, callback)

    /* Messages */
    fun onChatMessage(callback: Callback<Message>) = this.longPoll.registerCallback(MessageEvents.CHAT_MESSAGE.value, callback)

    fun onMessageWithFwds(callback: Callback<Message>) = this.longPoll.registerCallback(MessageEvents.MESSAGE_WITH_FORWARDS.value, callback)

    fun onAudioMessage(callback: Callback<Message>) = this.longPoll.registerCallback(MessageEvents.AUDIO_MESSAGE.value, callback)

    fun onDocMessage(callback: Callback<Message>) = this.longPoll.registerCallback(MessageEvents.DOC_MESSAGE.value, callback)

    fun onLinkMessage(callback: Callback<Message>) = this.longPoll.registerCallback(MessageEvents.LINK_MESSAGE.value, callback)

    fun onMessage(callback: Callback<Message>) = this.longPoll.registerCallback(MessageEvents.MESSAGE.value, callback)

    fun onPhotoMessage(callback: Callback<Message>) = this.longPoll.registerCallback(MessageEvents.PHOTO_MESSAGE.value, callback)

    fun onSimpleTextMessage(callback: Callback<Message>) = this.longPoll.registerCallback(MessageEvents.SIMPLE_TEXT_MESSAGE.value, callback)

    fun onStickerMessage(callback: Callback<Message>) = this.longPoll.registerCallback(MessageEvents.STICKER_MESSAGE.value, callback)

    fun onTyping(callback: Callback<Int>) = this.longPoll.registerCallback(Events.TYPING.value, callback)

    fun onVideoMessage(callback: Callback<Message>) = this.longPoll.registerCallback(MessageEvents.VIDEO_MESSAGE.value, callback)

    fun onVoiceMessage(callback: Callback<Message>) = this.longPoll.registerCallback(MessageEvents.VOICE_MESSAGE.value, callback)

    fun onWallMessage(callback: Callback<Message>) = this.longPoll.registerCallback(MessageEvents.WALL_MESSAGE.value, callback)

    /* Other */
    fun onFriendOnline(callback: CallbackDouble<Int, Int>) = this.longPoll.registerAbstractCallback(Events.FRIEND_ONLINE.value, callback)

    fun onFriendOffline(callback: CallbackDouble<Int, Int>) = this.longPoll.registerAbstractCallback(Events.FRIEND_OFFLINE.value, callback)

    /* Commands */
    fun onCommand(command: String, callback: Callback<Message>) = this.commands.add(Command(command, callback))

    fun onCommand(vararg commands: String, callback: Callback<Message>) = this.commands.add(Command(commands.toList(), callback))

    fun onCommand(list: List<String>, callback: Callback<Message>) = this.commands.add(Command(list, callback))


    /**
     * If true, all updates from longpoll server
     * will be logged to level 'INFO'
     *
     * @param enable true or false
     */
    fun enableLoggingUpdates(enable: Boolean) = this.longPoll.enableLoggingUpdates(enable)

    class Command {
        val commands: Array<String>
        val callback: Callback<Message>

        constructor(commands: Array<String>, callback: Callback<Message>) {
            this.commands = commands
            this.callback = callback
        }

        constructor(command: String, callback: Callback<Message>) {
            this.commands = arrayOf(command)
            this.callback = callback
        }

        constructor(command: List<String>, callback: Callback<Message>) {
            this.commands = command.toTypedArray()
            this.callback = callback
        }
    }

    override fun toString(): String {
        return String.format("{\"id\": %s}", id)
    }
}