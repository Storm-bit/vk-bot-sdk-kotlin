package com.github.stormbit.sdk.objects

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.vkapi.methods.docs.DocTypes
import com.github.stormbit.sdk.objects.models.PrivacySettings
import com.github.stormbit.sdk.utils.*
import com.github.stormbit.sdk.vkapi.methods.Attachment
import com.github.stormbit.sdk.objects.attachments.AttachmentType
import com.github.stormbit.sdk.vkapi.methods.BaseMedia
import com.github.stormbit.sdk.vkapi.methods.messages.ChatChangePhotoResponse
import com.github.stormbit.sdk.vkapi.methods.photos.OwnerCoverPhotoResponse
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonObject
import net.dongliu.requests.Requests
import net.dongliu.requests.body.Part
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

@Suppress("MemberVisibilityCanBePrivate", "unused")
class Upload(private val client: Client) {
    private val log = LoggerFactory.getLogger(Upload::class.java)

    /* Async methods */

    /**
     * @param photo String URL, link to vk photo or path to file
     * @param peerId peer id
     * @param callback callback
     */
    fun uploadPhotoAsync(photo: String, peerId: Int, callback: Callback<BaseMedia?>) {
        var type: String? = null
        val photoFile = File(photo)

        if (photoFile.exists()) {
            type = "fromFile"
        }

        var photoUrl: URL? = null

        if (type == null) {
            try {
                photoUrl = URL(photo)
                type = "fromUrl"
            } catch (ignored: MalformedURLException) {
                log.error("Error when trying add photo to message: file not found, or url is bad. Your param: {}", photo)
                callback.onResult(null)
                return
            }
        }

        val photoBytes: ByteArray?

        when (type) {
            "fromFile" -> {
                photoBytes = try {
                    Files.readAllBytes(Paths.get(photoFile.toURI()))
                } catch (ignored: IOException) {
                    log.error("Error when reading file {}", photoFile.absolutePath)
                    callback.onResult(null)
                    return
                }
            }

            "fromUrl" -> {
                try {
                    photoBytes = photoUrl!!.toByteArray()
                } catch (e: IOException) {
                    log.error("Error {} occurred when reading URL {}", e.toString(), photo)
                    callback.onResult(null)
                    return
                }
            }

            else -> {
                log.error("Bad 'photo' string: path to file, URL or already uploaded 'photo()_()' was expected.")
                callback.onResult(null)
                return
            }
        }

        uploadPhotoAsync(photoBytes, peerId, callback)
    }

    /**
     * Async uploading photos
     * @param photoBytes Photo bytes
     * @param peerId peer id
     * @param callback callback
     */
    fun uploadPhotoAsync(photoBytes: ByteArray?, peerId: Int, callback: Callback<BaseMedia?>) {
        if (photoBytes != null) {

            val response = client.photos.getMessagesUploadServer(peerId)!!

            if (response.toString().equals(null, ignoreCase = true)) {
                log.error("Can't get messages upload server, aborting. Photo wont be attached to message.")
                callback.onResult(null)
                return
            }

            val mimeType: String = try {
                Utils.getMimeType(photoBytes)
            } catch (e: IOException) {
                log.error(e.message)
                callback.onResult(null)
                return
            }

            val uploadFileResponse = Requests
                .post(response.uploadUrl)
                .multiPartBody(Part.file("photo", "image.$mimeType", photoBytes))
                .send().readToText()

            if (uploadFileResponse.length < 2 || uploadFileResponse.contains("error") || !uploadFileResponse.contains("photo")) {
                log.error("Photo wan't uploaded: {}", uploadFileResponse)
                callback.onResult(null)
                return
            }

            val getPhotoStringResponse: JsonObject = try {
                uploadFileResponse.toJsonObject()
            } catch (ignored: SerializationException) {
                log.error("Bad response of uploading photo: {}", uploadFileResponse)
                callback.onResult(null)
                return
            }

            if (!getPhotoStringResponse.containsKey("photo") || !getPhotoStringResponse.containsKey("server") || !getPhotoStringResponse.containsKey("hash")) {
                log.error("Bad response of uploading photo, no 'photo', 'server' of 'hash' param: {}", getPhotoStringResponse.toString())
                callback.onResult(null)
                return
            }

            val photoParam = getPhotoStringResponse.getString("photo")!!
            val serverParam = getPhotoStringResponse.getInt("server")!!
            val hashParam = getPhotoStringResponse.getString("hash")!!

            val response1 = client.photos.saveMessagesPhoto(serverParam, photoParam, hashParam)

            if (response1.toString().equals(null, ignoreCase = true)) {
                log.error("Error when saving uploaded photo: response is 'false', see execution errors.")
                callback.onResult(null)
                return
            }

            val saveMessagesPhotoResponse = response1!![0]

            val attach = BaseMedia(
                saveMessagesPhotoResponse.id,
                saveMessagesPhotoResponse.ownerId,
                saveMessagesPhotoResponse.accessKey,
                AttachmentType.PHOTO
            )

            callback.onResult(attach)
        }
    }

