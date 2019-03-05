import org.junit.Assert
import org.junit.Test
import com.yoox.net.models.outbound.*

class SearchResultsTest {
    @Test
    fun refinements() {
        val searchResults = SearchResults(
            listOf(),
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
            )
        )
        Assert.assertEquals(1, searchResults.refinements.colors().count())
        Assert.assertEquals(1, searchResults.refinements.categories().count())
        Assert.assertEquals(1, searchResults.refinements.designers().count())
    }
}