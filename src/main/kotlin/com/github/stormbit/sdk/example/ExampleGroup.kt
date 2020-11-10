package com.github.stormbit.sdk.example

import com.github.stormbit.sdk.clients.Group
import com.github.stormbit.sdk.events.message.MessageNewEvent
import com.github.stormbit.sdk.utils.vkapi.keyboard.KeyboardBuilder
import org.apache.log4j.BasicConfigurator

/**
 * Simple echo bot with keyboard
 */
class ExampleGroup {
    companion object {
        private const val token = "bfcdd2c46a068e84f844fdcc13fd3786e29261b9d08551b10a0bc5de6bb62b12b2f9f96693a0a013727e8" // Your token
        private const val id = 198307928 // Your group id

        @JvmStatic
        fun main(args: Array<String>) {
            BasicConfigurator.configure()

            val client = Group(token, id)

            client.on<MessageNewEvent> {
                client.sendMessage {
                    peerId = message.peerId
                    text = message.text

                    keyboard = KeyboardBuilder {
                        buttonsRow {
                            defaultButton("Press me!")
                        }
                    }.build()

                }
            }
        }
    }
}