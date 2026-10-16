package com.example.data.local

import android.content.Context
import com.example.data.model.AttachmentFile
import com.example.data.model.CustomerOrder
import com.example.data.model.OrderItem
import com.example.data.model.OrderStatus
import com.example.data.model.Product
import com.example.data.model.ServiceRequest
import com.example.data.model.ServiceStatus
import com.example.data.model.UserProfile
import com.example.util.DocumentGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class BizRepository(private val context: Context) {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _orders = MutableStateFlow<List<CustomerOrder>>(emptyList())
    val orders: StateFlow<List<CustomerOrder>> = _orders.asStateFlow()

    private val _serviceRequests = MutableStateFlow<List<ServiceRequest>>(emptyList())
    val serviceRequests: StateFlow<List<ServiceRequest>> = _serviceRequests.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _cartItems = MutableStateFlow<Map<String, Int>>(emptyMap())
    val cartItems: StateFlow<Map<String, Int>> = _cartItems.asStateFlow()

    init {
        seedInitialData()
    }

    private fun seedInitialData() {
        // Seed Products
        val initialProducts = listOf(
            Product(
                id = "PROD-101",
                name = "Apex Industrial Servo Motor X4",
                category = "Automation",
                sku = "IND-SRV-04",
                description = "High-torque precision brushless servo motor with integrated 18-bit absolute encoder.",
                price = 450.00,
                stockQuantity = 18,
                minStockThreshold = 5,
                unit = "units",
                rating = 4.9f
            ),
            Product(
                id = "PROD-102",
                name = "OptiCore Gigabit Fiber Transceiver",
                category = "IT & Cloud",
                sku = "NET-FBR-10G",
                description = "10G SFP+ single-mode optical transceiver module with 10km transmission range.",
                price = 120.00,
                stockQuantity = 4, // Low stock demo
                minStockThreshold = 6,
                unit = "pcs",
                rating = 4.8f
            ),
            Product(
                id = "PROD-103",
                name = "Titanium CNC End Mill Tooling Kit",
                category = "Tooling",
                sku = "MCH-MILL-TI",
                description = "4-flute carbide end mills with nano-composite coating for aerospace alloy machining.",
                price = 280.00,
                stockQuantity = 25,
                minStockThreshold = 8,
                unit = "kits",
                rating = 5.0f
            ),
            Product(
                id = "PROD-104",
                name = "Thermal Dissipation Heat Sink Array",
                category = "Hardware",
                sku = "THM-SNK-200",
                description = "Anodized aluminum vapor-chamber heat sink designed for heavy power inverter stacks.",
                price = 85.50,
                stockQuantity = 0, // Out of stock demo
                minStockThreshold = 10,
                unit = "pcs",
                rating = 4.7f
            ),
            Product(
                id = "PROD-105",
                name = "SecureCore Embedded IoT Gateway",
                category = "IT & Cloud",
                sku = "IOT-GTW-X2",
                description = "DIN-rail industrial edge computing gateway with dual LTE failover and TPM 2.0 security.",
                price = 620.00,
                stockQuantity = 12,
                minStockThreshold = 4,
                unit = "units",
                rating = 4.9f
            ),
            Product(
                id = "PROD-106",
                name = "UltraPure Electrostatic Filter Pack",
                category = "Consumables",
                sku = "FLT-IND-01",
                description = "HEPA H14 certified industrial air purification cartridge for cleanroom environments.",
                price = 65.00,
                stockQuantity = 40,
                minStockThreshold = 15,
                unit = "packs",
                rating = 4.6f
            )
        )
        _products.value = initialProducts

        // Pre-create initial Orders with attached real PDF & JPG files
        val order1Id = "ORD-8921"
        val order1Placed = System.currentTimeMillis() - 86400000L * 2
        val order1 = CustomerOrder(
            id = order1Id,
            customerName = "Alex Chen",
            customerEmail = "alex.chen@nexuscorp.com",
            items = listOf(
                OrderItem("PROD-101", "Apex Industrial Servo Motor X4", 2, 450.00),
                OrderItem("PROD-103", "Titanium CNC End Mill Tooling Kit", 1, 280.00)
            ),
            totalAmount = 1180.00,
            status = OrderStatus.DELIVERED,
            placedAtMillis = order1Placed,
            updatedAtMillis = System.currentTimeMillis() - 3600000L * 4,
            notes = "Please include official calibration certificate and expedited dock delivery.",
            shippingAddress = "Building 4, Dock B, 750 Enterprise Blvd, CA"
        )
        // Generate real PDF invoice & JPG proof for order1
        val order1InvoicePdf = DocumentGenerator.generateOrderPdfInvoice(
            context,
            order1,
            adminNotes = "Order dispatched and signed off by logistics team."
        )
        val order1ProofJpg = DocumentGenerator.generateJpgDeliverableProof(
            context,
            title = "Final Quality & Delivery Proof",
            entityId = order1Id,
            clientName = "Alex Chen",
            notes = "Delivered & Inspected at Dock B"
        )
        val fullOrder1 = order1.copy(attachments = listOf(order1InvoicePdf, order1ProofJpg))

        val order2Id = "ORD-9104"
        val order2 = CustomerOrder(
            id = order2Id,
            customerName = "Alex Chen",
            customerEmail = "alex.chen@nexuscorp.com",
            items = listOf(
                OrderItem("PROD-105", "SecureCore Embedded IoT Gateway", 1, 620.00)
            ),
            totalAmount = 620.00,
            status = OrderStatus.IN_PROGRESS,
            placedAtMillis = System.currentTimeMillis() - 3600000L * 12,
            updatedAtMillis = System.currentTimeMillis() - 3600000L * 2,
            notes = "Firmware v3.4 pre-configuration requested.",
            shippingAddress = "Building 4, Dock B, 750 Enterprise Blvd, CA"
        )
        val order2InvoicePdf = DocumentGenerator.generateOrderPdfInvoice(
            context,
            order2,
            adminNotes = "Under configuration by engineering lab."
        )
        val fullOrder2 = order2.copy(attachments = listOf(order2InvoicePdf))

        _orders.value = listOf(fullOrder2, fullOrder1)

        // Pre-create initial Service Requests with attached real files
        val srv1Id = "SRV-5021"
        val srv1Submitted = System.currentTimeMillis() - 86400000L * 3
        val srv1 = ServiceRequest(
            id = srv1Id,
            customerName = "Alex Chen",
            customerEmail = "alex.chen@nexuscorp.com",
            serviceType = "Custom Precision CNC Fabrication",
            requirementsDescription = "Fabrication of 4 custom anodized mounting brackets as per technical drawing rev B. Tolerance +/- 0.02mm, 6061-T6 aluminum.",
            urgency = "High",
            budgetEstimate = 1450.00,
            status = ServiceStatus.COMPLETED,
            submittedAtMillis = srv1Submitted,
            updatedAtMillis = System.currentTimeMillis() - 3600000L * 6,
            adminNotes = "Dimensional tolerances laser-verified. Anodizing pass rate 100%."
        )
        val srv1Pdf = DocumentGenerator.generateServicePdfReport(
            context,
            srv1,
            adminNotes = "All 4 units passed CMM coordinate measurement inspection."
        )
        val srv1Jpg = DocumentGenerator.generateJpgDeliverableProof(
            context,
            title = "CMM Quality Certification",
            entityId = srv1Id,
            clientName = "Alex Chen",
            notes = "Tolerance +/- 0.015mm confirmed."
        )
        val fullSrv1 = srv1.copy(attachments = listOf(srv1Pdf, srv1Jpg))

        val srv2Id = "SRV-5490"
        val srv2 = ServiceRequest(
            id = srv2Id,
            customerName = "Alex Chen",
            customerEmail = "alex.chen@nexuscorp.com",
            serviceType = "Enterprise IT Architecture & Security Audit",
            requirementsDescription = "Security vulnerability audit and performance stress test for our distributed IoT edge broker cluster.",
            urgency = "Normal",
            budgetEstimate = 2200.00,
            status = ServiceStatus.APPROVED,
            submittedAtMillis = System.currentTimeMillis() - 3600000L * 18,
            updatedAtMillis = System.currentTimeMillis() - 3600000L * 5,
            adminNotes = "Scheduled for remote testing window this Thursday."
        )
        val srv2Pdf = DocumentGenerator.generateServicePdfReport(
            context,
            srv2,
            adminNotes = "Audit scope defined and assigned to cybersecurity team."
        )
        val fullSrv2 = srv2.copy(attachments = listOf(srv2Pdf))

        _serviceRequests.value = listOf(fullSrv2, fullSrv1)
    }

    // --- Cart & Ordering ---

    fun addToCart(productId: String, count: Int = 1) {
        val current = _cartItems.value.toMutableMap()
        val currentCount = current[productId] ?: 0
        current[productId] = currentCount + count
        _cartItems.value = current
    }

    fun updateCartItemQuantity(productId: String, count: Int) {
        val current = _cartItems.value.toMutableMap()
        if (count <= 0) {
            current.remove(productId)
        } else {
            current[productId] = count
        }
        _cartItems.value = current
    }

    fun clearCart() {
        _cartItems.value = emptyMap()
    }

    fun placeOrder(notes: String, shippingAddress: String): CustomerOrder {
        val currentCart = _cartItems.value
        val allProducts = _products.value.associateBy { it.id }

        val items = currentCart.mapNotNull { (productId, qty) ->
            allProducts[productId]?.let { prod ->
                OrderItem(
                    productId = prod.id,
                    productName = prod.name,
                    quantity = qty,
                    unitPrice = prod.price
                )
            }
        }

        val total = items.sumOf { it.subtotal }
        val orderId = "ORD-" + ((1000..9999).random())
        val profile = _userProfile.value

        val newOrder = CustomerOrder(
            id = orderId,
            customerName = profile.name,
            customerEmail = profile.email,
            items = items,
            totalAmount = total,
            status = OrderStatus.PENDING,
            placedAtMillis = System.currentTimeMillis(),
            updatedAtMillis = System.currentTimeMillis(),
            notes = notes,
            shippingAddress = if (shippingAddress.isNotBlank()) shippingAddress else profile.shippingAddress,
            attachments = emptyList()
        )

        // Decrement product stock
        val updatedProducts = _products.value.map { prod ->
            val orderedQty = currentCart[prod.id] ?: 0
            if (orderedQty > 0) {
                prod.copy(stockQuantity = (prod.stockQuantity - orderedQty).coerceAtLeast(0))
            } else prod
        }
        _products.value = updatedProducts

        _orders.value = listOf(newOrder) + _orders.value
        clearCart()
        return newOrder
    }

    // --- Custom Service Request ---

    fun submitServiceRequest(
        serviceType: String,
        requirements: String,
        urgency: String,
        budget: Double
    ): ServiceRequest {
        val requestId = "SRV-" + ((1000..9999).random())
        val profile = _userProfile.value

        val newRequest = ServiceRequest(
            id = requestId,
            customerName = profile.name,
            customerEmail = profile.email,
            serviceType = serviceType,
            requirementsDescription = requirements,
            urgency = urgency,
            budgetEstimate = budget,
            status = ServiceStatus.SUBMITTED,
            submittedAtMillis = System.currentTimeMillis(),
            updatedAtMillis = System.currentTimeMillis(),
            adminNotes = "New request queued for admin assessment.",
            attachments = emptyList()
        )

        _serviceRequests.value = listOf(newRequest) + _serviceRequests.value
        return newRequest
    }

    // --- Admin Operations ---

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus, adminNotes: String = "") {
        _orders.value = _orders.value.map { order ->
            if (order.id == orderId) {
                val updatedNotes = if (adminNotes.isNotBlank()) adminNotes else order.notes
                order.copy(
                    status = newStatus,
                    updatedAtMillis = System.currentTimeMillis(),
                    notes = updatedNotes
                )
            } else order
        }
    }

    fun updateServiceStatus(serviceId: String, newStatus: ServiceStatus, adminNotes: String = "") {
        _serviceRequests.value = _serviceRequests.value.map { srv ->
            if (srv.id == serviceId) {
                srv.copy(
                    status = newStatus,
                    updatedAtMillis = System.currentTimeMillis(),
                    adminNotes = if (adminNotes.isNotBlank()) adminNotes else srv.adminNotes
                )
            } else srv
        }
    }

    /**
     * Admin uploads a PDF invoice for an order.
     */
    fun attachOrderPdfInvoice(orderId: String, adminNotes: String): AttachmentFile? {
        val order = _orders.value.find { it.id == orderId } ?: return null
        val attachment = DocumentGenerator.generateOrderPdfInvoice(context, order, adminNotes)
        _orders.value = _orders.value.map {
            if (it.id == orderId) {
                it.copy(attachments = listOf(attachment) + it.attachments)
            } else it
        }
        return attachment
    }

    /**
     * Admin uploads a JPG deliverable proof for an order.
     */
    fun attachOrderJpgProof(orderId: String, title: String, notes: String): AttachmentFile? {
        val order = _orders.value.find { it.id == orderId } ?: return null
        val attachment = DocumentGenerator.generateJpgDeliverableProof(
            context = context,
            title = title,
            entityId = order.id,
            clientName = order.customerName,
            notes = notes
        )
        _orders.value = _orders.value.map {
            if (it.id == orderId) {
                it.copy(attachments = listOf(attachment) + it.attachments)
            } else it
        }
        return attachment
    }

    /**
     * Admin uploads a PDF report for a service request.
     */
    fun attachServicePdfReport(serviceId: String, adminNotes: String): AttachmentFile? {
        val service = _serviceRequests.value.find { it.id == serviceId } ?: return null
        val attachment = DocumentGenerator.generateServicePdfReport(context, service, adminNotes)
        _serviceRequests.value = _serviceRequests.value.map {
            if (it.id == serviceId) {
                it.copy(attachments = listOf(attachment) + it.attachments)
            } else it
        }
        return attachment
    }

    /**
     * Admin uploads a JPG deliverable proof for a service request.
     */
    fun attachServiceJpgProof(serviceId: String, title: String, notes: String): AttachmentFile? {
        val service = _serviceRequests.value.find { it.id == serviceId } ?: return null
        val attachment = DocumentGenerator.generateJpgDeliverableProof(
            context = context,
            title = title,
            entityId = service.id,
            clientName = service.customerName,
            notes = notes
        )
        _serviceRequests.value = _serviceRequests.value.map {
            if (it.id == serviceId) {
                it.copy(attachments = listOf(attachment) + it.attachments)
            } else it
        }
        return attachment
    }

    // --- Inventory Management ---

    fun addProduct(
        name: String,
        category: String,
        sku: String,
        description: String,
        price: Double,
        stock: Int,
        minThreshold: Int,
        unit: String
    ) {
        val newProduct = Product(
            id = "PROD-" + ((100..999).random()),
            name = name,
            category = category,
            sku = sku,
            description = description,
            price = price,
            stockQuantity = stock,
            minStockThreshold = minThreshold,
            unit = unit
        )
        _products.value = listOf(newProduct) + _products.value
    }

    fun updateProductStock(productId: String, newStock: Int) {
        _products.value = _products.value.map {
            if (it.id == productId) it.copy(stockQuantity = newStock.coerceAtLeast(0)) else it
        }
    }

    fun updateProductDetails(
        productId: String,
        name: String,
        category: String,
        price: Double,
        stock: Int,
        minThreshold: Int
    ) {
        _products.value = _products.value.map {
            if (it.id == productId) {
                it.copy(
                    name = name,
                    category = category,
                    price = price,
                    stockQuantity = stock,
                    minStockThreshold = minThreshold
                )
            } else it
        }
    }

    fun deleteProduct(productId: String) {
        _products.value = _products.value.filter { it.id != productId }
    }

    // --- Profile ---

    fun updateProfile(profile: UserProfile) {
        _userProfile.value = profile
    }

    // Aggregated list of all user attachments across orders & service requests
    fun getAllCustomerAttachments(): List<AttachmentFile> {
        val orderFiles = _orders.value.flatMap { it.attachments }
        val serviceFiles = _serviceRequests.value.flatMap { it.attachments }
        return (orderFiles + serviceFiles).sortedByDescending { it.uploadedAtMillis }
    }
}
