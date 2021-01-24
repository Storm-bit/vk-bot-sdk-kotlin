package com.github.stormbit.vksdk.objects

import com.github.stormbit.vksdk.clients.Client
import com.github.stormbit.vksdk.objects.models.Chat
import com.github.stormbit.vksdk.vkapi.VkApiRequest
import com.github.stormbit.vksdk.vkapi.methods.messages.ChatChangePhotoResponse

@Suppress("unused")
class Chat(private val client: Client, private var chatId: Int) {
    companion object {
        const val CHAT_PREFIX = 2000000000
    }

    init {
        chatId = if (chatId > CHAT_PREFIX) chatId - CHAT_PREFIX else chatId
    }

    fun addUser(userId: Int): VkApiRequest<Int> = client.messages.addChatUser(chatId, userId)

    fun kickUser(userId: Int): VkApiRequest<Int> = client.messages.removeChatUser(chatId, userId)

    fun deletePhoto(): VkApiRequest<ChatChangePhotoResponse> = client.messages.deleteChatPhoto(chatId)

    fun editTitle(newTitle: String): VkApiRequest<Int> = client.messages.editChat(chatId, newTitle)

    suspend fun getUsers(): List<Int> = client.messages.getChatUsers(chatId)

    fun getChatInfo(): VkApiRequest<List<Chat>> = client.messages.getChat(listOf(chatId))
}