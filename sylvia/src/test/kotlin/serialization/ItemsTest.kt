package serialization

import com.yoox.net.ItemsBuilder
import com.yoox.net.Request
import com.yoox.net.models.inbound.Analytics
import com.yoox.net.models.outbound.Brand
import com.yoox.net.models.outbound.Category
import com.yoox.net.models.outbound.CategoryName
import com.yoox.net.models.outbound.Color
import com.yoox.net.models.outbound.Department
import com.yoox.net.models.outbound.Filter
import com.yoox.net.models.outbound.Image
import com.yoox.net.models.outbound.Item
import com.yoox.net.models.outbound.Price
import com.yoox.net.models.outbound.PriceFilter
import com.yoox.net.models.outbound.Prices
import com.yoox.net.models.outbound.Refinement
import com.yoox.net.models.outbound.SaleLine
import com.yoox.net.models.outbound.SearchResultColor
import com.yoox.net.models.outbound.SearchResultItem
import com.yoox.net.models.outbound.SearchResults
import com.yoox.net.models.outbound.SearchStats
import com.yoox.net.models.outbound.Size
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import com.yoox.net.models.inbound.Attribute as InboundAttribute
import com.yoox.net.models.inbound.AttributeUrl as InboundAttributeUrl
import com.yoox.net.models.inbound.CategoryAttribute as InboundCategoryAttribute
import com.yoox.net.models.inbound.Color as InboundColor
import com.yoox.net.models.inbound.ColorSizeQty as InboundColorSizeQty
import com.yoox.net.models.inbound.CommonFormattedPrices as InboundCommonFormattedPrices
import com.yoox.net.models.inbound.Composition as InboundComposition
import com.yoox.net.models.inbound.Filters as InboundFilters
import com.yoox.net.models.inbound.FormattedPrice as InboundFormattedPrice
import com.yoox.net.models.inbound.ImageUrls as InboundImageUrls
import com.yoox.net.models.inbound.Item as InboundItem
import com.yoox.net.models.inbound.ItemDescriptions as InboundItemDescriptions
import com.yoox.net.models.inbound.Price as InboundPrice
import com.yoox.net.models.inbound.Prices as InboundPrices
import com.yoox.net.models.inbound.Refinements as InboundRefinements
import com.yoox.net.models.inbound.SearchResultColor as InboundSearchResultColor
import com.yoox.net.models.inbound.SearchResultItem as InboundSearchResultItem
import com.yoox.net.models.inbound.SearchResults as InboundSearchResults
import com.yoox.net.models.inbound.Size as InboundSize
import com.yoox.net.models.inbound.StringEnvelop as InboundStringEnvelop

class ItemsTest {

