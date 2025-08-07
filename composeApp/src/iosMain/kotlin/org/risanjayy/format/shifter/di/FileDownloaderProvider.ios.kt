package org.risanjayy.format.shifter.di

import kotlinx.cinterop.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.risanjayy.format.shifter.domain.service.FileDownloader
import org.risanjayy.format.shifter.domain.service.FileDownloadException
import org.risanjayy.format.shifter.domain.model.ConversionTask
import org.risanjayy.format.shifter.domain.model.ImageFormat
import platform.Foundation.*
import platform.Photos.*
import platform.UIKit.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * iOS implementation of FileDownloaderProvider
 */
actual object FileDownloaderProvider {
    actual fun provide(): FileDownloader = IOSFileDownloader()
}

/**
 * iOS-specific file downloader implementation
 * Uses Photos library for images and Documents directory for other files
 */
private class IOSFileDownloader : FileDownloader {
    
    override suspend fun downloadFile(task: ConversionTask): Boolean {
        return try {
            if (task.outputData == null) return false
            
            val fileName = generateFileName(task.fileName, task.outputFormat)
            val mimeType = when (task.outputFormat) {
                ImageFormat.PNG -> "image/png"
                ImageFormat.JPG -> "image/jpeg"
                ImageFormat.HEIC -> "image/heic"
            }
            
            downloadFileInternal(task.outputData, fileName, mimeType, task.outputFormat)
        } catch (e: Exception) {
            throw FileDownloadException("Failed to download file: ${e.message}", e)
        }
    }
    
    override suspend fun downloadFile(data: ByteArray, fileName: String, mimeType: String): Boolean {
        return try {
            // Determine format from MIME type
            val format = when (mimeType) {
                "image/png" -> ImageFormat.PNG
                "image/jpeg" -> ImageFormat.JPG
                "image/heic" -> ImageFormat.HEIC
                else -> ImageFormat.PNG // Default fallback
            }
            
            downloadFileInternal(data, fileName, mimeType, format)
        } catch (e: Exception) {
            throw FileDownloadException("Failed to download file: ${e.message}", e)
        }
    }
    
    override suspend fun downloadFiles(tasks: List<ConversionTask>): Int {
        var successCount = 0
        for (task in tasks) {
            if (downloadFile(task)) {
                successCount++
            }
        }
        return successCount
    }
    
    override suspend fun downloadMultipleFiles(files: List<Triple<ByteArray, String, String>>): Int {
        var successCount = 0
        for ((data, fileName, mimeType) in files) {
            if (downloadFile(data, fileName, mimeType)) {
                successCount++
            }
        }
        return successCount
    }
    
    override fun generateFileName(originalName: String, outputFormat: ImageFormat): String {
        val nameWithoutExtension = originalName.substringBeforeLast('.')
        val extension = when (outputFormat) {
            ImageFormat.PNG -> "png"
            ImageFormat.JPG -> "jpg"
            ImageFormat.HEIC -> "heic"
        }
        return "${nameWithoutExtension}_converted.$extension"
    }
    
    /**
     * Internal method to handle file download based on type
     */
    @OptIn(ExperimentalForeignApi::class)
    private suspend fun downloadFileInternal(
        data: ByteArray,
        fileName: String,
        mimeType: String,
        format: ImageFormat
    ): Boolean = withContext(Dispatchers.Default) {
        
        // Convert ByteArray to NSData
        val nsData = data.toNSData()
        
        // For image formats, try to save to Photos library first
        if (mimeType.startsWith("image/")) {
            try {
                return@withContext saveToPhotosLibrary(nsData, format)
            } catch (e: Exception) {
                // If Photos library fails, fallback to Documents directory
                return@withContext saveToDocumentsDirectory(nsData, fileName)
            }
        } else {
            // For non-image files, save to Documents directory
            return@withContext saveToDocumentsDirectory(nsData, fileName)
        }
    }
    
    /**
     * Save image to iOS Photos library
     */
    @OptIn(ExperimentalForeignApi::class)
    private suspend fun saveToPhotosLibrary(data: NSData, format: ImageFormat): Boolean {
        return suspendCancellableCoroutine { continuation ->
            
            // Check authorization status
            val authStatus = PHPhotoLibrary.authorizationStatus()
            
            if (authStatus == PHAuthorizationStatusAuthorized) {
                performPhotosSave(data, format, continuation)
            } else if (authStatus == PHAuthorizationStatusNotDetermined) {
                // Request permission
                PHPhotoLibrary.requestAuthorization { status ->
                    if (status == PHAuthorizationStatusAuthorized) {
                        performPhotosSave(data, format, continuation)
                    } else {
                        continuation.resumeWithException(
                            FileDownloadException("Photos library access denied")
                        )
                    }
                }
            } else {
                continuation.resumeWithException(
                    FileDownloadException("Photos library access not authorized")
                )
            }
        }
    }
    
    /**
     * Perform the actual save to Photos library
     */
    @OptIn(ExperimentalForeignApi::class)
    private fun performPhotosSave(
        data: NSData,
        format: ImageFormat,
        continuation: kotlin.coroutines.Continuation<Boolean>
    ) {
        // Create UIImage from data
        val image = UIImage.imageWithData(data)
        if (image == null) {
            continuation.resumeWithException(
                FileDownloadException("Failed to create UIImage from data")
            )
            return
        }
        
        // Save to Photos library
        PHPhotoLibrary.sharedPhotoLibrary().performChanges(
            changeBlock = {
                PHAssetCreationRequest.creationRequestForAssetFromImage(image)
            },
            completionHandler = { success, error ->
                if (success) {
                    continuation.resume(true)
                } else {
                    val errorMessage = error?.localizedDescription ?: "Unknown error"
                    continuation.resumeWithException(
                        FileDownloadException("Failed to save to Photos library: $errorMessage")
                    )
                }
            }
        )
    }
    
    /**
     * Save file to Documents directory
     */
    @OptIn(ExperimentalForeignApi::class)
    private suspend fun saveToDocumentsDirectory(data: NSData, fileName: String): Boolean = 
        withContext(Dispatchers.Default) {
            try {
                // Get Documents directory
                val documentsPath = NSSearchPathForDirectoriesInDomains(
                    NSDocumentDirectory,
                    NSUserDomainMask,
                    true
                ).firstOrNull() as? String
                    ?: throw FileDownloadException("Failed to get Documents directory")
                
                // Create FormatShifter subdirectory
                val formatShifterDir = "$documentsPath/FormatShifter"
                val fileManager = NSFileManager.defaultManager
                
                if (!fileManager.fileExistsAtPath(formatShifterDir)) {
                    val success = fileManager.createDirectoryAtPath(
                        formatShifterDir,
                        withIntermediateDirectories = true,
                        attributes = null,
                        error = null
                    )
                    if (!success) {
                        throw FileDownloadException("Failed to create FormatShifter directory")
                    }
                }
                
                // Write file
                val filePath = "$formatShifterDir/$fileName"
                val success = data.writeToFile(filePath, atomically = true)
                
                if (!success) {
                    throw FileDownloadException("Failed to write file to Documents directory")
                }
                
                true
            } catch (e: Exception) {
                when (e) {
                    is FileDownloadException -> throw e
                    else -> throw FileDownloadException("Failed to save to Documents directory: ${e.message}", e)
                }
            }
        }
    
    /**
     * Extension function to convert ByteArray to NSData
     */
    @OptIn(ExperimentalForeignApi::class)
    private fun ByteArray.toNSData(): NSData {
        return this.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = this.size.toULong())
        }
    }
}