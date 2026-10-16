package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BizRepository
import com.example.data.model.AttachmentFile
import com.example.data.model.CustomerOrder
import com.example.data.model.OrderStatus
import com.example.data.model.Product
import com.example.data.model.ServiceRequest
import com.example.data.model.ServiceStatus
import com.example.data.model.UserProfile
import com.example.util.FileHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class UserRole {
    CUSTOMER,
    ADMIN
}

enum class MainTab {
    CATALOG,
    SERVICES,
    TRACKING,
    PROFILE,
    ADMIN_DASHBOARD
}

data class DocumentViewerState(
    val isOpen: Boolean = false,
    val attachment: AttachmentFile? = null,
    val pdfBitmaps: List<android.graphics.Bitmap> = emptyList(),
    val jpgBitmap: android.graphics.Bitmap? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class BizViewModel(application: Application) : AndroidViewModel(application) {

    val repository = BizRepository(application.applicationContext)

    val products: StateFlow<List<Product>> = repository.products
    val orders: StateFlow<List<CustomerOrder>> = repository.orders
    val serviceRequests: StateFlow<List<ServiceRequest>> = repository.serviceRequests
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val cartItems: StateFlow<Map<String, Int>> = repository.cartItems

    private val _currentRole = MutableStateFlow(UserRole.CUSTOMER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _selectedTab = MutableStateFlow(MainTab.CATALOG)
    val selectedTab: StateFlow<MainTab> = _selectedTab.asStateFlow()

    private val _documentViewerState = MutableStateFlow(DocumentViewerState())
    val documentViewerState: StateFlow<DocumentViewerState> = _documentViewerState.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    fun switchRole(role: UserRole) {
        _currentRole.value = role
        if (role == UserRole.ADMIN) {
            _selectedTab.value = MainTab.ADMIN_DASHBOARD
        } else if (_selectedTab.value == MainTab.ADMIN_DASHBOARD) {
            _selectedTab.value = MainTab.CATALOG
        }
    }

    fun selectTab(tab: MainTab) {
        _selectedTab.value = tab
    }

    fun showMessage(message: String) {
        _userMessage.value = message
    }

    fun clearMessage() {
        _userMessage.value = null
    }

    // --- Cart & Order Operations ---

    fun addToCart(productId: String) {
        repository.addToCart(productId, 1)
        showMessage("Item added to cart")
    }

    fun updateCartItemQuantity(productId: String, count: Int) {
        repository.updateCartItemQuantity(productId, count)
    }

    fun clearCart() {
        repository.clearCart()
    }

    fun placeOrder(notes: String, shippingAddress: String) {
        val newOrder = repository.placeOrder(notes, shippingAddress)
        showMessage("Order ${newOrder.id} placed successfully!")
        _selectedTab.value = MainTab.TRACKING
    }

    fun submitServiceRequest(
        serviceType: String,
        requirements: String,
        urgency: String,
        budget: Double
    ) {
        val request = repository.submitServiceRequest(serviceType, requirements, urgency, budget)
        showMessage("Service Request ${request.id} submitted for review!")
        _selectedTab.value = MainTab.TRACKING
    }

    // --- Admin Operations ---

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus, notes: String = "") {
        repository.updateOrderStatus(orderId, newStatus, notes)
        showMessage("Order $orderId status updated to ${newStatus.displayName}")
    }

    fun updateServiceStatus(serviceId: String, newStatus: ServiceStatus, notes: String = "") {
        repository.updateServiceStatus(serviceId, newStatus, notes)
        showMessage("Service $serviceId status updated to ${newStatus.displayName}")
    }

    fun uploadOrderInvoicePdf(orderId: String, adminNotes: String) {
        val file = repository.attachOrderPdfInvoice(orderId, adminNotes)
        if (file != null) {
            showMessage("PDF Invoice ${file.fileName} attached to $orderId")
        }
    }

    fun uploadOrderJpgProof(orderId: String, title: String, notes: String) {
        val file = repository.attachOrderJpgProof(orderId, title, notes)
        if (file != null) {
            showMessage("JPG deliverable attached to $orderId")
        }
    }

    fun uploadServicePdfReport(serviceId: String, adminNotes: String) {
        val file = repository.attachServicePdfReport(serviceId, adminNotes)
        if (file != null) {
            showMessage("Service PDF report attached to $serviceId")
        }
    }

    fun uploadServiceJpgProof(serviceId: String, title: String, notes: String) {
        val file = repository.attachServiceJpgProof(serviceId, title, notes)
        if (file != null) {
            showMessage("JPG certificate attached to $serviceId")
        }
    }

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
        repository.addProduct(name, category, sku, description, price, stock, minThreshold, unit)
        showMessage("Product added to inventory")
    }

    fun updateProductStock(productId: String, stock: Int) {
        repository.updateProductStock(productId, stock)
    }

    fun updateProductDetails(
        productId: String,
        name: String,
        category: String,
        price: Double,
        stock: Int,
        minThreshold: Int
    ) {
        repository.updateProductDetails(productId, name, category, price, stock, minThreshold)
        showMessage("Product updated")
    }

    fun deleteProduct(productId: String) {
        repository.deleteProduct(productId)
        showMessage("Product removed")
    }

    fun updateProfile(profile: UserProfile) {
        repository.updateProfile(profile)
        showMessage("Profile updated")
    }

    // --- Document Viewer ---

    fun openDocument(attachment: AttachmentFile) {
        _documentViewerState.value = DocumentViewerState(
            isOpen = true,
            attachment = attachment,
            isLoading = true
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (attachment.fileType.equals("PDF", ignoreCase = true)) {
                    val bitmaps = FileHelper.renderPdfToBitmaps(getApplication(), attachment.localFilePath)
                    _documentViewerState.value = _documentViewerState.value.copy(
                        pdfBitmaps = bitmaps,
                        isLoading = false
                    )
                } else {
                    val bitmap = FileHelper.loadJpgBitmap(attachment.localFilePath)
                    _documentViewerState.value = _documentViewerState.value.copy(
                        jpgBitmap = bitmap,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _documentViewerState.value = _documentViewerState.value.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage
                )
            }
        }
    }

    fun closeDocumentViewer() {
        _documentViewerState.value = DocumentViewerState()
    }
}
