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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.BizViewModel
import com.example.ui.theme.BizPrimary
import com.example.ui.theme.BizSecondary

data class ServiceOffering(
    val title: String,
    val description: String,
    val typicalTurnaround: String,
    val startingPrice: String,
    val icon: ImageVector,
    val defaultCategory: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    viewModel: BizViewModel,
    modifier: Modifier = Modifier
) {
    var showRequestSheet by remember { mutableStateOf(false) }
    var selectedPresetService by remember { mutableStateOf<ServiceOffering?>(null) }

    val offerings = remember {
        listOf(
            ServiceOffering(
                title = "Custom Precision CNC Fabrication",
                description = "High-tolerance subtractive CNC milling and turning for aluminum, stainless steel, and titanium components.",
                typicalTurnaround = "3-5 Business Days",
                startingPrice = "From $500",
                icon = Icons.Default.Engineering,
                defaultCategory = "Custom CNC Fabrication"
            ),
            ServiceOffering(
                title = "Enterprise IT Architecture & Security Audit",
                description = "Comprehensive pen-testing, distributed node vulnerability scanning, and latency benchmark auditing.",
                typicalTurnaround = "24-48 Hours",
                startingPrice = "From $1,200",
                icon = Icons.Default.Security,
                defaultCategory = "IT Infrastructure Audit"
            ),
            ServiceOffering(
                title = "Precision Calibration & Stress Testing",
                description = "ISO-certified thermal, vibrational, and electrical calibration reports with digital signature verification.",
                typicalTurnaround = "1-2 Business Days",
                startingPrice = "From $350",
                icon = Icons.Default.Speed,
                defaultCategory = "Calibration & Testing"
            ),
            ServiceOffering(
                title = "Industrial Equipment On-Site Maintenance",
                description = "Preventative diagnostics, servo motor overhaul, and hydraulic actuator rebuilds by certified field engineers.",
                typicalTurnaround = "Scheduled Dispatch",
                startingPrice = "From $400 / session",
                icon = Icons.Default.Handyman,
                defaultCategory = "Equipment Maintenance"
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                // Header Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BizPrimary),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Build,
                                    contentDescription = "Services",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Professional Services Suite",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Submit tailored service requests directly to our backend engineering and logistics teams. Track milestones, inspect approval notes, and download deliverables.",
                            fontSize = 12.sp,
                            color = Color(0xFFD8E2FF),
                            lineHeight = 17.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                selectedPresetService = null
                                showRequestSheet = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BizSecondary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("btn_request_custom_service")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Create Custom Service Request", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Standard Service Capabilities",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            items(offerings) { offering ->
                ServiceOfferingCard(
                    offering = offering,
                    onSelect = {
                        selectedPresetService = offering
                        showRequestSheet = true
                    }
                )
            }
        }

        // Floating Action Button for quick creation
        FloatingActionButton(
            onClick = {
                selectedPresetService = null
                showRequestSheet = true
            },
            containerColor = BizPrimary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("fab_create_service_request")
        ) {
            Icon(Icons.Default.Add, contentDescription = "New Service Request")
        }
    }

    // Modal Sheet for Submitting Service Request
    if (showRequestSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showRequestSheet = false },
            sheetState = sheetState
        ) {
            ServiceRequestForm(
                initialService = selectedPresetService,
                onSubmit = { title, requirements, urgency, budget ->
                    viewModel.submitServiceRequest(title, requirements, urgency, budget)
                    showRequestSheet = false
                },
                onCancel = { showRequestSheet = false }
            )
        }
    }
}

@Composable
fun ServiceOfferingCard(
    offering: ServiceOffering,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("card_service_${offering.defaultCategory.replace(" ", "_")}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = offering.icon,
                        contentDescription = offering.title,
                        tint = BizPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = offering.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = offering.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Turnaround: ",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = offering.typicalTurnaround,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F172A)
                    )
                }

                Button(
                    onClick = onSelect,
                    colors = ButtonDefaults.buttonColors(containerColor = BizPrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Select & Request", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun ServiceRequestForm(
    initialService: ServiceOffering?,
    onSubmit: (title: String, requirements: String, urgency: String, budget: Double) -> Unit,
    onCancel: () -> Unit
) {
    var serviceType by remember {
        mutableStateOf(initialService?.title ?: "Custom Precision Fabrication")
    }
    var requirements by remember { mutableStateOf("") }
    var urgency by remember { mutableStateOf("Normal") }
    var budgetText by remember { mutableStateOf(initialService?.startingPrice?.filter { it.isDigit() } ?: "500") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .testTag("form_service_request")
    ) {
        Text(
            text = "Request Custom Service",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Describe your project or service requirements. The backend admin will review, approve, and upload deliverable documents.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = serviceType,
            onValueChange = { serviceType = it },
            label = { Text("Service Type / Project Title") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_service_title"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = requirements,
            onValueChange = { requirements = it },
            label = { Text("Detailed Requirements & Technical Specs") },
            placeholder = { Text("e.g. Dimensions, material grade, inspection certificates needed...") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_service_requirements"),
            minLines = 3,
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Priority / Urgency",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Normal", "High", "Critical").forEach { level ->
                FilterChip(
                    selected = urgency == level,
                    onClick = { urgency = level },
                    label = { Text(level, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = if (level == "Critical") Color(0xFFDC2626) else BizPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = budgetText,
            onValueChange = { budgetText = it },
            label = { Text("Target Budget ($ USD)") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_service_budget"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

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
                    val budget = budgetText.toDoubleOrNull() ?: 500.0
                    val reqText = if (requirements.isNotBlank()) requirements else "Standard execution as per service specifications."
                    onSubmit(serviceType, reqText, urgency, budget)
                },
                colors = ButtonDefaults.buttonColors(containerColor = BizPrimary),
                modifier = Modifier
                    .weight(1.5f)
                    .testTag("btn_submit_service_form")
            ) {
                Text("Submit Request", fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
