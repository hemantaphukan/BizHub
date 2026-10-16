package com.example.data.model

data class AttachmentFile(
    val id: String,
    val fileName: String,
    val fileType: String, // "PDF" or "JPG"
    val description: String,
    val uploadedAtMillis: Long,
    val fileSizeKb: Int,
    val localFilePath: String,
    val relatedEntityId: String // Order ID or Service Request ID
)