    /**
     * @param video String URL, link to vk photo or path to file
     * @param name Video name
     * @param isPrivate Is video private
     * @param callback Callback
     */
    fun uploadVideoAsync(video: String, name: String, isPrivate: Boolean, callback: Callback<BaseMedia?>) {
        var type: String? = null
        val videoFile = File(video)

        if (videoFile.exists()) {
            type = "fromFile"
        }

        var videoUrl: URL? = null

        if (type == null) {
            try {
                videoUrl = URL(video)
                type = "fromUrl"
            } catch (ignored: MalformedURLException) {
                log.error("Error when trying add photo to message: file not found, or url is bad. Your param: {}", video)
                callback.onResult(null)
                return
            }
        }

        val videoBytes: ByteArray?

        when (type) {
            "fromFile" -> {
                videoBytes = try {
                    Files.readAllBytes(Paths.get(videoFile.toURI()))
                } catch (ignored: IOException) {
                    log.error("Error when reading file {}", videoFile.absolutePath)
                    callback.onResult(null)
                    return
                }
            }

            "fromUrl" -> {
                try {
                    videoBytes = videoUrl!!.toByteArray()
                } catch (e: IOException) {
                    log.error("Error {} occurred when reading URL {}", e.toString(), video)
                    callback.onResult(null)
                    return
                }
            }

            else -> {
                log.error("Bad 'photo' string: path to file, URL or already uploaded 'photo()_()' was expected.")
                callback.onResult(null)
                return
            }
        }

        uploadVideoAsync(videoBytes, name, isPrivate, callback)
    }

    /**
     * Async uploading photos
     * @param videoBytes Photo bytes
     * @param name Video name
     * @param isPrivate Is video private
     * @param callback Callback
     */
    fun uploadVideoAsync(videoBytes: ByteArray?, name: String, isPrivate: Boolean, callback: Callback<BaseMedia?>) {
        if (videoBytes != null) {

            val response = client.video.save(name, disableComments = true, privacyView = PrivacySettings(), privacyComment = PrivacySettings(), isPrivate = isPrivate, publishOnWall = false)

            if (response == null) {
                log.error("Can't get messages upload server, aborting. Video wont be attached to message.")
                callback.onResult(null)
                return
            }

            val mimeType: String = try {
                Utils.getMimeType(videoBytes)
            } catch (e: IOException) {
                log.error(e.message)
                callback.onResult(null)
                return
            }

            val uploadVideoResponse = Requests
                .post(response.uploadUrl)
                .multiPartBody(Part.file("video_file", "video.$mimeType", videoBytes))
                .send().readToText()

            if (uploadVideoResponse.length < 2 || uploadVideoResponse.contains("error") || !uploadVideoResponse.contains("video_id")) {
                log.error("Video won't uploaded: {}", uploadVideoResponse)
                callback.onResult(null)
                return
            }

            val getVideoResponse: JsonObject = try {
                uploadVideoResponse.toJsonObject()
            } catch (ignored: SerializationException) {
                log.error("Bad response of uploading video: {}", uploadVideoResponse)
                callback.onResult(null)
                return
            }

            val id = getVideoResponse.getInt("video_id")!!

            val media = BaseMedia(
                id,
                response.ownerId,
                response.accessKey,
                AttachmentType.VIDEO
            )

            return callback.onResult(media)
        }
    }

