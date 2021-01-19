package com.github.stormbit.sdk.longpoll

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.clients.GroupClient
import com.github.stormbit.sdk.events.Event
import com.github.stormbit.sdk.longpoll.updateshandlers.UpdatesHandler
import com.github.stormbit.sdk.longpoll.updateshandlers.UpdatesHandlerGroup
import com.github.stormbit.sdk.objects.models.LongPollServerResponse
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.vkapi.execute
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

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

    private val updatesHandler: UpdatesHandler<JsonObject> = UpdatesHandlerGroup(client)

    fun start() = runBlocking {
        val isDataSet = setData()

        if (!isDataSet) {
            log.error("Some error occurred when trying to get LongPoll settings, aborting. Trying again in 1 sec.")

            delay(1000)
        }

        GlobalScope.launch {
            updatesHandler.start()
        }

        if (!isLongPollStarted) {
            isLongPollStarted = true

            launch {
                startListening()
            }
        }
    }

    /**
     * If you need to set new LongPoll server, or restart listening
     * stop old before.
     */
    fun stop() {
        isLongPollStarted = false
    }

    fun <T : Event> registerEvent(callback: suspend T.() -> Unit, type: KClass<T>) {
        updatesHandler.registerEvent(callback, type)
    }

    private suspend fun setData(): Boolean {
        val serverResponse = when (client) {
            is GroupClient -> getLongPollServerGroup(client.id)

            else -> getLongPollServer()
        }

        var serv: String = serverResponse.server!!

        val (key, pts, _, ts) = serverResponse

        if (!serv.startsWith("https://")) {
            serv = "https://$serv"
        }

        this.server = serv
        this.key = key
        this.ts = ts
        this.pts = pts

        return true
    }

    private suspend fun getLongPollServer(): LongPollServerResponse {
        val result = client.messages.getLongPollServer(isNeedPts, version).execute()

        log.info("LongPollServerResponse: \n$result\n")

        return result
    }

    private suspend fun getLongPollServerGroup(groupId: Int): LongPollServerResponse {
        val result = client.groups.getLongPollServer(groupId).execute()

        log.info("LongPollServerResponse: \n$result\n")

        return result
    }

    private suspend fun startListening() {
        log.info("Started listening to events senderType VK LongPoll server...")

        while (isLongPollStarted) {
            var response: JsonObject?

            try {
                val url = "$server?act=a_check&key=$key&ts=$ts&wait=$wait&mode=$mode&version=$version&msgs_limit=100000"

                val responseString = longPollRequest(url) ?: continue

                response = responseString.toJsonObject()

            } catch (e: SerializationException) {
                log.error("Some error occurred, no updates got senderType LongPoll server: ", e)

                delay(1000)

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

                if (response.containsKey("ts") && response.containsKey("updates")) {
                    if (response.getJsonArray("updates")!!.isNotEmpty()) {
                        updatesHandler.send(response.getJsonArray("updates")!![0].jsonObject)
                    }
                } else {
                    log.error("Bad response senderType VK LongPoll server: no 'ts' or 'updates' array: $response")

                    delay(1000)
                }
            }
        }
    }

    private suspend fun longPollRequest(url: String): String? {
        return try {
            client.httpClient.get<String>(url) {
                header("Accept-Charset", "utf-8")
                timeout {
                    connectTimeoutMillis = 30000
                    requestTimeoutMillis = 30000
                }
            }
        } catch (e: TimeoutCancellationException) {
            null
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