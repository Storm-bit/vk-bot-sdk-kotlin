package com.github.stormbit.sdk.example

import com.github.stormbit.sdk.clients.User
import com.github.stormbit.sdk.objects.Message
import org.apache.log4j.BasicConfigurator

class ExampleUser {
    companion object {
        private const val login = "" // Your login
        private const val password = "" // Your password

        @JvmStatic
        fun main(args: Array<String>) {
            BasicConfigurator.configure()

            val client = User(login, password, saveCookie = true, loadFromCookie = true)

            client.onMessage {
                Message {
                    from(client)
                    to(it.authorId!!)
                    text(it.text)
                }.send()
            }
        }
    }
}