package com.github.stormbit.vksdk.objects.keyboard

import com.github.stormbit.vksdk.objects.models.Keyboard
import java.util.*
import com.github.stormbit.vksdk.objects.models.Keyboard.*
import com.github.stormbit.vksdk.objects.models.MessagePayload

@Suppress("unused", "MemberVisibilityCanBePrivate")
class KeyboardBuilder {
    private val buttons = ArrayList<List<Button>>()

    var isOneTime = false
    var isInline = false

    fun row(block: ButtonsBuilder.() -> Unit): KeyboardBuilder {
        val rowBuilder = ButtonsBuilder()
        block(rowBuilder)

        buttons.add(rowBuilder.getButtons())
        return this
    }

    fun locationButton(payload: MessagePayload): KeyboardBuilder {
        buttons.add(listOf(
            Button(
                action = Button.Action(
                    type = Button.Action.Type.LOCATION,
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
                    type = Button.Action.Type.VK_PAY,
                    hash = hash,
                    payload = payload
                )
            )
        ))
        return this
    }

    fun vkAppButton(label: String, appId: Int, payload: MessagePayload): KeyboardBuilder {
        buttons.add(listOf(
            Button(
                action = Button.Action(
                    type = Button.Action.Type.VK_APP,
                    label = label,
                    payload = payload,
                    appId = appId
                )
            )
        ))
        return this
    }

    fun openLinkButton(label: String, link: String, payload: MessagePayload): KeyboardBuilder {
        buttons.add(listOf(
            Button(
                action = Button.Action(
                    type = Button.Action.Type.OPEN_LINK,
                    label = label,
                    link = link,
                    payload = payload
                )
            )
        ))
        return this
    }

    fun build(): Keyboard {
        return Keyboard(isOneTime, isInline, buttons)
    }
}