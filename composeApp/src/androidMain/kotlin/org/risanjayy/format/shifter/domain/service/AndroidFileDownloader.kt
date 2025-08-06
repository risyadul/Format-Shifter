package org.risanjayy.format.shifter.domain.service

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.risanjayy.format.shifter.domain.model.ConversionTask
import org.risanjayy.format.shifter.domain.model.ConversionStatus
import org.risanjayy.format.shifter.domain.model.ImageFormat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Android implementation of FileDownloader
 * Uses MediaStore API for Android 10+ and legacy external storage for older versions
 */
class AndroidFileDownloader(private val context: Context) : FileDownloader {
    
    override suspend fun downloadFile(task: ConversionTask): Boolean {
        return try {
            if (task.outputData == null) return false
            
            val fileName = generateFileName(task.fileName, task.outputFormat)
            val mimeType = when (task.outputFormat) {
                ImageFormat.PNG -> "image/png"
                ImageFormat.JPG -> "image/jpeg"
                else -> "application/octet-stream"
            }
            
            downloadFileInternal(task.outputData, fileName, mimeType)
        } catch (e: Exception) {
            throw FileDownloadException("Failed to download file: ${e.message}", e)
        }
    }
    
    override suspend fun downloadFile(data: ByteArray, fileName: String, mimeType: String): Boolean {
        return try {
            downloadFileInternal(data, fileName, mimeType)
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
            else -> "bin"
        }
        return "${nameWithoutExtension}_converted.$extension"
    }
    
    private suspend fun downloadFileInternal(
        data: ByteArray,
        fileName: String,
        mimeType: String
    ): Boolean = withContext(Dispatchers.IO) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            downloadUsingMediaStore(data, fileName, mimeType)
        } else {
            downloadUsingLegacyStorage(data, fileName)
        }
        true
    }
    
    private suspend fun downloadUsingMediaStore(
        data: ByteArray,
        fileName: String,
        mimeType: String
    ) = withContext(Dispatchers.IO) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/FormatShifter")
        }
        
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: throw IOException("Failed to create MediaStore entry")
        
        resolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(data)
            outputStream.flush()
        } ?: throw IOException("Failed to open output stream")
    }
    
    private suspend fun downloadUsingLegacyStorage(
        data: ByteArray,
        fileName: String
    ) = withContext(Dispatchers.IO) {
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val formatShifterDir = File(picturesDir, "FormatShifter")
        
        if (!formatShifterDir.exists()) {
            formatShifterDir.mkdirs()
        }
        
        val file = File(formatShifterDir, fileName)
        FileOutputStream(file).use { outputStream ->
            outputStream.write(data)
            outputStream.flush()
        }
        
        // Notify media scanner about the new file
        val mediaScanIntent = android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        mediaScanIntent.data = android.net.Uri.fromFile(file)
        context.sendBroadcast(mediaScanIntent)
    }
}