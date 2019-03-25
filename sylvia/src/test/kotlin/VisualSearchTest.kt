import com.yoox.net.mapping.toOutboundVisualSearch
import com.yoox.net.models.inbound.VisualSearchAlternativeColor
import com.yoox.net.models.inbound.VisualSearchBrand
import com.yoox.net.models.inbound.VisualSearchCategory
import com.yoox.net.models.inbound.VisualSearchColor
import com.yoox.net.models.inbound.VisualSearchImage
import com.yoox.net.models.inbound.VisualSearchItem
import com.yoox.net.models.inbound.VisualSearchItemImage
import com.yoox.net.models.inbound.VisualSearchPrice
import com.yoox.net.models.inbound.VisualSearchResult
import com.yoox.net.models.inbound.VisualSearchResultCategory
import com.yoox.net.models.inbound.VisualSearchSize
import com.yoox.net.models.outbound.Brand
import com.yoox.net.models.outbound.Category
import com.yoox.net.models.outbound.CategoryName
import com.yoox.net.models.outbound.Price
import com.yoox.net.models.outbound.SearchResultColor
import com.yoox.net.models.outbound.SearchResultItem
import com.yoox.net.models.outbound.VisualSearch
import junit.framework.TestCase.assertEquals
import org.junit.Test
import com.yoox.net.models.inbound.VisualSearch as InboundVisualSearch

class VisualSearchTest {
    @Test
    fun visualSearchMapping() {

        val actual = InboundVisualSearch(
            listOf(
                VisualSearchResult(
                    listOf(
                        VisualSearchCategory(
                            listOf(
                                inboundVisualSearchItem("code1"),
                                inboundVisualSearchItem("code2"),
                                inboundVisualSearchItem("code2")
                            )
                        )
                    )
                )
            )
        ).toOutboundVisualSearch()

        val expected = VisualSearch(
            listOf(
                visualSearchItem("code1"),
                visualSearchItem("code2")
            )
        )

        assertEquals(expected, actual)
    }

    private fun inboundVisualSearchItem(id: String) =
        VisualSearchItem(
            "women",
            VisualSearchBrand(
                "brand",
                "macroBrand"
            ),
            VisualSearchPrice(
                "EUR",
                145.4356f,
                145.4356f,
                100.00f
            ),
            listOf(
                VisualSearchResultCategory(
                    "macroCat",
                    "microCat"
                )
            ),
            listOf(
                VisualSearchAlternativeColor(
                    "boutiqueId1",
                    VisualSearchColor(
                        "1",
                        "rgb1",
                        "desc1",
                        "main1"
                    )
                ),
                VisualSearchAlternativeColor(
                    "boutiqueId2",
                    VisualSearchColor(
                        "2",
                        "rgb2",
                        "desc2",
                        "main2"
                    )
                ),
                VisualSearchAlternativeColor(
                    "boutiqueId2",
                    VisualSearchColor(
                        "2",
                        "rgb2",
                        "desc2",
                        "main2"
                    )
                )
            ),
            listOf(
                VisualSearchSize(
                    "desc",
                    "sizeId",
                    "stock"
                )
            ),
            "desc",
            emptyList(),
            emptyList(),
            listOf(
                VisualSearchColor(
                    "1",
                    "rgb1",
                    "desc1",
                    "main1"
                ),
                VisualSearchColor(
                    "2",
                    "rgb2",
                    "desc2",
                    "main2"
                ),
                VisualSearchColor(
                    "2",
                    "rgb2",
                    "desc2",
                    "main2"
                ),
                VisualSearchColor(
                    "3",
                    "rgb3",
                    "desc3",
                    "main3"
                )
            ),
            VisualSearchImage(
                2f,
                emptyList()
            ),
            id,
            "id",
            "name",
            listOf(
                VisualSearchItemImage(
                    "f",
                    "http://ypic.yoox.biz/ypic/yoox/-resize/{size}/f/38802123BW.jpg"
                ),
                VisualSearchItemImage(
                    "r",
                    "http://ypic.yoox.biz/ypic/yoox/-resize/{size}/r/38802123BW.jpg"
                )
            )
        )

    private fun visualSearchItem(id: String) =
        SearchResultItem(
            Brand(
                null,
                "brand"
            ),
            Category(
                CategoryName(
                    "microCat",
                    "microCat"
                ),
                CategoryName(
                    "macroCat",
                    "macroCat"
                )
            ),
            listOf(
                SearchResultColor(
                    1,
                    "boutiqueId1",
                    "desc1",
                    "rgb1",
                    listOf(
                        "http://ypic.yoox.biz/ypic/yoox/-resize/sizeId/f/boutiqueId1.jpg",
                        "http://ypic.yoox.biz/ypic/yoox/-resize/sizeId/r/boutiqueId1.jpg"
                    )
                ),
                SearchResultColor(
                    2,
                    "boutiqueId2",
                    "desc2",
                    "rgb2",
                    listOf(
                        "http://ypic.yoox.biz/ypic/yoox/-resize/sizeId/f/boutiqueId2.jpg",
                        "http://ypic.yoox.biz/ypic/yoox/-resize/sizeId/r/boutiqueId2.jpg"
                    )
                ),
                SearchResultColor(
                    3,
                    id,
                    "desc3",
                    "rgb3",
                    listOf(
                        "http://ypic.yoox.biz/ypic/yoox/-resize/sizeId/f/$id.jpg",
                        "http://ypic.yoox.biz/ypic/yoox/-resize/sizeId/r/$id.jpg"
                    )
                )
            ),
            Price(
                145.4356f,
                "EUR 145.44"
            ),
            Price(
                100.0f,
                "EUR 100.00"
            ),
            "https://cdn.yoox.biz/co/$id" + "_10_f.jpg",
            id
        )
}