    /**
     * Async uploading doc
     * @param doc Doc link: url, from disk or already uploaded to VK as doc{owner_id}_{id}
     * @param peerId peer id
     * @param callback callback
     */
    fun uploadDocAsync(doc: String, peerId: Int, typeOfDoc: DocTypes, callback: Callback<BaseMedia?>) {
        var type: String? = null
        val fileNameField: String
        val docFile = File(doc)

        if (docFile.exists()) {
            type = "fromFile"
        }

        var docUrl: URL? = null

        if (type == null) {
            try {
                docUrl = URL(doc)
                type = "fromUrl"
            } catch (ignored: MalformedURLException) {
                log.error("Error when trying add doc to message: file not found, or url is bad. Your param: $doc")
                callback.onResult(null)
                return
            }
        }

        val docBytes: ByteArray?

        when (type) {
            "fromFile" -> {
                try {
                    docBytes = Files.readAllBytes(Paths.get(docFile.toURI()))
                    fileNameField = docFile.name
                } catch (ignored: IOException) {
                    log.error("Error when reading file ${docFile.absolutePath}")
                    callback.onResult(null)
                    return
                }
            }

            "fromUrl" -> {
                try {
                    val conn = docUrl!!.openConnection()
                    try {
                        docBytes = conn.toByteArray()
                        fileNameField = Utils.guessFileNameByContentType(conn.contentType)
                    } finally {
                        conn.close()
                    }
                } catch (ignored: IOException) {
                    log.error("Error when reading URL $doc")
                    callback.onResult(null)
                    return
                }
            }

            else -> {
                log.error("Bad file or url provided as doc: $doc")
                return
            }
        }

        if (docBytes != null) {
            val response = client.docs.getMessagesUploadServer(peerId)

            if (response == null) {
                log.error("Can't get messages upload server, aborting. Doc wont be attached to message.")
                callback.onResult(null)
                return
            }

            val uploadFileResponse = Requests
                .post(response.uploadUrl)
                .multiPartBody(Part.file("file", fileNameField, docBytes))
                .send().readToText()

            if (uploadFileResponse.length < 2 || uploadFileResponse.contains("error") || !uploadFileResponse.contains("file")) {
                log.error("Doc won't uploaded: $uploadFileResponse")
                callback.onResult(null)
                return
            }

            val getFileStringResponse: JsonObject = try {
                uploadFileResponse.toJsonObject()
            } catch (ignored: SerializationException) {
                log.error("Bad response of uploading file: $uploadFileResponse")
                callback.onResult(null)
                return
            }

            if (!getFileStringResponse.containsKey("file")) {
                log.error("Bad response of uploading doc, no 'file' param: $getFileStringResponse")
                callback.onResult(null)
                return
            }

            val file = getFileStringResponse.getString("file")!!

            // Saving the doc
            val document: Attachment? = when (typeOfDoc) {
                DocTypes.DOC -> client.docs.save(file)!!.document

                DocTypes.AUDIO_MESSAGE -> client.docs.save(file)!!.voice

                DocTypes.GRAFFITI -> client.docs.save(file)!!.graffiti
            }

            if (document == null) {
                log.error("Some error occurred when saving document: $document")
                callback.onResult(null)
                return
            }

            val media = BaseMedia(
                document.id,
                document.ownerId,
                document.accessKey,
                document.typeAttachment
            )

            callback.onResult(media)
        }
    }

