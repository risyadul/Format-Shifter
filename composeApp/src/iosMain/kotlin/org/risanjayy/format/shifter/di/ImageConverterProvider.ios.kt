package org.risanjayy.format.shifter.di

import org.risanjayy.format.shifter.domain.model.ImageFormat
import org.risanjayy.format.shifter.domain.service.ImageConverter
import org.risanjayy.format.shifter.domain.service.ImageConversionException

/**
 * iOS implementation of ImageConverterProvider (placeholder)
 */
actual object ImageConverterProvider {
    actual fun provide(): ImageConverter = IOSImageConverter()
}

/**
 * iOS-specific image converter (placeholder implementation)
 */
class IOSImageConverter : ImageConverter {
    
    override suspend fun convertImage(
        inputData: ByteArray,
        inputFormat: ImageFormat,
        outputFormat: ImageFormat,
        quality: Int
    ): ByteArray {
        // TODO: Implement iOS image conversion using UIImage and Core Graphics
        throw ImageConversionException("iOS image conversion not yet implemented")
    }
}