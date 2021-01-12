package com.github.stormbit.sdk.objects.keyboard

import com.github.stormbit.sdk.objects.models.Keyboard
import java.util.*
import com.github.stormbit.sdk.objects.models.Keyboard.*
import com.github.stormbit.sdk.objects.models.MessagePayload

@Suppress("unused", "MemberVisibilityCanBePrivate")
class KeyboardBuilder(builder: KeyboardBuilder.() -> Unit) {
    private val buttons = ArrayList<List<Button>>()

    var isOneTime = false
    var isInline = false
    var authorId: Int? = null

    init {
        builder()
    }

    fun buttons(block: ButtonsBuilder.() -> Unit): KeyboardBuilder {
        val rowBuilder = ButtonsBuilder()
        block(rowBuilder)

        buttons.add(rowBuilder.getButtons())
        return this
    }

    fun locationButton(payload: MessagePayload): KeyboardBuilder {
        buttons.add(listOf(
            Button(
                action = Button.Action(
                    type = Button.Action.Type.TEXT,
                    payload = payload
                )
            ))
        )
        return this
    }

    fun vkPayButton(hash: String, payload: MessagePayload): KeyboardBuilder {
        buttons.add(listOf(
            Button(
                action = Button.Action(
                    type = Button.Action.Type.TEXT,
                    hash = hash,
                    payload = payload
                )
            )
        ))
        return this
    }

    fun vkAppButton(label: String, payload: MessagePayload): KeyboardBuilder {
        buttons.add(listOf(
            Button(
                action = Button.Action(
                    type = Button.Action.Type.TEXT,
                    label = label,
                    payload = payload
                )
            )
        ))
        return this
    }

    fun openLinkButton(label: String, link: String, payload: MessagePayload): KeyboardBuilder {
        buttons.add(listOf(
            Button(
                action = Button.Action(
                    type = Button.Action.Type.TEXT,
                    label = label,
                    link = link,
                    payload = payload
                )
            )
        ))
        return this
    }

    fun build(): Keyboard {
        return Keyboard(isOneTime, isInline, buttons, authorId)
    }
}