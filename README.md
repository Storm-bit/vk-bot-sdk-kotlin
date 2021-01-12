# VK Bot SDK Kotlin

## Examples:
**User echo bot**
```kotlin
    class ExampleUser {
        companion object {
            private const val login = "" // Your login
            private const val password = "" // Your password
    
            @JvmStatic
            fun main(args: Array<String>) {
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
                client.auth()
                
                client.startLongPoll() // Start long poll listening

                // get users by ids
                val users = client.users.getById(listOf(1, 2, 3))!!
                
                // register long poll event
                client.on<MessageNewEvent> {
                    client.sendMessage {
                        text = message.text
                        peerId = message.peerId
                    }
                }
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
        fun main(args: Array<String>) {
            BasicConfigurator.configure()

            val client = Group(token, id)
            client.auth()
            
            client.startLongPoll() // Start long poll listening

            // register long poll event
            client.on<MessageNewEvent> {
                client.sendMessage {
                    peerId = message.peerId
                    text = message.text

                    keyboard = KeyboardBuilder {
                        buttons {
                            defaultButton("Press me!")
                        }
                    }.build()
                }
            }
        }
    }
}
```