    @Test
    fun jsonToItem() {
        runBlocking {
            val inboundItem = InboundItem(
                InboundImageUrls(
                    listOf("http://exampl.ecom/-COLOR-/image.png"),
                    listOf("http://exampl.ecom/-COLOR-/image_zoom.png"),
                    "http://exampl.ecom/-COLOR-/soldout.png"
                ),
                InboundItemDescriptions(listOf()),
                InboundCommonFormattedPrices(
                    InboundPrice(90200, "GBP"),
                    InboundPrice(105100, "GBP")
                ),
                "women",
                "women",
                2,
                InboundStringEnvelop("MARNI"),
                203,
                InboundStringEnvelop("Coat"),
                "Coats and Jackets",
                "Coats",
                InboundFormattedPrice("1,051.00", "902.00"),
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
                    InboundColorSizeQty("MO", 3, 1),
                    InboundColorSizeQty("MO", 4, 1)
                ),
                "P",
                "PLV_JUST IN",
                "2268",
                InboundCategoryAttribute(
                    InboundAttributeUrl(
                        "/it/donna/shoponline",
                        "/it/donna/shoponline",
                        "/it/donna/shoponline"
                    ),
                    "Coat",
                    "Coats"
                ),
                InboundCategoryAttribute(
                    InboundAttributeUrl(
                        "/it/donna/shoponline",
                        "/it/donna/shoponline",
                        "/it/donna/shoponline"
                    ),
                    "Coats and Jackets",
                    "Coats and Jackets"
                ),
                InboundComposition("Composition", "100% Virgin Wool")
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
                        ),
                        listOf(
                            Image(
                                "http://exampl.ecom/MO/image.png",
                                "http://exampl.ecom/MO/image_zoom.png"
                            )
                        )
                    )
                ),
                Price(1051.00f, "1,051.00"),
                Price(902.00f, "902.00"),
                "http://exampl.ecom/-COLOR-/soldout.png"
            )
            val engine = MockEngine {
                MockHttpResponse(
                    call,
                    HttpStatusCode.OK,
                    ByteReadChannel(rawInboundItem),
                    Headers.build { set(HttpHeaders.ContentType, "application/json") }
                )
            }
            val request: Request<Item> = ItemsBuilder("uk").build(engine).get("41868153")
            val actual: Item = request.execute()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun jsonToSearchResults() {
        runBlocking {
            val inboundSearchResults = InboundSearchResults(
                "MEN",
                InboundRefinements(
                    InboundFilters(
                        InboundPrices(
                            20,
                            3200,
                            20,
                            3200,
                            50
                        ),
                        listOf(
                            InboundAttribute(
                                "ctgr",
                                "ctgr",
                                true,
                                "Categories",
                                listOf(
                                    InboundAttribute(
                                        "ccssr",
                                        "category",
                                        false,
                                        "Accessories",
                                        listOf()
                                    ),
                                    InboundAttribute(
                                        "rt1",
                                        "category",
                                        false,
                                        "Art",
                                        listOf()
                                    )
                                )
                            )
                        )
                    )
                ),
                listOf(
                    InboundSearchResultItem(
                        "49441077GJ",
                        "PRADA",
                        63,
                        "gcc2",
                        "Blazer",
                        "Blazers",
                        "bt",
                        "Suits and Blazers",
                        "Suits and Blazers",
                        690,
                        "690.00",
                        690,
                        "690.00",
                        listOf(
                            InboundSearchResultColor(
                                18,
                                "49441077GJ",
                                "Blue",
                                "214377"
                            )
                        )
                    )
                ),
                Analytics(
                    "",
                    13,
                    4,
                    1,
                    41
                )
            )
            val rawInboundSearchResults = Json.stringify(
                InboundSearchResults.serializer(),
                inboundSearchResults
            )
            val expected = SearchResults(
                listOf(
                    SearchResultItem(
                        Brand(
                            63,
                            "PRADA"
                        ),
                        Category(
                            CategoryName("Blazer", "Blazers"),
                            CategoryName("Suits and Blazers", "Suits and Blazers")
                        ),
                        listOf(
                            SearchResultColor(
                                18,
                                "49441077GJ",
                                "Blue",
                                "214377",
                                listOf(
                                    "https://cdn.yoox.biz/49/49441077GJ_9_f.jpg",
                                    "https://cdn.yoox.biz/49/49441077GJ_10_f.jpg",
                                    "https://cdn.yoox.biz/49/49441077GJ_11_f.jpg",
                                    "https://cdn.yoox.biz/49/49441077GJ_13_f.jpg"
                                )
                            )
                        ),
                        Price(
                            690f,
                            "690.00"
                        ),
                        Price(
                            690f,
                            "690.00"
                        ),
                        "https://cdn.yoox.biz/49/49441077GJ_10_f.jpg",
                        "49441077GJ"
                    )
                ),
                listOf(
                    Refinement.Category(
                        "Categories",
                        listOf(
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
                        ),
                        true
                    )
                ),
                Prices(
                    20,
                    3200,
                    PriceFilter(
                        20,
                        3200
                    )
                ),
                SearchStats(
                    13,
                    41,
                    1
                )
            )
            val engine = MockEngine {
                MockHttpResponse(
                    call,
                    HttpStatusCode.OK,
                    ByteReadChannel(rawInboundSearchResults),
                    Headers.build { set(HttpHeaders.ContentType, "application/json") }
                )
            }
            val actual = ItemsBuilder("uk")
                .build(engine)
                .search("men")
                .execute()
            assertEquals(expected, actual)
        }
    }
}