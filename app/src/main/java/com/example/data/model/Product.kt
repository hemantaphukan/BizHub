package com.example.data.model

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val sku: String,
    val description: String,
    val price: Double,
    val stockQuantity: Int,
    val minStockThreshold: Int = 5,
    val unit: String = "pcs",
    val rating: Float = 4.8f
) {
    val isLowStock: Boolean get() = stockQuantity in 1..minStockThreshold
    val isOutOfStock: Boolean get() = stockQuantity <= 0
}
