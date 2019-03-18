package com.yoox.net

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.URLBuilder
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
            val stringResult = client.get<String> {
                url(uri.buildString())
            }
            return parseJson(stringResult)
        }
    }

    internal class Post<R : Any, T>(
        client: HttpClient,
        uri: URLBuilder,
        serializer: KSerializer<T>,
        private val body: R? = null
    ) : KtorRequest<T>(client, uri, serializer) {

        override suspend fun execute(): T {
            val stringResult = client.post<String> {
                url(uri.buildString())
                headers.append("Content-Type", "application/json")
                this@Post.body?.let {
                    body = it
                }
            }
            return parseJson(stringResult)
        }
    }

    protected fun parseJson(result: String): T {
        return Json.nonstrict.parse(serializer, result)
    }
}

private class MapRequest<T, R>(
    private val request: Request<T>,
    private val map: (T) -> R
) : Request<R> {

    override suspend fun execute(): R = map(request.execute())
}