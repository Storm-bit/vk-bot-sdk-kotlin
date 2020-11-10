package com.github.stormbit.sdk.objects

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.google.gson.JsonObject

@Suppress("unused")
class Chat(private val client: Client, private val chatId: Int) {
    companion object {
        const val CHAT_PREFIX = 2000000000
    }

    fun addUser(userId: Int, callback: Callback<JsonObject>? = null) = callback?.onResult(client.messages.addChatUser((chatId - CHAT_PREFIX), userId))

    fun kickUser(userId: Int, callback: Callback<JsonObject>? = null) = callback?.onResult(client.messages.removeChatUser((chatId - CHAT_PREFIX), userId))

    fun deletePhoto(callback: Callback<JsonObject>? = null) = callback?.onResult(client.messages.deleteChatPhoto((chatId - CHAT_PREFIX)))

    fun editTitle(newTitle: String, callback: Callback<JsonObject>? = null) = callback?.onResult(client.messages.editChat((chatId - CHAT_PREFIX), newTitle))

    fun getUsers(callback: Callback<List<Int>>) = callback.onResult(client.messages.getChatUsers(chatId - CHAT_PREFIX))

    fun getChatInfo(callback: Callback<JsonObject>) = callback.onResult(client.messages.getChat(listOf(chatId)))
}