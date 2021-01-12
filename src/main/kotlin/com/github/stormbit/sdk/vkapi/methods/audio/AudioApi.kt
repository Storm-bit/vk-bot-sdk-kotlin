package com.github.stormbit.sdk.vkapi.methods.audio

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.attachments.Audio
import com.github.stormbit.sdk.objects.models.ListResponse
import com.github.stormbit.sdk.utils.parametersOf
import com.github.stormbit.sdk.vkapi.methods.MethodsContext
import kotlinx.serialization.json.JsonElement

class AudioApi(private val client: Client) : MethodsContext() {
    fun get(
        ownerId: Int? = null,
        albumId: Int? = null,
        audioIds: List<Int>? = null,
        offset: Int = 0,
        count: Int = 1
    ): ListResponse<Audio>? = Methods.get.callSync(
        client, ListResponse.serializer(Audio.serializer()), parametersOf {
            append("owner_id", ownerId)
            append("album_id", albumId)
            append("audio_ids", audioIds?.joinToString(", "))
            append("offset", offset)
            append("count", count)
            append("need_user", 0)
        }
    )

    fun getById(
        audios: List<String>
    ): JsonElement? = Methods.getById.callSync(
        client, JsonElement.serializer(), parametersOf {
            append("audios", audios.joinToString(", "))
        }
    )

    companion object {
        object Methods {
            private const val it = "audio."
            const val get = it + "get"
            const val getById = it + "getById"
        }
    }
}