package com.yoox.net.models.outbound

data class Item(
    val department: Department,
    val brand: Brand,
    val category: Category,
    val composition: String,
    val saleLine: SaleLine,
    val colors: List<Color>,
    val fullPrice: Price,
    val discountedPrice: Price
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
    val chips: List<Chip>,
    val refinements: List<Refinement>
)

data class SearchResultItem(
    val brand: Brand,
    val category: Category,
    val colors: List<SearchResultColor>,
    val fullPrice: Price,
    val discountedPrice: Price
)

data class SearchResultColor(
    val id: Int,
    val productId: String,
    val name: String,
    val rgb: String,
    val images: List<String>
)

data class Chip(
    val label: String,
    val attributes: Map<String, List<String>>,
    val active: Boolean
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

data class PriceFilter(
    val min: Int,
    val max: Int
)

fun Iterable<Refinement>.colors(): Iterable<Refinement.Color> =
    filterIsInstance<Refinement.Color>()

fun Iterable<Refinement>.designers(): Iterable<Refinement.Designer> =
    filterIsInstance<Refinement.Designer>()

fun Iterable<Refinement>.categories(): Iterable<Refinement.Category> =
    filterIsInstance<Refinement.Category>()