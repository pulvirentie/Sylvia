package com.yoox.net.mapping

import com.yoox.net.models.outbound.Brand
import com.yoox.net.models.outbound.Category
import com.yoox.net.models.outbound.CategoryName
import com.yoox.net.models.outbound.Color
import com.yoox.net.models.outbound.Department
import com.yoox.net.models.outbound.Image
import com.yoox.net.models.outbound.Item
import com.yoox.net.models.outbound.Price
import com.yoox.net.models.outbound.SaleLine
import com.yoox.net.models.outbound.Size
import com.yoox.net.models.inbound.Color as InboundColor
import com.yoox.net.models.inbound.ColorSizeQty as InboundColorSizeQty
import com.yoox.net.models.inbound.Item as InboundItem
import com.yoox.net.models.inbound.Size as InboundSize

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
        this.composition.value,
        SaleLine(this.salesLineId, this.saleLine),
        this.colors.map {
            Color(
                it.colorId,
                it.code10,
                it.name,
                it.rgb,
                availableSizeEntries(it, this.sizes, this.colorSizeQty),
                this.imageUrls.normal.zip(this.imageUrls.zoom)
                    .map { zipped ->
                        Image(it.getImageUrl(zipped.first), it.getImageUrl(zipped.second))
                    }
            )
        },
        Price(
            this.commonFormattedPrices.full.valueCent / 100f,
            this.formattedPrice.fullPrice
        ),
        Price(
            this.commonFormattedPrices.discounted.valueCent / 100f,
            this.formattedPrice.discountedPrice
        ),
        imageUrls.soldout
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

private fun InboundColor.getImageUrl(url: String): String = url.replace("-COLOR-", this.colorCode)