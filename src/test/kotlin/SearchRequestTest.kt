
import com.yoox.net.DepartmentSearchRequest
import com.yoox.net.FilterableRequest
import com.yoox.net.ItemsBuilder
import com.yoox.net.models.outbound.Chip
import com.yoox.net.models.outbound.Filter
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import kotlinx.serialization.map
import org.junit.Assert.assertEquals
import org.junit.Test

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
            val request: FilterableRequest = ItemsBuilder("uk")
                .build(engine)
                .search("men")
            assertEquals("men", (request as DepartmentSearchRequest).uri.parameters["dept"])
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
            val request: FilterableRequest = ItemsBuilder("uk")
                .build(engine)
                .search("men")
                .filterBy(
                    Chip(
                        "Suits and Blazers",
                        hashMapOf("ctgr" to listOf("x")),
                        true
                    ),
                    Chip(
                        "Grey",
                        hashMapOf("clr" to listOf("25")),
                        false
                    )
                )
                .filterBy(
                    Filter(
                        false,
                        "Suits and Blazers",
                        "x",
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
                    )
                )
            val castedRequest: DepartmentSearchRequest = request as DepartmentSearchRequest
            assertEquals("men", castedRequest.uri.parameters["dept"])
            val actual = Json.parse(
                (StringSerializer to StringSerializer.list).map,
                castedRequest.uri.parameters["attributes"] ?: "{}"
            )
            assertEquals(2, actual.keys.size)
            assertEquals(listOf("x", "ccssr", "rt1"), actual["ctgr"])
            assertEquals(listOf("25"), actual["clr"])
        }
    }
}