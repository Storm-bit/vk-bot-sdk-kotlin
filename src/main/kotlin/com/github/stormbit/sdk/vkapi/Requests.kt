package com.github.stormbit.sdk.vkapi

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.BASE_API_URL
import com.github.stormbit.sdk.utils.formPart
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.KSerializer

class FileContent(
    val filename: String,
    val bytes: ByteArray
)

class UploadableFile(
    val key: String,
    val content: FileContent
)

class VkApiRequest<T : Any>(
    val client: Client,
    val httpMethod: HttpMethod,
    val method: String,
    val parameters: Parameters,
    var serializer: KSerializer<T>
)

class UploadFilesRequest<T : Any>(
    val client: Client,
    val uploadUrl: String,
    val files: List<UploadableFile>,
    val parameters: Parameters,
    val serializer: KSerializer<T>
)

internal suspend inline operator fun <T : Any> VkApiRequest<T>.invoke(): HttpStatement =
    client.httpClient.request(BASE_API_URL + method) {
        method = httpMethod
        val parameters = client.baseParams + parameters
        if (httpMethod == HttpMethod.Post) body = FormDataContent(parameters) else url.parameters.appendAll(parameters)
    }

internal suspend inline operator fun <T : Any> UploadFilesRequest<T>.invoke(): HttpStatement =
    client.httpClient.submitFormWithBinaryData(uploadUrl, formData {
        parameters.flattenEntries().map(Pair<String, String>::formPart).forEach(::append)
        files.map(UploadableFile::formPart).forEach(::append)
    })

suspend fun <T : Any> VkApiRequest<T>.execute(): T {
    return client.api.executeMethod(this)
}

suspend fun <T : Any> UploadFilesRequest<T>.execute(): T {
    return client.api.uploadFile(this)
}