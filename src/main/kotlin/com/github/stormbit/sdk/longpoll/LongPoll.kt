package com.github.stormbit.sdk.longpoll

import com.github.stormbit.sdk.callbacks.AbstractCallback
import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.clients.Group
import com.github.stormbit.sdk.longpoll.responses.GetLongpollServerResponse
import com.github.stormbit.sdk.utils.Utils
import com.github.stormbit.sdk.utils.Utils.Companion.toJsonObject
import com.github.stormbit.sdk.utils.getInt
import com.github.stormbit.sdk.utils.getString
import com.github.stormbit.sdk.utils.vkapi.methods.messages.MessagesApi
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
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
     * attachments + pts + random_id
     */
    private val mode = 162

    private var version = 3
    private val isNeedPts = true
    private val apiVersion = Utils.version

    @Volatile
    private var longpollIsOn = false

    private val updatesHandler: UpdatesHandler = if (client is Group) UpdatesHandlerGroup(client) else UpdatesHandlerUser(client)


    /**
     * If true, all updates from longpoll server
     * will be logged to level 'INFO'
     */
    @Volatile
    private var logUpdates = false

    init {
        updatesHandler.start()
        val dataSetted = setData()

        if (!dataSetted) {
            log.error("Some error occurred when trying to get longpoll settings, aborting. Trying again in 1 sec.")

            try {
                Thread.sleep(1000)
            } catch (ignored: InterruptedException) {
            }

        }

        if (!longpollIsOn) {
            longpollIsOn = true
            val threadLongpollListener = Thread(this::startListening)
            threadLongpollListener.name = "threadLongpollListener"
            threadLongpollListener.start()
        }
    }

    /**
     * If you need to set new longpoll server, or restart listening
     * off old before.
     */
    fun off() {
        longpollIsOn = false
    }

    /**
     * Add callback to the map
     *
     * @param name     Callback name
     * @param callback Callback
     */
    fun registerCallback(name: String, callback: Callback<*>) = updatesHandler.registerCallback(name, callback)

    /**
     * Add callback to the map
     *
     * @param name     Callback name
     * @param callback Callback
     */
    fun registerAbstractCallback(name: String, callback: AbstractCallback) = updatesHandler.registerAbstractCallback(name, callback)

    /**
     * Add callback to the map
     *
     * @param name     Callback name
     * @param callback Callback
     */
    fun registerChatCallback(name: String, callback: AbstractCallback) = updatesHandler.registerChatCallback(name, callback)

    private fun setData(): Boolean {

        val serverResponse = if (client is Group) {
            getLongpollServerGroup(client.id)
        } else {
            getLongPollServer()
        }

        if (serverResponse == null) {
            log.error("Some error occurred, bad response returned from getting LongPoll server settings (server, key, ts, pts).")
            return false
        }

        var serv: String = serverResponse.server

        if (!serv.startsWith("https://")) {
            serv = "https://$serv"
        }

        server = serv
        key = serverResponse.key
        ts = serverResponse.ts
        pts = serverResponse.pts

        return true
    }

    private fun getLongPollServer(): GetLongpollServerResponse? {
        val method = MessagesApi.Companion.Methods.getLongPollServer

        if (!Utils.hashes.has(method)) {
            Utils.getHash(client.auth, method)
        }

        val result = client.messages.getLongPollServer(isNeedPts, version)

        val response: JsonObject

        if (!result.has("response") || !result.getAsJsonObject("response").has("key") || !result.getAsJsonObject("response").has("server") || !result.getAsJsonObject("response").has("ts")) {
            log.error("Bad response of getting longpoll server!\nQuery: {\"need_pts\": \"$isNeedPts\", \"lp_version\": \"$version\"}\n Response: {}", result)
            return null
        }

        response = try {
            result.getAsJsonObject("response")
        } catch (e: JsonParseException) {
            log.error("Bad response of getting longpoll server.")
            return null
        }

        log.info("GetLongPollServerResponse: \n{}\n", response)

        return GetLongpollServerResponse(
                response.getString("key"),
                response.getString("server"),
                response.getInt("ts"),
                response.getInt("pts")
        )
    }

    private fun getLongpollServerGroup(groupId: Int): GetLongpollServerResponse? {
        val result = client.groups.getLongPollServer(groupId)

        val response: JsonObject

        if (!result.has("response") || !result.getAsJsonObject("response").has("key") || !result.getAsJsonObject("response").has("server") || !result.getAsJsonObject("response").has("ts")) {
            log.error("Bad response of getting longpoll server!\nQuery: {groupId: $groupId}\n Response: {}", result)
            return null
        }

        response = try {
            result.getAsJsonObject("response")
        } catch (e: JsonParseException) {
            log.error("Bad response of getting longpoll server.")
            return null
        }

        log.info("GetLongPollServerResponse: \n{}\n", response)

        return GetLongpollServerResponse(
                response.getString("key"),
                response.getString("server"),
                response.getInt("ts")
        )
    }

    private fun startListening() {
        log.info("Started listening to events from VK LongPoll server...")

        while (longpollIsOn) {
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

                response = toJsonObject(responseString)

            } catch (ignored: JsonParseException) {
                log.error("Some error occurred, no updates got from longpoll server: {}", responseString)

                try {
                    Thread.sleep(1000)
                } catch (ignored: InterruptedException) {
                }

                continue
            }

            if (logUpdates) log.info("Response of getting updates: \n{}\n", response)

            if (response.has("failed")) {
                val code = response.getInt("failed")

                log.error("Response of VK LongPoll fallen with error code {}", code)

                if (code == 4) {
                    version = response.getInt("max_version")
                } else {
                    if (response.has("ts")) {
                        ts = response.getInt("ts")
                    }
                }
                setData()
            } else {
                if (response.has("ts")) ts = response.getInt("ts")

                if (response.has("pts")) pts = response.getInt("pts")

                if (this.updatesHandler.callbacksCount() > 0 || this.updatesHandler.commandsCount() > 0 || this.updatesHandler.chatCallbacksCount() > 0) {
                    if (response.has("ts") && response.has("updates")) {
                        this.updatesHandler.handle(response.getAsJsonArray("updates"))
                    } else {
                        log.error("Bad response from VK LongPoll server: no `ts` or `updates` array: {}", response)
                        try {
                            Thread.sleep(1000)
                        } catch (ignored: InterruptedException) {
                        }
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

    fun enableLoggingUpdates(enable: Boolean) {
        logUpdates = enable
    }
}