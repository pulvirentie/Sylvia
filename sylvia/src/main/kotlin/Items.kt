package com.yoox.net

import com.yoox.net.mapping.toOutboundItem
import com.yoox.net.mapping.toOutboundSearchResults
import com.yoox.net.models.outbound.Chip
import com.yoox.net.models.outbound.Filter
import com.yoox.net.models.outbound.Item
import com.yoox.net.models.outbound.PriceFilter
import com.yoox.net.models.outbound.SearchResults
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.http.URLBuilder
import kotlinx.serialization.KSerializer
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
        )
            .map(InboundItem::toOutboundItem)

    fun search(department: String): FilterableRequest =
        DepartmentSearchRequest(
            client,
            URLBuilder("$API_BASE_URL${DIVISION_CODE}_${country.toUpperCase()}/SearchResults?dept=$department")
        )
}

interface FilterableRequest : Request<SearchResults> {
    fun filterBy(vararg chips: Chip): FilterableRequest

    fun filterBy(vararg filters: Filter): FilterableRequest

    fun filterBy(filter: PriceFilter): FilterableRequest
}

internal val attributesSerializer =
    (StringSerializer to StringSerializer.list).map
internal val chipsSerializer =
    (StringSerializer to attributesSerializer).map

typealias ListOfAttributes = Map<String, List<String>>

internal class DepartmentSearchRequest internal constructor(
    private val client: HttpClient,
    internal val uri: URLBuilder
) : FilterableRequest {
    override suspend fun execute(): SearchResults =
        KtorRequest(
            client,
            uri,
            InboundSearchResults.serializer()
        ).map(InboundSearchResults::toOutboundSearchResults)
            .execute()

    override fun filterBy(vararg chips: Chip): DepartmentSearchRequest =
            filter("chip",
                chipsSerializer,
                { mapOf("attributes" to it) },
                { it["attributes"].orEmpty() })(flattenAttributes(chips.flatMap { it.attributes.toList() }))

    override fun filterBy(vararg filters: Filter): DepartmentSearchRequest =
            filter("attributes",
                attributesSerializer,
                { it },
                { it })(filters.groupBy({ it.field }, { it.value }))

    override fun filterBy(filter: PriceFilter): FilterableRequest {
        uri.parameters["priceMin"] = filter.min.toString()
        uri.parameters["priceMax"] = filter.max.toString()
        return DepartmentSearchRequest(
            client,
            uri
        )
    }

    private fun flattenAttributes(source: List<Pair<String, List<String>>>): ListOfAttributes =
        source.groupBy({ it.first }, { it.second })
            .mapValues { it.value.flatten() }

    private fun <T> filter(key: String,
                           serializer: KSerializer<T>,
                           output: (ListOfAttributes) -> T,
                           input: (T) -> ListOfAttributes): (ListOfAttributes) -> DepartmentSearchRequest {
        return { next ->
            val previous = input(
                Json.parse(
                    serializer,
                    uri.parameters[key] ?: "{}"
                )
            )
            val union =
                flattenAttributes(previous.toList() + next.toList())
                    .toList()
                    .associateBy({ it.first }, { it.second.distinct() })
            uri.parameters[key] =
                Json.stringify(
                    serializer,
                    output(union)
                )
            DepartmentSearchRequest(
                client,
                uri
            )
        }
    }
}