    /**
     *
     * @param photo String URL, link to vk photo or path to file
     * @param chatId chat id
     * @param callback callback
     */
    fun uploadPhotoChatAsync(photo: String, chatId: Int, callback: Callback<ChatChangePhotoResponse?>) {
        var type: String? = null

        val photoFile = File(photo)

        if (photoFile.exists()) {
            type = "fromFile"
        }

        var photoUrl: URL? = null

        if (type == null) {
            try {
                photoUrl = URL(photo)
                type = "fromUrl"
            } catch (ignored: MalformedURLException) {
                log.error("Error when trying add photo to message: file not found, or url is bad. Your param: $photo")
                callback.onResult(null)
                return
            }
        }

        val photoBytes: ByteArray

        when (type) {
            "fromFile" -> {
                photoBytes = try {
                    Files.readAllBytes(Paths.get(photoFile.toURI()))
                } catch (ignored: IOException) {
                    log.error("Error when reading file ${photoFile.absolutePath}")
                    callback.onResult(null)
                    return
                }
            }

            "fromUrl" -> {
                try {
                    photoBytes = photoUrl!!.toByteArray()
                } catch (e: IOException) {
                    log.error("Error $e occurred when reading URL $photo")
                    callback.onResult(null)
                    return
                }
            }

            else -> {
                log.error("Bad 'photo' string: path to file, URL or already uploaded 'photo()_()' was expected.")
                callback.onResult(null)
                return
            }
        }

        uploadPhotoChatAsync(photoBytes, chatId, callback)
    }

    /**
     * @param photoBytes bytes
     * @param chatId chat id
     * @param callback callback
     */
    fun uploadPhotoChatAsync(photoBytes: ByteArray?, chatId: Int, callback: Callback<ChatChangePhotoResponse?>) {
        if (photoBytes != null) {

            val response = client.photos.getChatUploadServer(chatId)!!

            if (response.toString().equals(null, ignoreCase = true)) {
                log.error("Can't get messages upload server, aborting. Photo wont be attached to message.")
                callback.onResult(null)
                return
            }

            val mimeType: String = try {
                Utils.getMimeType(photoBytes)
            } catch (e: IOException) {
                log.error(e.message)
                callback.onResult(null)
                return
            }

            val responseUploadFileString = Requests
                .post(response.uploadUrl)
                .multiPartBody(Part.file("file", "photo.$mimeType", photoBytes))
                .send().readToText()

            if (responseUploadFileString.length < 2 || responseUploadFileString.contains("error") || !responseUploadFileString.contains("response")) {
                log.error("Photo has not been uploaded: $responseUploadFileString")
                callback.onResult(null)
                return
            }

            val getPhotoStringResponse: JsonObject = try {
                responseUploadFileString.toJsonObject()
            } catch (ignored: SerializationException) {
                log.error("Bad response of uploading photo: $responseUploadFileString")
                callback.onResult(null)
                return
            }

            if (!getPhotoStringResponse.containsKey("response")) {
                log.error("Bad response of uploading chat photo, no 'response' param: $getPhotoStringResponse")
                callback.onResult(null)
                return
            }

            val responseParam = getPhotoStringResponse.getString("response")!!

            val response1 = client.messages.setChatPhoto(responseParam)!!

            callback.onResult(response1)
        }
    }

    /**
     * Upload group cover by file from url or from disk
     *
     * @param cover    cover
     * @param groupId group id
     * @param callback callback
     */
    fun uploadCoverGroupAsync(cover: String, groupId: Int, callback: Callback<OwnerCoverPhotoResponse?>?) {
        if (groupId == 0) {
            log.error("Please, provide group_id when initialising the client, because it's impossible to upload cover to group not knowing it id.")
            return
        }

        val bytes: ByteArray
        val coverFile = File(cover)

        if (coverFile.exists()) {
            try {
                bytes = coverFile.toURI().toURL().toByteArray()
            } catch (e: IOException) {
                log.error("Cover file was exists, but IOException occurred: ", e)
                return
            }
        } else {
            val coverUrl: URL
            try {
                coverUrl = URL(cover)
                bytes = coverUrl.toByteArray()
            } catch (e: IOException) {
                log.error("Bad string was provided to uploadCover method: path to file or url was expected, but got this: $cover, error: ", e)
                return
            }
        }

        uploadCoverGroupAsync(bytes, groupId, callback)
    }


