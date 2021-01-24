## VK Bot SDK Kotlin

## Using in your projects
### Maven
```xml
<dependency>
    <groupId>com.github.storm-bit</groupId>
    <artifactId>vk-bot-sdk-kotlin</artifactId>
    <version>1.1.9</version>
</dependency>
```

## Examples:
**User echo bot**
```kotlin
class ExampleVkUser {
    companion object {
        private const val login = ""
        private const val password = ""

        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            BasicConfigurator.configure()
            
            val captchaHandler = CaptchaHandler {
                println("Captcha url: ${it.url}")
                print("Code: ")
                readLine()
            }
            
            val twoFactorHandler = TwoFactorHandler {
                print("Code: ")
                Pair(readLine() ?: "", true)
            }

            val client = VkUserClient(login, password, captchaHandler = captchaHandler, twoFactorHandler = twoFactorHandler)
            client.auth()

            // Handle any message
            client.onMessage {
                handle {
                    it.respond(it.message.text)
                }
            }

            client.startLongPoll()
        }
    }
}
```

**Group echo bot with a simple keyboard**
```kotlin
class ExampleGroup {
    companion object {
        private const val token = "" // Your token
        private const val id = 0 // Your group id

        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            BasicConfigurator.configure()

            val client = GroupClient(token, id)
            client.auth()

            // Handle any message
            client.onMessage {
                
                // Handle message from user
                onMessageFrom<User> {
                    handle {
                        val keyboard = Keyboard.build {
                            row {
                                defaultButton("Press me!")
                            }
                        }

                        it.respond(it.message.text, keyboard = keyboard)
                    }
                }

                // Handle message from chat
                onMessageFrom<Chat> {
                    
                    // Handle chat_title_update action
                    onServiceAction<ChatTitleUpdate> {
                        handle {
                            it.respond("New title: ${it.newTitle}")
                        }
                    }
                    
                    // Handle if no action isn't handled
                    intercept {
                        it.respond("Unknown service type!")
                    }
                }

                // Handle if no route isn't handled                
                intercept {
                    it.respond("Who are you?")
                }
            }

            client.startLongPoll()
        }
    }
}
```