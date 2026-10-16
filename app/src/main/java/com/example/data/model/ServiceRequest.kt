package com.example.data.model

enum class ServiceStatus(val displayName: String, val stepIndex: Int) {
    SUBMITTED("Submitted", 0),
    UNDER_REVIEW("Under Review", 1),
    APPROVED("Approved & Scheduled", 2),
    IN_EXECUTION("In Execution", 3),
    COMPLETED("Completed & Verified", 4),
    REJECTED("Declined", -1)
}

data class ServiceRequest(
    val id: String,
    val customerName: String,
    val customerEmail: String,
    val serviceType: String,
    val requirementsDescription: String,
    val urgency: String = "Normal", // "Normal", "High", "Critical"
    val budgetEstimate: Double = 0.0,
    val status: ServiceStatus = ServiceStatus.SUBMITTED,
    val submittedAtMillis: Long,
    val updatedAtMillis: Long,
    val adminNotes: String = "",
    val attachments: List<AttachmentFile> = emptyList()
)
