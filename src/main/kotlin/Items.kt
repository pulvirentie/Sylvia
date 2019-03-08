package com.yoox.net

import com.yoox.net.mapping.toOutboundItem
import com.yoox.net.mapping.toOutboundSearchResults
import com.yoox.net.models.outbound.Chip
import com.yoox.net.models.outbound.Filter
import com.yoox.net.models.outbound.Item
import com.yoox.net.models.outbound.SearchResults
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.URLBuilder
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import kotlinx.serialization.map
import com.yoox.net.models.inbound.Item as InboundItem
import com.yoox.net.models.inbound.SearchResults as InboundSearchResults

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

    fun get(id: String): Request<Item> =
        ConcreteRequest(
            client,
            URLBuilder("$API_BASE_URL${DIVISION_CODE}_${country.toUpperCase()}/items/$id")
        ) { Json.parse(InboundItem.serializer(), it) }
            .map(InboundItem::toOutboundItem)

    fun search(department: String): FilterableRequest =
        DepartmentSearchRequest(
            client,
            URLBuilder("$API_BASE_URL${DIVISION_CODE}_${country.toUpperCase()}/SearchResults?dept=$department")
        ) { Json.parse(InboundSearchResults.serializer(), it) }
}

interface Request<T> {
    suspend fun execute(): T
}

interface FilterableRequest : Request<SearchResults> {
    fun filterBy(vararg chips: Chip): FilterableRequest

    fun filterBy(vararg filters: Filter): FilterableRequest
}

internal class ConcreteRequest<T>(
    private val client: HttpClient,
    private val uri: URLBuilder,
    private val mapToInbound: (String) -> T
) : Request<T> {
    override suspend fun execute(): T =
        mapToInbound(client.get {
            url(uri.buildString())
        })
}

fun <T, R> Request<T>.map(f: (T) -> R): Request<R> = MapRequest(request = this, map = f)

internal class MapRequest<T, R>(
    private val request: Request<T>,
    private val map: (T) -> R
) : Request<R> {

    override suspend fun execute(): R = map(request.execute())
}

internal class DepartmentSearchRequest internal constructor(
    private val client: HttpClient,
    internal val uri: URLBuilder,
    private val mapToInbound: (String) -> InboundSearchResults
) : FilterableRequest {
    override suspend fun execute(): SearchResults =
        ConcreteRequest(
            client,
            uri,
            mapToInbound
        ).map(InboundSearchResults::toOutboundSearchResults)
            .execute()

    override fun filterBy(vararg chips: Chip): DepartmentSearchRequest =
        filter(flattenAttributes(chips.flatMap { it.attributes.toList() }))

    override fun filterBy(vararg filters: Filter): DepartmentSearchRequest =
        filter(filters.groupBy({ it.field }, { it.value }))

    private fun flattenAttributes(source: List<Pair<String, List<String>>>): Map<String, List<String>> =
        source.groupBy({ it.first }, { it.second })
            .mapValues { it.value.flatten() }

    private fun filter(next: Map<String, List<String>>): DepartmentSearchRequest {
        val serializer =
            (StringSerializer to StringSerializer.list).map
        val previous =
            Json.parse(
                serializer,
                uri.parameters["attributes"] ?: "{}"
            )
        val union: Map<String, List<String>> =
            flattenAttributes(previous.toList() + next.toList())
                .toList()
                .associateBy({ it.first }, { it.second.distinct() })
        uri.parameters["attributes"] =
            Json.stringify(
                serializer,
                union
            )
        return DepartmentSearchRequest(
            client,
            uri,
            mapToInbound
        )
    }
}