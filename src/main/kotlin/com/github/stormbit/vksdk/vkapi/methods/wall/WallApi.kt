package com.github.stormbit.vksdk.vkapi.methods.wall

import com.github.stormbit.vksdk.clients.Client
import com.github.stormbit.vksdk.utils.append
import com.github.stormbit.vksdk.utils.attachmentString
import com.github.stormbit.vksdk.utils.toInt
import com.github.stormbit.vksdk.utils.unixtime
import com.github.stormbit.vksdk.vkapi.VkApiRequest
import com.github.stormbit.vksdk.vkapi.methods.*
import io.ktor.util.date.*
import kotlinx.serialization.json.JsonObject

@Suppress("unused")
class WallApi(client: Client) : MethodsContext(client) {
    fun closeComments(
        ownerId: Int,
        postId: Int
    ): VkApiRequest<JsonObject> = Methods.closeComments.call(JsonObject.serializer()) {
        append("owner_id", ownerId)
        append("post_id", postId)
    }


    fun createComment(
        postId: Int,
        ownerId: Int? = null,
        fromGroup: Int = 0,
        message: String? = null,
        replyToCommentId: Int? = null,
        attachments: List<CommentAttachment>? = null,
        stickerId: Int? = null,
        guid: String? = null
    ): VkApiRequest<JsonObject> = Methods.createComment.call(JsonObject.serializer()) {
        append("post_id", postId)
        append("owner_id", ownerId)
        append("from_group", fromGroup)
        append("message", message)
        append("reply_to_comment", replyToCommentId)
        append("items", attachments?.joinToString(",", transform = CommentAttachment::attachmentString))
        append("sticker_id", stickerId)
        append("guid", guid)
    }


    fun createComment(
        postId: Int,
        ownerId: Int? = null,
        message: String? = null,
        replyToCommentId: Int? = null,
        attachments: List<MessageAttachment>? = null,
        stickerId: Int? = null,
        guid: String? = null
    ): VkApiRequest<JsonObject> = Methods.createComment.call(JsonObject.serializer()) {
        append("post_id", postId)
        append("owner_id", ownerId)
        append("message", message)
        append("reply_to_comment", replyToCommentId)
        append("items", attachments?.joinToString(",", transform = MessageAttachment::attachmentString))
        append("sticker_id", stickerId)
        append("guid", guid)
    }


