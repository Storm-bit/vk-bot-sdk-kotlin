package com.github.stormbit.sdk.objects

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import org.json.JSONObject

@Suppress("unused")
class Chat(private val client: Client, private val chatId: Int) {
    companion object {
        const val CHAT_PREFIX = 2000000000
    }

    fun addUser(userId: Int, callbacks: Callback<JSONObject>? = null) = callbacks?.onResult(client.messages.addChatUser((chatId - CHAT_PREFIX), userId))

    fun kickUser(userId: Int, callbacks: Callback<JSONObject>? = null) = callbacks?.onResult(client.messages.removeChatUser((chatId - CHAT_PREFIX), userId))

    fun deletePhoto(callbacks: Callback<JSONObject>? = null) = callbacks?.onResult(client.messages.deleteChatPhoto((chatId - CHAT_PREFIX)))

    fun editTitle(newTitle: String, callbacks: Callback<JSONObject>? = null) = callbacks?.onResult(client.messages.editChat((chatId - CHAT_PREFIX), newTitle))

    fun getUsers(callbacks: Callback<List<Int>>) = callbacks.onResult(client.messages.getChatUsers((chatId - CHAT_PREFIX)))

    fun getChatInfo(callback: Callback<JSONObject>) = callback.onResult(client.messages.getChat(listOf(chatId)))
}