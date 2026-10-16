package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttachmentFile
import com.example.data.model.CustomerOrder
import com.example.data.model.ServiceRequest
import com.example.ui.BizViewModel
import com.example.ui.components.OrderStatusBadge
import com.example.ui.components.OrderStatusTimeline
import com.example.ui.components.ServiceStatusBadge
import com.example.ui.components.ServiceStatusTimeline
import com.example.ui.theme.BizPrimary
import com.example.ui.theme.BizSecondary
import com.example.util.FileHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrderTrackingScreen(
    orders: List<CustomerOrder>,
    serviceRequests: List<ServiceRequest>,
    viewModel: BizViewModel,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.US) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Filter bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "Commercial Orders", "Service Requests").forEach { filter ->
                val isSelected = selectedFilter == filter
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BizPrimary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_tracking_$filter")
                )
            }
        }

        // List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (selectedFilter == "All" || selectedFilter == "Commercial Orders") {
                if (orders.isEmpty() && selectedFilter == "Commercial Orders") {
                    item {
                        EmptyTrackingState(message = "No orders found. Browse the catalog to place an order.")
                    }
                } else {
                    items(orders, key = { it.id }) { order ->
                        OrderTrackingCard(
                            order = order,
                            formattedDate = dateFormat.format(Date(order.placedAtMillis)),
                            onViewDocument = { attachment -> viewModel.openDocument(attachment) },
                            onDownloadDocument = { attachment -> FileHelper.downloadToDevice(context, attachment) }
                        )
                    }
                }
            }

            if (selectedFilter == "All" || selectedFilter == "Service Requests") {
                if (serviceRequests.isEmpty() && selectedFilter == "Service Requests") {
                    item {
                        EmptyTrackingState(message = "No service requests found.")
                    }
                } else {
                    items(serviceRequests, key = { it.id }) { service ->
                        ServiceTrackingCard(
                            service = service,
                            formattedDate = dateFormat.format(Date(service.submittedAtMillis)),
                            onViewDocument = { attachment -> viewModel.openDocument(attachment) },
                            onDownloadDocument = { attachment -> FileHelper.downloadToDevice(context, attachment) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderTrackingCard(
    order: CustomerOrder,
    formattedDate: String,
    onViewDocument: (AttachmentFile) -> Unit,
    onDownloadDocument: (AttachmentFile) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("order_card_${order.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = order.id,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = BizPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFEFF6FF))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "ORDER",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = BizPrimary
                            )
                        }
                    }
                    Text(
                        text = formattedDate,
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                OrderStatusBadge(status = order.status)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Real-time Status Stepper
            OrderStatusTimeline(currentStatus = order.status)

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(10.dp))

            // Items summary
            Text(
                text = "Items (${order.items.sumOf { it.quantity }} items):",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            order.items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "• ${item.productName} (x${item.quantity})",
                        fontSize = 11.sp,
                        color = Color(0xFF334155),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = String.format(Locale.US, "$%.2f", item.subtotal),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F172A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total Charged:",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = String.format(Locale.US, "$%.2f", order.totalAmount),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = BizPrimary
                )
            }

            if (order.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Client Instructions: ${order.notes}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }

            // Attached Documents from Admin
            if (order.attachments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Official Documents Uploaded by Admin (${order.attachments.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BizSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))

                order.attachments.forEach { file ->
                    AttachmentItemRow(
                        file = file,
                        onView = { onViewDocument(file) },
                        onDownload = { onDownloadDocument(file) }
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceTrackingCard(
    service: ServiceRequest,
    formattedDate: String,
    onViewDocument: (AttachmentFile) -> Unit,
    onDownloadDocument: (AttachmentFile) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("service_card_${service.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = service.id,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = BizSecondary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFCCFBF1))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "SERVICE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = BizSecondary
                            )
                        }
                    }
                    Text(
                        text = formattedDate,
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                ServiceStatusBadge(status = service.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = service.serviceType,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Service Timeline
            ServiceStatusTimeline(currentStatus = service.status)

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Client Requirements:",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF475569)
            )
            Text(
                text = service.requirementsDescription,
                fontSize = 11.sp,
                color = Color(0xFF334155),
                lineHeight = 15.sp
            )

            if (service.adminNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEFF6FF))
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = "Admin Engineering Notes:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = BizPrimary
                        )
                        Text(
                            text = service.adminNotes,
                            fontSize = 11.sp,
                            color = Color(0xFF1E3A8A)
                        )
                    }
                }
            }

            // Attached Deliverables / Reports from Admin
            if (service.attachments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Service Deliverables & Certifications (${service.attachments.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BizSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))

                service.attachments.forEach { file ->
                    AttachmentItemRow(
                        file = file,
                        onView = { onViewDocument(file) },
                        onDownload = { onDownloadDocument(file) }
                    )
                }
            }
        }
    }
}

@Composable
fun AttachmentItemRow(
    file: AttachmentFile,
    onView: () -> Unit,
    onDownload: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("file_item_${file.id}"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (file.fileType.equals("PDF", ignoreCase = true)) Color(0xFFFEE2E2) else Color(0xFFE0E7FF)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (file.fileType.equals("PDF", ignoreCase = true)) Icons.Default.PictureAsPdf else Icons.Default.Image,
                    contentDescription = file.fileType,
                    tint = if (file.fileType.equals("PDF", ignoreCase = true)) Color(0xFFDC2626) else Color(0xFF4338CA),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = file.fileName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    maxLines = 1
                )
                Text(
                    text = "${file.fileType} • ${file.fileSizeKb} KB • ${file.description}",
                    fontSize = 10.sp,
                    color = Color(0xFF64748B),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
                onClick = onView,
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.testTag("btn_view_file_${file.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "View",
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("View", fontSize = 11.sp)
            }

            IconButton(
                onClick = onDownload,
                modifier = Modifier.testTag("btn_dl_file_${file.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download to Device",
                    tint = BizPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyTrackingState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.ReceiptLong,
                contentDescription = "No data",
                modifier = Modifier.size(50.dp),
                tint = Color(0xFF94A3B8)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = message,
                fontSize = 13.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}
