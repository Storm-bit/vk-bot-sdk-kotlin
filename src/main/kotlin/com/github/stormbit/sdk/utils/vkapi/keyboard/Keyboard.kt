package com.github.stormbit.sdk.utils.vkapi.keyboard

import com.github.stormbit.sdk.utils.gson
import com.google.gson.JsonObject
import kotlinx.serialization.Serializable
import java.util.*

@Suppress("unused")
@Serializable
class Keyboard {
    private var isOneTime = false
    private var buttons: List<List<Button>> = ArrayList()

    constructor(isOneTime: Boolean, buttons: List<List<Button>>) {
        this.isOneTime = isOneTime
        this.buttons = buttons
    }

    constructor(isOneTime: Boolean) {
        this.isOneTime = isOneTime
    }

    constructor(buttons: List<List<Button>>) {
        this.buttons = buttons
    }

    fun isOne_time(): Boolean {
        return this.isOneTime
    }

    fun setOne_time(one_time: Boolean) {
        this.isOneTime = one_time
    }

    fun getButtons(): List<List<Button>> {
        return buttons
    }

    fun setButtons(buttons: List<List<Button>>) {
        this.buttons = buttons
    }

    @Serializable
    class Button {
        var action = Action()
        var color = ButtonColor.DEFAULT.color
            private set

        fun setColor(color: ButtonColor) {
            this.color = color.color
        }

        constructor(color: ButtonColor, action: Action) {
            this.color = color.color
            this.action = action
        }

        constructor(color: ButtonColor) {
            this.color = color.color
        }

        constructor(action: Action) {
            this.action = action
        }

        @Serializable
        class Action {
            var type: String? = null
            var label: String? = null
            var payload = "{}"
            var hash: String? = null
            var link: String? = null

            fun setType(type: Type) {
                this.type = type.type
            }

            fun setPayload(payload: JsonObject) {
                this.payload = payload.toString()
            }

            constructor(type: Type, label: String?, payload: JsonObject, hash: String?, link: String?) {
                this.type = type.type
                this.label = label
                this.payload = payload.toString()
                this.hash = hash
                this.link = link
            }

            constructor(type: Type, label: String?, payload: JsonObject) {
                this.type = type.type
                this.label = label
                this.payload = payload.toString()
            }

            constructor(type: Type, label: String?, payload: JsonObject, hash: String?) {
                this.type = type.type
                this.label = label
                this.payload = payload.toString()
                this.hash = hash
            }

            constructor(type: Type, label: String?, link: String?, payload: JsonObject) {
                this.type = type.type
                this.label = label
                this.payload = payload.toString()
                this.link = link
            }

            constructor(type: Type, payload: JsonObject, hash: String?) {
                this.type = type.type
                this.payload = payload.toString()
                this.hash = hash
            }

            constructor(label: String?, payload: JsonObject) {
                this.label = label
                this.payload = payload.toString()
            }

            constructor(type: Type, payload: JsonObject) {
                this.type = type.type
                this.payload = payload.toString()
            }

            constructor(type: Type, label: String?) {
                this.type = type.type
                this.label = label
            }

            constructor(type: Type) {
                this.type = type.type
            }

            constructor()

            @Serializable
            enum class Type(val type: String) {
                TEXT("text"),
                LOCATION("location"),
                VK_PAY("vkpay"),
                OPEN_APP("open_app"),
                OPEN_LINK("open_link"),
                OPEN_PHOTO("open_photo");

            }
        }

        @Serializable
        enum class ButtonColor(val color: String) {
            PRIMARY("primary"),
            DEFAULT("secondary"),
            NEGATIVE("negative"),
            POSITIVE("positive");

        }
    }

    override fun toString(): String {
        return gson.toJsonTree(this).toString()
    }
}