package com.yoox.net

import com.yoox.net.models.mapping.toOutboundItem
import com.yoox.net.models.mapping.toOutboundSearchResults
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
        KtorRequest(
            client,
            URLBuilder("$API_BASE_URL${DIVISION_CODE}_${country.toUpperCase()}/items/$id"),
            InboundItem.serializer()
        ).map(InboundItem::toOutboundItem)

    fun search(department: String): FilterableRequest =
        DepartmentSearchRequest(
            client,
            URLBuilder("$API_BASE_URL${DIVISION_CODE}_${country.toUpperCase()}/SearchResults?dept=$department"),
            { Json.parse(InboundSearchResults.serializer(), it) },
            InboundSearchResults::toOutboundSearchResults
        )
}


interface FilterableRequest : Request<SearchResults> {

    fun filterBy(vararg chips: Chip): FilterableRequest

    fun filterBy(vararg filters: Filter): FilterableRequest
}


internal class DepartmentSearchRequest internal constructor(
    private val client: HttpClient,
    internal val uri: URLBuilder,
    private val mapToInbound: (String) -> InboundSearchResults,
    private val mapToOutbound: (InboundSearchResults) -> SearchResults
) : FilterableRequest {

    override suspend fun execute(): SearchResults =
        mapToOutbound(
            mapToInbound(client.get {
                url(uri.buildString())
            })
        )

    override fun filterBy(vararg chips: Chip): DepartmentSearchRequest =
        filter(chips.flatMap { it.attributes.toList() }.toMap())

    override fun filterBy(vararg filters: Filter): DepartmentSearchRequest =
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
        return DepartmentSearchRequest(
            client,
            uri,
            mapToInbound,
            mapToOutbound
        )
    }
}