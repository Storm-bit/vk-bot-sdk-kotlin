package com.github.stormbit.sdk.example

import com.github.stormbit.sdk.clients.User
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
            val captchaHandler = { it: Captcha -> println(it.getUrl()) }

            // Two Factor handler
            val twoFactorHandler = {
                print("Код: ")
                Pair(readLine() ?: "", true)
            }

            val client = User(login, password, twoFactorListener = twoFactorHandler, captchaListener = captchaHandler)

            client.onMessage {
                client.sendMessage {
                    text = it.text
                    peerId = it.peerId
                }
            }
        }
    }
}