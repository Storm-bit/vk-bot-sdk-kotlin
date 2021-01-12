package com.github.stormbit.sdk.objects

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.models.Chat
import com.github.stormbit.sdk.vkapi.methods.messages.ChatChangePhotoResponse

@Suppress("unused")
class Chat(private val client: Client, private var chatId: Int) {
    companion object {
        const val CHAT_PREFIX = 2000000000
    }

    init {
        chatId = if (chatId > CHAT_PREFIX) chatId - CHAT_PREFIX else chatId
    }

    fun addUser(userId: Int): Int? = client.messages.addChatUser(chatId, userId)

    fun kickUser(userId: Int): Int? = client.messages.removeChatUser(chatId, userId)

    fun deletePhoto(): ChatChangePhotoResponse? = client.messages.deleteChatPhoto(chatId)

    fun editTitle(newTitle: String): Int? = client.messages.editChat(chatId, newTitle)

    fun getUsers(): List<Int>? = client.messages.getChatUsers(chatId)

    fun getChatInfo(): List<Chat>? = client.messages.getChat(listOf(chatId))


    fun addUserAsync(userId: Int, callback: Callback<Int?>? = null) = client.messagesAsync.addChatUser(chatId, userId, callback = callback)

    fun kickUserAsync(userId: Int, callback: Callback<Int?>? = null) = client.messagesAsync.removeChatUser(chatId, userId, callback = callback)

    fun deletePhotoAsync(callback: Callback<ChatChangePhotoResponse?>? = null) = client.messagesAsync.deleteChatPhoto(chatId, callback = callback)

    fun editTitleAsync(newTitle: String, callback: Callback<Int?>? = null) = client.messagesAsync.editChat(chatId, newTitle, callback = callback)

    fun getUsersAsync(callback: Callback<List<Int>?>) = client.messagesAsync.getChatUsers(chatId, callback = callback)

    fun getChatInfoAsync(callback: Callback<List<Chat>?>) = client.messagesAsync.getChat(listOf(chatId), callback = callback)
}