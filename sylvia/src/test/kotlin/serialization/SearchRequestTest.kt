package serialization

import com.yoox.net.DepartmentSearchRequest
import com.yoox.net.FilterableRequest
import com.yoox.net.ItemsBuilder
import com.yoox.net.attributesSerializer
import com.yoox.net.models.outbound.DepartmentType
import com.yoox.net.models.outbound.Filter
import com.yoox.net.models.outbound.PriceFilter
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
                .search(DepartmentType.Men)
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
                .search(DepartmentType.Men)
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
                    ),
                    Filter(
                        false,
                        "Suits and Blazers",
                        "lttbgn1",
                        "ctgr"
                    )
                )
            val byDepartmentRequest: DepartmentSearchRequest = request as DepartmentSearchRequest
            assertEquals("men", byDepartmentRequest.uri.parameters["dept"])
            val attributes = Json.parse(
                attributesSerializer,
                byDepartmentRequest.uri.parameters["attributes"] ?: "{}"
            )
            assertEquals(2, attributes.keys.size)
            assertEquals(listOf("lttbgn1", "ccssr", "rt1"), attributes["ctgr"])
            assertEquals(listOf("21", "93"), attributes["clr"])
        }
    }

    @Test
    fun filterByPrice() {
        val engine = MockEngine {
            MockHttpResponse(
                call,
                HttpStatusCode.OK
            )
        }
        runBlocking {
            val filter = PriceFilter(10, 20)
            val request: FilterableRequest = ItemsBuilder("uk")
                .build(engine)
                .search(DepartmentType.Men)
                .filterBy(filter)
            val byDepartmentRequest = request as DepartmentSearchRequest
            assertEquals(filter.min.toString(), byDepartmentRequest.uri.parameters["priceMin"])
            assertEquals(filter.max.toString(), byDepartmentRequest.uri.parameters["priceMax"])
        }
    }

    @Test
    fun paged() {
        val engine = MockEngine {
            MockHttpResponse(
                call,
                HttpStatusCode.OK
            )
        }
        runBlocking {
            val filter = PriceFilter(10, 20)
            val pageIndex = 4
            val request: FilterableRequest = ItemsBuilder("uk")
                .build(engine)
                .search(DepartmentType.Men)
                .filterBy(filter)
                .page(pageIndex)
            val byDepartmentRequest = request as DepartmentSearchRequest
            assertEquals(pageIndex.toString(), byDepartmentRequest.uri.parameters["page"])
        }
    }

    @Test
    fun byFreeText() {
        val engine = MockEngine {
            MockHttpResponse(
                call,
                HttpStatusCode.OK
            )
        }
        runBlocking {
            val text = "Jeans"
            val request: FilterableRequest = ItemsBuilder("uk")
                .build(engine)
                .search(DepartmentType.Men)
                .filterBy(text)
            val byDepartmentRequest = request as DepartmentSearchRequest
            assertEquals(text, byDepartmentRequest.uri.parameters["textSearch"])
        }
    }
}