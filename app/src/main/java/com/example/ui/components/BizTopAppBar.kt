package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.UserRole
import com.example.ui.theme.BizPrimary
import com.example.ui.theme.BizSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BizTopAppBar(
    currentRole: UserRole,
    cartItemCount: Int,
    onToggleRole: () -> Unit,
    onOpenCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (currentRole == UserRole.ADMIN) Color(0xFF0F172A) else BizPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (currentRole == UserRole.ADMIN) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                        contentDescription = "App Icon",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "BizHub",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (currentRole == UserRole.ADMIN) Color(0xFFEF4444).copy(alpha = 0.15f) else BizSecondary.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (currentRole == UserRole.ADMIN) "BACKEND ADMIN" else "CLIENT PORTAL",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (currentRole == UserRole.ADMIN) Color(0xFFDC2626) else BizSecondary
                            )
                        }
                    }
                    Text(
                        text = if (currentRole == UserRole.ADMIN) "Inventory & Service Management" else "Enterprise Orders & Deliverables",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            // Role switcher button
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onToggleRole() }
                    .testTag("btn_toggle_role"),
                color = if (currentRole == UserRole.ADMIN) Color(0xFFFEF2F2) else Color(0xFFEFF6FF),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (currentRole == UserRole.ADMIN) Icons.Default.Person else Icons.Default.AdminPanelSettings,
                        contentDescription = "Switch Role",
                        tint = if (currentRole == UserRole.ADMIN) Color(0xFFDC2626) else BizPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (currentRole == UserRole.ADMIN) "To Client" else "Admin View",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (currentRole == UserRole.ADMIN) Color(0xFFDC2626) else BizPrimary
                    )
                }
            }

            // Shopping Cart Icon (visible in Customer mode)
            if (currentRole == UserRole.CUSTOMER) {
                IconButton(
                    onClick = onOpenCart,
                    modifier = Modifier.testTag("btn_open_cart")
                ) {
                    BadgedBox(
                        badge = {
                            if (cartItemCount > 0) {
                                Badge(
                                    containerColor = BizSecondary,
                                    contentColor = Color.White
                                ) {
                                    Text("$cartItemCount")
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Shopping Cart",
                            tint = BizPrimary
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
    )
}
