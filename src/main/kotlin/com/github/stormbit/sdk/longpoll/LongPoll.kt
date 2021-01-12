package com.github.stormbit.sdk.longpoll

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.clients.Group
import com.github.stormbit.sdk.events.Event
import com.github.stormbit.sdk.longpoll.updateshandlers.UpdatesHandler
import com.github.stormbit.sdk.longpoll.updateshandlers.UpdatesHandlerGroup
import com.github.stormbit.sdk.longpoll.updateshandlers.UpdatesHandlerUser
import com.github.stormbit.sdk.objects.models.LongPollServerResponse
import com.github.stormbit.sdk.utils.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonObject
import net.dongliu.requests.Header
import net.dongliu.requests.Requests
import net.dongliu.requests.exception.RequestsException
import org.slf4j.LoggerFactory

@Suppress("unused")
class LongPoll(private val client: Client) {
    private val log = LoggerFactory.getLogger(LongPoll::class.java)

    private var server: String? = null
    private var key: String? = null
    private var ts: Int? = null
    private var pts: Int? = null

    private val wait = 25

    /**
     * 2 + 32 + 128
     * items + pts + random_id
     */
    private val mode = 162

    private var version = 3
    private val isNeedPts = true
    private val apiVersion = VK_API_VERSION

    @Volatile
    private var isLongPollStarted = false

    val updatesHandler: UpdatesHandler = if (client is Group) UpdatesHandlerGroup(client) else UpdatesHandlerUser(client)

    fun start() {
        updatesHandler.start()
        val isDataSet = setData()

        if (!isDataSet) {
            log.error("Some error occurred when trying to get longpoll settings, aborting. Trying again in 1 sec.")

            try {
                Thread.sleep(1000)
            } catch (ignored: InterruptedException) { }
        }

        if (!isLongPollStarted) {
            isLongPollStarted = true

            val longPollThread = Thread(this::startListening)
            longPollThread.name = "LongPoll Listener"
            longPollThread.start()
        }
    }

    /**
     * If you need to set new longpoll server, or restart listening
     * stop old before.
     */
    fun stop() {
        isLongPollStarted = false
    }

    inline fun <reified T : Event> registerEvent(noinline consumer: T.() -> Unit) {
        updatesHandler.registerEvent(consumer)
    }

    private fun setData(): Boolean {
        val serverResponse = when (client) {
            is Group -> getLongpollServerGroup(client.id)

            else -> getLongPollServer()
        }

        var serv: String = serverResponse.server!!

        if (!serv.startsWith("https://")) {
            serv = "https://$serv"
        }

        server = serv
        key = serverResponse.key
        ts = serverResponse.ts
        pts = serverResponse.pts

        return true
    }

    private fun getLongPollServer(): LongPollServerResponse {
        val result = client.messages.getLongPollServer(isNeedPts, version)!!

        log.info("LongPollServerResponse: \n$result\n")

        return result
    }

    private fun getLongpollServerGroup(groupId: Int): LongPollServerResponse {
        val result = client.groups.getLongPollServer(groupId)!!

        log.info("LongPollServerResponse: \n$result\n")

        return result
    }

    private fun startListening() {
        log.info("Started listening to events from VK LongPoll server...")

        while (isLongPollStarted) {
            var response: JsonObject?
            var responseString = "{}"

            try {
                val url = "$server?act=a_check&key=$key&ts=$ts&wait=$wait&mode=$mode&version=$version&msgs_limit=100000"

                responseString = try {
                    Requests.get(url)
                            .timeout(30000)
                            .headers(Header("Accept-Charset", "utf-8"))
                            .send().readToText()
                } catch (ignored: RequestsException) { continue }

                response = responseString.toJsonObject()

            } catch (ignored: SerializationException) {
                log.error("Some error occurred, no updates got from longpoll server: $responseString")

                try {
                    Thread.sleep(1000)
                } catch (ignored: InterruptedException) { }

                continue
            }

            if (response.containsKey("failed")) {
                val code = response.getInt("failed")

                log.error("Response of VK LongPoll fallen with error code $code")

                if (code == 4) {
                    version = response.getInt("max_version")!!
                } else {
                    if (response.containsKey("ts")) {
                        ts = response.getInt("ts")
                    }
                }
                setData()
            } else {
                if (response.containsKey("ts")) ts = response.getInt("ts")

                if (response.containsKey("pts")) pts = response.getInt("pts")

                if (this.updatesHandler.eventsCount() > 0 || this.updatesHandler.commandsCount() > 0) {
                    if (response.containsKey("ts") && response.containsKey("updates")) {
                        this.updatesHandler.handle(response.getJsonArray("updates")!!)
                    } else {
                        log.error("Bad response from VK LongPoll server: no `ts` or `updates` array: $response")
                        try {
                            Thread.sleep(1000)
                        } catch (ignored: InterruptedException) { }
                    }
                }
            }
        }
    }

    /**
     * If the client need to start typing
     * after receiving message
     * and until client's message is sent
     * @param enable true or false
     */
    fun enableTyping(enable: Boolean) {
        updatesHandler.sendTyping = enable
    }
}