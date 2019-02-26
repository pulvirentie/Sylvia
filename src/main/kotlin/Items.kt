import io.ktor.client.*
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import kotlinx.serialization.json.Json
import mapping.toOutbound
import com.yoox.net.models.inbound.Item as InboundItem
import com.yoox.net.models.outbound.Item

const val ITEM_API_BASE_URL: String = "https://secure.api.yoox.biz/YooxCore.API/1.0/"
const val DIVISION_CODE: String = "YOOX"

class ItemsBuilder(private val country: String) {
    fun build(engine: HttpClientEngine = OkHttpEngine(OkHttpConfig())): Items = Items(engine, country)
}

class Items(private val engine: HttpClientEngine, private val country: String) {
    private val client by lazy {
        HttpClient(engine) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json.nonstrict)
            }
        }
    }

    suspend fun get(id: String): Item = client.get<InboundItem> {
        url("$ITEM_API_BASE_URL${DIVISION_CODE}_${country.toUpperCase()}/items/$id")
    }.toOutbound()
}