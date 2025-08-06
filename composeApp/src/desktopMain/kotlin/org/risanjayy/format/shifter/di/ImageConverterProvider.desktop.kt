package org.risanjayy.format.shifter.di

import org.risanjayy.format.shifter.domain.model.ImageFormat
import org.risanjayy.format.shifter.domain.service.ImageConverter
import org.risanjayy.format.shifter.domain.service.ImageConversionException

/**
 * Desktop implementation of ImageConverterProvider (placeholder)
 */
actual object ImageConverterProvider {
    actual fun provide(): ImageConverter = DesktopImageConverter()
}

/**
 * Desktop-specific image converter (placeholder implementation)
 */
class DesktopImageConverter : ImageConverter {
    
    override suspend fun convertImage(
        inputData: ByteArray,
        inputFormat: ImageFormat,
        outputFormat: ImageFormat,
        quality: Int
    ): ByteArray {
        // TODO: Implement desktop image conversion using Java ImageIO or other libraries
        throw ImageConversionException("Desktop image conversion not yet implemented")
    }
}