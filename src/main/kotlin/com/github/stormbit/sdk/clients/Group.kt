package com.github.stormbit.sdk.clients

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.longpoll.Events
import org.json.JSONObject

/**
 * Created by Storm-bit
 *
 * Group client, that contains important methods to work with groups
 */
@Suppress("unused")
class Group(accessToken: String, id: Int) : Client(accessToken, id) {

    /* LongPoll API */
    fun onAudioNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.AUDIO_NEW.value, callback)

    fun onBoardPostDelete(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.BOARD_POST_DELETE.value, callback)

    fun onBoardPostEdit(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.BOARD_POST_EDIT.value, callback)

    fun onBoardPostNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.BOARD_POST_NEW.value, callback)

    fun onBoardPostRestore(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.BOARD_POST_RESTORE.value, callback)

    fun onGroupChangePhoto(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.GROUP_CHANGE_PHOTO.value, callback)

    fun onGroupChangeSettings(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.GROUP_CHANGE_SETTINGS.value, callback)

    fun onGroupJoin(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.GROUP_JOIN.value, callback)

    fun onGroupLeave(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.GROUP_LEAVE.value, callback)

    fun onGroupOfficersEdit(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.GROUP_OFFICERS_EDIT.value, callback)

    fun onPollVoteNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.POLL_VOTE_NEW.value, callback)

    fun onMarketCommentDelete(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.MARKET_COMMENT_DELETE.value, callback)

    fun onMarketCommentEdit(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.MARKET_COMMENT_EDIT.value, callback)

    fun onMarketCommentNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.MARKET_COMMENT_NEW.value, callback)

    fun onMarketCommentRestore(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.MARKET_COMMENT_RESTORE.value, callback)

    fun onMessageAllow(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.MESSAGE_ALLOW.value, callback)

    fun onMessageDeny(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.MESSAGE_DENY.value, callback)

    fun onMessageNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.MESSAGE_NEW.value, callback)

    fun onMessageReply(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.MESSAGE_REPLY.value, callback)

    fun onPhotoCommentEdit(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.PHOTO_COMMENT_EDIT.value, callback)

    fun onPhotoCommentNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.PHOTO_COMMENT_NEW.value, callback)

    fun onPhotoCommentRestore(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.PHOTO_COMMENT_RESTORE.value, callback)

    fun onPhotoNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.PHOTO_NEW.value, callback)

    fun onPhotoCommentDelete(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.PHOTO_COMMENT_DELETE.value, callback)

    fun onVideoCommentEdit(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.VIDEO_COMMENT_EDIT.value, callback)

    fun onVideoCommentNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.VIDEO_COMMENT_NEW.value, callback)

    fun onVideoCommentRestore(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.VIDEO_COMMENT_RESTORE.value, callback)

    fun onVideoNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.VIDEO_NEW.value, callback)

    fun onVideoCommentDelete(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.VIDEO_COMMENT_DELETE.value, callback)

    fun onWallPostNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.WALL_POST_NEW.value, callback)

    fun onWallReplyDelete(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.WALL_REPLY_DELETE.value, callback)

    fun onWallReplyEdit(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.WALL_REPLY_EDIT.value, callback)

    fun onWallReplyNew(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.WALL_REPLY_NEW.value, callback)

    fun onWallReplyRestore(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.WALL_REPLY_RESTORE.value, callback)

    fun onWallRepost(callback: Callback<JSONObject?>) = this.longPoll.registerCallback(Events.WALL_REPOST.value, callback)
}