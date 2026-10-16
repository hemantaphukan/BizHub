package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.DocumentViewerState
import com.example.ui.theme.BizPrimary
import com.example.util.FileHelper

@Composable
fun DocumentViewerDialog(
    state: DocumentViewerState,
    onDismiss: () -> Unit
) {
    if (!state.isOpen || state.attachment == null) return

    val context = LocalContext.current
    val attachment = state.attachment

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clip(RoundedCornerShape(20.dp))
                .testTag("document_viewer_dialog"),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BizPrimary)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = "Document type",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = attachment.fileName,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${attachment.fileType} • ${attachment.fileSizeKb} KB • ${attachment.description}",
                            color = Color(0xFFD8E2FF),
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("btn_close_document_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }

                // Document Content Viewer
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFF0F172A).copy(alpha = 0.05f))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        state.isLoading -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = BizPrimary)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Rendering document preview...",
                                    fontSize = 13.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        state.errorMessage != null -> {
                            Text(
                                text = "Unable to preview document: ${state.errorMessage}",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 13.sp
                            )
                        }

                        attachment.fileType.equals("PDF", ignoreCase = true) -> {
                            if (state.pdfBitmaps.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    itemsIndexed(state.pdfBitmaps) { pageIdx, bitmap ->
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Column {
                                                Image(
                                                    bitmap = bitmap.asImageBitmap(),
                                                    contentDescription = "PDF Page ${pageIdx + 1}",
                                                    modifier = Modifier.fillMaxWidth(),
                                                    contentScale = ContentScale.FillWidth
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(Color(0xFFF1F5F9))
                                                        .padding(vertical = 4.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = "Page ${pageIdx + 1} of ${state.pdfBitmaps.size}",
                                                        fontSize = 11.sp,
                                                        color = Color(0xFF64748B)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    text = "PDF file ready. Use the actions below to open or export.",
                                    fontSize = 13.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        else -> { // JPG Image
                            state.jpgBitmap?.let { bmp ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Image(
                                        bitmap = bmp.asImageBitmap(),
                                        contentDescription = "JPG Deliverable",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            } ?: Text(
                                text = "Preview image not loaded",
                                fontSize = 13.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }

                // Action Bar (Download, Share, Open Externally)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { FileHelper.downloadToDevice(context, attachment) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_download_document"),
                        colors = ButtonDefaults.buttonColors(containerColor = BizPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Download", fontSize = 13.sp)
                    }

                    FilledTonalButton(
                        onClick = { FileHelper.shareDocument(context, attachment) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_share_document")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share", fontSize = 13.sp)
                    }

                    OutlinedButton(
                        onClick = { FileHelper.openDocumentExternally(context, attachment) },
                        modifier = Modifier.testTag("btn_open_external_document")
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Open App",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
