package com.yoox.net

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.URLBuilder
import io.ktor.http.content.PartData
import kotlinx.io.InputStream
import kotlinx.io.streams.asInput
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

interface Request<T> {
    suspend fun execute(): T
}

fun <T, R> Request<T>.map(f: (T) -> R): Request<R> = MapRequest(request = this, map = f)

internal sealed class KtorRequest<T>(
    open val client: HttpClient,
    open val uri: URLBuilder,
    private val serializer: KSerializer<T>
) : Request<T> {

    internal class Get<T>(
        client: HttpClient,
        uri: URLBuilder,
        serializer: KSerializer<T>
    ) : KtorRequest<T>(client, uri, serializer) {

        override suspend fun execute(): T {
            val result: String = client.get {
                url(uri.buildString())
            }
            return parseJson(result)
        }
    }

    internal class Post<R : Any, T>(
        client: HttpClient,
        uri: URLBuilder,
        serializer: KSerializer<T>,
        private val body: R? = null
    ) : KtorRequest<T>(client, uri, serializer) {

        override suspend fun execute(): T {
            val result: String = client.post {
                url(uri.buildString())
                headers.append("Content-Type", "application/json")
                this@Post.body?.let {
                    body = it
                }
            }
            return parseJson(result)
        }
    }

    internal class SubmitForm<T>(
        client: HttpClient,
        uri: URLBuilder,
        serializer: KSerializer<T>,
        private val gender: String,
        private val form: InputStream
    ) : KtorRequest<T>(client, uri, serializer) {

        override suspend fun execute(): T {
            val data: List<PartData> = formData {
                append("image", form.asInput())
                append("gender", gender)
            }
            val result: String = client.submitFormWithBinaryData(
                formData = data,
                block = {
                    url(uri.buildString())
                    headers.append("Accept", "application/json")
                }
            )
            return parseJson(result)
        }
    }

    protected fun parseJson(result: String): T = Json.nonstrict.parse(serializer, result)
}

private class MapRequest<T, R>(
    private val request: Request<T>,
    private val map: (T) -> R
) : Request<R> {

    override suspend fun execute(): R = map(request.execute())
}