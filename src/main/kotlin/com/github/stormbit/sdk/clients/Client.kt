package com.github.stormbit.sdk.clients

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.callbacks.CallbackDouble
import com.github.stormbit.sdk.callbacks.CallbackFourth
import com.github.stormbit.sdk.callbacks.CallbackTriple
import com.github.stormbit.sdk.longpoll.LongPoll
import com.github.stormbit.sdk.objects.Chat
import com.github.stormbit.sdk.objects.Message
import com.github.stormbit.sdk.utils.Utils
import com.github.stormbit.sdk.utils.vkapi.API
import com.github.stormbit.sdk.utils.vkapi.Auth
import com.github.stormbit.sdk.utils.vkapi.apis.APIGroup
import com.github.stormbit.sdk.utils.vkapi.apis.APIUser
import com.github.stormbit.sdk.utils.vkapi.methods.docs.DocsApi
import com.github.stormbit.sdk.utils.vkapi.methods.friends.FriendsApi
import com.github.stormbit.sdk.utils.vkapi.methods.groups.GroupsApi
import com.github.stormbit.sdk.utils.vkapi.methods.messages.MessagesApi
import com.github.stormbit.sdk.utils.vkapi.methods.photos.PhotosApi
import com.github.stormbit.sdk.utils.vkapi.methods.users.UsersApi
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors

@Suppress("unused")
abstract class Client {

    /**
     * Main params
     */
    var id: Int
    val api: API
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
    val docs = DocsApi(this)
    val friends = FriendsApi(this)

    companion object {
        /**
         * Executor services for threadsafing and fast work
         */
        val service = Executors.newCachedThreadPool()
        val scheduler = Executors.newSingleThreadScheduledExecutor()
    }

    constructor(login: String, password: String) {
        this.auth = Auth(login, password)
        this.auth.auth()

        this.api = APIUser(this)
        this.id = Utils.getId(this)
        this.longPoll = LongPoll(this)
    }

    constructor(login: String, password: String, listener: Auth.Listener) {
        this.auth = Auth(login, password, listener)
        this.auth.auth()

        this.api = APIUser(this)
        this.id = Utils.getId(this)
        this.longPoll = LongPoll(this)
    }

    constructor(accessToken: String, id: Int) {
        this.auth = Auth()
        this.id = id
        this.token = accessToken
        this.api = APIGroup(this)

        this.longPoll = LongPoll(this)
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
    fun onLongPollEvent(callback: Callback<JSONArray>) = this.longPoll.registerCallback("OnEveryLongPollEventCallback", callback)


    /* Chats */
    fun onChatJoin(callback: CallbackTriple<Int, Int, Int>) = this.longPoll.registerChatCallback("OnChatJoinCallback", callback)

    fun onChatLeave(callback: CallbackTriple<Int, Int, Int>) = this.longPoll.registerChatCallback("OnChatLeaveCallback", callback)

    fun onChatTitleChanged(callback: CallbackFourth<String, String, Int, Int>) = this.longPoll.registerChatCallback("OnChatTitleChangedCallback", callback)

    fun onChatPhotoChanged(callback: CallbackTriple<JSONObject, Int, Int>) = this.longPoll.registerChatCallback("onChatPhotoChangedCallback", callback)

    fun onChatPhotoRemoved(callback: CallbackDouble<Int?, Int?>?) = this.longPoll.registerChatCallback("onChatPhotoRemovedCallback", callback)

    fun onChatCreated(callback: CallbackTriple<String?, Int?, Int?>?) = this.longPoll.registerChatCallback("onChatCreatedCallback", callback)

    /* Messages */
    fun onChatMessage(callback: Callback<Message>) = this.longPoll.registerCallback("OnChatMessageCallback", callback)

    fun onEveryMessage(callback: Callback<Message>) = this.longPoll.registerCallback("OnEveryMessageCallback", callback)

    fun onMessageWithFwds(callback: Callback<Message>) = this.longPoll.registerCallback("OnMessageWithFwdsCallback", callback)

    fun onAudioMessage(callback: Callback<Message>) = this.longPoll.registerCallback("OnAudioMessageCallback", callback)

    fun onDocMessage(callback: Callback<Message>) = this.longPoll.registerCallback("OnDocMessageCallback", callback)

    fun onGifMessage(callback: Callback<Message>) = this.longPoll.registerCallback("OnGifMessageCallback", callback)

    fun onLinkMessage(callback: Callback<Message>) = this.longPoll.registerCallback("OnLinkMessageCallback", callback)

    fun onMessage(callback: Callback<Message>) = this.longPoll.registerCallback("OnMessageCallback", callback)


    fun onPhotoMessage(callback: Callback<Message>) = this.longPoll.registerCallback("OnPhotoMessageCallback", callback)


    fun onSimpleTextMessage(callback: Callback<Message>) = this.longPoll.registerCallback("OnSimpleTextMessageCallback", callback)


    fun onStickerMessage(callback: Callback<Message>) = this.longPoll.registerCallback("OnStickerMessageCallback", callback)


    fun onTyping(callback: Callback<Int>) = this.longPoll.registerCallback("OnTypingCallback", callback)


    fun onVideoMessage(callback: Callback<Message>) = this.longPoll.registerCallback("OnVideoMessageCallback", callback)

    fun onVoiceMessage(callback: Callback<Message>) = this.longPoll.registerCallback("OnVoiceMessageCallback", callback)

    fun onWallMessage(callback: Callback<Message>) = this.longPoll.registerCallback("OnWallMessageCallback", callback)

    /* Other */
    fun onFriendOnline(callback: CallbackDouble<Int, Int>) = this.longPoll.registerAbstractCallback("OnFriendOnlineCallback", callback)

    fun onFriendOffline(callback: CallbackDouble<Int, Int>) = this.longPoll.registerAbstractCallback("OnFriendOfflineCallback", callback)

    /* Commands */
    fun onCommand(command: Any, callback: Callback<Message>) = this.commands.add(Command(command, callback))


    fun onCommand(callback: Callback<Message>, vararg commands: Any?) = this.commands.add(Command(commands, callback))


    fun onCommand(commands: Array<Any>, callback: Callback<Message>) = this.commands.add(Command(commands, callback))


    fun onCommand(list: List<*>, callback: Callback<Message>) = this.commands.add(Command(list, callback))



    /**
     * If true, all updates from longpoll server
     * will be logged to level 'INFO'
     *
     * @param enable true or false
     */
    fun enableLoggingUpdates(enable: Boolean) = this.longPoll.enableLoggingUpdates(enable)

    class Command {
        val commands: Array<Any>
        val callback: Callback<Message>

        constructor(commands: Array<Any>, callback: Callback<Message>) {
            this.commands = commands
            this.callback = callback
        }

        constructor(command: Any, callback: Callback<Message>) {
            this.commands = arrayOf(command)
            this.callback = callback
        }

        constructor(command: List<Any>, callback: Callback<Message>) {
            this.commands = command.toTypedArray()
            this.callback = callback
        }
    }

    // Getters and setters
    fun getId(): Int? = id

    fun setId(id: Int?) {
        this.id = id!!
    }

    override fun toString(): String {
        return String.format("{\"id\": %s}", id)
    }
}