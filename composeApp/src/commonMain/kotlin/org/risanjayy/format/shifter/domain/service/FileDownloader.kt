package org.risanjayy.format.shifter.domain.service

import org.risanjayy.format.shifter.domain.model.ConversionTask
import org.risanjayy.format.shifter.domain.model.ImageFormat

/**
 * Interface for downloading converted files to device storage
 */
interface FileDownloader {
    /**
     * Download a single converted file using task data
     * @param task The conversion task containing the converted data
     * @return true if download was successful, false otherwise
     */
    suspend fun downloadFile(task: ConversionTask): Boolean
    
    /**
     * Download a single file with custom data and filename
     * @param data The file data to download
     * @param fileName The filename to save as
     * @param mimeType The MIME type of the file
     * @return true if download was successful, false otherwise
     */
    suspend fun downloadFile(data: ByteArray, fileName: String, mimeType: String): Boolean
    
    /**
     * Download multiple converted files
     * @param tasks List of conversion tasks containing converted data
     * @return number of successfully downloaded files
     */
    suspend fun downloadFiles(tasks: List<ConversionTask>): Int
    
    /**
     * Download multiple files with custom data
     * @param files List of (data, fileName, mimeType) triples
     * @return number of successfully downloaded files
     */
    suspend fun downloadMultipleFiles(files: List<Triple<ByteArray, String, String>>): Int
    
    /**
     * Generate appropriate filename for the converted file
     * @param originalName Original file name
     * @param outputFormat Target format
     * @return Generated filename with proper extension
     */
    fun generateFileName(originalName: String, outputFormat: ImageFormat): String
}

/**
 * Exception thrown when file download fails
 */
class FileDownloadException(message: String, cause: Throwable? = null) : Exception(message, cause)