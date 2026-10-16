package com.example.data.model

data class UserProfile(
    val name: String = "Alex Chen",
    val email: String = "alex.chen@nexuscorp.com",
    val phone: String = "+1 (555) 234-8901",
    val company: String = "Nexus Tech Industries Ltd.",
    val shippingAddress: String = "Suite 400, 750 Enterprise Blvd, Tech District, CA 94105",
    val tier: String = "Gold Enterprise Client",
    val notificationsEnabled: Boolean = true,
    val autoDownloadInvoices: Boolean = true
)
