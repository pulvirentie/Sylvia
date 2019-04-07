package com.yoox.net.models.inbound

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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
    @SerialName("Zoom") val zoom: List<String>,
    @SerialName("Soldout") val soldout: String
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
    @SerialName("Url") val attributeUrl: AttributeUrl,
    @SerialName("SingleDescr") val singleDescr: String,
    @SerialName("PluralDescr") val pluralDescr: String
)

@Serializable
internal data class AttributeUrl(
    @SerialName("Path") val path: String,
    @SerialName("SeoFriendlyPathWithQueryString") val pathWithQuery: String,
    @SerialName("SeoFriendlyPathWithHash") val pathWithHash: String
)

@Serializable
internal data class Composition(
    @SerialName("Name") val name: String,
    @SerialName("Value") val value: String
)

@Serializable
internal data class SearchResults(
    @SerialName("SearchResultTitle") val searchResultTitle: String,
    @Optional @SerialName("Refinements") val refinements: Refinements? = null,
    @Optional @SerialName("Items") val items: List<SearchResultItem> = listOf(),
    @SerialName("Analytics") val analytics: Analytics
)

@Serializable
internal data class Refinements(
    @SerialName("Filters") val filters: Filters
)

@Serializable
internal data class Filters(
    @SerialName("Prices") val prices: Prices,
    @SerialName("Attributes") val attributes: List<Attribute>
)

@Serializable
internal data class Prices(
    @SerialName("Min") val min: Int,
    @SerialName("Max") val max: Int,
    @SerialName("PriceMin") val priceMin: Int,
    @SerialName("PriceMax") val priceMax: Int,
    @SerialName("Step") val step: Int
)

@Serializable
internal data class Attribute(
    @Optional @SerialName("Code") val code: String? = null,
    @Optional @SerialName("Type") val type: String? = null,
    @SerialName("IsSelected") val isSelected: Boolean,
    @Optional @SerialName("Value") val value: String? = null,
    @SerialName("Attributes") val attributes: List<Attribute>
)

@Serializable
internal data class SearchResultItem(
    @SerialName("Cod10") val cod10: String,
    @SerialName("Brand") val brand: String,
    @SerialName("BrandId") val brandId: Int,
    @SerialName("MicroCategoryCode") val microCategoryCode: String,
    @SerialName("MicroCategory") val microCategory: String,
    @SerialName("MicroCategoryPlural") val microCategoryPlural: String,
    @SerialName("MacroCategoryCode") val macroCategoryCode: String,
    @SerialName("MacroCategory") val macroCategory: String,
    @SerialName("MacroCategoryPlural") val macroCategoryPlural: String,
    @SerialName("FullPrice") val fullPrice: Int,
    @SerialName("FormattedFullPrice") val formattedFullPrice: String,
    @SerialName("DiscountedPrice") val discountedPrice: Int,
    @SerialName("FormattedDiscountedPrice") val formattedDiscountedPrice: String,
    @SerialName("Colors") val colors: List<SearchResultColor>
)

@Serializable
internal data class SearchResultColor(
    @SerialName("Id") val colorId: Int,
    @SerialName("Cod10") val cod10: String,
    @SerialName("Description") val description: String,
    @SerialName("Rgb") val rgb: String
)

@Serializable
internal data class Analytics(
    @SerialName("TypedTextSearch") val typedTextSearch: String,
    @SerialName("TotalPages") val totalPages: Int,
    @SerialName("ItemPerPage") val itemPerPage: Int,
    @SerialName("SelectedPage") val selectedPage: Int,
    @SerialName("TotalItems") val totalItems: Int
)

@Serializable
internal data class VisualSearchRequest(
    @SerialName("wegender") val gender: String,
    @SerialName("image") val image: String
) {
    @SerialName("language")
    val lang: String = "en"
}

@Serializable
internal data class VisualSearch(
    @SerialName("result") private val result: List<VisualSearchResult>
) {

    @Transient
    val products: List<VisualSearchItem>
        get() = result.flatMap(VisualSearchResult::categories).flatMap(VisualSearchCategory::products)
            .distinctBy { it.boutiqueItemId }
}

@Serializable
internal data class VisualSearchResult(
    @SerialName("categories") val categories: List<VisualSearchCategory>
)

@Serializable
internal data class VisualSearchCategory(
    @SerialName("products") val products: List<VisualSearchItem>
)

@Serializable
internal data class VisualSearchItem(
    @SerialName("gender") val gender: String,
    @SerialName("brand") val brand: VisualSearchBrand,
    @SerialName("price") val price: VisualSearchPrice,
    @SerialName("categories") val categories: List<VisualSearchResultCategory>,
    @SerialName("alternativeColours") val alternativeColours: List<VisualSearchAlternativeColor>,
    @SerialName("availableSizes") val availableSizes: List<VisualSearchSize>,
    @SerialName("description") val description: String,
    @SerialName("keywords") val keywords: List<String>,
    @SerialName("attributes") val attributes: List<VisualSearchAttribute>,
    @SerialName("colors") val colors: List<VisualSearchColor>,
    @SerialName("images") val images: VisualSearchImage,
    @SerialName("boutiqueItemId") val boutiqueItemId: String,
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("itemImages") val itemImages: List<VisualSearchItemImage>
)

@Serializable
internal data class VisualSearchBrand(
    @SerialName("name") val name: String,
    @SerialName("macroName") val macroName: String
)

@Serializable
internal data class VisualSearchPrice(
    @SerialName("currency") val currency: String,
    @SerialName("fullPrice") val fullPrice: Float,
    @SerialName("retailPrice") val retailPrice: Float,
    @SerialName("discount") val discount: Float
)

@Serializable
internal data class VisualSearchResultCategory(
    @SerialName("macro") val macro: String,
    @SerialName("micro") val micro: String
)

@Serializable
internal data class VisualSearchAttribute(
    @SerialName("key") val key: String,
    @SerialName("value") val value: String
)

@Serializable
internal data class VisualSearchColor(
    @SerialName("id") val id: String,
    @SerialName("rgb") val rgb: String,
    @SerialName("description") val description: String,
    @SerialName("mainColor") val mainColor: String
)

@Serializable
internal data class VisualSearchAlternativeColor(
    @SerialName("boutiqueItemId") val boutiqueItemId: String,
    @SerialName("color") val color: VisualSearchColor
)

@Serializable
internal data class VisualSearchSize(
    @SerialName("description") val description: String,
    @SerialName("sku") val sku: String,
    @SerialName("stockLevel") val stockLevel: String
)

@Serializable
internal data class VisualSearchImage(
    @SerialName("ratio") val ratio: Float,
    @SerialName("types") val types: List<String>
)

@Serializable
internal data class VisualSearchItemImage(
    @SerialName("format") val format: String,
    @SerialName("urlTemplate") val urlTemplate: String
)