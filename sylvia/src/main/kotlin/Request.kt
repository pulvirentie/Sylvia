package com.yoox.net

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.URLBuilder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

interface Request<T> {
    suspend fun execute(): T
}

fun <T, R> Request<T>.map(f: (T) -> R): Request<R> = MapRequest(request = this, map = f)


internal class KtorRequest<T>(
    private val client: HttpClient,
    private val uri: URLBuilder,
    private val serializer: KSerializer<T>
) : Request<T> {

    override suspend fun execute(): T {
        val stringResult = client.get<String> {
            url(uri.buildString())
        }
        return Json.nonstrict.parse(serializer, stringResult)
    }

}

private class MapRequest<T, R>(
    private val request: Request<T>,
    private val map: (T) -> R
) : Request<R> {

    override suspend fun execute(): R = map(request.execute())
}