    /**
     * Updating cover by bytes (of file or url)
     *
     * @param bytes    bytes[]
     * @param groupId  group id
     * @param callback response will return to callback
     */
    fun uploadCoverGroupAsync(bytes: ByteArray, groupId: Int, callback: Callback<OwnerCoverPhotoResponse?>?) {

        val response = client.photos.getOwnerCoverPhotoUploadServer(groupId, 0, 0, 1590, 400)!!

        val mimeType: String = try {
            Utils.getMimeType(bytes)
        } catch (e: IOException) {
            log.error(e.message)
            callback?.onResult(null)
            return
        }

        var coverUploadedResponseString = Requests
                .post(response.uploadUrl)
                .multiPartBody(Part.file("photo", "image.$mimeType", bytes))
                .send().readToText()

        coverUploadedResponseString = if (coverUploadedResponseString != null && coverUploadedResponseString.length > 2) coverUploadedResponseString else "{}"

        val coverUploadedResponse = coverUploadedResponseString.toJsonObject()

        if (coverUploadedResponse.containsKey("hash") && coverUploadedResponse.containsKey("photo")) {
            val hashField = coverUploadedResponse.getString("hash")!!
            val photoField = coverUploadedResponse.getString("photo")!!

            val responseS = client.photos.saveOwnerCoverPhoto(hashField, photoField)

            if (responseS.toString().length < 10 || responseS.toString().contains("error")) {
                log.error("Some error occurred, cover not uploaded: $responseS")
            }

            callback?.onResult(responseS)
        } else {
            log.error("Error occurred when uploading cover: no 'photo' or 'hash' param in response $coverUploadedResponse")
            callback?.onResult(null)
        }
    }

    /* Sync */

    /**
     * @param photo String URL, link to vk photo or path to file
     * @param group_id group id
     * @param album_id album id
     * @return attachment
     */
    fun uploadPhotoToAlbum(photo: String, group_id: Int, album_id: Int): BaseMedia? {
        if (group_id == 0) {
            log.error("Please, provide group_id when initialising the client, because it's impossible to upload cover to group not knowing it id.")
            return null
        }

        val bytes: ByteArray
        val coverFile = File(photo)

        if (coverFile.exists()) {
            try {
                bytes = coverFile.toURI().toURL().toByteArray()
            } catch (e: IOException) {
                log.error("Cover file was exists, but IOException occurred: ", e)
                return null
            }
        } else {
            val coverUrl: URL
            try {
                coverUrl = URL(photo)
                bytes = coverUrl.toByteArray()
            } catch (e: IOException) {
                log.error("Bad string was provided to uploadPhotoToAlbum method: path to file or url was expected, but got this: $photo, error: ", e)
                return null
            }
        }

        return uploadPhotoToAlbum(bytes, group_id, album_id)
    }

