package com.github.stormbit.sdk.clients

import com.github.stormbit.sdk.callbacks.Callback
import org.json.JSONObject

/**
 * Created by Storm-bit
 *
 * Group client, that contains important methods to work with groups
 */
@Suppress("unused")
class Group(accessToken: String, id: Int) : Client(accessToken, id) {

    /* LongPoll API */
    fun onAudioNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("audio_new", callback)

    fun onBoardPostDelete(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("board_post_delete", callback)

    fun onBoardPostEdit(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("board_post_edit", callback)

    fun onBoardPostNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("board_post_new", callback)

    fun onBoardPostRestore(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("board_post_restore", callback)

    fun onGroupChangePhoto(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("group_change_photo", callback)

    fun onGroupChangeSettings(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("group_change_settings", callback)

    fun onGroupJoin(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("group_join", callback)

    fun onGroupLeave(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("group_leave", callback)

    fun onGroupOfficersEdit(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("group_officers_edit", callback)

    fun onPollVoteNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("poll_vote_new", callback)

    fun onMarketCommentDelete(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("market_comment_delete", callback)

    fun onMarketCommentEdit(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("market_comment_edit", callback)

    fun onMarketCommentNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("market_comment_new", callback)

    fun onMarketCommentRestore(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("market_comment_restore", callback)

    fun onMessageAllow(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("message_allow", callback)

    fun onMessageDeny(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("message_deny", callback)

    fun onMessageNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("message_new", callback)

    fun onMessageReply(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("message_reply", callback)

    fun onPhotoCommentEdit(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("photo_comment_edit", callback)

    fun onPhotoCommentNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("photo_comment_new", callback)

    fun onPhotoCommentRestore(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("photo_comment_restore", callback)

    fun onPhotoNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("photo_new", callback)

    fun onPhotoCommentDelete(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("photo_comment_delete", callback)

    fun onVideoCommentEdit(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("video_comment_edit", callback)

    fun onVideoCommentNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("video_comment_new", callback)

    fun onVideoCommentRestore(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("video_comment_restore", callback)

    fun onVideoNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("video_new", callback)

    fun onVideoCommentDelete(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("video_comment_delete", callback)

    fun onWallPostNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("wall_post_new", callback)

    fun onWallReplyDelete(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("wall_reply_delete", callback)

    fun onWallReplyEdit(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("wall_reply_edit", callback)

    fun onWallReplyNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("wall_reply_new", callback)

    fun onWallReplyRestore(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("wall_reply_restore", callback)

    fun onWallRepost(callback: Callback<JSONObject?>) = this.longPoll.registerCallback("wall_repost", callback)
}