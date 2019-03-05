import org.junit.Assert.*
import org.junit.Test
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import kotlinx.serialization.map
import com.yoox.net.ItemsBuilder
import com.yoox.net.models.outbound.*

class SearchRequestTest {
    @Test
    fun byDepartment() {
        val engine = MockEngine {
            MockHttpResponse(
                call,
                HttpStatusCode.OK
            )
        }
        runBlocking {
            val request = ItemsBuilder("uk")
                .build(engine)
                .search("men")
            assertEquals("men", request.uri.parameters["dept"])
            val actual = Json.parse(
                (StringSerializer to StringSerializer.list).map,
                request.uri.parameters["attributes"] ?: "{}"
            )
            assertEquals(0, actual.keys.size)
        }
    }

    @Test
    fun filtering() {
        val engine = MockEngine {
            MockHttpResponse(
                call,
                HttpStatusCode.OK
            )
        }
        runBlocking {
            val request = ItemsBuilder("uk")
                .build(engine)
                .search("men")
                .filterBy(
                    Chip(
                        "",
                        hashMapOf("ctgr" to listOf("cntr2")),
                        true
                    ),
                    Chip(
                        "",
                        hashMapOf("clr" to listOf("25", "21", "22")),
                        false
                    ),
                    Chip(
                        "",
                        hashMapOf("ctgr" to listOf("brs", "lttbgn1")),
                        false
                    )
                )
                .filterBy(
                    Filter(
                        false,
                        "Suits and Blazers",
                        "lttbgn1",
                        "ctgr"
                    ),
                    Filter(
                        false,
                        "Accessories",
                        "ccssr",
                        "ctgr"
                    ),
                    Filter(
                        false,
                        "Art",
                        "rt1",
                        "ctgr"
                    ),
                    Filter(
                        true,
                        "Black",
                        "21",
                        "clr"
                    ),
                    Filter(
                        true,
                        "Green",
                        "93",
                        "clr"
                    )
                )
            assertEquals("men", request.uri.parameters["dept"])
            val actual = Json.parse(
                (StringSerializer to StringSerializer.list).map,
                request.uri.parameters["attributes"] ?: "{}"
            )
            assertEquals(2, actual.keys.size)
            assertEquals(listOf("cntr2", "brs", "lttbgn1", "ccssr", "rt1"), actual["ctgr"])
            assertEquals(listOf("25", "21", "22", "93"), actual["clr"])
        }
    }
}