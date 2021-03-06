package com.github.stormbit.vksdk.vkapi.methods.audio

import com.github.stormbit.vksdk.clients.Client
import com.github.stormbit.vksdk.objects.attachments.Audio
import com.github.stormbit.vksdk.objects.models.ListResponse
import com.github.stormbit.vksdk.utils.append
import com.github.stormbit.vksdk.vkapi.VkApiRequest
import com.github.stormbit.vksdk.vkapi.methods.MethodsContext
import kotlinx.serialization.json.JsonElement

@Suppress("unused")
class AudioApi(client: Client) : MethodsContext(client) {
    fun get(
        ownerId: Int? = null,
        albumId: Int? = null,
        audioIds: List<Int>? = null,
        offset: Int = 0,
        count: Int = 1
    ): VkApiRequest<ListResponse<Audio>> = Methods.get.call(ListResponse.serializer(Audio.serializer())) {
        append("owner_id", ownerId)
        append("album_id", albumId)
        append("audio_ids", audioIds?.joinToString(", "))
        append("offset", offset)
        append("count", count)
        append("need_user", 0)
    }

    fun getById(
        audios: List<String>
    ): VkApiRequest<JsonElement> = Methods.getById.call(JsonElement.serializer()) {
        append("audios", audios.joinToString(", "))
    }

    companion object {
        object Methods {
            private const val it = "audio."
            const val get = it + "get"
            const val getById = it + "getById"
        }
    }
}