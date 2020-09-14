@file:Suppress("unused")

package com.github.stormbit.sdk.utils.vkapi.methods

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
        override val accessKey: String?
) : Media

data class BaseAttachment(
        override val id: Int,
        override val ownerId: Int,
        override val accessKey: String?,
        override val typeAttachment: AttachmentType
) : CommentAttachment, WallAttachment, MessageAttachment