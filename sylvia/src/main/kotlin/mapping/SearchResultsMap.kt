package com.yoox.net.mapping

import com.yoox.net.models.outbound.Brand
import com.yoox.net.models.outbound.Category
import com.yoox.net.models.outbound.CategoryName
import com.yoox.net.models.outbound.Filter
import com.yoox.net.models.outbound.Price
import com.yoox.net.models.outbound.PriceFilter
import com.yoox.net.models.outbound.Prices
import com.yoox.net.models.outbound.Refinement
import com.yoox.net.models.outbound.SearchResultColor
import com.yoox.net.models.outbound.SearchResultItem
import com.yoox.net.models.outbound.SearchResults
import com.yoox.net.models.outbound.SearchStats
import com.yoox.net.models.inbound.Attribute as InboundAttribute
import com.yoox.net.models.inbound.SearchResults as InboundSearchResults

const val IMAGES_BASE_URL: String = "https://cdn.yoox.biz"
const val IMAGE_EXTENSION: String = ".jpg"
val IMAGES_FORMAT_SET: List<String> = listOf("_9_f", "_10_f", "_11_f", "_13_f")

internal fun InboundSearchResults.toOutboundSearchResults(): SearchResults {
    return SearchResults(
        this.items.map {
            SearchResultItem(
                Brand(it.brandId, it.brand),
                Category(
                    CategoryName(it.microCategory, it.microCategoryPlural),
                    CategoryName(it.macroCategory, it.macroCategoryPlural)
                ),
                it.colors.map { source ->
                    val cod10 = source.cod10
                    SearchResultColor(
                        source.colorId,
                        cod10,
                        source.description,
                        source.rgb,
                        IMAGES_FORMAT_SET
                            .map { format ->
                                buildImageUrl(cod10, format)
                            }
                    )
                },
                Price(it.fullPrice.toFloat(), it.formattedFullPrice),
                Price(it.discountedPrice.toFloat(), it.formattedDiscountedPrice),
                buildImageUrl(it.cod10, "_10_f"),
                it.cod10
            )
        },
        this.refinements?.filters?.attributes.orEmpty().mapNotNull { buildRefinement(it) },
        buildPrices(this.refinements?.filters?.prices),
        SearchStats(
            this.analytics.totalPages,
            this.analytics.totalItems,
            this.analytics.selectedPage
        )
    )
}

private fun buildImageUrl(cod10: String, format: String) =
    "$IMAGES_BASE_URL/${cod10.substring(0, 2)}/$cod10$format$IMAGE_EXTENSION"

typealias RefinementBuilder<T> = (String, List<Filter>, Boolean) -> T

private fun <T : Refinement> toCanonicalRefinement(attribute: InboundAttribute): (RefinementBuilder<T>) -> T {
    return { it ->
        it(
            attribute.value.orEmpty(),
            attribute.attributes.mapNotNull {
                if (it.code.isNullOrEmpty())
                    null
                else
                    Filter(
                        it.isSelected,
                        it.value.orEmpty(),
                        it.code,
                        attribute.code.orEmpty()
                    )
            },
            attribute.isSelected
        )
    }
}

internal fun buildRefinement(attribute: InboundAttribute): Refinement? {
    return when (attribute.type) {
        "Color" -> toCanonicalRefinement<Refinement.Color>(attribute)() { a, b, c -> Refinement.Color(a, b, c) }
        "dsgnrs" -> toCanonicalRefinement<Refinement.Designer>(attribute)() { a, b, c -> Refinement.Designer(a, b, c) }
        "ctgr" -> toCanonicalRefinement<Refinement.Category>(attribute)() { a, b, c -> Refinement.Category(a, b, c) }
        else -> null
    }
}

internal fun buildPrices(input: com.yoox.net.models.inbound.Prices?): Prices {
    return if (input == null) {
        Prices(
            0,
            0,
            PriceFilter(
                0,
                0
            )
        )
    } else {
        Prices(
            input.min,
            input.max,
            PriceFilter(
                input.priceMin,
                input.priceMax
            )
        )
    }
}