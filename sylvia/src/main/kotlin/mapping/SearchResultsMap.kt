package com.yoox.net.mapping

import com.yoox.net.models.outbound.*
import com.yoox.net.models.inbound.Attribute as InboundAttribute
import com.yoox.net.models.inbound.SearchResults as InboundSearchResults

const val IMAGES_BASE_URL: String = "https://cdn.yoox.biz"
const val IMAGE_EXTENSION: String = ".jpg"
val IMAGES_FORMAT_SET: List<String> = listOf("_9_f", "_10_f", "_11_f", "_13_f")

internal fun InboundSearchResults.toOutboundSearchResults(): SearchResults {
    return SearchResults(this.items.map {
        SearchResultItem(
            Brand(it.brandId, it.brand),
            Category(
                CategoryName(it.microCategory, it.microCategoryPlural),
                CategoryName(it.macroCategory, it.macroCategoryPlural)
            ),
            it.colors.map { source ->
                SearchResultColor(
                    source.colorId,
                    source.cod10,
                    source.description,
                    source.rgb,
                    IMAGES_FORMAT_SET
                        .map {
                            format -> "$IMAGES_BASE_URL/${source.cod10.substring(0, 2)}/${source.cod10}$format$IMAGE_EXTENSION"
                        }
                )
            },
            Price(it.fullPrice.toFloat(), it.formattedFullPrice),
            Price(it.discountedPrice.toFloat(), it.formattedDiscountedPrice)
        )
    },
        this.refinements?.filters?.attributes.orEmpty().mapNotNull { buildRefinement(it) },
        SearchStats(
            this.analytics.totalPages,
            this.analytics.totalItems,
            this.analytics.selectedPage
        ))
}

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