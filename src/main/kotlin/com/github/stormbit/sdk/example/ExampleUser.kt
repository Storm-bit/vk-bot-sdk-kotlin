package com.github.stormbit.sdk.example

import com.github.stormbit.sdk.clients.User
import com.github.stormbit.sdk.events.message.MessageNewEvent
import com.github.stormbit.sdk.objects.Captcha
import org.apache.log4j.BasicConfigurator

/**
 * Simple echo bot
 */
class ExampleUser {
    companion object {
        private const val login = "" // Your login
        private const val password = "" // Your password

        @JvmStatic
        fun main(args: Array<String>) {
            BasicConfigurator.configure()

            // Captcha handler
            val captchaHandler = { it: Captcha ->
                println("Captcha url: ${it.url}")
                print("Code: ")
                readLine() ?: ""
            }

            // Two Factor handler
            val twoFactorHandler = {
                print("Code: ")
                Pair(readLine() ?: "", true)
            }

            val client = User(login, password, twoFactorListener = twoFactorHandler, captchaListener = captchaHandler)

            client.longPoll.enableTyping(true)

            client.on<MessageNewEvent> {
                client.sendMessage {
                    text = message.text
                    peerId = message.peerId
                }
            }
        }
    }
}