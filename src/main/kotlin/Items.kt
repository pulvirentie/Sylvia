package com.yoox.net

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.internal.StringSerializer
import io.ktor.client.*
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.http.URLBuilder
import com.yoox.net.models.mapping.*
import com.yoox.net.models.inbound.Item as InboundItem
import com.yoox.net.models.inbound.SearchResults as InboundSearchResults
import com.yoox.net.models.outbound.*

const val AUTHORITY: String = "secure.api.yoox.biz"
const val API_BASE_URL: String = "https://$AUTHORITY/YooxCore.API/1.0/"
const val DIVISION_CODE: String = "YOOX"

class ItemsBuilder(private val country: String) {
    fun build(engine: HttpClientEngine = OkHttpEngine(OkHttpConfig())): Items =
        Items(engine, country)
}

class Items(
    private val engine: HttpClientEngine,
    private val country: String
) {
    private val client by lazy {
        HttpClient(engine) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json.nonstrict)
            }
        }
    }

    suspend fun get(id: String): Item =
        client.get<InboundItem> {
            url("$API_BASE_URL${DIVISION_CODE}_${country.toUpperCase()}/items/$id")
        }.toOutboundItem()

    fun search(department: String): DepartmentSearchRequest = DepartmentSearchRequest(
        client,
        URLBuilder("$API_BASE_URL${DIVISION_CODE}_${country.toUpperCase()}/SearchResults?dept=$department")
    )
}

abstract class SearchRequest(
    protected val client: HttpClient,
    internal val uri: URLBuilder
) {
    open suspend fun execute(): SearchResults =
        client.get<InboundSearchResults> {
            url(uri.buildString())
        }.toOutboundSearchResults()
}

class DepartmentSearchRequest internal constructor(
    client: HttpClient,
    uri: URLBuilder
) : SearchRequest(client, uri) {
    fun filterBy(vararg chips: Chip): DepartmentSearchRequest =
        filter(chips.flatMap { it.attributes.toList() }.toMap())

    fun filterBy(vararg filters: Filter): DepartmentSearchRequest =
        filter(filters.groupBy({ it.field }, { it.value }))

    private fun filter(next: Map<String, List<String>>): DepartmentSearchRequest {
        val serializer =
            (StringSerializer to StringSerializer.list).map
        val previous =
            Json.parse(
                serializer,
                uri.parameters["attributes"] ?: "{}"
            )
        val union: Map<String, List<String>> =
            (previous.asSequence() + next.asSequence())
                .associateBy({ it.key }, { it.value.distinct() })
        uri.parameters["attributes"] =
            Json.stringify(
                serializer,
                union
            )
        return DepartmentSearchRequest(client, uri)
    }
}