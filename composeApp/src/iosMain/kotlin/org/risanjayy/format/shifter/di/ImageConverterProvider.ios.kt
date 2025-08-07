package org.risanjayy.format.shifter.di

import kotlinx.cinterop.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.risanjayy.format.shifter.domain.model.ImageFormat
import org.risanjayy.format.shifter.domain.service.ImageConverter
import org.risanjayy.format.shifter.domain.service.ImageConversionException
import platform.CoreFoundation.*
import platform.CoreGraphics.*
import platform.Foundation.*
import platform.ImageIO.*
import platform.UIKit.*
import platform.UniformTypeIdentifiers.*

/**
 * iOS implementation of ImageConverterProvider using UIImage and ImageIO
 */
actual object ImageConverterProvider {
    actual fun provide(): ImageConverter = IOSImageConverter()
}

/**
 * iOS-specific image converter using UIImage and Core Graphics
 */
class IOSImageConverter : ImageConverter {
    
    @OptIn(ExperimentalForeignApi::class)
    override suspend fun convertImage(
        inputData: ByteArray,
        inputFormat: ImageFormat,
        outputFormat: ImageFormat,
        quality: Int
    ): ByteArray = withContext(Dispatchers.Default) {
        try {
            // Convert ByteArray to NSData
            val nsData = inputData.toNSData()
            
            // Create UIImage from NSData
            val image = UIImage.imageWithData(nsData)
                ?: throw ImageConversionException("Failed to create UIImage from input data")
            
            // Convert based on output format
            val outputData = when (outputFormat) {
                ImageFormat.PNG -> convertToPNG(image)
                ImageFormat.JPG -> convertToJPEG(image, quality)
                ImageFormat.HEIC -> convertToHEIC(image, quality)
            }
            
            // Convert NSData back to ByteArray
            return@withContext outputData.toByteArray()
            
        } catch (e: Exception) {
            when (e) {
                is ImageConversionException -> throw e
                else -> throw ImageConversionException("Image conversion failed: ${e.message}", e)
            }
        }
    }
    
    /**
     * Convert UIImage to PNG format
     */
    @OptIn(ExperimentalForeignApi::class)
    private fun convertToPNG(image: UIImage): NSData {
        return UIImagePNGRepresentation(image)
            ?: throw ImageConversionException("Failed to convert image to PNG format")
    }
    
    /**
     * Convert UIImage to JPEG format with quality
     */
    @OptIn(ExperimentalForeignApi::class)
    private fun convertToJPEG(image: UIImage, quality: Int): NSData {
        val jpegQuality = (quality.coerceIn(1, 100) / 100.0)
        return UIImageJPEGRepresentation(image, jpegQuality)
            ?: throw ImageConversionException("Failed to convert image to JPEG format")
    }
    
    /**
     * Convert UIImage to HEIC format with quality using ImageIO
     */
    @OptIn(ExperimentalForeignApi::class)
    private fun convertToHEIC(image: UIImage, quality: Int): NSData {
        // Create mutable data for output
        val outputData = NSMutableData()
        
        // Create image destination for HEIC
        val destination = CGImageDestinationCreateWithData(
            outputData as CFMutableDataRef,
            UTTypeHEIC.identifier as CFStringRef,
            1u,
            null
        ) ?: throw ImageConversionException("Failed to create HEIC image destination")
        
        // Get CGImage from UIImage
        val cgImage = image.CGImage
            ?: throw ImageConversionException("Failed to get CGImage from UIImage")
        
        // Create properties dictionary for quality
        val properties = CFDictionaryCreateMutable(
            kCFAllocatorDefault,
            0,
            null,
            null
        )
        
        // Set HEIC quality (0.0 to 1.0)
        val heicQuality = (quality.coerceIn(1, 100) / 100.0)
        memScoped {
            val qualityValue = alloc<DoubleVar>()
            qualityValue.value = heicQuality
            val qualityNumber = CFNumberCreate(
                kCFAllocatorDefault,
                kCFNumberDoubleType,
                qualityValue.ptr
            )
            CFDictionarySetValue(
                properties,
                kCGImageDestinationLossyCompressionQuality,
                qualityNumber
            )
            
            // Add image to destination with properties
            CGImageDestinationAddImage(destination, cgImage, properties)
            
            // Finalize the destination
            val success = CGImageDestinationFinalize(destination)
            
            // Clean up
            CFRelease(properties)
            CFRelease(qualityNumber)
            CFRelease(destination)
            
            if (!success) {
                throw ImageConversionException("Failed to finalize HEIC image conversion")
            }
        }
        
        return outputData.copy() as NSData
    }
}

/**
 * Extension function to convert ByteArray to NSData
 */
@OptIn(ExperimentalForeignApi::class)
private fun ByteArray.toNSData(): NSData {
    if (isEmpty()) return NSData()
    
    return usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = size.toULong())
    }
}

/**
 * Extension function to convert NSData to ByteArray
 */
@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val length = this.length.toInt()
    val byteArray = ByteArray(length)
    
    if (length > 0) {
        byteArray.usePinned { pinned ->
            platform.posix.memcpy(pinned.addressOf(0), this.bytes, this.length)
        }
    }
    
    return byteArray
}