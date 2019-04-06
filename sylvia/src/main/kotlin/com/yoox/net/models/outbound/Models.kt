package com.yoox.net.models.outbound

enum class DepartmentType(internal val value: String) {
    Men("men"),
    Women("women"),
    Art("art"),
    GirlBaby("collgirl_baby"),
    GirlKid("collgirl_kid"),
    GirlJunior("collgirl_junior"),
    BoyBaby("collboy_baby"),
    BoyKid("collboy_kid"),
    BoyJunior("collboy_junior")
}

enum class Sort(internal val value: Int) {
    NewArrivals(2),
    PriceMin(3),
    PriceMax(4)
}

data class Item(
    val department: Department,
    val brand: Brand,
    val category: Category,
    val composition: String,
    val saleLine: SaleLine,
    val colors: List<Color>,
    val fullPrice: Price,
    val discountedPrice: Price,
    val previewImage: String
)

fun Item.globalAvailability(): Int =
    this.colors.fold(0) { acc, next -> acc + next.globalAvailability() }

data class Department(
    val name: String,
    val parent: String
)

data class Brand(
    val id: Int,
    val name: String
)

data class Category(
    val name: CategoryName,
    val parent: CategoryName
)

data class CategoryName(
    val name: String,
    val pluralName: String
)

data class Image(
    val thumbnailUrl: String,
    val zoomUrl: String
)

data class SaleLine(
    val id: String,
    val name: String
)

data class Color(
    val id: Int,
    val productId: String,
    val name: String,
    val rgb: String,
    val sizeList: List<Size>,
    val images: List<Image>
)

fun Color.globalAvailability(): Int =
    this.sizeList.fold(0) { acc, next -> acc + next.availableUnits }

data class Size(
    val id: Int,
    val name: String,
    val family: String,
    val availableUnits: Int
)

data class Price(
    val amount: Float,
    val rawPrice: String
)

data class SearchResults(
    val items: List<SearchResultItem>,
    val refinements: List<Refinement>,
    val prices: Prices,
    val stats: SearchStats
)

data class SearchResultItem(
    val brand: Brand,
    val category: Category,
    val colors: List<SearchResultColor>,
    val fullPrice: Price,
    val discountedPrice: Price,
    val previewImage: String,
    val id: String
)

data class SearchResultColor(
    val id: Int,
    val productId: String,
    val name: String,
    val rgb: String,
    val images: List<String>
)

sealed class Refinement {
    abstract val label: String
    abstract val filters: List<Filter>
    abstract val active: Boolean

    data class Color(
        override val label: String,
        override val filters: List<Filter>,
        override val active: Boolean
    ) : Refinement()

    data class Designer(
        override val label: String,
        override val filters: List<Filter>,
        override val active: Boolean
    ) : Refinement()

    data class Category(
        override val label: String,
        override val filters: List<Filter>,
        override val active: Boolean
    ) : Refinement()
}

data class Filter(
    val active: Boolean,
    val label: String,
    internal val value: String,
    internal val field: String
)

data class Prices(
    val availableMin: Int,
    val availableMax: Int,
    val currentFilter: PriceFilter
)

data class PriceFilter(
    val min: Int,
    val max: Int
)

data class SearchStats(
    val pageCount: Int,
    val itemCount: Int,
    val currentPageIndex: Int
)

fun Iterable<Refinement>.colors() =
    filterIsInstance<Refinement.Color>().first().filters.toList()

fun Iterable<Refinement>.designers() =
    filterIsInstance<Refinement.Designer>().first().filters.toList()

fun Iterable<Refinement>.categories() =
    filterIsInstance<Refinement.Category>().first().filters.toList()
