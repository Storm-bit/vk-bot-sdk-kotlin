@file:Suppress("unused")

package com.github.stormbit.sdk.objects.attachments

import com.github.stormbit.sdk.utils.EnumIntSerializer
import com.github.stormbit.sdk.utils.IntEnum
import com.github.stormbit.sdk.vkapi.methods.CommentAttachment
import com.github.stormbit.sdk.vkapi.methods.MessageAttachment
import com.github.stormbit.sdk.vkapi.methods.WallAttachment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Document(
    @SerialName("id") override val id: Int,
    @SerialName("owner_id") override val ownerId: Int,
    @SerialName("date") val date: Int,
    @SerialName("type") val type: Type,
    @SerialName("title") val title: String,
    @SerialName("size") val size: Int, // byte
    @SerialName("ext") val extension: String,
    @SerialName("url") val url: String,
    @SerialName("preview") val preview: Preview? = null,
    @SerialName("tags") val tags: List<String>? = null,
    @SerialName("access_key") override val accessKey: String? = null
) : CommentAttachment, WallAttachment, MessageAttachment {

    override val typeAttachment: AttachmentType get() = AttachmentType.DOC

    @Serializable
    enum class Type(override val value: Int) : IntEnum {
        TEXT(1),
        ARCHIVE(2),
        GIF(3),
        IMAGE(4),
        AUDIO(5),
        VIDEO(6),
        EBOOK(7),
        UNKNOWN(8);

        companion object : EnumIntSerializer<Type>(Type::class, values())
    }

    @Serializable
    data class Preview(
        @SerialName("photo") val photo: Photo? = null,
        @SerialName("graffiti") val graffiti: Graffiti? = null,
        @SerialName("audio_message") val audioMessage: AudioMessage? = null) {

        @Serializable
        data class Photo(
            @SerialName("sizes") val sizes: List<Size>
        ) {

            @Serializable
            data class Size(
                @SerialName("src") val src: String,
                @SerialName("width") val width: Double,
                @SerialName("height") val height: Double,
                @SerialName("type") val type: String)
        }

        @Serializable
        data class Graffiti(
            @SerialName("src") val src: String,
            @SerialName("width") val width: Int,
            @SerialName("height") val height: Int)

        @Serializable
        data class AudioMessage(
            @SerialName("duration") val duration: Int, // sec
            @SerialName("waveform") val waveform: List<Int>,
            @SerialName("link_ogg") val linkOgg: String,
            @SerialName("link_mp3") val linkMp3: String)
    }
}