    /**
     * @param photoBytes bytes
     * @param album_id album id
     * @param group_id group id
     * @return attachment
     */
    fun uploadPhotoToAlbum(photoBytes: ByteArray?, group_id: Int, album_id: Int): BaseMedia? {
        if (photoBytes != null) {

            val response = if (group_id != 0) {
                client.photos.getUploadServer(album_id, group_id)
            } else {
                client.photos.getUploadServer(album_id)
            }

            if (response == null) {
                log.error("Can't get messages upload server, aborting. Photo wont be attached to message.")
                return null
            }

            val mimeType: String = try {
                Utils.getMimeType(photoBytes)
            } catch (e: IOException) {
                log.error(e.message)
                return null
            }

            // Uploading the photo
            val uploadFileStringResponse = Requests
                .post(response.uploadUrl)
                .multiPartBody(Part.file("photo", "image.$mimeType", photoBytes))
                .send().readToText()

            if (uploadFileStringResponse.length < 2 || uploadFileStringResponse.contains("error") || !uploadFileStringResponse.contains("photo")) {
                log.error("Photo wan't uploaded: $uploadFileStringResponse")
                return null
            }

            val getPhotoStringResponse: JsonObject = try {
                uploadFileStringResponse.toJsonObject()
            } catch (ignored: SerializationException) {
                log.error("Bad response of uploading photo: $uploadFileStringResponse")
                return null
            }

            if (!getPhotoStringResponse.containsKey("photo") || !getPhotoStringResponse.containsKey("server") || !getPhotoStringResponse.containsKey("hash")) {
                log.error("Bad response of uploading photo, no 'photo', 'server' of 'hash' param: {}", getPhotoStringResponse.toString())
                return null
            }

            val photoParam = getPhotoStringResponse.getString("photo")!!
            val serverParam = getPhotoStringResponse.getInt("server")!!
            val hashParam = getPhotoStringResponse.getString("hash")!!

            val photo = client.photos.saveMessagesPhoto(serverParam, photoParam, hashParam)

            if (photo == null) {
                log.error("Error when saving uploaded photo: response is 'false', see execution errors.")
                return null
            }

            return BaseMedia(
                photo[0].id,
                photo[0].ownerId,
                photo[0].accessKey,
                AttachmentType.PHOTO
            )
        }

        return null
    }

    /**
     * @param photo String URL, link to vk photo or path to file
     * @param peerId peer id
     * @return attachment
     */
    fun uploadPhoto(photo: String, peerId: Int = 0): BaseMedia? {
        var type: String? = null
        val photoFile = File(photo)

        if (photoFile.exists()) {
            type = "fromFile"
        }

        var photoUrl: URL? = null
        if (type == null) {
            try {
                photoUrl = URL(photo)
                type = "fromUrl"
            } catch (e: MalformedURLException) {
                log.error("Error when trying add photo to message: file not found, or url is bad. Your param: {}", photo)
                return null
            }
        }

        val photoBytes: ByteArray

        when (type) {
            "fromFile" -> {
                photoBytes = try {
                    Files.readAllBytes(Paths.get(photoFile.toURI()))
                } catch (ignored: IOException) {
                    log.error("Error when reading file {}", photoFile.absolutePath)
                    return null
                }
            }

            "fromUrl" -> {
                try {
                    photoBytes = photoUrl!!.toByteArray()
                } catch (e: IOException) {
                    log.error("Error $e occurred when reading URL $photo")
                    return null
                }
            }

            else -> {
                log.error("Bad 'photo' string: path to file, URL or already uploaded 'photo()_()' was expected.")
                return null
            }
        }

        return uploadPhoto(photoBytes, peerId)
    }

