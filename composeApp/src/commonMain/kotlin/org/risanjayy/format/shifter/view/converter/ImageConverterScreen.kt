package org.risanjayy.format.shifter.view.converter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.random.Random
import org.risanjayy.format.shifter.domain.model.ConversionStatus
import org.risanjayy.format.shifter.domain.model.ConversionTask
import org.risanjayy.format.shifter.domain.model.ImageFormat
import org.risanjayy.format.shifter.domain.service.ImageConversionException
import org.risanjayy.format.shifter.di.ImageConverterProvider
import org.risanjayy.format.shifter.domain.service.SelectedFile
import org.risanjayy.format.shifter.domain.service.FileDownloadException
import org.risanjayy.format.shifter.view.converter.components.*
import org.risanjayy.format.shifter.view.theme.FSTextStyle

/**
 * Main screen for image conversion
 * Mobile-first design with responsive layout
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageConverterScreen(
    modifier: Modifier = Modifier
) {
    var selectedFiles by remember { mutableStateOf<List<SelectedFile>>(emptyList()) }
    var outputFormat by remember { mutableStateOf(ImageFormat.PNG) }
    var quality by remember { mutableStateOf(90) }
    var conversionTasks by remember { mutableStateOf<List<ConversionTask>>(emptyList()) }
    var isConverting by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val fileDownloader = rememberFileDownloader()
    val snackbarHostState = remember { SnackbarHostState() }
    
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        // Header
        Text(
            text = "Image Converter",
            style = FSTextStyle.h3Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "Convert HEIC images to PNG or JPEG format",
            style = FSTextStyle.bodyRegular,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        // File Upload Section
        FileUploadSection(
            selectedFiles = selectedFiles,
            onFilesSelected = { selectedFiles = it },
            modifier = Modifier.fillMaxWidth()
        )
        
        // Conversion Options
        if (selectedFiles.isNotEmpty()) {
            ConversionOptionsSection(
                outputFormat = outputFormat,
                quality = quality,
                onOutputFormatChanged = { outputFormat = it },
                onQualityChanged = { quality = it },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Convert Button
            Button(
                onClick = {
                    if (!isConverting) {
                        scope.launch {
                            isConverting = true
                            
                            val imageConverter = ImageConverterProvider.provide()
                            
                            // Create initial conversion tasks
                            val initialTasks = selectedFiles.mapIndexed { index, file ->
                                ConversionTask(
                                    id = "task_${Random.nextLong()}_$index",
                                    fileName = file.name,
                                    inputFormat = ImageFormat.HEIC,
                                    outputFormat = outputFormat,
                                    quality = quality,
                                    status = ConversionStatus.PENDING,
                                    inputData = file.data
                                )
                            }
                            conversionTasks = initialTasks
                            
                            // Process each task
                            val updatedTasks = mutableListOf<ConversionTask>()
                            
                            for (task in initialTasks) {
                                // Update status to processing
                                val processingTask = task.copy(status = ConversionStatus.PROCESSING)
                                updatedTasks.add(processingTask)
                                conversionTasks = updatedTasks.toList()
                                
                                try {
                                    // Perform conversion
                                    val convertedData = imageConverter.convertImage(
                                        inputData = task.inputData!!,
                                        inputFormat = task.inputFormat,
                                        outputFormat = task.outputFormat,
                                        quality = task.quality
                                    )
                                    
                                    // Update task with success
                                    val successTask = processingTask.copy(
                                        status = ConversionStatus.COMPLETED,
                                        outputData = convertedData
                                    )
                                    updatedTasks[updatedTasks.lastIndex] = successTask
                                    conversionTasks = updatedTasks.toList()
                                    
                                } catch (e: ImageConversionException) {
                                    // Update task with error
                                    val errorTask = processingTask.copy(
                                        status = ConversionStatus.FAILED,
                                        errorMessage = e.message
                                    )
                                    updatedTasks[updatedTasks.lastIndex] = errorTask
                                    conversionTasks = updatedTasks.toList()
                                }
                            }
                            
                            isConverting = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedFiles.isNotEmpty() && !isConverting
            ) {
                if (isConverting) {
                    Text("Converting...")
                } else {
                    Text("Convert ${selectedFiles.size} file(s)")
                }
            }
        }
        
        // Conversion Progress
        if (conversionTasks.isNotEmpty()) {
            ConversionProgressSection(
                tasks = conversionTasks,
                onTasksUpdated = { conversionTasks = it },
                onDownloadFile = { task ->
                    scope.launch {
                        try {
                            if (task.outputData != null) {
                                val fileName = fileDownloader.generateFileName(
                                    originalName = task.fileName,
                                    outputFormat = task.outputFormat
                                )
                                fileDownloader.downloadFile(
                                     data = task.outputData,
                                     fileName = fileName,
                                     mimeType = when (task.outputFormat) {
                                         ImageFormat.PNG -> "image/png"
                                         ImageFormat.JPG -> "image/jpeg"
                                         else -> "application/octet-stream"
                                     }
                                 )
                                 
                                 // Show success message
                                 snackbarHostState.showSnackbar(
                                     message = "File $fileName berhasil didownload",
                                     duration = SnackbarDuration.Short
                                 )
                            }
                        } catch (e: FileDownloadException) {
                            // Show error message to user
                            snackbarHostState.showSnackbar(
                                message = "Download gagal: ${e.message}",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                },
                onDownloadAll = { tasks ->
                    scope.launch {
                        try {
                            val downloadData = tasks.mapNotNull { task ->
                                if (task.outputData != null) {
                                    val fileName = fileDownloader.generateFileName(
                                        originalName = task.fileName,
                                        outputFormat = task.outputFormat
                                    )
                                    Triple(
                                        task.outputData,
                                        fileName,
                                        when (task.outputFormat) {
                                            ImageFormat.PNG -> "image/png"
                                            ImageFormat.JPG -> "image/jpeg"
                                            else -> "application/octet-stream"
                                        }
                                    )
                                } else null
                            }
                            
                            if (downloadData.isNotEmpty()) {
                                fileDownloader.downloadMultipleFiles(downloadData)
                                
                                // Show success message
                                snackbarHostState.showSnackbar(
                                    message = "${downloadData.size} file berhasil didownload",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        } catch (e: FileDownloadException) {
                            // Show error message to user
                            snackbarHostState.showSnackbar(
                                message = "Download semua file gagal: ${e.message}",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        }
    }
}