package com.github.stormbit.sdk.utils.vkapi.methods.wall

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.call
import com.github.stormbit.sdk.utils.attachmentString
import com.github.stormbit.sdk.utils.put
import com.github.stormbit.sdk.utils.vkapi.methods.*
import com.google.gson.JsonObject

@Suppress("unused")
class WallApiAsync(private val client: Client) {
    fun closeComments(
            ownerId: Int,
            postId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.closeComments.call(client, callback, JsonObject()
            .put("owner_id", ownerId)
            .put("post_id", postId)
    )

    fun createComment(
            postId: Int,
            ownerId: Int?,
            fromGroup: Int = 0,
            message: String?,
            replyToCommentId: Int? = null,
            attachments: List<CommentAttachment>? = null,
            stickerId: Int? = null,
            guid: String? = null,
            callback: Callback<JsonObject?>
    ) = Methods.createComment.call(client, callback, JsonObject()
            .put("post_id", postId)
            .put("owner_id", ownerId)
            .put("from_group", fromGroup)
            .put("message", message)
            .put("reply_to_comment", replyToCommentId)
            .put("attachments", attachments?.joinToString(",", transform = CommentAttachment::attachmentString))
            .put("sticker_id", stickerId)
            .put("guid", guid)
    )

    fun createComment(
            postId: Int,
            ownerId: Int?,
            message: String?,
            replyToCommentId: Int?,
            attachments: List<MessageAttachment>?,
            stickerId: Int?,
            guid: String?,
            callback: Callback<JsonObject?>
    ) = Methods.createComment.call(client, callback, JsonObject()
            .put("post_id", postId)
            .put("owner_id", ownerId)
            .put("message", message)
            .put("reply_to_comment", replyToCommentId)
            .put("attachments", attachments?.joinToString(",", transform = MessageAttachment::attachmentString))
            .put("sticker_id", stickerId)
            .put("guid", guid)
    )

    fun delete(
            postId: Int,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.delete.call(client, callback, JsonObject()
            .put("post_id", postId)
            .put("owner_id", ownerId)
    )

    fun deleteComment(
            commentId: Int,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.deleteComment.call(client, callback, JsonObject()
            .put("comment_id", commentId)
            .put("owner_id", ownerId)
    )

    fun edit(
            postId: Int,
            ownerId: Int?,
            friendsOnly: Boolean?,
            message: String?,
            attachments: List<MessageAttachment>?,
            attachmentLink: String?,
            servicesForExport: List<String>?,
            signed: Boolean?,
            publishDate: GMTDate?,
            latitude: Double?,
            longitude: Double?,
            placeId: Int?,
            markAsAds: Boolean?,
            closeComments: Boolean?,
            posterBackgroundId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.edit.call(client, callback, JsonObject()
            .put("post_id", postId)
            .put("owner_id", ownerId)
            .put("friends_only", friendsOnly?.asInt())
            .put("message", message)
            .put("attachments", attachments?.joinToString(",", transform = MessageAttachment::attachmentString).append(attachmentLink))
            .put("services", servicesForExport?.joinToString(","))
            .put("signed", signed?.asInt())
            .put("publish_date", publishDate?.timestamp)
            .put("lat", latitude)
            .put("long", longitude)
            .put("place_id", placeId)
            .put("mark_as_ads", markAsAds?.asInt())
            .put("close_comments", closeComments?.asInt())
            .put("poster_bkg_id", posterBackgroundId)
    )

    fun editAdsStealth(
            postId: Int,
            ownerId: Int?,
            message: String?,
            attachments: List<MessageAttachment>?,
            attachmentLink: String?,
            signed: Boolean,
            latitude: Double?,
            longitude: Double?,
            placeId: Int?,
            linkButton: LinkButtonType?,
            linkTitle: String?,
            linkImage: String?,
            callback: Callback<JsonObject?>
    ) = Methods.editAdsStealth.call(client, callback, JsonObject()
            .put("post_id", postId)
            .put("owner_id", ownerId)
            .put("message", message)
            .put("attachments", attachments?.joinToString(",", transform = MessageAttachment::attachmentString).append(attachmentLink))
            .put("signed", signed.asInt())
            .put("lat", latitude)
            .put("long", longitude)
            .put("place_id", placeId)
            .put("link_button", linkButton?.value)
            .put("link_title", linkTitle)
            .put("link_image", linkImage)
    )

    fun editComment(
            commentId: Int,
            ownerId: Int?,
            message: String?,
            attachments: List<CommentAttachment>?,
            callback: Callback<JsonObject?>
    ) = Methods.editComment.call(client, callback, JsonObject()
            .put("comment_id", commentId)
            .put("owner_id", ownerId)
            .put("message", message)
            .put("attachments", attachments?.joinToString(",", transform = CommentAttachment::attachmentString))
    )

    fun get(
            ownerId: Int?,
            offset: Int,
            count: Int,
            filter: WallPostFilter,
            extended: Boolean,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = Methods.get.call(client, callback, JsonObject()
            .put("owner_id", ownerId)
            .put("offset", offset)
            .put("count", count)
            .put("filter", filter.value)
            .put("extended", extended.asInt())
            .put("fields", fields.joinToString(",") { it.value })
    )

    fun getById(
            posts: Map<Int, List<Int>>,
            copyHistoryDepth: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getById.call(client, callback, JsonObject()
            .put("posts", posts.toList().joinToString(",") { it.posts })
            .put("copy_history_depth", copyHistoryDepth)
    )

    fun getByIdExtended(
            posts: Map<Int, List<Int>>,
            copyHistoryDepth: Int,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = Methods.getById.call(client, callback, JsonObject()
            .put("posts", posts.toList().joinToString(",") { it.posts })
            .put("copy_history_depth", copyHistoryDepth)
            .put("extended", 1)
            .put("fields", fields.joinToString(",") { it.value })
    )

    fun getComment(
            commentId: Int,
            ownerId: Int?,
            extended: Boolean,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = Methods.getComment.call(client, callback, JsonObject()
            .put("comment_id", commentId)
            .put("owner_id", ownerId)
            .put("extended", extended.asInt())
            .put("fields", fields.joinToString(",") { it.value })
    )

    fun getComments(
            postId: Int,
            ownerId: Int?,
            commentId: Int?,
            needLikes: Boolean,
            startCommentId: Int?,
            threadItemsCount: Int,
            offset: Int,
            count: Int,
            sort: CommentsSort,
            previewLength: Int,
            extended: Boolean,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = Methods.getComments.call(client, callback, JsonObject()
            .put("post_id", postId)
            .put("owner_id", ownerId)
            .put("comment_id", commentId)
            .put("need_likes", needLikes.asInt())
            .put("start_comment_id", startCommentId)
            .put("thread_items_count", threadItemsCount)
            .put("offset", offset)
            .put("count", count)
            .put("sort", sort.value)
            .put("preview_length", previewLength)
            .put("extended", extended.asInt())
            .put("fields", fields.joinToString(",") { it.value })
    )

    fun getReposts(
            postId: Int,
            ownerId: Int?,
            offset: Int,
            count: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getReposts.call(client, callback, JsonObject()
            .put("post_id", postId)
            .put("owner_id", ownerId)
            .put("offset", offset)
            .put("count", count)
    )

    fun openComments(
            ownerId: Int,
            postId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.openComments.call(client, callback, JsonObject()
            .put("owner_id", ownerId)
            .put("post_id", postId)
    )

    fun pin(
            postId: Int,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.pin.call(client, callback, JsonObject()
            .put("post_id", postId)
            .put("owner_id", ownerId)
    )

    fun post(
            ownerId: Int?,
            friendsOnly: Boolean,
            fromGroup: Boolean,
            message: String?,
            attachments: List<MessageAttachment>?,
            attachmentLink: String?,
            servicesForExport: List<String>?,
            signed: Boolean,
            publishDate: GMTDate?,
            latitude: Double?,
            longitude: Double?,
            placeId: Int?,
            postId: Int?,
            guid: String?,
            markAsAds: Boolean,
            closeComments: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.post.call(client, callback, JsonObject()
            .put("owner_id", ownerId)
            .put("friends_only", friendsOnly.asInt())
            .put("from_group", fromGroup.asInt())
            .put("message", message)
            .put("attachments", attachments?.joinToString(",", transform = MessageAttachment::attachmentString).append(attachmentLink))
            .put("services", servicesForExport?.joinToString(","))
            .put("signed", signed.asInt())
            .put("publish_date", publishDate?.timestamp)
            .put("lat", latitude)
            .put("long", longitude)
            .put("place_id", placeId)
            .put("post_id", postId)
            .put("guid", guid)
            .put("mark_as_ads", markAsAds.asInt())
            .put("close_comments", closeComments.asInt())
    )

    fun postAdsStealth(
            ownerId: Int,
            message: String?,
            attachments: List<MessageAttachment>?,
            attachmentLink: String?,
            signed: Boolean,
            latitude: Double?,
            longitude: Double?,
            placeId: Int?,
            guid: String?,
            linkButton: LinkButtonType?,
            linkTitle: String?,
            linkImage: String?,
            callback: Callback<JsonObject?>
    ) = Methods.postAdsStealth.call(client, callback, JsonObject()
            .put("owner_id", ownerId)
            .put("message", message)
            .put("attachments", attachments?.joinToString(",", transform = MessageAttachment::attachmentString).append(attachmentLink))
            .put("signed", signed.asInt())
            .put("lat", latitude)
            .put("long", longitude)
            .put("place_id", placeId)
            .put("guid", guid)
            .put("link_button", linkButton?.value)
            .put("link_title", linkTitle)
            .put("link_image", linkImage)
    )

    fun reportComment(
            ownerId: Int,
            commentId: Int,
            reason: PostReportComplaintType,
            callback: Callback<JsonObject?>
    ) = Methods.reportComment.call(client, callback, JsonObject()
            .put("owner_id", ownerId)
            .put("comment_id", commentId)
            .put("reason", reason.value)
    )

    fun reportPost(
            ownerId: Int,
            postId: Int,
            reason: PostReportComplaintType,
            callback: Callback<JsonObject?>
    ) = Methods.reportPost.call(client, callback, JsonObject()
            .put("owner_id", ownerId)
            .put("post_id", postId)
            .put("reason", reason.value)
    )

    fun repost(
            repostObject: MessageAttachment,
            message: String?,
            groupId: Int?,
            markAsAds: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.repost.call(client, callback, JsonObject()
            .put("object", repostObject.attachmentString)
            .put("message", message)
            .put("group_id", groupId)
            .put("mark_as_ads", markAsAds.asInt())
    )

    fun restore(
            postId: Int,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.restore.call(client, callback, JsonObject()
            .put("post_id", postId)
            .put("owner_id", ownerId)
    )

    fun restoreComment(
            commentId: Int,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.restoreComment.call(client, callback, JsonObject()
            .put("comment_id", commentId)
            .put("owner_id", ownerId)
    )

    fun search(
            query: String,
            ownerId: Int?,
            ownersOnly: Boolean,
            count: Int,
            offset: Int,
            extended: Boolean,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = Methods.search.call(client, callback, JsonObject()
            .put("query", query)
            .put("owner_id", ownerId)
            .put("owners_only", ownersOnly.asInt())
            .put("count", count)
            .put("offset", offset)
            .put("extended", extended.asInt())
            .put("fields", fields.joinToString(",") { it.value })
    )

    fun unpin(
            postId: Int,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.unpin.call(client, callback, JsonObject()
            .put("post_id", postId)
            .put("owner_id", ownerId)
    )

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