    /**
     * Synchronous adding photo to the message
     *
     * @param photoBytes photo bytes
     * @param peerId peer id
     * @return attachment
     */
    fun uploadPhoto(photoBytes: ByteArray?, peerId: Int): BaseMedia? {
        if (photoBytes != null) {

            val response = client.photos.getMessagesUploadServer(peerId)

            if (response == null) {
                log.error("Can't get messages upload server, aborting. Photo wont be attached to message.")
                return null
            }

            val mimeType: String = try {
                Utils.getMimeType(photoBytes)
            } catch (e: IOException) {
                log.error(e.message)
                return null
            }

            val uploadFileStringResponse = Requests
                .post(response.uploadUrl)
                .multiPartBody(Part.file("photo", "image.$mimeType", photoBytes))
                .send().readToText()

            if (uploadFileStringResponse.length < 2 || uploadFileStringResponse.contains("error") || !uploadFileStringResponse.contains("photo")) {
                log.error("Photo has not been uploaded: $uploadFileStringResponse")
                return null
            }

            val getPhotoStringResponse: JsonObject = try {
                uploadFileStringResponse.toJsonObject()
            } catch (ignored: SerializationException) {
                log.error("Bad response of uploading photo: $uploadFileStringResponse")
                return null
            }

            if (!getPhotoStringResponse.containsKey("photo") || !getPhotoStringResponse.containsKey("server") || !getPhotoStringResponse.containsKey("hash")) {
                log.error("Bad response of uploading photo, no 'photo', 'server' of 'hash' param: $getPhotoStringResponse")
                return null
            }

            val photoParam = getPhotoStringResponse.getString("photo")!!
            val serverParam = getPhotoStringResponse.getInt("server")!!
            val hashParam = getPhotoStringResponse.getString("hash")!!

            val photo = client.photos.saveMessagesPhoto(serverParam, photoParam, hashParam)

            if (photo == null) {
                log.error("Error when saving uploaded photo: response is 'false', see execution errors.")
                return null
            }

            return BaseMedia(
                photo[0].id,
                photo[0].ownerId,
                photo[0].accessKey,
                AttachmentType.PHOTO
            )
        }

        return null
    }

    /**
     * Synchronous uploading doc
     * @param doc       String URL, link to vk doc or path to file
     * @param peerId peer id
     * @param typeOfDoc Type of doc, 'audio_message' or 'graffiti' ('doc' as default)
     * @return attachment
     */
    fun uploadDoc(doc: String, peerId: Int, typeOfDoc: DocTypes = DocTypes.DOC): BaseMedia? {
        var type: String? = null

        val docFile = File(doc)

        if (docFile.exists()) {
            type = "fromFile"
        }

        var docUrl: URL? = null

        if (type == null) {
            try {
                docUrl = URL(doc)
                type = "fromUrl"
            } catch (ignored: MalformedURLException) {
                log.error("Error when trying add doc to message: file not found, or url is bad. Your param: $doc")
                return null
            }
        }

        val docBytes: ByteArray
        val fileNameField: String

        when (type) {
            "fromFile" -> {
                try {
                    docBytes = Files.readAllBytes(Paths.get(docFile.toURI()))
                    fileNameField = docFile.name
                } catch (e: IOException) {
                    log.error("Error when reading file ${docFile.absolutePath}")
                    return null
                }
            }

            "fromUrl" -> {
                try {
                    val conn = docUrl!!.openConnection()
                    try {
                        docBytes = conn.toByteArray()
                        fileNameField = Utils.guessFileNameByContentType(conn.contentType)
                    } finally {
                        conn.close()
                    }
                } catch (e: IOException) {
                    log.error("Error $e occurred when reading URL $doc")
                    return null
                }
            }

            else -> {
                log.error("Bad 'doc' string: path to file, URL or already uploaded 'doc()_()' was expected, but got this: {}", doc)
                return null
            }
        }

        return uploadDoc(docBytes, peerId, typeOfDoc, fileNameField)
    }

    /**
     * @param docBytes bytes
     * @param peerId peer id
     * @param typeOfDoc Type of doc, 'audio_message' or 'graffiti' ('doc' as default)
     * @param fileNameField file name field
     * @return attachment
     */
    fun uploadDoc(docBytes: ByteArray?, peerId: Int, typeOfDoc: DocTypes, fileNameField: String?): BaseMedia? {
        if (docBytes != null) {

            // Getting of server for uploading the photo
            val response = client.docs.getMessagesUploadServer(peerId, typeOfDoc)

            // Some error
            if (response == null) {
                log.error("No upload url in response: $response")
                return null
            }

            // Uploading the doc
            val uploadingOfDocResponseString = Requests
                .post(response.uploadUrl)
                .multiPartBody(Part.file("file", fileNameField, docBytes))
                .send().readToText()

            val uploadingOfDocResponse: JsonObject = try {
                uploadingOfDocResponseString.toJsonObject()
            } catch (e: SerializationException) {
                log.error("Bad response of uploading doc: $uploadingOfDocResponseString, error: ", e)
                return null
            }

            // Getting necessary params
            val file: String = if (uploadingOfDocResponse.containsKey("file")) {
                uploadingOfDocResponse.getString("file")!!
            } else {
                log.error("No 'file' param in response $uploadingOfDocResponseString")
                return null
            }

            // Saving the doc
            val document: Attachment? = when (typeOfDoc) {
                DocTypes.DOC -> client.docs.save(file)!!.document

                DocTypes.AUDIO_MESSAGE -> client.docs.save(file)!!.voice

                DocTypes.GRAFFITI -> client.docs.save(file)!!.graffiti
            }

            if (document == null) {
                log.error("Some error occurred when saving photo: $document")
                return null
            }

            return BaseMedia(
                document.id,
                document.ownerId,
                document.accessKey,
                document.typeAttachment
            )

        } else {
            log.error("Got file or url of doc to be uploaded, but some error occurred and read 0 bytes.")
        }

        return null
    }

