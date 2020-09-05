package com.github.stormbit.sdk.utils.vkapi.keyboard

import org.json.JSONObject
import java.io.Serializable
import java.util.*

@Suppress("unused")
class KeyboardBuilder(built: KeyboardBuilder.() -> KeyboardBuilder) : Serializable {
    private var oneTime = false
    private val rows = ArrayList<List<Keyboard.Button>>()

    init {
        built()
    }

    fun isOneTime(isOneTime: Boolean): KeyboardBuilder? {
        oneTime = isOneTime
        return this
    }

    fun buttonsRow(builder: RowBuilder): KeyboardBuilder {
        rows.add(builder.getButtons())
        return this
    }

    fun locationButton(payload: JSONObject): KeyboardBuilder {
        rows.add(listOf(Keyboard.Button(Keyboard.Button.Action(Keyboard.Button.Action.Type.LOCATION, payload))))
        return this
    }

    fun vkPayButton(hash: String, payload: JSONObject): KeyboardBuilder {
        rows.add(listOf(Keyboard.Button(Keyboard.Button.Action(Keyboard.Button.Action.Type.VK_PAY, payload, hash))))
        return this
    }

    fun vkAppButton(label: String, payload: JSONObject): KeyboardBuilder {
        rows.add(listOf(Keyboard.Button(Keyboard.Button.Action(Keyboard.Button.Action.Type.LOCATION, label, payload))))
        return this
    }

    fun openLinkButton(label: String, link: String, payload: JSONObject): KeyboardBuilder? {
        rows.add(listOf(Keyboard.Button(Keyboard.Button.Action(Keyboard.Button.Action.Type.LOCATION, label, link, payload))))
        return this
    }

    fun build(): Keyboard {
        return Keyboard(oneTime, rows)
    }
}