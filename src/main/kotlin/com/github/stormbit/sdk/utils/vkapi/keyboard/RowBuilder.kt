package com.github.stormbit.sdk.utils.vkapi.keyboard

import com.github.stormbit.sdk.utils.vkapi.keyboard.Keyboard.Button.ButtonColor
import org.json.JSONObject
import java.io.Serializable
import java.util.*

@Suppress("unused")
class RowBuilder : Serializable {
    private var buttons = ArrayList<Keyboard.Button>()

    constructor()

    constructor(build: RowBuilder.() -> RowBuilder) {
        build()
    }

    fun primaryButton(label: String, payload: JSONObject): RowBuilder {
        addButton(label, payload, ButtonColor.PRIMARY)
        return this
    }

    fun defaultButton(label: String, payload: JSONObject): RowBuilder {
        addButton(label, payload, ButtonColor.DEFAULT)
        return this
    }

    fun positiveButton(label: String, payload: JSONObject): RowBuilder {
        addButton(label, payload, ButtonColor.POSITIVE)
        return this
    }

    fun negativeButton(label: String, payload: JSONObject): RowBuilder {
        addButton(label, payload, ButtonColor.NEGATIVE)
        return this
    }


    fun primaryButton(label: String): RowBuilder {
        addButton(label, JSONObject(), ButtonColor.PRIMARY)
        return this
    }

    fun defaultButton(label: String): RowBuilder {
        addButton(label, JSONObject(), ButtonColor.DEFAULT)
        return this
    }

    fun positiveButton(label: String): RowBuilder {
        addButton(label, JSONObject(), ButtonColor.POSITIVE)
        return this
    }

    fun negativeButton(label: String): RowBuilder {
        addButton(label, JSONObject(), ButtonColor.NEGATIVE)
        return this
    }

    private fun addButton(label: String, payload: JSONObject, color: ButtonColor) {
        buttons.add(Keyboard.Button(color, Keyboard.Button.Action(Keyboard.Button.Action.Type.TEXT, label, payload)))
    }

    fun getButtons(): ArrayList<Keyboard.Button> {
        return buttons
    }

    fun setButtons(buttons: ArrayList<Keyboard.Button>) {
        this.buttons = buttons
    }
}