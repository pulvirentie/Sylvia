package com.yoox.net.models.mapping

import com.yoox.net.models.outbound.*
import com.yoox.net.models.inbound.ColorSizeQty as InboundColorSizeQty
import com.yoox.net.models.inbound.Color as InboundColor
import com.yoox.net.models.inbound.Size as InboundSize
import com.yoox.net.models.inbound.Item as InboundItem

internal fun InboundItem.toOutboundItem(): Item {
    return Item(
        Department(this.department, this.parentDepartment),
        Brand(this.brandId, this.brand.name),
        Category(
            CategoryName(
                this.microCategoryAttribute.singleDescr,
                this.microCategoryAttribute.pluralDescr
            ),
            CategoryName(
                this.macroCategoryAttribute.singleDescr,
                this.macroCategoryAttribute.pluralDescr
            )
        ),
        this.imageUrls.normal.mapIndexed { index, it -> Image(it, this.imageUrls.zoom[index]) },
        this.composition.value,
        SaleLine(this.salesLineId, this.saleLine),
        this.colors.map {
            Color(
                it.colorId,
                it.code10,
                it.name,
                it.rgb,
                availableSizeEntries(it, this.sizes, this.colorSizeQty)
            )
        },
        Price(
            this.commonFormattedPrices.full.valueCent / 100f,
            this.formattedPrice.fullPrice
        ),
        Price(
            this.commonFormattedPrices.discounted.valueCent / 100f,
            this.formattedPrice.discountedPrice
        )
    )
}

private fun availableSizeEntries(
    source: InboundColor,
    sizeList: List<InboundSize>,
    availability: List<InboundColorSizeQty>
): List<Size> =
    availability.mapNotNull { s ->
        getSize(sizeList, s, source)?.let {
            Size(
                it.id,
                it.name,
                it.isoTwoLetterCountryCode,
                s.quantity
            )
        }
    }

private fun getSize(
    sizeList: List<InboundSize>,
    s: InboundColorSizeQty,
    source: InboundColor
): InboundSize? =
    sizeList.find { it.id == s.sizeId && source.colorCode == s.colorCode }
