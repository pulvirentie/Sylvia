package com.yoox.net.models.inbound

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Item(
    @SerialName("ImageUrls") val imageUrls: ImageUrls,
    @SerialName("ItemDescriptions") val itemDescriptions: ItemDescriptions,
    @SerialName("CommonFormattedPrices") val commonFormattedPrices: CommonFormattedPrices,
    @SerialName("Department") val department: String,
    @SerialName("ParentDepartment") val parentDepartment: String,
    @SerialName("Quantity") val quantity: Int,
    @SerialName("Brand") val brand: StringEnvelop,
    @SerialName("BrandId") val brandId: Int,
    @SerialName("Category") val category: StringEnvelop,
    @SerialName("MacroCategory") val macroCategory: String,
    @SerialName("MicroCategoryPlural") val microCategoryPlural: String,
    @SerialName("FormattedPrice") val formattedPrice: FormattedPrice,
    @SerialName("Colors") val colors: List<Color>,
    @SerialName("Sizes") val sizes: List<Size>,
    @SerialName("ColorSizeQty") val colorSizeQty: List<ColorSizeQty>,
    @SerialName("Season") val season: String,
    @SerialName("SaleLine") val saleLine: String,
    @SerialName("SalesLineId") val salesLineId: String,
    @SerialName("MicroCategoryAttribute") val microCategoryAttribute: CategoryAttribute,
    @SerialName("MacroCategoryAttribute") val macroCategoryAttribute: CategoryAttribute,
    @SerialName("Composition") val composition: Composition
)

@Serializable
internal data class ImageUrls(
    @SerialName("Normal") val normal: List<String>,
    @SerialName("Zoom") val zoom: List<String>
)

@Serializable
internal data class ItemDescriptions(@SerialName("ProductInfo") val productInfo: List<String>)

@Serializable
internal data class CommonFormattedPrices(
    @SerialName("Discounted") val discounted: Price,
    @SerialName("Full") val full: Price
)

@Serializable
internal data class Price(
    @SerialName("ValueCent") val valueCent: Int,
    @SerialName("Currency") val currency: String
)

@Serializable
internal data class FormattedPrice(
    @SerialName("FullPrice") val fullPrice: String,
    @SerialName("DiscountedPrice") val discountedPrice: String
)

@Serializable
internal data class StringEnvelop(@SerialName("Name") val name: String)

@Serializable
internal data class Color(
    @SerialName("ColorId") val colorId: Int,
    @SerialName("ColorCode") val colorCode: String,
    @SerialName("Code10") val code10: String,
    @SerialName("Name") val name: String,
    @SerialName("Rgb") val rgb: String
)

@Serializable
internal data class Size(
    @SerialName("Id") val id: Int,
    @SerialName("Name") val name: String,
    @SerialName("IsoTwoLetterCountryCode") val isoTwoLetterCountryCode: String,
    @SerialName("DefaultSizeLabel") val defaultSizeLabel: String,
    @SerialName("DefaultClassFamily") val defaultClassFamily: String,
    @SerialName("DefaultText") val defaultText: String,
    @SerialName("AlternativeSizeLabel") val alternativeSizeLabel: String,
    @SerialName("AlternativeClassFamily") val alternativeClassFamily: String,
    @SerialName("AlternativeText") val alternativeText: String
)

@Serializable
internal data class ColorSizeQty(
    @SerialName("ColorCode") val colorCode: String,
    @SerialName("SizeId") val sizeId: Int,
    @SerialName("Quantity") val quantity: Int
)

@Serializable
internal data class CategoryAttribute(
    @SerialName("Code") val code: String,
    @SerialName("SingleDescr") val singleDescr: String,
    @SerialName("PluralDescr") val pluralDescr: String
)

@Serializable
internal data class Composition(
    @SerialName("Name") val name: String,
    @SerialName("Value") val value: String
)