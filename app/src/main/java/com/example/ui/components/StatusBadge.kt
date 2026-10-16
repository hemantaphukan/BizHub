package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderStatus
import com.example.data.model.ServiceStatus
import com.example.ui.theme.StatusApprovedBg
import com.example.ui.theme.StatusApprovedText
import com.example.ui.theme.StatusCompletedBg
import com.example.ui.theme.StatusCompletedText
import com.example.ui.theme.StatusInProgressBg
import com.example.ui.theme.StatusInProgressText
import com.example.ui.theme.StatusPendingBg
import com.example.ui.theme.StatusPendingText

@Composable
fun OrderStatusBadge(status: OrderStatus, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (status) {
        OrderStatus.PENDING -> StatusPendingBg to StatusPendingText
        OrderStatus.APPROVED -> StatusApprovedBg to StatusApprovedText
        OrderStatus.IN_PROGRESS -> StatusInProgressBg to StatusInProgressText
        OrderStatus.READY_FOR_DELIVERY -> Color(0xFFE0F2FE) to Color(0xFF0369A1)
        OrderStatus.DELIVERED -> StatusCompletedBg to StatusCompletedText
        OrderStatus.CANCELLED -> Color(0xFFFEE2E2) to Color(0xFFB91C1C)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.displayName,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ServiceStatusBadge(status: ServiceStatus, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (status) {
        ServiceStatus.SUBMITTED -> StatusPendingBg to StatusPendingText
        ServiceStatus.UNDER_REVIEW -> Color(0xFFFEF9C3) to Color(0xFF854D0E)
        ServiceStatus.APPROVED -> StatusApprovedBg to StatusApprovedText
        ServiceStatus.IN_EXECUTION -> StatusInProgressBg to StatusInProgressText
        ServiceStatus.COMPLETED -> StatusCompletedBg to StatusCompletedText
        ServiceStatus.REJECTED -> Color(0xFFFEE2E2) to Color(0xFFB91C1C)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.displayName,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
