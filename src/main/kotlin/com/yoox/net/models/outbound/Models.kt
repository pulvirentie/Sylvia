package com.yoox.net.models.outbound

data class Item(
    val department: Department,
    val brand: Brand,
    val category: Category,
    val images: List<Image>,
    val composition: String,
    val saleLine: SaleLine,
    val colors: List<Color>,
    val fullPrice: Price,
    val discountedPrice: Price
)

fun Item.globalAvailability(): Int = this.colors.fold(0) { acc, next -> acc + next.globalAvailability() }

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
    val sizeList: List<Size>
)

fun Color.globalAvailability(): Int = this.sizeList.fold(0) { acc, next -> acc + next.availableUnits }

data class Size(
    val id: Int,
    val name: String,
    val family: String,
    val availableUnits: Int
)

data class Price(
    val amount: Float,
    val currency: String,
    val rawPrice: String
)