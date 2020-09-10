package com.github.stormbit.sdk.example

import com.github.stormbit.sdk.clients.Group
import com.github.stormbit.sdk.utils.vkapi.keyboard.KeyboardBuilder
import com.github.stormbit.sdk.utils.vkapi.keyboard.RowBuilder
import org.apache.log4j.BasicConfigurator

class ExampleGroup {
    companion object {
        private const val token = "" // Your token
        private const val id = 0 // Your group id

        @JvmStatic
        fun main(args: Array<String>) {
            BasicConfigurator.configure()

            val client = Group(token, id)

            client.onMessage {
                client.sendMessage {
                    peerId = it.authorId
                    text = it.text

                    keyboard = KeyboardBuilder {
                        buttonsRow(RowBuilder {
                            defaultButton("lol")
                        })
                    }.build()

                }
            }
        }
    }
}