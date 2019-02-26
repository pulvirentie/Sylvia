package mapping

import com.yoox.net.models.inbound.ColorSizeQty
import com.yoox.net.models.outbound.*
import com.yoox.net.models.inbound.Color as InboundColor
import com.yoox.net.models.inbound.Size as InboundSize
import com.yoox.net.models.inbound.Item as InboundItem

internal fun InboundItem.toOutbound(): Item {
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
            this.commonFormattedPrices.full.currency,
            this.formattedPrice.fullPrice
        ),
        Price(
            this.commonFormattedPrices.discounted.valueCent / 100f,
            this.commonFormattedPrices.discounted.currency,
            this.formattedPrice.discountedPrice
        )
    )
}

private fun availableSizeEntries(
    source: InboundColor,
    sizeList: List<InboundSize>,
    availability: List<ColorSizeQty>
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
    s: ColorSizeQty,
    source: InboundColor
): InboundSize? = sizeList.find { it.id == s.sizeId && source.colorCode == s.colorCode }
