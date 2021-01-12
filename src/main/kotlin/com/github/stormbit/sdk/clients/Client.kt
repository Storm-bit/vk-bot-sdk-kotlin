package com.github.stormbit.sdk.clients

import com.github.stormbit.sdk.events.CommandEvent
import com.github.stormbit.sdk.events.Event
import com.github.stormbit.sdk.longpoll.LongPoll
import com.github.stormbit.sdk.objects.Chat
import com.github.stormbit.sdk.objects.Message
import com.github.stormbit.sdk.objects.Upload
import com.github.stormbit.sdk.vkapi.API
import com.github.stormbit.sdk.vkapi.Auth
import com.github.stormbit.sdk.vkapi.methods.docs.DocsApi
import com.github.stormbit.sdk.vkapi.methods.docs.DocsApiAsync
import com.github.stormbit.sdk.vkapi.methods.friends.FriendsApi
import com.github.stormbit.sdk.vkapi.methods.friends.FriendsApiAsync
import com.github.stormbit.sdk.vkapi.methods.groups.GroupsApi
import com.github.stormbit.sdk.vkapi.methods.groups.GroupsApiAsync
import com.github.stormbit.sdk.vkapi.methods.likes.LikesApi
import com.github.stormbit.sdk.vkapi.methods.likes.LikesApiAsync
import com.github.stormbit.sdk.vkapi.methods.messages.MessagesApi
import com.github.stormbit.sdk.vkapi.methods.messages.MessagesApiAsync
import com.github.stormbit.sdk.vkapi.methods.photos.PhotosApi
import com.github.stormbit.sdk.vkapi.methods.photos.PhotosApiAsync
import com.github.stormbit.sdk.vkapi.methods.users.UsersApi
import com.github.stormbit.sdk.vkapi.methods.users.UsersApiAsync
import com.github.stormbit.sdk.vkapi.methods.video.VideoApi
import com.github.stormbit.sdk.vkapi.methods.video.VideoApiAsync
import com.github.stormbit.sdk.vkapi.methods.wall.WallApi
import com.github.stormbit.sdk.vkapi.methods.wall.WallApiAsync
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.*

@Suppress("unused", "LeakingThis")
abstract class Client {
    internal val log: Logger = LoggerFactory.getLogger(Client::class.java)

    /**
     * Main params
     */
    var id: Int = 0
    lateinit var api: API
    lateinit var longPoll: LongPoll
    internal lateinit var auth: Auth
    var token: String? = null
    var values: Map<String, Any> = mapOf()
    var headers: Map<String, String> = mapOf()

    val commands = CopyOnWriteArrayList<Command>()
    private val chats = ConcurrentHashMap<String, Chat>()

    /**
     * Api methods
     */
    val messages = MessagesApi(this)
    val groups = GroupsApi(this)
    val users = UsersApi(this)
    val photos = PhotosApi(this)
    val video = VideoApi(this)
    val docs = DocsApi(this)
    val friends = FriendsApi(this)
    val likes = LikesApi(this)
    val wall = WallApi(this)

    val messagesAsync = MessagesApiAsync(this)
    val groupsAsync = GroupsApiAsync(this)
    val usersAsync = UsersApiAsync(this)
    val photosAsync = PhotosApiAsync(this)
    val videoAsync = VideoApiAsync(this)
    val docsAsync = DocsApiAsync(this)
    val friendsAsync = FriendsApiAsync(this)
    val likesAsync = LikesApiAsync(this)
    val wallAsync = WallApiAsync(this)

    companion object {
        /**
         * Executor services for thread safing and fast work
         */
        internal val service: ExecutorService = Executors.newCachedThreadPool()
        internal val scheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    }

    abstract fun auth()

    fun sendMessage(block: Message.() -> Unit): Int? {
        val message = Message()
        message.from(this)

        block(message)

        return message.send()
    }

    fun startLongPoll() {
        this.longPoll.start()
    }

    /**
     * If the client need to start typing
     * after receiving message
     * and until client's message is sent
     *
     * @param enable true or false
     */
    fun enableTyping(enable: Boolean) = this.longPoll.enableTyping(enable)

    /* Commands */
    fun onCommand(command: String, callback: CommandEvent.() -> Unit) = this.commands.add(Command(command, callback))

    fun onCommand(vararg commands: String, callback: CommandEvent.() -> Unit) = this.commands.add(Command(commands.toList(), callback))

    fun onCommand(list: List<String>, callback: CommandEvent.() -> Unit) = this.commands.add(Command(list, callback))


    inline fun <reified T : Event> on(noinline callback: T.() -> Unit) = this.longPoll.registerEvent(callback)

    class Command {
        val commands: Array<String>
        val callback: CommandEvent.() -> Unit

        constructor(commands: Array<String>, callback: CommandEvent.() -> Unit) {
            this.commands = commands
            this.callback = callback
        }

        constructor(command: String, callback: CommandEvent.() -> Unit) {
            this.commands = arrayOf(command)
            this.callback = callback
        }

        constructor(command: List<String>, callback: CommandEvent.() -> Unit) {
            this.commands = command.toTypedArray()
            this.callback = callback
        }
    }

    fun chat(chatId: Int): Chat {
        return Chat(this, chatId)
    }

    fun upload(): Upload {
        return Upload(this)
    }

    internal fun getClientId(): Int {
        val user = users.get()!![0]

        return user.id
    }

    override fun toString(): String = "{\"id\": $id}"
}