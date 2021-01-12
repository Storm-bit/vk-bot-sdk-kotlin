package com.github.stormbit.sdk.objects.keyboard

import com.github.stormbit.sdk.objects.models.Keyboard
import com.github.stormbit.sdk.objects.models.Keyboard.Button.*
import com.github.stormbit.sdk.objects.models.MessagePayload
import java.io.Serializable
import java.util.*

@Suppress("unused")
class ButtonsBuilder : Serializable {
    private var buttons = ArrayList<Keyboard.Button>()

    constructor()

    constructor(build: ButtonsBuilder.() -> ButtonsBuilder) {
        build()
    }

    fun primaryButton(label: String, payload: MessagePayload): ButtonsBuilder {
        addButton(label, payload, ButtonColor.PRIMARY)
        return this
    }

    fun defaultButton(label: String, payload: MessagePayload): ButtonsBuilder {
        addButton(label, payload, ButtonColor.DEFAULT)
        return this
    }

    fun positiveButton(label: String, payload: MessagePayload): ButtonsBuilder {
        addButton(label, payload, ButtonColor.POSITIVE)
        return this
    }

    fun negativeButton(label: String, payload: MessagePayload): ButtonsBuilder {
        addButton(label, payload, ButtonColor.NEGATIVE)
        return this
    }


    fun primaryButton(label: String): ButtonsBuilder {
        addButton(label, MessagePayload(""), ButtonColor.PRIMARY)
        return this
    }

    fun defaultButton(label: String): ButtonsBuilder {
        addButton(label, MessagePayload(""), ButtonColor.DEFAULT)
        return this
    }

    fun positiveButton(label: String): ButtonsBuilder {
        addButton(label, MessagePayload(""), ButtonColor.POSITIVE)
        return this
    }

    fun negativeButton(label: String): ButtonsBuilder {
        addButton(label, MessagePayload(""), ButtonColor.NEGATIVE)
        return this
    }

    private fun addButton(label: String, payload: MessagePayload, color: ButtonColor) {
        buttons.add(Keyboard.Button(
            color = color,
            action = Action(
                type = Action.Type.TEXT,
                label = label,
                payload = payload
            )
        ))
    }

    fun getButtons(): ArrayList<Keyboard.Button> {
        return buttons
    }

    fun setButtons(buttons: ArrayList<Keyboard.Button>) {
        this.buttons = buttons
    }
}