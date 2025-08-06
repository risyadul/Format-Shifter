package org.risanjayy.format.shifter.di

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.risanjayy.format.shifter.domain.model.ImageFormat
import org.risanjayy.format.shifter.domain.service.ImageConversionException
import org.risanjayy.format.shifter.domain.service.ImageConverter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * Android implementation of ImageConverterProvider
 */
actual object ImageConverterProvider {
    actual fun provide(): ImageConverter = AndroidImageConverter()
}

/**
 * Android-specific image converter using Android's Bitmap APIs
 */
class AndroidImageConverter : ImageConverter {
    
    override suspend fun convertImage(
        inputData: ByteArray,
        inputFormat: ImageFormat,
        outputFormat: ImageFormat,
        quality: Int
    ): ByteArray = withContext(Dispatchers.IO) {
        try {
            // Decode input image to Bitmap
            val originalBitmap = BitmapFactory.decodeByteArray(inputData, 0, inputData.size)
                ?: throw ImageConversionException("Failed to decode input image")
            
            // Handle EXIF orientation to prevent unwanted rotation
            val correctedBitmap = correctImageOrientation(inputData, originalBitmap)
            
            // Convert to output format
            val outputStream = ByteArrayOutputStream()
            val compressFormat = when (outputFormat) {
                ImageFormat.PNG -> Bitmap.CompressFormat.PNG
                ImageFormat.JPG -> Bitmap.CompressFormat.JPEG
                ImageFormat.HEIC -> throw ImageConversionException("HEIC output format not supported on Android")
            }
            
            val compressQuality = if (outputFormat == ImageFormat.PNG) 100 else quality.coerceIn(1, 100)
            
            val success = correctedBitmap.compress(compressFormat, compressQuality, outputStream)
            if (!success) {
                throw ImageConversionException("Failed to compress image to $outputFormat")
            }
            
            // Clean up bitmaps
            if (correctedBitmap != originalBitmap) {
                correctedBitmap.recycle()
            }
            originalBitmap.recycle()
            
            outputStream.toByteArray()
            
        } catch (e: Exception) {
            when (e) {
                is ImageConversionException -> throw e
                else -> throw ImageConversionException("Image conversion failed: ${e.message}", e)
            }
        }
    }
    
    /**
     * Corrects image orientation based on EXIF data
     */
    private fun correctImageOrientation(imageData: ByteArray, bitmap: Bitmap): Bitmap {
        return try {
            val inputStream = ByteArrayInputStream(imageData)
            val exif = ExifInterface(inputStream)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            
            val rotationDegrees = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> 0f // Handle flip separately if needed
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> 0f // Handle flip separately if needed
                ExifInterface.ORIENTATION_TRANSPOSE -> 90f // Rotate 90 + flip
                ExifInterface.ORIENTATION_TRANSVERSE -> 270f // Rotate 270 + flip
                else -> 0f
            }
            
            if (rotationDegrees == 0f) {
                // No rotation needed
                bitmap
            } else {
                // Apply rotation
                val matrix = Matrix()
                matrix.postRotate(rotationDegrees)
                
                // Handle flipping for transpose/transverse orientations
                when (orientation) {
                    ExifInterface.ORIENTATION_FLIP_HORIZONTAL,
                    ExifInterface.ORIENTATION_TRANSPOSE -> {
                        matrix.postScale(-1f, 1f)
                    }
                    ExifInterface.ORIENTATION_FLIP_VERTICAL,
                    ExifInterface.ORIENTATION_TRANSVERSE -> {
                        matrix.postScale(1f, -1f)
                    }
                }
                
                Bitmap.createBitmap(
                    bitmap, 0, 0, 
                    bitmap.width, bitmap.height, 
                    matrix, true
                )
            }
        } catch (e: Exception) {
            // If EXIF reading fails, return original bitmap
            bitmap
        }
    }
}