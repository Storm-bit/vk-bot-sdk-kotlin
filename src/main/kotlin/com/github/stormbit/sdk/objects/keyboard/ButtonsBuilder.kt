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

    fun primaryButton(label: String, payload: MessagePayload = MessagePayload(""), type: Action.Type = Action.Type.TEXT): ButtonsBuilder {
        addButton(label, payload, ButtonColor.PRIMARY, type)
        return this
    }

    fun defaultButton(label: String, payload: MessagePayload = MessagePayload(""), type: Action.Type = Action.Type.TEXT): ButtonsBuilder {
        addButton(label, payload, ButtonColor.DEFAULT, type)
        return this
    }

    fun positiveButton(label: String, payload: MessagePayload = MessagePayload(""), type: Action.Type = Action.Type.TEXT): ButtonsBuilder {
        addButton(label, payload, ButtonColor.POSITIVE, type)
        return this
    }

    fun negativeButton(label: String, payload: MessagePayload = MessagePayload(""), type: Action.Type = Action.Type.TEXT): ButtonsBuilder {
        addButton(label, payload, ButtonColor.NEGATIVE, type)
        return this
    }

    private fun addButton(label: String, payload: MessagePayload, color: ButtonColor, type: Action.Type) {
        buttons.add(Keyboard.Button(
            color = color,
            action = Action(
                type = type,
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