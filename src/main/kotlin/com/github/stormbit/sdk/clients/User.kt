package com.github.stormbit.sdk.clients

import com.github.stormbit.sdk.utils.vkapi.Auth

/**
 * User client, that contains important methods to work with users
 *
 * Not need now to put methods there: use API.call
 */
class User : Client {
    /**
     * Default constructor
     *
     * @param login    Login of your VK bot
     * @param password Password of your VK bot
     */
    constructor(login: String, password: String) : super(login, password)

    /**
     * Second constructor
     *
     * @param login    Login of your VK bot
     * @param password Password of your VK bot
     * @param listener Listener for two factor
     */
    constructor(login: String, password: String, listener: Auth.Listener) : super(login, password, listener)
}