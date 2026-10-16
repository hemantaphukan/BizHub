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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.data.model.CustomerOrder
import com.example.data.model.ServiceRequest
import com.example.data.model.UserProfile
import com.example.ui.BizViewModel
import com.example.ui.theme.BizPrimary
import com.example.ui.theme.BizSecondary
import com.example.util.FileHelper
import java.util.Locale

@Composable
fun UserProfileScreen(
    userProfile: UserProfile,
    orders: List<CustomerOrder>,
    serviceRequests: List<ServiceRequest>,
    viewModel: BizViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }

    var editName by remember(userProfile) { mutableStateOf(userProfile.name) }
    var editCompany by remember(userProfile) { mutableStateOf(userProfile.company) }
    var editEmail by remember(userProfile) { mutableStateOf(userProfile.email) }
    var editPhone by remember(userProfile) { mutableStateOf(userProfile.phone) }
    var editAddress by remember(userProfile) { mutableStateOf(userProfile.shippingAddress) }
    var notificationsEnabled by remember(userProfile) { mutableStateOf(userProfile.notificationsEnabled) }

    val totalSpent = remember(orders) { orders.sumOf { it.totalAmount } }
    val allAttachments = remember(orders, serviceRequests) {
        viewModel.repository.getAllCustomerAttachments()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_user_profile"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(BizPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Avatar",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = userProfile.name,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = userProfile.company,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFFEF3C7))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Stars,
                                        contentDescription = "Tier",
                                        tint = Color(0xFFD97706),
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = userProfile.tier,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFB45309)
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = { isEditing = !isEditing },
                            modifier = Modifier.testTag("btn_edit_profile")
                        ) {
                            Icon(
                                imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = BizPrimary
                            )
                        }
                    }

                    if (isEditing) {
                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("Full Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_profile_name"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = editCompany,
                            onValueChange = { editCompany = it },
                            label = { Text("Company Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = editEmail,
                            onValueChange = { editEmail = it },
                            label = { Text("Business Email") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = editPhone,
                            onValueChange = { editPhone = it },
                            label = { Text("Phone") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = editAddress,
                            onValueChange = { editAddress = it },
                            label = { Text("Primary Shipping Address") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                viewModel.updateProfile(
                                    userProfile.copy(
                                        name = editName,
                                        company = editCompany,
                                        email = editEmail,
                                        phone = editPhone,
                                        shippingAddress = editAddress
                                    )
                                )
                                isEditing = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_save_profile"),
                            colors = ButtonDefaults.buttonColors(containerColor = BizPrimary)
                        ) {
                            Text("Save Account Changes")
                        }
                    } else {
                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        Spacer(modifier = Modifier.height(10.dp))

                        ProfileInfoRow(icon = Icons.Default.Email, label = "Email", value = userProfile.email)
                        ProfileInfoRow(icon = Icons.Default.Phone, label = "Phone", value = userProfile.phone)
                        ProfileInfoRow(icon = Icons.Default.LocationOn, label = "Address", value = userProfile.shippingAddress)
                    }
                }
            }
        }

        // Stats Overview Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Orders Placed",
                    value = "${orders.size}",
                    subtitle = "Commercial",
                    color = BizPrimary,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Services Active",
                    value = "${serviceRequests.size}",
                    subtitle = "Projects",
                    color = BizSecondary,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Total Value",
                    value = String.format(Locale.US, "$%.0f", totalSpent),
                    subtitle = "Invested",
                    color = Color(0xFF0284C7),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Centralized Deliverables Library
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FolderShared,
                        contentDescription = "Folder",
                        tint = BizPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Client Deliverables & Files (${allAttachments.size})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        if (allAttachments.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No files received yet. Admin uploaded PDF/JPG documents will appear here.",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        } else {
            items(allAttachments, key = { it.id }) { file ->
                AttachmentItemRow(
                    file = file,
                    onView = { viewModel.openDocument(file) },
                    onDownload = { FileHelper.downloadToDevice(context, file) }
                )
            }
        }

        // Account Preferences & Settings
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Account Preferences",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Order Status Notifications",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Receive real-time milestone updates when admin approves orders",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = {
                                notificationsEnabled = it
                                viewModel.updateProfile(userProfile.copy(notificationsEnabled = it))
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = BizPrimary)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Auto-save Deliverable Files",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Automatically keep a local cached copy of all signed PDFs",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                        Switch(
                            checked = userProfile.autoDownloadInvoices,
                            onCheckedChange = {
                                viewModel.updateProfile(userProfile.copy(autoDownloadInvoices = it))
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = BizSecondary)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF64748B),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "$label: ",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF475569)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            color = Color(0xFF0F172A)
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = Color(0xFF94A3B8)
            )
        }
    }
}
