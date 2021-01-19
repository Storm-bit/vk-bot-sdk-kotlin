package com.github.stormbit.sdk.vkapi

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.events.MessageEvent
import com.github.stormbit.sdk.objects.Message
import com.github.stormbit.sdk.objects.models.Keyboard

@ContextDsl
class ApiContext(val client: Client) {

    @Suppress("unused")
    suspend fun respond(peerId: Int, message: CharSequence, attachments: Array<String> = emptyArray(), keyboard: Keyboard? = null) {
        client.sendMessage {
            this.peerId = peerId
            text = message
            attachments(*attachments)
            this.keyboard = keyboard
        }
    }

    @Suppress("unused")
    suspend fun respondWithReply(peerId: Int, replyTo: Int, message: CharSequence, attachments: Array<String> = emptyArray(), keyboard: Keyboard? = null) {
        client.sendMessage {
            this.peerId = peerId
            text = message
            attachments(*attachments)
            this.keyboard = keyboard
            this.replyTo = replyTo
        }
    }

    @Suppress("unused")
    suspend fun MessageEvent.respond(message: CharSequence, attachments: Array<String> = emptyArray(), keyboard: Keyboard? = null) {
        this@ApiContext.respond(
            if (this.message.senderType == Message.SenderType.CHAT) this.message.chatIdLong
            else this.message.peerId,
            message,
            attachments,
            keyboard
        )
    }

    @Suppress("unused")
    suspend fun MessageEvent.respondWithReply(message: CharSequence, attachments: Array<String> = emptyArray(), keyboard: Keyboard? = null) {
        this@ApiContext.respondWithReply(
            if (this.message.senderType == Message.SenderType.CHAT) this.message.chatIdLong
            else this.message.peerId,
            this.message.messageId,
            message,
            attachments,
            keyboard
        )
    }
}