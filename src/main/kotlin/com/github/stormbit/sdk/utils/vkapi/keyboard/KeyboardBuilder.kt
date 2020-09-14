package com.github.stormbit.sdk.utils.vkapi.keyboard

import com.google.gson.JsonObject
import java.io.Serializable
import java.util.*

@Suppress("unused")
class KeyboardBuilder(buildd: KeyboardBuilder.() -> Unit) : Serializable {
    var isOneTime = false
    private val rows = ArrayList<List<Keyboard.Button>>()

    init {
        buildd()
    }

    fun buttonsRow(block: RowBuilder.() -> Unit): KeyboardBuilder {
        val rowBuilder = RowBuilder()
        block(rowBuilder)

        rows.add(rowBuilder.getButtons())
        return this
    }

    fun locationButton(payload: JsonObject): KeyboardBuilder {
        rows.add(listOf(Keyboard.Button(Keyboard.Button.Action(Keyboard.Button.Action.Type.LOCATION, payload))))
        return this
    }

    fun vkPayButton(hash: String, payload: JsonObject): KeyboardBuilder {
        rows.add(listOf(Keyboard.Button(Keyboard.Button.Action(Keyboard.Button.Action.Type.VK_PAY, payload, hash))))
        return this
    }

    fun vkAppButton(label: String, payload: JsonObject): KeyboardBuilder {
        rows.add(listOf(Keyboard.Button(Keyboard.Button.Action(Keyboard.Button.Action.Type.LOCATION, label, payload))))
        return this
    }

    fun openLinkButton(label: String, link: String, payload: JsonObject): KeyboardBuilder {
        rows.add(listOf(Keyboard.Button(Keyboard.Button.Action(Keyboard.Button.Action.Type.LOCATION, label, link, payload))))
        return this
    }

    fun build(): Keyboard {
        return Keyboard(isOneTime, rows)
    }
}