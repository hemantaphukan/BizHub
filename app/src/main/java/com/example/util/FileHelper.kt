package com.example.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.model.AttachmentFile
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object FileHelper {

    /**
     * Renders pages of a PDF file to a list of Bitmaps using Android native PdfRenderer.
     */
    fun renderPdfToBitmaps(context: Context, filePath: String): List<Bitmap> {
        val file = File(filePath)
        if (!file.exists()) return emptyList()

        val bitmaps = mutableListOf<Bitmap>()
        try {
            val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(fileDescriptor)

            val pageCount = pdfRenderer.pageCount
            for (i in 0 until pageCount) {
                val page = pdfRenderer.openPage(i)
                // High-fidelity rendering scale (2x)
                val width = page.width * 2
                val height = page.height * 2
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                bitmaps.add(bitmap)
                page.close()
            }
            pdfRenderer.close()
            fileDescriptor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmaps
    }

    /**
     * Loads a JPG file into a Bitmap.
     */
    fun loadJpgBitmap(filePath: String): Bitmap? {
        val file = File(filePath)
        if (!file.exists()) return null
        return try {
            BitmapFactory.decodeFile(file.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Shares a document file using Android Sharesheet with FileProvider.
     */
    fun shareDocument(context: Context, attachment: AttachmentFile) {
        val file = File(attachment.localFilePath)
        if (!file.exists()) {
            Toast.makeText(context, "File does not exist on device", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val authority = "${context.packageName}.provider"
            val uri: Uri = FileProvider.getUriForFile(context, authority, file)
            val mimeType = if (attachment.fileType.equals("PDF", ignoreCase = true)) "application/pdf" else "image/jpeg"

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = mimeType
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, attachment.fileName)
                putExtra(Intent.EXTRA_TEXT, "Attached document from BizHub: ${attachment.description}")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share ${attachment.fileName}"))
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Unable to share file: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Opens document in external viewer app.
     */
    fun openDocumentExternally(context: Context, attachment: AttachmentFile) {
        val file = File(attachment.localFilePath)
        if (!file.exists()) {
            Toast.makeText(context, "File does not exist on device", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val authority = "${context.packageName}.provider"
            val uri: Uri = FileProvider.getUriForFile(context, authority, file)
            val mimeType = if (attachment.fileType.equals("PDF", ignoreCase = true)) "application/pdf" else "image/jpeg"

            val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, mimeType)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(viewIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "No app found to open ${attachment.fileType} file", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Exports/saves file to the public Downloads folder.
     */
    fun downloadToDevice(context: Context, attachment: AttachmentFile) {
        val sourceFile = File(attachment.localFilePath)
        if (!sourceFile.exists()) {
            Toast.makeText(context, "File source not found", Toast.LENGTH_SHORT).show()
            return
        }

        val mimeType = if (attachment.fileType.equals("PDF", ignoreCase = true)) "application/pdf" else "image/jpeg"

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, attachment.fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/BizHub")
                }
                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        FileInputStream(sourceFile).use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    Toast.makeText(context, "Saved to Downloads/BizHub/${attachment.fileName}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Failed to create download entry", Toast.LENGTH_SHORT).show()
                }
            } else {
                @Suppress("DEPRECATION")
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val bizDir = File(downloadsDir, "BizHub")
                if (!bizDir.exists()) bizDir.mkdirs()
                val targetFile = File(bizDir, attachment.fileName)
                FileInputStream(sourceFile).use { input ->
                    FileOutputStream(targetFile).use { output ->
                        input.copyTo(output)
                    }
                }
                Toast.makeText(context, "Saved to Downloads/BizHub/${attachment.fileName}", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Download failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
}
