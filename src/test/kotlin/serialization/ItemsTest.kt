package serialization

import ItemsBuilder
import com.yoox.net.models.inbound.*
import com.yoox.net.models.outbound.*
import org.junit.Assert.*
import org.junit.Test
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.*
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import com.yoox.net.models.inbound.Item as InboundItem
import com.yoox.net.models.inbound.Color as InboundColor
import com.yoox.net.models.inbound.Size as InboundSize
import com.yoox.net.models.inbound.Price as InboundPrice

class ItemsTest {
    @Test
    fun jsonToItem() {
        runBlocking {
            val inboundItem = InboundItem(
                ImageUrls(
                    listOf("http://exampl.ecom/image.png"),
                    listOf("http://exampl.ecom/image_zoom.png")
                ),
                ItemDescriptions(listOf()),
                CommonFormattedPrices(
                    InboundPrice(90200, "GBP"),
                    InboundPrice(105100, "GBP")
                ),
                "women",
                "women",
                2,
                StringEnvelop("MARNI"),
                203,
                StringEnvelop("Coat"),
                "Coats and Jackets",
                "Coats",
                FormattedPrice("1,051.00", "902.00"),
                listOf(
                    InboundColor(
                        51,
                        "MO",
                        "41868153MO",
                        "Brick red",
                        "906058"
                    )
                ),
                listOf(
                    InboundSize(
                        3,
                        "8",
                        "UK",
                        "8 (UK)",
                        "UK",
                        "8",
                        "40 (IT)",
                        "IT",
                        "40"
                    ),
                    InboundSize(
                        4,
                        "10",
                        "UK",
                        "10 (UK)",
                        "UK",
                        "10",
                        "42 (IT)",
                        "IT",
                        "42"
                    )
                ),
                listOf(
                    ColorSizeQty("MO", 3, 1),
                    ColorSizeQty("MO", 4, 1)
                ),
                "P",
                "PLV_JUST IN",
                "2268",
                CategoryAttribute("cpptt", "Coat", "Coats"),
                CategoryAttribute("cpspll", "Coats and Jackets", "Coats and Jackets"),
                Composition("Composition", "100% Virgin Wool")
            )
            val rawInboundItem = Json.stringify(InboundItem.serializer(), inboundItem)
            val expected = Item(
                Department(
                    "women", "women"
                ),
                Brand(203, "MARNI"),
                Category(
                    CategoryName("Coat", "Coats"),
                    CategoryName("Coats and Jackets", "Coats and Jackets")
                ),
                listOf(
                    Image(
                        "http://exampl.ecom/image.png",
                        "http://exampl.ecom/image_zoom.png"
                    )
                ),
                "100% Virgin Wool",
                SaleLine("2268", "PLV_JUST IN"),
                listOf(
                    Color(
                        51,
                        "41868153MO",
                        "Brick red",
                        "906058",
                        listOf(
                            Size(
                                3,
                                "8",
                                "UK",
                                1
                            ),
                            Size(
                                4,
                                "10",
                                "UK",
                                1
                            )
                        )
                    )
                ),
                Price(1051.00f, "GBP", "1,051.00"),
                Price(902.00f, "GBP", "902.00")
            )
            val engine = MockEngine {
                MockHttpResponse(
                    call,
                    HttpStatusCode.OK,
                    ByteReadChannel(rawInboundItem),
                    Headers.build { set(HttpHeaders.ContentType, "application/json") }
                )
            }
            val actual = ItemsBuilder("uk").build(engine).get("41868153")
            assertEquals(expected, actual)
        }
    }
}