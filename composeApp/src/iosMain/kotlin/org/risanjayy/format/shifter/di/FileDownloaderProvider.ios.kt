package org.risanjayy.format.shifter.di

import org.risanjayy.format.shifter.domain.service.FileDownloader
import org.risanjayy.format.shifter.domain.service.FileDownloadException
import org.risanjayy.format.shifter.domain.model.ConversionTask
import org.risanjayy.format.shifter.domain.model.ImageFormat

/**
 * iOS implementation of FileDownloaderProvider (placeholder)
 */
actual object FileDownloaderProvider {
    actual fun provide(): FileDownloader = IosFileDownloader()
}

private class IosFileDownloader : FileDownloader {
    override suspend fun downloadFile(task: ConversionTask): Boolean {
        throw FileDownloadException("iOS file download not implemented yet")
    }
    
    override suspend fun downloadFile(data: ByteArray, fileName: String, mimeType: String): Boolean {
        throw FileDownloadException("iOS file download not implemented yet")
    }
    
    override suspend fun downloadFiles(tasks: List<ConversionTask>): Int {
        throw FileDownloadException("iOS file download not implemented yet")
    }
    
    override suspend fun downloadMultipleFiles(files: List<Triple<ByteArray, String, String>>): Int {
        throw FileDownloadException("iOS file download not implemented yet")
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
}