package com.github.stormbit.vksdk.vkapi

import com.github.stormbit.vksdk.utils.pow

/**
 * Перечисление прав пользователя.
 * Список прав получается побитовым сложением (x | y) каждого права.
 * Подробнее в документации VK API: https://vk.com/dev/permissions
 */
@Suppress("unused")
class VkUserPermissions(override var mask: Int = 0) : Bitmask(mask) {
    var notify by bitmask(1)

    // Доступ к друзьям.
    var friend by bitmask(2)

    // Доступ к фотографиям.
    var photos by bitmask(2 pow  2)

    // Доступ к аудиозаписям.
    // При отсутствии доступа к закрытому API аудиозаписей это право позволяет
    // только загрузку аудио.
    var audio by bitmask(2 pow 3)

    // Доступ к видеозаписям.
    var video by bitmask(2 pow 4)

    // Доступ к историям.
    var stories by bitmask(2 pow 6)

    // Доступ к wiki-страницам.
    var pages by bitmask(2 pow 7)

    // Добавление ссылки на приложение в меню слева.
    var addLink by bitmask(2 pow 8)

    // Доступ к статусу пользователя.
    var status by bitmask(2 pow 10)

    // Доступ к заметкам пользователя.
    var notes by bitmask(2 pow 11)

    // Доступ к расширенным методам работы с сообщениями.
    var messages by bitmask(2 pow 12)

    // Доступ к обычным и расширенным методам работы со стеной.
    var wall by bitmask(2 pow 13)

    // Доступ к расширенным методам работы с рекламным API.
    var ads by bitmask(2 pow 15)

    // Доступ к API в любое время. Рекомендуется при работе с этой библиотекой.
    var offline by bitmask(2 pow 16)

    // Доступ к документам.
    var docs by bitmask(2 pow 17)

    // Доступ к группам пользователя.
    var groups by bitmask(2 pow 18)

    // Доступ к оповещениям об ответах пользователю.
    var notifications by bitmask(2 pow 19)

    // Доступ к статистике групп и приложений пользователя, администратором которых он является.
    var stats by bitmask(2 pow 20)

    // Доступ к email пользователя.
    var email by bitmask(2 pow 22)

    // Доступ к товарам.
    var market by bitmask(2 pow 27)

    var allPermissions by bitmask(140488159)
}
