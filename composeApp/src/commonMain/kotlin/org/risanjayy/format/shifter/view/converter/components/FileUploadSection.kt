package org.risanjayy.format.shifter.view.converter.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.risanjayy.format.shifter.domain.service.SelectedFile
import org.risanjayy.format.shifter.view.theme.FSTextStyle

/**
 * File upload section with drag & drop area and file list
 * Mobile-first design with touch-friendly interactions
 */
@Composable
fun FileUploadSection(
    selectedFiles: List<SelectedFile>,
    onFilesSelected: (List<SelectedFile>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Platform-specific file picker launcher
        val filePickerLauncher = rememberFilePickerLauncher { newFiles ->
            if (newFiles.isNotEmpty()) {
                onFilesSelected(selectedFiles + newFiles)
            }
        }
        
        // Upload Area
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
                .clickable {
                    filePickerLauncher()
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ),
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CloudUpload,
                    contentDescription = "Upload",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Tap to select HEIC images",
                    style = FSTextStyle.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "or drag and drop files here",
                    style = FSTextStyle.captionRegular,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
        
        // Selected Files List
        if (selectedFiles.isNotEmpty()) {
            Text(
                text = "Selected Files (${selectedFiles.size})",
                style = FSTextStyle.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 200.dp)
            ) {
                items(selectedFiles) { file ->
                    FileItem(
                        file = file,
                        onRemove = {
                            onFilesSelected(selectedFiles - file)
                        }
                    )
                }
            }
            
            // Clear All Button
            TextButton(
                onClick = { onFilesSelected(emptyList()) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear All")
            }
        }
    }
}

/**
 * Platform-specific file picker launcher
 */
@Composable
expect fun rememberFilePickerLauncher(
    onFilesSelected: (List<SelectedFile>) -> Unit
): () -> Unit

/**
 * Formats file size in bytes to human readable format
 */
private fun formatFileSize(bytes: Long): String {
    if (bytes < 1024) return "$bytes B"
    val kb = bytes / 1024.0
    if (kb < 1024) return "${(kb * 10).toInt() / 10.0} KB"
    val mb = kb / 1024.0
    if (mb < 1024) return "${(mb * 10).toInt() / 10.0} MB"
    val gb = mb / 1024.0
    return "${(gb * 10).toInt() / 10.0} GB"
}

@Composable
private fun FileItem(
    file: SelectedFile,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = "Image",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = file.name,
                    style = FSTextStyle.bodyRegular,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = formatFileSize(file.size),
                    style = FSTextStyle.captionRegular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}