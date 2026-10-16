package com.example.data.model

enum class OrderStatus(val displayName: String, val stepIndex: Int) {
    PENDING("Pending Approval", 0),
    APPROVED("Order Approved", 1),
    IN_PROGRESS("In Production", 2),
    READY_FOR_DELIVERY("Ready for Dispatch", 3),
    DELIVERED("Delivered", 4),
    CANCELLED("Cancelled", -1)
}

data class OrderItem(
    val productId: String,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double
) {
    val subtotal: Double get() = quantity * unitPrice
}

data class CustomerOrder(
    val id: String,
    val customerName: String,
    val customerEmail: String,
    val items: List<OrderItem>,
    val totalAmount: Double,
    val status: OrderStatus,
    val placedAtMillis: Long,
    val updatedAtMillis: Long,
    val notes: String = "",
    val shippingAddress: String = "",
    val attachments: List<AttachmentFile> = emptyList()
)
