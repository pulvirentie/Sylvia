package com.yoox.net.models.mapping

import com.yoox.net.models.outbound.*
import com.yoox.net.models.inbound.Attribute as InboundAttribute
import com.yoox.net.models.inbound.SearchResults as InboundSearchResults

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
                    source.rgb
                )
            },
            Price(it.fullPrice.toFloat(), it.formattedFullPrice),
            Price(it.discountedPrice.toFloat(), it.formattedDiscountedPrice)
        )
    },
        this.chips.map { Chip(it.label, it.attributes, it.isSelected) },
        this.refinements?.filters?.attributes.orEmpty().mapNotNull { buildRefinement(it) })
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
        "Color" -> toCanonicalRefinement<ColorRefinement>(attribute)(::ColorRefinement)
        "dsgnrs" -> toCanonicalRefinement<DesignerRefinement>(attribute)(::DesignerRefinement)
        "ctgr" -> toCanonicalRefinement<CategoryRefinement>(attribute)(::CategoryRefinement)
        else -> null
    }
}