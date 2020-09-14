package com.github.stormbit.sdk.utils.vkapi.keyboard

import com.github.stormbit.sdk.utils.vkapi.keyboard.Keyboard.Button.ButtonColor
import com.google.gson.JsonObject
import java.io.Serializable
import java.util.*

@Suppress("unused")
class RowBuilder : Serializable {
    private var buttons = ArrayList<Keyboard.Button>()

    constructor()

    constructor(build: RowBuilder.() -> RowBuilder) {
        build()
    }

    fun primaryButton(label: String, payload: JsonObject): RowBuilder {
        addButton(label, payload, ButtonColor.PRIMARY)
        return this
    }

    fun defaultButton(label: String, payload: JsonObject): RowBuilder {
        addButton(label, payload, ButtonColor.DEFAULT)
        return this
    }

    fun positiveButton(label: String, payload: JsonObject): RowBuilder {
        addButton(label, payload, ButtonColor.POSITIVE)
        return this
    }

    fun negativeButton(label: String, payload: JsonObject): RowBuilder {
        addButton(label, payload, ButtonColor.NEGATIVE)
        return this
    }


    fun primaryButton(label: String): RowBuilder {
        addButton(label, JsonObject(), ButtonColor.PRIMARY)
        return this
    }

    fun defaultButton(label: String): RowBuilder {
        addButton(label, JsonObject(), ButtonColor.DEFAULT)
        return this
    }

    fun positiveButton(label: String): RowBuilder {
        addButton(label, JsonObject(), ButtonColor.POSITIVE)
        return this
    }

    fun negativeButton(label: String): RowBuilder {
        addButton(label, JsonObject(), ButtonColor.NEGATIVE)
        return this
    }

    private fun addButton(label: String, payload: JsonObject, color: ButtonColor) {
        buttons.add(Keyboard.Button(color, Keyboard.Button.Action(Keyboard.Button.Action.Type.TEXT, label, payload)))
    }

    fun getButtons(): ArrayList<Keyboard.Button> {
        return buttons
    }

    fun setButtons(buttons: ArrayList<Keyboard.Button>) {
        this.buttons = buttons
    }
}