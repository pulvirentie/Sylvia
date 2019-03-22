import com.yoox.net.models.outbound.PriceFilter
import com.yoox.net.models.outbound.Prices
import com.yoox.net.models.outbound.Refinement
import com.yoox.net.models.outbound.SearchResults
import com.yoox.net.models.outbound.SearchStats
import com.yoox.net.models.outbound.categories
import com.yoox.net.models.outbound.colors
import com.yoox.net.models.outbound.designers
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
                    listOf(),
                    true
                ),
                Refinement.Color(
                    "Colors",
                    listOf(),
                    true
                ),
                Refinement.Designer(
                    "Designers",
                    listOf(),
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
        Assert.assertEquals(1, searchResults.refinements.colors().count())
        Assert.assertEquals(1, searchResults.refinements.categories().count())
        Assert.assertEquals(1, searchResults.refinements.designers().count())
    }
}