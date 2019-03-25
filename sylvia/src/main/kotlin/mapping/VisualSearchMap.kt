package com.yoox.net.mapping

import com.yoox.net.models.inbound.VisualSearchAlternativeColor
import com.yoox.net.models.inbound.VisualSearchBrand
import com.yoox.net.models.inbound.VisualSearchColor
import com.yoox.net.models.inbound.VisualSearchItem
import com.yoox.net.models.inbound.VisualSearchItemImage
import com.yoox.net.models.inbound.VisualSearchPrice
import com.yoox.net.models.inbound.VisualSearchResultCategory
import com.yoox.net.models.outbound.Brand
import com.yoox.net.models.outbound.Category
import com.yoox.net.models.outbound.CategoryName
import com.yoox.net.models.outbound.Price
import com.yoox.net.models.outbound.SearchResultColor
import com.yoox.net.models.outbound.SearchResultItem
import com.yoox.net.models.outbound.VisualSearch
import com.yoox.net.models.inbound.VisualSearch as InboundVisualSearch

internal fun InboundVisualSearch.toOutboundVisualSearch(): VisualSearch =
    VisualSearch(products.map { it.toOutboundSearchResultItem() })

internal fun VisualSearchItem.toOutboundSearchResultItem(): SearchResultItem =
    SearchResultItem(
        brand = brand.toOutboundBrand(),
        category = categories.toOutboundCategory(),
        colors = (alternativeColours + colors.map { it.toVisualSearchAlternativeColor(boutiqueItemId) })
            .distinctBy { it.color.id }
            .map {
                it.toOutboundSearchResultColor(
                    itemImages.toImageURLTemplateList(
                        availableSizes.firstOrNull()?.sku.orEmpty()
                    )
                )
            },
        fullPrice = price.toOutboundFullPrice(),
        discountedPrice = price.toOutboundDiscountPrice(),
        previewImage = buildImageUrl(boutiqueItemId, PREVIEW_IMAGE_FORMAT),
        id = boutiqueItemId
    )

internal fun VisualSearchBrand.toOutboundBrand(): Brand =
    Brand(
        id = null,
        name = name
    )

internal fun List<VisualSearchResultCategory>.toOutboundCategory(): Category =
    this.firstOrNull().let {
        Category(
            name = CategoryName(it?.micro.orEmpty(), it?.micro.orEmpty()),
            parent = CategoryName(it?.macro.orEmpty(), it?.macro.orEmpty())
        )
    }

internal fun VisualSearchColor.toVisualSearchAlternativeColor(id: String): VisualSearchAlternativeColor =
    VisualSearchAlternativeColor(
        color = this, boutiqueItemId = id
    )

internal fun List<VisualSearchItemImage>.toImageURLTemplateList(size: String): List<String> =
    map {
        with(it.urlTemplate) {
            replace("{size}", size)
                .replaceBeforeExtension("")
        }
    }

internal fun VisualSearchAlternativeColor.toOutboundSearchResultColor(images: List<String>): SearchResultColor =
    SearchResultColor(
        id = color.id.toInt(),
        productId = boutiqueItemId,
        name = color.description,
        rgb = color.rgb,
        images = images.map { it.replaceBeforeExtension(boutiqueItemId) }
    )

internal fun VisualSearchPrice.toOutboundFullPrice(): Price =
    Price(
        amount = fullPrice,
        rawPrice = fullPrice.formatPrice(currency)
    )

internal fun VisualSearchPrice.toOutboundDiscountPrice(): Price =
    Price(
        amount = discount,
        rawPrice = discount.formatPrice(currency)
    )

private fun Float.formatPrice(currency: String): String =
    currency + " " + String.format("%.2f", this)

private fun String.replaceBeforeExtension(replacement: String): String =
    replaceRange(lastIndexOf("/") + 1, lastIndexOf("."), replacement)