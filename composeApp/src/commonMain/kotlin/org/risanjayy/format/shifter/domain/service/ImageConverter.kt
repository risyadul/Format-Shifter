package org.risanjayy.format.shifter.domain.service

import org.risanjayy.format.shifter.domain.model.ImageFormat

/**
 * Platform-specific image converter interface
 * Handles image format conversion across different platforms
 */
interface ImageConverter {
    /**
     * Converts image data from one format to another
     * @param inputData The input image data as byte array
     * @param inputFormat The format of the input image
     * @param outputFormat The desired output format
     * @param quality Quality setting for JPEG (1-100), ignored for PNG
     * @return Converted image data as byte array
     * @throws ImageConversionException if conversion fails
     */
    suspend fun convertImage(
        inputData: ByteArray,
        inputFormat: ImageFormat,
        outputFormat: ImageFormat,
        quality: Int = 90
    ): ByteArray
}

/**
 * Exception thrown when image conversion fails
 */
class ImageConversionException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Result of image conversion operation
 */
data class ConversionResult(
    val success: Boolean,
    val data: ByteArray? = null,
    val error: String? = null,
    val outputFileName: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ConversionResult

        if (success != other.success) return false
        if (data != null) {
            if (other.data == null) return false
            if (!data.contentEquals(other.data)) return false
        } else if (other.data != null) return false
        if (error != other.error) return false
        if (outputFileName != other.outputFileName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = success.hashCode()
        result = 31 * result + (data?.contentHashCode() ?: 0)
        result = 31 * result + (error?.hashCode() ?: 0)
        result = 31 * result + (outputFileName?.hashCode() ?: 0)
        return result
    }
}