    /**
     * @param video String URL, link to vk photo or path to file
     * @param name Video name
     */
    fun uploadVideo(video: String, name: String, isPrivate: Boolean): BaseMedia? {
        var type: String? = null
        val videoFile = File(video)

        if (videoFile.exists()) {
            type = "fromFile"
        }

        var videoUrl: URL? = null

        if (type == null) {
            try {
                videoUrl = URL(video)
                type = "fromUrl"
            } catch (ignored: MalformedURLException) {
                log.error("Error when trying add photo to message: file not found, or url is bad. Your param: {}", video)
                return null
            }
        }

        val videoBytes: ByteArray?

        when (type) {
            "fromFile" -> {
                videoBytes = try {
                    Files.readAllBytes(Paths.get(videoFile.toURI()))
                } catch (ignored: IOException) {
                    log.error("Error when reading file {}", videoFile.absolutePath)
                    return null
                }
            }

            "fromUrl" -> {
                try {
                    videoBytes = videoUrl!!.toByteArray()
                } catch (e: IOException) {
                    log.error("Error $e occurred when reading URL $video")
                    return null
                }
            }

            else -> {
                log.error("Bad 'photo' string: path to file, URL or already uploaded 'photo()_()' was expected.")
                return null
            }
        }

        return uploadVideo(videoBytes, name, isPrivate)
    }

    /**
     * Async uploading photos
     * @param videoBytes Photo bytes
     * @param name Video name
     * @param isPrivate is video private
     */
    fun uploadVideo(videoBytes: ByteArray?, name: String, isPrivate: Boolean): BaseMedia? {
        if (videoBytes != null) {

            val video = client.video.save(
                name,
                disableComments = true,
                privacyView = PrivacySettings(),
                privacyComment = PrivacySettings(),
                isPrivate = isPrivate,
                publishOnWall = false
            )

            if (video == null) {
                log.error("Can't get messages upload server, aborting. Photo wont be attached to message.")
                return null
            }

            val mimeType: String = try {
                Utils.getMimeType(videoBytes)
            } catch (e: IOException) {
                log.error(e.message)
                return null
            }

            val uploadVideoResponse = Requests
                .post(video.uploadUrl)
                .multiPartBody(Part.file("video_file", "video.$mimeType", videoBytes))
                .send().readToText()

            if (uploadVideoResponse.length < 2 || uploadVideoResponse.contains("error") || !uploadVideoResponse.contains(
                    "video_id"
                )
            ) {
                log.error("Photo won't uploaded: $uploadVideoResponse")
                return null
            }

            val getVideoResponse: JsonObject = try {
                uploadVideoResponse.toJsonObject()
            } catch (ignored: SerializationException) {
                log.error("Bad response of uploading photo: $uploadVideoResponse")
                return null
            }

            val id = getVideoResponse.getInt("video_id")!!

            return BaseMedia(
                id,
                video.ownerId,
                video.accessKey,
                AttachmentType.VIDEO
            )
        }

        return null
    }
}