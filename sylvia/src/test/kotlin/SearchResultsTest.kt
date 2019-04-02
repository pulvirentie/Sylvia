import com.yoox.net.models.outbound.categories
import com.yoox.net.models.outbound.colors
import com.yoox.net.models.outbound.designers
import com.yoox.net.models.outbound.Filter
import com.yoox.net.models.outbound.PriceFilter
import com.yoox.net.models.outbound.Prices
import com.yoox.net.models.outbound.Refinement
import com.yoox.net.models.outbound.SearchResults
import com.yoox.net.models.outbound.SearchStats
import org.junit.Assert
import org.junit.Test

class SearchResultsTest {
    @Test
    fun refinements() {
        val searchResults = SearchResults(
            listOf(),
            listOf(
                Refinement.Category(
                    "Categories",
                    listOf(
                        Filter(true, "shoes", "a", "b"),
                        Filter(true, "bags", "c", "d"),
                        Filter(true, "denim", "e", "f")
                    ),
                    true
                ),
                Refinement.Color(
                    "Colors",
                    listOf(
                        Filter(true, "red", "a", "b"),
                        Filter(true, "blue", "c", "d")
                    ),
                    true
                ),
                Refinement.Designer(
                    "Designers",
                    listOf(
                        Filter(true, "YOOX", "a", "b")
                    ),
                    true
                )
            ),
            Prices(
                10,
                15570,
                PriceFilter(
                    10,
                    15570
                )
            ),
            SearchStats(
                13,
                41,
                1
            )
        )
        Assert.assertEquals(2, searchResults.refinements.colors().count())
        Assert.assertEquals(3, searchResults.refinements.categories().count())
        Assert.assertEquals(1, searchResults.refinements.designers().count())
    }
}
