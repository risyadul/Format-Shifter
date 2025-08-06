package org.risanjayy.format.shifter.view.converter.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.risanjayy.format.shifter.domain.model.ConversionStatus
import org.risanjayy.format.shifter.domain.model.ConversionTask
import org.risanjayy.format.shifter.view.theme.AppColors
import org.risanjayy.format.shifter.view.theme.FSTextStyle

/**
 * Progress section showing conversion status for each file
 * Mobile-optimized with clear visual feedback
 */
@Composable
fun ConversionProgressSection(
    tasks: List<ConversionTask>,
    onTasksUpdated: (List<ConversionTask>) -> Unit,
    onDownloadFile: (ConversionTask) -> Unit,
    onDownloadAll: (List<ConversionTask>) -> Unit,
    modifier: Modifier = Modifier
) {
    // No simulation needed - conversion is handled by ImageConverterScreen
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header with overall progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Conversion Progress",
                    style = FSTextStyle.bodyBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                val completedCount = tasks.count { it.status == ConversionStatus.COMPLETED }
                val totalCount = tasks.size
                
                Text(
                    text = "$completedCount/$totalCount",
                    style = FSTextStyle.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Overall progress bar
            val progress = if (tasks.isNotEmpty()) {
                tasks.count { it.status == ConversionStatus.COMPLETED || it.status == ConversionStatus.FAILED } / tasks.size.toFloat()
            } else 0f
            
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = AppColors.Primary
            )
            
            // Individual task progress
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tasks.forEach { task ->
                    ConversionTaskItem(
                        task = task,
                        onDownload = {
                            onDownloadFile(task)
                        },
                        onRetry = {
                            val updatedTasks = tasks.map { 
                                if (it.id == task.id) {
                                    it.copy(status = ConversionStatus.PENDING)
                                } else it
                            }
                            onTasksUpdated(updatedTasks)
                        }
                    )
                }
            }
            
            // Action buttons
            if (tasks.any { it.status == ConversionStatus.COMPLETED }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val completedTasks = tasks.filter { it.status == ConversionStatus.COMPLETED }
                            onDownloadAll(completedTasks)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Download All")
                    }
                    
                    OutlinedButton(
                        onClick = {
                            onTasksUpdated(emptyList())
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear")
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversionTaskItem(
    task: ConversionTask,
    onDownload: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status Icon
            when (task.status) {
                ConversionStatus.PENDING -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
                ConversionStatus.PROCESSING -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
                ConversionStatus.COMPLETED -> {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completed",
                        tint = AppColors.Success,
                        modifier = Modifier.size(24.dp)
                    )
                }
                ConversionStatus.FAILED -> {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Failed",
                        tint = AppColors.Error,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // File info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.fileName,
                    style = FSTextStyle.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${task.inputFormat.displayName} → ${task.outputFormat.displayName}",
                    style = FSTextStyle.captionRegular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Show error message if failed
                if (task.status == ConversionStatus.FAILED && task.errorMessage != null) {
                    Text(
                        text = task.errorMessage,
                        style = FSTextStyle.captionRegular,
                        color = AppColors.Error
                    )
                }
            }
            
            // Action button
            when (task.status) {
                ConversionStatus.COMPLETED -> {
                    IconButton(onClick = onDownload) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download",
                            tint = AppColors.Primary
                        )
                    }
                }
                ConversionStatus.FAILED -> {
                    IconButton(onClick = onRetry) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Retry",
                            tint = AppColors.Warning
                        )
                    }
                }
                else -> {
                    // No action for pending/processing
                }
            }
        }
    }
}