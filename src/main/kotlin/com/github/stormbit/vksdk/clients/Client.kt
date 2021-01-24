package com.github.stormbit.vksdk.clients

import com.github.stormbit.vksdk.events.Event
import com.github.stormbit.vksdk.longpoll.LongPoll
import com.github.stormbit.vksdk.objects.Chat
import com.github.stormbit.vksdk.objects.Message
import com.github.stormbit.vksdk.objects.upload.Upload
import com.github.stormbit.vksdk.utils.VK_API_VERSION
import com.github.stormbit.vksdk.utils.append
import com.github.stormbit.vksdk.vkapi.*
import com.github.stormbit.vksdk.vkapi.methods.docs.DocsApi
import com.github.stormbit.vksdk.vkapi.methods.friends.FriendsApi
import com.github.stormbit.vksdk.vkapi.methods.groups.GroupsApi
import com.github.stormbit.vksdk.vkapi.methods.likes.LikesApi
import com.github.stormbit.vksdk.vkapi.methods.messages.MessagesApi
import com.github.stormbit.vksdk.vkapi.methods.photos.PhotosApi
import com.github.stormbit.vksdk.vkapi.methods.users.UsersApi
import com.github.stormbit.vksdk.vkapi.methods.video.VideoApi
import com.github.stormbit.vksdk.vkapi.methods.wall.WallApi
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.http.*
import kotlinx.serialization.KSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused", "LeakingThis")
abstract class Client(internal var httpClient: HttpClient) {
    internal val log: Logger = LoggerFactory.getLogger(Client::class.java)

    init {
        httpClient = httpClient.configure()
    }

    /**
     * Main params
     */
    var id: Int = 0
    internal var api: API = API()
    lateinit var longPoll: LongPoll
    internal var token: String? = null
    internal var values: Parameters = parametersOf()
    internal var headers: Headers = headersOf()

    internal var baseParams: Parameters = Parameters.Empty

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
    val upload = Upload(this)

    internal var messageHandler: DefaultMessageRoute? = null

    open suspend fun auth() {
        longPoll = LongPoll(this)

        baseParams = Parameters.build {
            append("v", VK_API_VERSION)
            append("access_token", token)
        }
    }

    suspend fun sendMessage(block: Message.() -> Unit): Int {
        val message = Message(this, block)

        return message.send()
    }

    fun startLongPoll() {
        longPoll.start()
    }

    suspend fun onMessage(block: (suspend DefaultMessageRoute.() -> Unit)) {
        messageHandler = DefaultMessageRoute(ApiContext(this), null, emptyList())
        messageHandler?.block()
    }

    /**
     * If the client need to start typing
     * after receiving message
     * and until client's message is sent
     *
     * @param enable true or false
     */
    fun enableTyping(enable: Boolean) = longPoll.enableTyping(enable)

    inline fun <reified T : Event> on(noinline callback: suspend T.() -> Unit) = longPoll.registerEvent(callback, T::class)

    fun chat(chatId: Int): Chat {
        return Chat(this, chatId)
    }

    fun <T : Any> execute(code: String, serializer: KSerializer<T>): VkApiRequest<T> {
        return VkApiRequest(
            this,
            HttpMethod.Get,
            "execute",
            parametersOf("code", code),
            serializer
        )
    }

    internal suspend fun getClientId(): Int {
        val user = users.get().execute()[0]

        return user.id
    }

    private fun HttpClient.configure() = config {
        install(HttpTimeout)
    }

    override fun toString(): String = "{\"id\": $id}"
}