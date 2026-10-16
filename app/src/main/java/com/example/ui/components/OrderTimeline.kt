package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderStatus
import com.example.data.model.ServiceStatus
import com.example.ui.theme.BizPrimary
import com.example.ui.theme.BizSecondary

@Composable
fun OrderStatusTimeline(currentStatus: OrderStatus, modifier: Modifier = Modifier) {
    val steps = listOf(
        "Placed" to OrderStatus.PENDING,
        "Approved" to OrderStatus.APPROVED,
        "Production" to OrderStatus.IN_PROGRESS,
        "Ready" to OrderStatus.READY_FOR_DELIVERY,
        "Delivered" to OrderStatus.DELIVERED
    )

    val currentStepIndex = currentStatus.stepIndex

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, pair ->
                val isCompleted = currentStepIndex >= pair.second.stepIndex && currentStepIndex != -1
                val isCurrent = currentStepIndex == pair.second.stepIndex

                // Step Circle
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCurrent -> BizPrimary
                                isCompleted -> BizSecondary
                                else -> Color(0xFFE2E8F0)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted && !isCurrent) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    } else if (isCurrent) {
                        Icon(
                            imageVector = Icons.Default.HourglassTop,
                            contentDescription = "In progress",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    } else {
                        Text(
                            text = "${index + 1}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                // Connector line between steps
                if (index < steps.size - 1) {
                    val nextStepCompleted = currentStepIndex > pair.second.stepIndex && currentStepIndex != -1
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .background(
                                if (nextStepCompleted) BizSecondary else Color(0xFFE2E8F0)
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Step text labels
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            steps.forEachIndexed { _, pair ->
                val isCurrent = currentStepIndex == pair.second.stepIndex
                Text(
                    text = pair.first,
                    fontSize = 10.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCurrent) BizPrimary else Color(0xFF64748B),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ServiceStatusTimeline(currentStatus: ServiceStatus, modifier: Modifier = Modifier) {
    val steps = listOf(
        "Submitted" to ServiceStatus.SUBMITTED,
        "Reviewed" to ServiceStatus.UNDER_REVIEW,
        "Approved" to ServiceStatus.APPROVED,
        "Execution" to ServiceStatus.IN_EXECUTION,
        "Verified" to ServiceStatus.COMPLETED
    )

    val currentStepIndex = currentStatus.stepIndex

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, pair ->
                val isCompleted = currentStepIndex >= pair.second.stepIndex && currentStepIndex != -1
                val isCurrent = currentStepIndex == pair.second.stepIndex

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCurrent -> BizPrimary
                                isCompleted -> BizSecondary
                                else -> Color(0xFFE2E8F0)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted && !isCurrent) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    } else if (isCurrent) {
                        Icon(
                            imageVector = Icons.Default.HourglassTop,
                            contentDescription = "Active",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    } else {
                        Text(
                            text = "${index + 1}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                if (index < steps.size - 1) {
                    val nextStepCompleted = currentStepIndex > pair.second.stepIndex && currentStepIndex != -1
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .background(
                                if (nextStepCompleted) BizSecondary else Color(0xFFE2E8F0)
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            steps.forEachIndexed { _, pair ->
                val isCurrent = currentStepIndex == pair.second.stepIndex
                Text(
                    text = pair.first,
                    fontSize = 10.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCurrent) BizPrimary else Color(0xFF64748B),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
