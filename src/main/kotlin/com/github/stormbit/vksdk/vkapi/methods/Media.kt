@file:Suppress("unused")

package com.github.stormbit.vksdk.vkapi.methods

import com.github.stormbit.vksdk.objects.attachments.AttachmentType

interface Media {
    val id: Int
    val ownerId: Int
    val accessKey: String?
}

interface Attachment : Media {
    val typeAttachment: AttachmentType
    override val accessKey: String? get() = null
}

interface MessageAttachment : Attachment
interface WallAttachment : Attachment
interface CommentAttachment : Attachment

data class BaseMedia(
    override val id: Int,
    override val ownerId: Int,
    override val accessKey: String?,
    val type: AttachmentType
) : Media {
    val fullId = if (accessKey != null)
        "${type.value}${ownerId}_${id}_$accessKey"
    else "${type.value}${ownerId}_${id}"
}

data class BaseAttachment(
    override val id: Int,
    override val ownerId: Int,
    override val accessKey: String?,
    override val typeAttachment: AttachmentType
) : CommentAttachment, WallAttachment, MessageAttachment {
    val fullId = "${typeAttachment.value}${ownerId}_$id"
}