package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import com.example.data.model.AttachmentFile
import com.example.data.model.CustomerOrder
import com.example.data.model.ServiceRequest
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object DocumentGenerator {

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US)

    /**
     * Generates an official PDF invoice for a CustomerOrder.
     */
    fun generateOrderPdfInvoice(
        context: Context,
        order: CustomerOrder,
        adminNotes: String = "Authorized by Enterprise Admin"
    ): AttachmentFile {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // Standard A4 points
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Header Background Banner
        paint.color = Color.parseColor("#1E3A8A")
        canvas.drawRect(0f, 0f, 595f, 110f, paint)

        // Company Title
        paint.color = Color.WHITE
        paint.textSize = 24f
        paint.isFakeBoldText = true
        canvas.drawText("BIZHUB ENTERPRISE SUITE", 40f, 50f, paint)

        paint.textSize = 12f
        paint.isFakeBoldText = false
        canvas.drawText("Official Commercial Invoice & Fulfillment Document", 40f, 75f, paint)
        canvas.drawText("Document ID: DOC-${System.currentTimeMillis() % 100000}", 40f, 95f, paint)

        // Date & Invoice Meta
        paint.textAlign = Paint.Align.RIGHT
        paint.textSize = 11f
        canvas.drawText("Date: ${dateFormat.format(Date(order.placedAtMillis))}", 555f, 50f, paint)
        canvas.drawText("Order: ${order.id}", 555f, 70f, paint)
        canvas.drawText("Status: ${order.status.displayName}", 555f, 90f, paint)
        paint.textAlign = Paint.Align.LEFT

        // Customer Info Card
        var currentY = 145f
        paint.color = Color.parseColor("#F1F5F9")
        val clientRect = RectF(40f, currentY, 555f, currentY + 75f)
        canvas.drawRoundRect(clientRect, 8f, 8f, paint)

        paint.color = Color.parseColor("#0F172A")
        paint.textSize = 13f
        paint.isFakeBoldText = true
        canvas.drawText("BILL TO / CLIENT DETAILS", 55f, currentY + 25f, paint)

        paint.textSize = 11f
        paint.isFakeBoldText = false
        paint.color = Color.parseColor("#334155")
        canvas.drawText("Client: ${order.customerName} (${order.customerEmail})", 55f, currentY + 45f, paint)
        val displayAddress = if (order.shippingAddress.isNotBlank()) order.shippingAddress else "Standard Logistics Delivery"
        canvas.drawText("Destination: $displayAddress", 55f, currentY + 63f, paint)

        // Table Header
        currentY += 105f
        paint.color = Color.parseColor("#0D9488")
        val tableHeaderRect = RectF(40f, currentY, 555f, currentY + 30f)
        canvas.drawRoundRect(tableHeaderRect, 6f, 6f, paint)

        paint.color = Color.WHITE
        paint.textSize = 11f
        paint.isFakeBoldText = true
        canvas.drawText("ITEM DESCRIPTION", 55f, currentY + 20f, paint)
        canvas.drawText("QTY", 360f, currentY + 20f, paint)
        canvas.drawText("UNIT PRICE", 420f, currentY + 20f, paint)
        canvas.drawText("TOTAL", 495f, currentY + 20f, paint)

        // Table Items
        currentY += 35f
        paint.isFakeBoldText = false
        paint.color = Color.parseColor("#1E293B")
        var alternate = false

        for (item in order.items) {
            if (alternate) {
                val rowBg = Paint()
                rowBg.color = Color.parseColor("#F8FAFC")
                canvas.drawRect(40f, currentY - 5f, 555f, currentY + 22f, rowBg)
            }
            alternate = !alternate

            paint.textSize = 11f
            val itemTitle = if (item.productName.length > 38) item.productName.take(35) + "..." else item.productName
            canvas.drawText(itemTitle, 55f, currentY + 14f, paint)
            canvas.drawText("${item.quantity}", 365f, currentY + 14f, paint)
            canvas.drawText(String.format(Locale.US, "$%.2f", item.unitPrice), 420f, currentY + 14f, paint)
            canvas.drawText(String.format(Locale.US, "$%.2f", item.subtotal), 495f, currentY + 14f, paint)
            currentY += 25f
        }

        // Horizontal Separator
        currentY += 10f
        paint.color = Color.parseColor("#CBD5E1")
        paint.strokeWidth = 1f
        canvas.drawLine(40f, currentY, 555f, currentY, paint)

        // Summary Calculations
        currentY += 25f
        paint.color = Color.parseColor("#475569")
        paint.textSize = 12f
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(String.format(Locale.US, "Subtotal: $%.2f", order.totalAmount), 555f, currentY, paint)
        currentY += 20f
        canvas.drawText("Estimated Taxes (0%): $0.00", 555f, currentY, paint)
        currentY += 25f
        paint.textSize = 15f
        paint.isFakeBoldText = true
        paint.color = Color.parseColor("#1E3A8A")
        canvas.drawText(String.format(Locale.US, "TOTAL AMOUNT: $%.2f", order.totalAmount), 555f, currentY, paint)
        paint.textAlign = Paint.Align.LEFT

        // Admin Approval Stamp & Notes
        currentY += 45f
        val stampRect = RectF(40f, currentY, 320f, currentY + 95f)
        paint.color = Color.parseColor("#ECFDF5")
        canvas.drawRoundRect(stampRect, 8f, 8f, paint)

        paint.color = Color.parseColor("#047857")
        paint.strokeWidth = 1.5f
        paint.style = Paint.Style.STROKE
        canvas.drawRoundRect(stampRect, 8f, 8f, paint)

        paint.style = Paint.Style.FILL
        paint.textSize = 12f
        paint.isFakeBoldText = true
        canvas.drawText("VERIFIED & APPROVED BY ADMIN", 55f, currentY + 25f, paint)

        paint.isFakeBoldText = false
        paint.textSize = 10f
        paint.color = Color.parseColor("#065F46")
        canvas.drawText("Official Digital Authorization Stamp", 55f, currentY + 45f, paint)
        canvas.drawText("Note: $adminNotes", 55f, currentY + 63f, paint)
        canvas.drawText("Generated on ${dateFormat.format(Date())}", 55f, currentY + 80f, paint)

        // Footer
        paint.color = Color.parseColor("#94A3B8")
        paint.textSize = 9f
        canvas.drawText("BizHub Global Enterprise Systems • Official Delivery & Accounting Copy • Confidential", 40f, 810f, paint)

        document.finishPage(page)

        val fileName = "Invoice_${order.id}.pdf"
        val file = File(context.filesDir, fileName)
        FileOutputStream(file).use { out ->
            document.writeTo(out)
        }
        document.close()

        val fileSizeKb = (file.length() / 1024).toInt().coerceAtLeast(1)

        return AttachmentFile(
            id = "ATT-" + UUID.randomUUID().toString().take(8).uppercase(),
            fileName = fileName,
            fileType = "PDF",
            description = "Official Commercial Invoice & Fulfillment Slip",
            uploadedAtMillis = System.currentTimeMillis(),
            fileSizeKb = fileSizeKb,
            localFilePath = file.absolutePath,
            relatedEntityId = order.id
        )
    }

    /**
     * Generates a PDF Work Order & Inspection Report for a ServiceRequest.
     */
    fun generateServicePdfReport(
        context: Context,
        service: ServiceRequest,
        adminNotes: String = "Inspection and requirements validated by engineering lead."
    ): AttachmentFile {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Header Background Banner
        paint.color = Color.parseColor("#0F172A")
        canvas.drawRect(0f, 0f, 595f, 110f, paint)

        // Title
        paint.color = Color.WHITE
        paint.textSize = 22f
        paint.isFakeBoldText = true
        canvas.drawText("SERVICE EXECUTION & QA REPORT", 40f, 48f, paint)

        paint.textSize = 12f
        paint.isFakeBoldText = false
        paint.color = Color.parseColor("#94A3B8")
        canvas.drawText("Technical Service Specification & Admin Sign-off", 40f, 72f, paint)
        canvas.drawText("Request Ref: ${service.id} • Urgency: ${service.urgency.uppercase()}", 40f, 92f, paint)

        paint.textAlign = Paint.Align.RIGHT
        paint.color = Color.WHITE
        paint.textSize = 11f
        canvas.drawText("Status: ${service.status.displayName}", 555f, 50f, paint)
        canvas.drawText("Date: ${dateFormat.format(Date(service.submittedAtMillis))}", 555f, 72f, paint)
        paint.textAlign = Paint.Align.LEFT

        // Client & Request Overview
        var currentY = 145f
        paint.color = Color.parseColor("#F8FAFC")
        val cardRect = RectF(40f, currentY, 555f, currentY + 120f)
        canvas.drawRoundRect(cardRect, 8f, 8f, paint)

        paint.color = Color.parseColor("#0F172A")
        paint.textSize = 13f
        paint.isFakeBoldText = true
        canvas.drawText("SERVICE SUMMARY", 55f, currentY + 25f, paint)

        paint.textSize = 11f
        paint.isFakeBoldText = false
        paint.color = Color.parseColor("#334155")
        canvas.drawText("Service Type: ${service.serviceType}", 55f, currentY + 48f, paint)
        canvas.drawText("Client: ${service.customerName} (${service.customerEmail})", 55f, currentY + 70f, paint)
        canvas.drawText(String.format(Locale.US, "Agreed Budget / Value: $%.2f", service.budgetEstimate), 55f, currentY + 92f, paint)
        canvas.drawText("Priority Level: ${service.urgency}", 55f, currentY + 112f, paint)

        // Requirements Scope
        currentY += 150f
        paint.color = Color.parseColor("#0F172A")
        paint.textSize = 13f
        paint.isFakeBoldText = true
        canvas.drawText("CLIENT SPECIFICATIONS & REQUIREMENTS", 40f, currentY, paint)

        currentY += 20f
        paint.color = Color.parseColor("#475569")
        paint.textSize = 11f
        paint.isFakeBoldText = false

        // Wrap requirements text
        val reqLines = splitLines(service.requirementsDescription, 80)
        for (line in reqLines) {
            canvas.drawText(line, 40f, currentY, paint)
            currentY += 18f
        }

        // Admin Deliverable Approval Section
        currentY += 25f
        paint.color = Color.parseColor("#0F172A")
        paint.textSize = 13f
        paint.isFakeBoldText = true
        canvas.drawText("ADMIN ENGINEERING SIGN-OFF & NOTES", 40f, currentY, paint)

        currentY += 20f
        paint.color = Color.parseColor("#334155")
        paint.textSize = 11f
        paint.isFakeBoldText = false
        val finalNotes = if (adminNotes.isNotBlank()) adminNotes else service.adminNotes.ifBlank { "Service verified and approved by admin." }
        for (line in splitLines(finalNotes, 80)) {
            canvas.drawText(line, 40f, currentY, paint)
            currentY += 18f
        }

        // Quality Seal Box
        currentY += 30f
        val sealRect = RectF(40f, currentY, 555f, currentY + 80f)
        paint.color = Color.parseColor("#EFF6FF")
        canvas.drawRoundRect(sealRect, 8f, 8f, paint)

        paint.color = Color.parseColor("#1D4ED8")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.5f
        canvas.drawRoundRect(sealRect, 8f, 8f, paint)

        paint.style = Paint.Style.FILL
        paint.isFakeBoldText = true
        paint.textSize = 12f
        canvas.drawText("OFFICIAL COMPLIANCE & QUALITY GUARANTEE", 55f, currentY + 28f, paint)

        paint.isFakeBoldText = false
        paint.textSize = 10f
        paint.color = Color.parseColor("#1E40AF")
        canvas.drawText("All project specifications, tolerances, and milestone deliverables have been inspected", 55f, currentY + 48f, paint)
        canvas.drawText("and confirmed in accordance with Enterprise ISO standards.", 55f, currentY + 65f, paint)

        // Footer
        paint.color = Color.parseColor("#94A3B8")
        paint.textSize = 9f
        canvas.drawText("BizHub Global Services • Confidential Document • Customer Specific Access", 40f, 810f, paint)

        document.finishPage(page)

        val fileName = "Service_Report_${service.id}.pdf"
        val file = File(context.filesDir, fileName)
        FileOutputStream(file).use { out ->
            document.writeTo(out)
        }
        document.close()

        val fileSizeKb = (file.length() / 1024).toInt().coerceAtLeast(1)

        return AttachmentFile(
            id = "ATT-" + UUID.randomUUID().toString().take(8).uppercase(),
            fileName = fileName,
            fileType = "PDF",
            description = "Quality Assurance & Service Work Order Report",
            uploadedAtMillis = System.currentTimeMillis(),
            fileSizeKb = fileSizeKb,
            localFilePath = file.absolutePath,
            relatedEntityId = service.id
        )
    }

    /**
     * Generates a high-resolution JPG proof / certificate deliverable image.
     */
    fun generateJpgDeliverableProof(
        context: Context,
        title: String,
        entityId: String,
        clientName: String,
        notes: String = "Quality Assured & Certified Deliverable"
    ): AttachmentFile {
        val width = 1080
        val height = 1080
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Background
        paint.color = Color.parseColor("#0F172A")
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        // Decorative Gradient / Framing Borders
        paint.color = Color.parseColor("#1E293B")
        canvas.drawRect(40f, 40f, (width - 40).toFloat(), (height - 40).toFloat(), paint)

        paint.color = Color.parseColor("#14B8A6")
        paint.strokeWidth = 8f
        paint.style = Paint.Style.STROKE
        canvas.drawRect(60f, 60f, (width - 60).toFloat(), (height - 60).toFloat(), paint)

        // Inner Card
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#1E3A8A")
        canvas.drawRoundRect(RectF(100f, 100f, 980f, 320f), 24f, 24f, paint)

        // Header Text
        paint.color = Color.WHITE
        paint.textSize = 42f
        paint.isFakeBoldText = true
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("BIZHUB ENTERPRISE", width / 2f, 180f, paint)

        paint.textSize = 28f
        paint.color = Color.parseColor("#38BDF8")
        paint.isFakeBoldText = false
        canvas.drawText("CERTIFIED DIGITAL DELIVERABLE & PROOF", width / 2f, 230f, paint)

        paint.textSize = 20f
        paint.color = Color.parseColor("#94A3B8")
        canvas.drawText("Authentication Stamp • Ref: $entityId", width / 2f, 275f, paint)

        // Document Body
        paint.color = Color.WHITE
        paint.textSize = 34f
        paint.isFakeBoldText = true
        canvas.drawText(title.uppercase(), width / 2f, 390f, paint)

        paint.textSize = 24f
        paint.isFakeBoldText = false
        paint.color = Color.parseColor("#CBD5E1")
        canvas.drawText("Client: $clientName", width / 2f, 440f, paint)
        canvas.drawText("Issued On: ${dateFormat.format(Date())}", width / 2f, 480f, paint)

        // Central Visual Badge Circle
        val badgeCenterY = 630f
        paint.color = Color.parseColor("#0D9488")
        canvas.drawCircle(width / 2f, badgeCenterY, 110f, paint)

        paint.color = Color.parseColor("#5EEAD4")
        paint.strokeWidth = 6f
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(width / 2f, badgeCenterY, 120f, paint)

        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        paint.textSize = 50f
        paint.isFakeBoldText = true
        canvas.drawText("✓", width / 2f, badgeCenterY + 16f, paint)

        // Badge Subtext
        paint.textSize = 22f
        paint.color = Color.parseColor("#E2E8F0")
        paint.isFakeBoldText = true
        canvas.drawText("VERIFIED BY ADMIN", width / 2f, 800f, paint)

        paint.textSize = 18f
        paint.isFakeBoldText = false
        paint.color = Color.parseColor("#94A3B8")
        canvas.drawText(notes, width / 2f, 840f, paint)

        // Barcode / Verification Stamp Footer
        paint.color = Color.parseColor("#334155")
        canvas.drawRect(120f, 900f, 960f, 980f, paint)

        paint.color = Color.parseColor("#38BDF8")
        paint.textSize = 18f
        canvas.drawText("HASH: ${UUID.randomUUID().toString().replace("-", "").take(24).uppercase()}", width / 2f, 950f, paint)

        val cleanTitle = title.replace(Regex("[^a-zA-Z0-9]"), "_")
        val fileName = "${cleanTitle}_$entityId.jpg"
        val file = File(context.filesDir, fileName)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 92, out)
        }

        val fileSizeKb = (file.length() / 1024).toInt().coerceAtLeast(1)

        return AttachmentFile(
            id = "ATT-" + UUID.randomUUID().toString().take(8).uppercase(),
            fileName = fileName,
            fileType = "JPG",
            description = title,
            uploadedAtMillis = System.currentTimeMillis(),
            fileSizeKb = fileSizeKb,
            localFilePath = file.absolutePath,
            relatedEntityId = entityId
        )
    }

    private fun splitLines(text: String, maxChars: Int): List<String> {
        if (text.isBlank()) return listOf("(No additional notes provided)")
        val words = text.split(" ")
        val lines = mutableListOf<String>()
        var current = StringBuilder()

        for (word in words) {
            if (current.length + word.length + 1 > maxChars) {
                lines.add(current.toString())
                current = StringBuilder(word)
            } else {
                if (current.isNotEmpty()) current.append(" ")
                current.append(word)
            }
        }
        if (current.isNotEmpty()) {
            lines.add(current.toString())
        }
        return lines
    }
}
