package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomerOrder
import com.example.data.model.OrderStatus
import com.example.data.model.Product
import com.example.data.model.ServiceRequest
import com.example.data.model.ServiceStatus
import com.example.ui.BizViewModel
import com.example.ui.components.OrderStatusBadge
import com.example.ui.components.ServiceStatusBadge
import com.example.ui.theme.BizPrimary
import com.example.ui.theme.BizSecondary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    products: List<Product>,
    orders: List<CustomerOrder>,
    serviceRequests: List<ServiceRequest>,
    viewModel: BizViewModel,
    modifier: Modifier = Modifier
) {
    var adminSubTab by remember { mutableIntStateOf(0) } // 0 = Inventory, 1 = Orders, 2 = Services

    // Dialog & Sheet States
    var showAddProductSheet by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<Product?>(null) }
    var uploadFileDialogTarget by remember { mutableStateOf<UploadTarget?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Admin Dashboard Top Metrics Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFDC2626)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = "Admin",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Admin Management Console",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF1E293B))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "LIVE BACKEND",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AdminMetricBox(
                        title = "Pending Orders",
                        value = "${orders.count { it.status == OrderStatus.PENDING }}",
                        color = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )
                    AdminMetricBox(
                        title = "Service Reviews",
                        value = "${serviceRequests.count { it.status == ServiceStatus.SUBMITTED || it.status == ServiceStatus.UNDER_REVIEW }}",
                        color = Color(0xFF38BDF8),
                        modifier = Modifier.weight(1f)
                    )
                    AdminMetricBox(
                        title = "Low Stock Alerts",
                        value = "${products.count { it.isLowStock || it.isOutOfStock }}",
                        color = Color(0xFFEF4444),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Sub Tabs: Inventory vs Orders vs Services
        TabRow(
            selectedTabIndex = adminSubTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = BizPrimary
        ) {
            Tab(
                selected = adminSubTab == 0,
                onClick = { adminSubTab = 0 },
                text = { Text("Inventory (${products.size})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                icon = { Icon(Icons.Default.Inventory, contentDescription = null, modifier = Modifier.size(16.dp)) },
                modifier = Modifier.testTag("tab_admin_inventory")
            )
            Tab(
                selected = adminSubTab == 1,
                onClick = { adminSubTab = 1 },
                text = { Text("Orders (${orders.size})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                icon = { Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(16.dp)) },
                modifier = Modifier.testTag("tab_admin_orders")
            )
            Tab(
                selected = adminSubTab == 2,
                onClick = { adminSubTab = 2 },
                text = { Text("Services (${serviceRequests.size})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                icon = { Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(16.dp)) },
                modifier = Modifier.testTag("tab_admin_services")
            )
        }

        // Tab Content
        Box(modifier = Modifier.weight(1f)) {
            when (adminSubTab) {
                0 -> AdminInventoryView(
                    products = products,
                    onAddNewProduct = { showAddProductSheet = true },
                    onEditProduct = { editingProduct = it },
                    onAdjustStock = { id, newStock -> viewModel.updateProductStock(id, newStock) },
                    onDeleteProduct = { id -> viewModel.deleteProduct(id) }
                )
                1 -> AdminOrdersView(
                    orders = orders,
                    onUpdateStatus = { id, status, notes -> viewModel.updateOrderStatus(id, status, notes) },
                    onUploadFile = { order ->
                        uploadFileDialogTarget = UploadTarget.ForOrder(order)
                    },
                    onViewDocument = { attachment -> viewModel.openDocument(attachment) }
                )
                2 -> AdminServicesView(
                    serviceRequests = serviceRequests,
                    onUpdateStatus = { id, status, notes -> viewModel.updateServiceStatus(id, status, notes) },
                    onUploadFile = { service ->
                        uploadFileDialogTarget = UploadTarget.ForService(service)
                    },
                    onViewDocument = { attachment -> viewModel.openDocument(attachment) }
                )
            }
        }
    }

    // Modal Sheet for Adding New Product
    if (showAddProductSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showAddProductSheet = false },
            sheetState = sheetState
        ) {
            ProductFormSheet(
                productToEdit = null,
                onSave = { name, category, sku, desc, price, stock, minThresh, unit ->
                    viewModel.addProduct(name, category, sku, desc, price, stock, minThresh, unit)
                    showAddProductSheet = false
                },
                onCancel = { showAddProductSheet = false }
            )
        }
    }

    // Modal Sheet for Editing Existing Product
    editingProduct?.let { prod ->
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { editingProduct = null },
            sheetState = sheetState
        ) {
            ProductFormSheet(
                productToEdit = prod,
                onSave = { name, category, _, _, price, stock, minThresh, _ ->
                    viewModel.updateProductDetails(prod.id, name, category, price, stock, minThresh)
                    editingProduct = null
                },
                onCancel = { editingProduct = null }
            )
        }
    }

    // Admin File Upload Dialog (PDF Invoice / Work Order Report / JPG Deliverable Proof)
    uploadFileDialogTarget?.let { target ->
        AdminUploadFileDialog(
            target = target,
            onDismiss = { uploadFileDialogTarget = null },
            onUploadPdf = { entityId, isOrder, notes ->
                if (isOrder) {
                    viewModel.uploadOrderInvoicePdf(entityId, notes)
                } else {
                    viewModel.uploadServicePdfReport(entityId, notes)
                }
                uploadFileDialogTarget = null
            },
            onUploadJpg = { entityId, isOrder, title, notes ->
                if (isOrder) {
                    viewModel.uploadOrderJpgProof(entityId, title, notes)
                } else {
                    viewModel.uploadServiceJpgProof(entityId, title, notes)
                }
                uploadFileDialogTarget = null
            }
        )
    }
}

sealed class UploadTarget {
    data class ForOrder(val order: CustomerOrder) : UploadTarget()
    data class ForService(val service: ServiceRequest) : UploadTarget()
}

@Composable
fun AdminMetricBox(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF1E293B))
            .padding(8.dp)
    ) {
        Column {
            Text(text = title, fontSize = 9.sp, color = Color(0xFF94A3B8))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

// ------------------- SUBTAB 1: INVENTORY MANAGEMENT -------------------

@Composable
fun AdminInventoryView(
    products: List<Product>,
    onAddNewProduct: () -> Unit,
    onEditProduct: (Product) -> Unit,
    onAdjustStock: (String, Int) -> Unit,
    onDeleteProduct: (String) -> Unit
) {
    var stockFilter by remember { mutableStateOf("All") }

    val filtered = products.filter {
        when (stockFilter) {
            "Low Stock" -> it.isLowStock
            "Out of Stock" -> it.isOutOfStock
            else -> true
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("All", "Low Stock", "Out of Stock").forEach { filter ->
                    FilterChip(
                        selected = stockFilter == filter,
                        onClick = { stockFilter = filter },
                        label = { Text(filter, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BizPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Button(
                onClick = onAddNewProduct,
                colors = ButtonDefaults.buttonColors(containerColor = BizPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("btn_admin_add_product")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Item", fontSize = 12.sp)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filtered, key = { it.id }) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = product.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${product.category} • SKU: ${product.sku} • Price: $${product.price}",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { onEditProduct(product) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = BizPrimary, modifier = Modifier.size(18.dp))
                                }
                                IconButton(onClick = { onDeleteProduct(product.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Stock control row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF8FAFC))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Current Stock: ",
                                    fontSize = 12.sp,
                                    color = Color(0xFF475569)
                                )
                                Text(
                                    text = "${product.stockQuantity} ${product.unit}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (product.isOutOfStock) Color(0xFFDC2626) else if (product.isLowStock) Color(0xFFD97706) else Color(0xFF059669)
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedButton(
                                    onClick = { onAdjustStock(product.id, product.stockQuantity - 1) },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("-1", fontSize = 11.sp)
                                }
                                OutlinedButton(
                                    onClick = { onAdjustStock(product.id, product.stockQuantity + 5) },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("+5", fontSize = 11.sp)
                                }
                                Button(
                                    onClick = { onAdjustStock(product.id, product.stockQuantity + 20) },
                                    colors = ButtonDefaults.buttonColors(containerColor = BizPrimary),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("+20 Stock", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ------------------- SUBTAB 2: ORDERS MANAGEMENT & FILE UPLOAD -------------------

@Composable
fun AdminOrdersView(
    orders: List<CustomerOrder>,
    onUpdateStatus: (id: String, status: OrderStatus, notes: String) -> Unit,
    onUploadFile: (CustomerOrder) -> Unit,
    onViewDocument: (com.example.data.model.AttachmentFile) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(orders, key = { it.id }) { order ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_order_card_${order.id}"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${order.id} • ${order.customerName}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Value: $${order.totalAmount} • Destination: ${order.shippingAddress}",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                        OrderStatusBadge(status = order.status)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Items: " + order.items.joinToString(", ") { "${it.productName} (x${it.quantity})" },
                        fontSize = 11.sp,
                        color = Color(0xFF334155)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Status Workflow Transitions for Admin
                    Text(
                        text = "Advance Order Workflow:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (order.status == OrderStatus.PENDING) {
                            Button(
                                onClick = { onUpdateStatus(order.id, OrderStatus.APPROVED, "Approved by Admin") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_admin_approve_order_${order.id}")
                            ) {
                                Text("Approve Order", fontSize = 11.sp)
                            }
                        } else if (order.status == OrderStatus.APPROVED) {
                            Button(
                                onClick = { onUpdateStatus(order.id, OrderStatus.IN_PROGRESS, "Production started") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB45309)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Mark In Production", fontSize = 11.sp)
                            }
                        } else if (order.status == OrderStatus.IN_PROGRESS) {
                            Button(
                                onClick = { onUpdateStatus(order.id, OrderStatus.READY_FOR_DELIVERY, "Packaged and ready") },
                                colors = ButtonDefaults.buttonColors(containerColor = BizPrimary),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Ready for Dispatch", fontSize = 11.sp)
                            }
                        } else if (order.status == OrderStatus.READY_FOR_DELIVERY) {
                            Button(
                                onClick = { onUpdateStatus(order.id, OrderStatus.DELIVERED, "Delivered to client") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4338CA)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Mark Delivered", fontSize = 11.sp)
                            }
                        } else {
                            OutlinedButton(
                                onClick = { onUpdateStatus(order.id, OrderStatus.PENDING, "Reset") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Reset Workflow", fontSize = 11.sp)
                            }
                        }

                        // Upload / Deliver File button
                        Button(
                            onClick = { onUploadFile(order) },
                            colors = ButtonDefaults.buttonColors(containerColor = BizSecondary),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_admin_upload_order_file_${order.id}")
                        ) {
                            Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Upload File", fontSize = 11.sp)
                        }
                    }

                    // Show attached files for this order
                    if (order.attachments.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Attached Client Files (${order.attachments.size}):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BizPrimary
                        )
                        order.attachments.forEach { file ->
                            AttachmentItemRow(
                                file = file,
                                onView = { onViewDocument(file) },
                                onDownload = { /* handle download */ }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ------------------- SUBTAB 3: SERVICES MANAGEMENT & DELIVERABLES -------------------

@Composable
fun AdminServicesView(
    serviceRequests: List<ServiceRequest>,
    onUpdateStatus: (id: String, status: ServiceStatus, notes: String) -> Unit,
    onUploadFile: (ServiceRequest) -> Unit,
    onViewDocument: (com.example.data.model.AttachmentFile) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(serviceRequests, key = { it.id }) { service ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_service_card_${service.id}"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${service.id} • ${service.serviceType}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Client: ${service.customerName} • Urgency: ${service.urgency} • Est: $${service.budgetEstimate}",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                        ServiceStatusBadge(status = service.status)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Requirements: ${service.requirementsDescription}",
                        fontSize = 11.sp,
                        color = Color(0xFF334155),
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Service Lifecycle Actions:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        when (service.status) {
                            ServiceStatus.SUBMITTED -> {
                                Button(
                                    onClick = { onUpdateStatus(service.id, ServiceStatus.UNDER_REVIEW, "Under engineering assessment") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF854D0E)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Mark Under Review", fontSize = 11.sp)
                                }
                            }
                            ServiceStatus.UNDER_REVIEW -> {
                                Button(
                                    onClick = { onUpdateStatus(service.id, ServiceStatus.APPROVED, "Specifications validated") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Approve & Schedule", fontSize = 11.sp)
                                }
                            }
                            ServiceStatus.APPROVED -> {
                                Button(
                                    onClick = { onUpdateStatus(service.id, ServiceStatus.IN_EXECUTION, "Fabrication / testing in progress") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB45309)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Start Execution", fontSize = 11.sp)
                                }
                            }
                            ServiceStatus.IN_EXECUTION -> {
                                Button(
                                    onClick = { onUpdateStatus(service.id, ServiceStatus.COMPLETED, "Execution complete. Reports uploaded.") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4338CA)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Verify & Complete", fontSize = 11.sp)
                                }
                            }
                            else -> {
                                OutlinedButton(
                                    onClick = { onUpdateStatus(service.id, ServiceStatus.SUBMITTED, "Reset") },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Reset Status", fontSize = 11.sp)
                                }
                            }
                        }

                        // Upload Deliverable button
                        Button(
                            onClick = { onUploadFile(service) },
                            colors = ButtonDefaults.buttonColors(containerColor = BizSecondary),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_admin_upload_service_file_${service.id}")
                        ) {
                            Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Upload File", fontSize = 11.sp)
                        }
                    }

                    if (service.attachments.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Uploaded Service Deliverables (${service.attachments.size}):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BizSecondary
                        )
                        service.attachments.forEach { file ->
                            AttachmentItemRow(
                                file = file,
                                onView = { onViewDocument(file) },
                                onDownload = { /* handle download */ }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ------------------- FILE UPLOAD MODAL DIALOG -------------------

@Composable
fun AdminUploadFileDialog(
    target: UploadTarget,
    onDismiss: () -> Unit,
    onUploadPdf: (entityId: String, isOrder: Boolean, notes: String) -> Unit,
    onUploadJpg: (entityId: String, isOrder: Boolean, title: String, notes: String) -> Unit
) {
    val isOrder = target is UploadTarget.ForOrder
    val entityId = if (target is UploadTarget.ForOrder) target.order.id else (target as UploadTarget.ForService).service.id
    val clientName = if (target is UploadTarget.ForOrder) target.order.customerName else (target as UploadTarget.ForService).service.customerName

    var fileFormat by remember { mutableStateOf("PDF") } // "PDF" or "JPG"
    var documentTitle by remember {
        mutableStateOf(if (isOrder) "Official Invoice & Packing Slip" else "Quality Inspection & QA Certificate")
    }
    var adminRemarks by remember {
        mutableStateOf("Inspected and signed off by Enterprise Admin.")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Upload Deliverable File",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "For $entityId ($clientName)",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Select Document Format:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = fileFormat == "PDF",
                        onClick = {
                            fileFormat = "PDF"
                            documentTitle = if (isOrder) "Commercial Tax Invoice & Receipt" else "QA Engineering Specification Report"
                        },
                        label = { Text("PDF Document", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(14.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BizPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                    FilterChip(
                        selected = fileFormat == "JPG",
                        onClick = {
                            fileFormat = "JPG"
                            documentTitle = if (isOrder) "Dock Delivery Photo Proof" else "Certified Inspection Seal"
                        },
                        label = { Text("JPG Image Proof", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(14.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BizSecondary,
                            selectedLabelColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = documentTitle,
                    onValueChange = { documentTitle = it },
                    label = { Text("Document / File Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_admin_file_title"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = adminRemarks,
                    onValueChange = { adminRemarks = it },
                    label = { Text("Admin Remarks / Certificate Notes") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_admin_file_notes"),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "File will be generated, signed, and immediately linked to client's timeline & profile for in-app preview and download.",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (fileFormat == "PDF") {
                        onUploadPdf(entityId, isOrder, adminRemarks)
                    } else {
                        onUploadJpg(entityId, isOrder, documentTitle, adminRemarks)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BizPrimary),
                modifier = Modifier.testTag("btn_confirm_upload_file")
            ) {
                Text("Generate & Upload")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// ------------------- PRODUCT FORM MODAL SHEET -------------------

@Composable
fun ProductFormSheet(
    productToEdit: Product?,
    onSave: (name: String, category: String, sku: String, desc: String, price: Double, stock: Int, minThresh: Int, unit: String) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(productToEdit?.name ?: "") }
    var category by remember { mutableStateOf(productToEdit?.category ?: "Hardware") }
    var sku by remember { mutableStateOf(productToEdit?.sku ?: "IND-GEN-01") }
    var desc by remember { mutableStateOf(productToEdit?.description ?: "") }
    var priceStr by remember { mutableStateOf(productToEdit?.price?.toString() ?: "150.00") }
    var stockStr by remember { mutableStateOf(productToEdit?.stockQuantity?.toString() ?: "20") }
    var minThreshStr by remember { mutableStateOf(productToEdit?.minStockThreshold?.toString() ?: "5") }
    var unit by remember { mutableStateOf(productToEdit?.unit ?: "pcs") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .testTag("form_product_inventory")
    ) {
        Text(
            text = if (productToEdit == null) "Add New Inventory Item" else "Edit Product",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Product / Material Name") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_product_name"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = sku,
                onValueChange = { sku = it },
                label = { Text("SKU") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = priceStr,
                onValueChange = { priceStr = it },
                label = { Text("Unit Price ($)") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = stockStr,
                onValueChange = { stockStr = it },
                label = { Text("Stock Quantity") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = minThreshStr,
                onValueChange = { minThreshStr = it },
                label = { Text("Min Alert") },
                modifier = Modifier.weight(0.8f),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            label = { Text("Technical Description") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.filledTonalButtonColors(),
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    val price = priceStr.toDoubleOrNull() ?: 100.0
                    val stock = stockStr.toIntOrNull() ?: 10
                    val minThresh = minThreshStr.toIntOrNull() ?: 5
                    val prodName = if (name.isNotBlank()) name else "Industrial Component"
                    onSave(prodName, category, sku, desc, price, stock, minThresh, unit)
                },
                colors = ButtonDefaults.buttonColors(containerColor = BizPrimary),
                modifier = Modifier
                    .weight(1.5f)
                    .testTag("btn_save_product")
            ) {
                Text("Save to Inventory", fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
