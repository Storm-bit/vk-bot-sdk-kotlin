package com.github.stormbit.sdk.clients

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.longpoll.Events
import com.google.gson.JsonObject

/**
 * Created by Storm-bit
 *
 * Group client, that contains important methods to work with groups
 */
@Suppress("unused")
class Group(accessToken: String, id: Int) : Client(accessToken, id) {

    /* LongPoll API */
    fun onAudioNew(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.AUDIO_NEW.value, callback)

    fun onBoardPostDelete(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.BOARD_POST_DELETE.value, callback)

    fun onBoardPostEdit(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.BOARD_POST_EDIT.value, callback)

    fun onBoardPostNew(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.BOARD_POST_NEW.value, callback)

    fun onBoardPostRestore(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.BOARD_POST_RESTORE.value, callback)

    fun onGroupChangePhoto(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.GROUP_CHANGE_PHOTO.value, callback)

    fun onGroupChangeSettings(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.GROUP_CHANGE_SETTINGS.value, callback)

    fun onGroupJoin(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.GROUP_JOIN.value, callback)

    fun onGroupLeave(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.GROUP_LEAVE.value, callback)

    fun onGroupOfficersEdit(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.GROUP_OFFICERS_EDIT.value, callback)

    fun onPollVoteNew(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.POLL_VOTE_NEW.value, callback)

    fun onMarketCommentDelete(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.MARKET_COMMENT_DELETE.value, callback)

    fun onMarketCommentEdit(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.MARKET_COMMENT_EDIT.value, callback)

    fun onMarketCommentNew(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.MARKET_COMMENT_NEW.value, callback)

    fun onMarketCommentRestore(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.MARKET_COMMENT_RESTORE.value, callback)

    fun onMessageAllow(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.MESSAGE_ALLOW.value, callback)

    fun onMessageDeny(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.MESSAGE_DENY.value, callback)

    fun onMessageNew(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.MESSAGE_NEW.value, callback)

    fun onMessageReply(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.MESSAGE_REPLY.value, callback)

    fun onPhotoCommentEdit(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.PHOTO_COMMENT_EDIT.value, callback)

    fun onPhotoCommentNew(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.PHOTO_COMMENT_NEW.value, callback)

    fun onPhotoCommentRestore(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.PHOTO_COMMENT_RESTORE.value, callback)

    fun onPhotoNew(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.PHOTO_NEW.value, callback)

    fun onPhotoCommentDelete(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.PHOTO_COMMENT_DELETE.value, callback)

    fun onVideoCommentEdit(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.VIDEO_COMMENT_EDIT.value, callback)

    fun onVideoCommentNew(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.VIDEO_COMMENT_NEW.value, callback)

    fun onVideoCommentRestore(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.VIDEO_COMMENT_RESTORE.value, callback)

    fun onVideoNew(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.VIDEO_NEW.value, callback)

    fun onVideoCommentDelete(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.VIDEO_COMMENT_DELETE.value, callback)

    fun onWallPostNew(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.WALL_POST_NEW.value, callback)

    fun onWallReplyDelete(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.WALL_REPLY_DELETE.value, callback)

    fun onWallReplyEdit(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.WALL_REPLY_EDIT.value, callback)

    fun onWallReplyNew(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.WALL_REPLY_NEW.value, callback)

    fun onWallReplyRestore(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.WALL_REPLY_RESTORE.value, callback)

    fun onWallRepost(callback: Callback<JsonObject?>) = this.longPoll.registerCallback(Events.WALL_REPOST.value, callback)
}