    fun delete(
        postId: Int,
        ownerId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.delete.call(JsonObject.serializer()) {
        append("post_id", postId)
        append("owner_id", ownerId)
    }


    fun deleteComment(
        commentId: Int,
        ownerId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.deleteComment.call(JsonObject.serializer()) {
        append("comment_id", commentId)
        append("owner_id", ownerId)
    }


    fun edit(
        postId: Int,
        ownerId: Int? = null,
        friendsOnly: Boolean? = null,
        message: String? = null,
        attachments: List<MessageAttachment>? = null,
        attachmentLink: String? = null,
        servicesForExport: List<String>? = null,
        signed: Boolean? = null,
        publishDate: GMTDate? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        placeId: Int? = null,
        markAsAds: Boolean? = null,
        closeComments: Boolean? = null,
        posterBackgroundId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.edit.call(JsonObject.serializer()) {
        append("post_id", postId)
        append("owner_id", ownerId)
        append("friends_only", friendsOnly?.toInt())
        append("message", message)
        append(
            "items",
            attachments?.joinToString(",", transform = MessageAttachment::attachmentString)
                .append(attachmentLink)
        )
        append("services", servicesForExport?.joinToString(","))
        append("signed", signed?.toInt())
        append("publish_date", publishDate?.unixtime)
        append("lat", latitude)
        append("long", longitude)
        append("place_id", placeId)
        append("mark_as_ads", markAsAds?.toInt())
        append("close_comments", closeComments?.toInt())
        append("poster_bkg_id", posterBackgroundId)
    }


    fun editAdsStealth(
        postId: Int,
        ownerId: Int? = null,
        message: String? = null,
        attachments: List<MessageAttachment>? = null,
        attachmentLink: String? = null,
        signed: Boolean,
        latitude: Double? = null,
        longitude: Double? = null,
        placeId: Int? = null,
        linkButton: LinkButtonType? = null,
        linkTitle: String? = null,
        linkImage: String? = null
    ): VkApiRequest<JsonObject> = Methods.editAdsStealth.call(JsonObject.serializer()) {
        append("post_id", postId)
        append("owner_id", ownerId)
        append("message", message)
        append(
            "items",
            attachments?.joinToString(",", transform = MessageAttachment::attachmentString).append(attachmentLink)
        )
        append("signed", signed.toInt())
        append("lat", latitude)
        append("long", longitude)
        append("place_id", placeId)
        append("link_button", linkButton?.value)
        append("link_title", linkTitle)
        append("link_image", linkImage)
    }


    fun editComment(
        commentId: Int,
        ownerId: Int? = null,
        message: String? = null,
        attachments: List<CommentAttachment>? = null
    ): VkApiRequest<JsonObject> = Methods.editComment.call(JsonObject.serializer()) {
        append("comment_id", commentId)
        append("owner_id", ownerId)
        append("message", message)
        append("items", attachments?.joinToString(",", transform = CommentAttachment::attachmentString))
    }


    fun get(
        ownerId: Int? = null,
        offset: Int,
        count: Int,
        filter: WallPostFilter,
        extended: Boolean,
        fields: List<ObjectField>
    ): VkApiRequest<JsonObject> = Methods.get.call(JsonObject.serializer()) {
        append("owner_id", ownerId)
        append("offset", offset)
        append("count", count)
        append("filter", filter.value)
        append("extended", extended.toInt())
        append("fields", fields.joinToString(",") { it.value })
    }


    fun getById(
        posts: Map<Int, List<Int>>,
        copyHistoryDepth: Int
    ): VkApiRequest<JsonObject> = Methods.getById.call(JsonObject.serializer()) {
        append("posts", posts.toList().joinToString(",") { it.posts })
        append("copy_history_depth", copyHistoryDepth)
    }


    fun getByIdExtended(
        posts: Map<Int, List<Int>>,
        copyHistoryDepth: Int,
        fields: List<ObjectField>
    ): VkApiRequest<JsonObject> = Methods.getById.call(JsonObject.serializer()) {
        append("posts", posts.toList().joinToString(",") { it.posts })
        append("copy_history_depth", copyHistoryDepth)
        append("extended", 1)
        append("fields", fields.joinToString(",") { it.value })
    }


    fun getComment(
        commentId: Int,
        ownerId: Int? = null,
        extended: Boolean,
        fields: List<ObjectField>
    ): VkApiRequest<JsonObject> = Methods.getComment.call(JsonObject.serializer()) {
        append("comment_id", commentId)
        append("owner_id", ownerId)
        append("extended", extended.toInt())
        append("fields", fields.joinToString(",") { it.value })
    }


    fun getComments(
        postId: Int,
        ownerId: Int? = null,
        commentId: Int? = null,
        needLikes: Boolean,
        startCommentId: Int? = null,
        threadItemsCount: Int,
        offset: Int,
        count: Int,
        sort: CommentsSort,
        previewLength: Int,
        extended: Boolean,
        fields: List<ObjectField>
    ): VkApiRequest<JsonObject> = Methods.getComments.call(JsonObject.serializer()) {
        append("post_id", postId)
        append("owner_id", ownerId)
        append("comment_id", commentId)
        append("need_likes", needLikes.toInt())
        append("start_comment_id", startCommentId)
        append("thread_items_count", threadItemsCount)
        append("offset", offset)
        append("count", count)
        append("sort", sort.value)
        append("preview_length", previewLength)
        append("extended", extended.toInt())
        append("fields", fields.joinToString(",") { it.value })
    }


    fun getReposts(
        postId: Int,
        ownerId: Int? = null,
        offset: Int,
        count: Int
    ): VkApiRequest<JsonObject> = Methods.getReposts.call(JsonObject.serializer()) {
        append("post_id", postId)
        append("owner_id", ownerId)
        append("offset", offset)
        append("count", count)
    }


    fun openComments(
        ownerId: Int,
        postId: Int
    ): VkApiRequest<JsonObject> = Methods.openComments.call(JsonObject.serializer()) {
        append("owner_id", ownerId)
        append("post_id", postId)
    }


    fun pin(
        postId: Int,
        ownerId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.pin.call(JsonObject.serializer()) {
        append("post_id", postId)
        append("owner_id", ownerId)
    }


    fun post(
        ownerId: Int? = null,
        friendsOnly: Boolean,
        fromGroup: Boolean,
        message: String? = null,
        attachments: List<MessageAttachment>? = null,
        attachmentLink: String? = null,
        servicesForExport: List<String>? = null,
        signed: Boolean,
        publishDate: GMTDate? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        placeId: Int? = null,
        postId: Int? = null,
        guid: String? = null,
        markAsAds: Boolean,
        closeComments: Boolean
    ): VkApiRequest<JsonObject> = Methods.post.call(JsonObject.serializer()) {
        append("owner_id", ownerId)
        append("friends_only", friendsOnly.toInt())
        append("from_group", fromGroup.toInt())
        append("message", message)
        append(
            "items",
            attachments?.joinToString(",", transform = MessageAttachment::attachmentString).append(attachmentLink)
        )
        append("services", servicesForExport?.joinToString(","))
        append("signed", signed.toInt())
        append("publish_date", publishDate?.unixtime)
        append("lat", latitude)
        append("long", longitude)
        append("place_id", placeId)
        append("post_id", postId)
        append("guid", guid)
        append("mark_as_ads", markAsAds.toInt())
        append("close_comments", closeComments.toInt())
    }


    fun postAdsStealth(
        ownerId: Int,
        message: String? = null,
        attachments: List<MessageAttachment>? = null,
        attachmentLink: String? = null,
        signed: Boolean,
        latitude: Double? = null,
        longitude: Double? = null,
        placeId: Int? = null,
        guid: String? = null,
        linkButton: LinkButtonType? = null,
        linkTitle: String? = null,
        linkImage: String? = null
    ): VkApiRequest<JsonObject> = Methods.postAdsStealth.call(JsonObject.serializer()) {
        append("owner_id", ownerId)
        append("message", message)
        append(
            "items",
            attachments?.joinToString(",", transform = MessageAttachment::attachmentString).append(attachmentLink)
        )
        append("signed", signed.toInt())
        append("lat", latitude)
        append("long", longitude)
        append("place_id", placeId)
        append("guid", guid)
        append("link_button", linkButton?.value)
        append("link_title", linkTitle)
        append("link_image", linkImage)
    }


    fun reportComment(
        ownerId: Int,
        commentId: Int,
        reason: PostReportComplaintType
    ): VkApiRequest<JsonObject> = Methods.reportComment.call(JsonObject.serializer()) {
        append("owner_id", ownerId)
        append("comment_id", commentId)
        append("reason", reason.value)
    }


    fun reportPost(
        ownerId: Int,
        postId: Int,
        reason: PostReportComplaintType
    ): VkApiRequest<JsonObject> = Methods.reportPost.call(JsonObject.serializer()) {
        append("owner_id", ownerId)
        append("post_id", postId)
        append("reason", reason.value)
    }


    fun repost(
        repostObject: MessageAttachment,
        message: String? = null,
        groupId: Int? = null,
        markAsAds: Boolean
    ): VkApiRequest<JsonObject> = Methods.repost.call(JsonObject.serializer()) {
        append("object", repostObject.attachmentString)
        append("message", message)
        append("group_id", groupId)
        append("mark_as_ads", markAsAds.toInt())
    }


    fun restore(
        postId: Int,
        ownerId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.restore.call(JsonObject.serializer()) {
        append("post_id", postId)
        append("owner_id", ownerId)
    }


    fun restoreComment(
        commentId: Int,
        ownerId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.restoreComment.call(JsonObject.serializer()) {
        append("comment_id", commentId)
        append("owner_id", ownerId)
    }


    fun search(
        query: String,
        ownerId: Int? = null,
        ownersOnly: Boolean,
        count: Int,
        offset: Int,
        extended: Boolean,
        fields: List<ObjectField>
    ): VkApiRequest<JsonObject> = Methods.search.call(JsonObject.serializer()) {
        append("query", query)
        append("owner_id", ownerId)
        append("owners_only", ownersOnly.toInt())
        append("count", count)
        append("offset", offset)
        append("extended", extended.toInt())
        append("fields", fields.joinToString(",") { it.value })
    }


    fun unpin(
        postId: Int,
        ownerId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.unpin.call(JsonObject.serializer()) {
        append("post_id", postId)
        append("owner_id", ownerId)
    }


    companion object {

        private fun String?.append(other: String?): String? =
            when {
                this == null -> other
                    other == null -> this
                    else -> "$this,$other"
                }

        private inline val Pair<Int, List<Int>>.posts: String
            get() = second.joinToString(",") { "${first}_$it" }

    }

    private object Methods {
        private const val it = "wall."
        const val closeComments = it + "closeComments"
        const val createComment = it + "createComment"
        const val delete = it + "delete"
        const val deleteComment = it + "deleteComment"
        const val edit = it + "edit"
        const val editAdsStealth = it + "editAdsStealth"
        const val editComment = it + "editComment"
        const val get = it + "get"
        const val getById = it + "getById"
        const val getComment = it + "getComment"
        const val getComments = it + "getComments"
        const val getReposts = it + "getReposts"
        const val openComments = it + "openComments"
        const val pin = it + "pin"
        const val post = it + "post"
        const val postAdsStealth = it + "postAdsStealth"
        const val reportComment = it + "reportComment"
        const val reportPost = it + "reportPost"
        const val repost = it + "repost"
        const val restore = it + "restore"
        const val restoreComment = it + "restoreComment"
        const val search = it + "search"
        const val unpin = it + "unpin"
    }
}