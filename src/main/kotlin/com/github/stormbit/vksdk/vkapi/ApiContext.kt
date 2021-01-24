package com.github.stormbit.vksdk.vkapi

import com.github.stormbit.vksdk.clients.Client
import com.github.stormbit.vksdk.events.MessageEvent
import com.github.stormbit.vksdk.objects.Message
import com.github.stormbit.vksdk.objects.models.Keyboard

@ContextDsl
class ApiContext(val client: Client) {

    @Suppress("unused")
    suspend fun respond(peerId: Int, message: CharSequence, attachments: Array<String> = emptyArray(), keyboard: Keyboard? = null) {
        client.sendMessage {
            this.peerId = peerId
            text = message
            this.attachments.addAll(attachments)
            this.keyboard = keyboard
        }
    }

    @Suppress("unused")
    suspend fun respondWithReply(peerId: Int, replyTo: Int, message: CharSequence, attachments: Array<String> = emptyArray(), keyboard: Keyboard? = null) {
        client.sendMessage {
            this.peerId = peerId
            text = message
            this.attachments.addAll(attachments)
            this.keyboard = keyboard
            this.replyTo = replyTo
        }
    }

    @Suppress("unused")
    suspend fun respondWithForwards(peerId: Int, forwardMessages: List<Int>, message: CharSequence, attachments: Array<String> = emptyArray(), keyboard: Keyboard? = null) {
        client.sendMessage {
            this.peerId = peerId
            text = message
            this.attachments.addAll(attachments)
            this.keyboard = keyboard
            this.forwardedMessages.addAll(forwardMessages)
        }
    }


    @Suppress("unused")
    suspend fun MessageEvent.respond(message: CharSequence, attachments: Array<String> = emptyArray(), keyboard: Keyboard? = null) {
        this@ApiContext.respond(
            if (this.message.senderType == Message.SenderType.CHAT) this.message.chatIdLong else this.message.peerId,
            message,
            attachments,
            keyboard
        )
    }

    @Suppress("unused")
    suspend fun MessageEvent.respondWithReply(message: CharSequence, attachments: Array<String> = emptyArray(), keyboard: Keyboard? = null) {
        this@ApiContext.respondWithReply(
            if (this.message.senderType == Message.SenderType.CHAT) this.message.chatIdLong else this.message.peerId,
            this.message.messageId,
            message,
            attachments,
            keyboard
        )
    }

    @Suppress("unused")
    suspend fun MessageEvent.respondWithForwards(message: CharSequence, attachments: Array<String> = emptyArray(), keyboard: Keyboard? = null) {
        this@ApiContext.respondWithForwards(
            if (this.message.senderType == Message.SenderType.CHAT) this.message.chatIdLong else this.message.peerId,
            listOf(this.message.messageId),
            message,
            attachments,
            keyboard
        )
    }
}