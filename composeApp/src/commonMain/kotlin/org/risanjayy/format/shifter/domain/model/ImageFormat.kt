package org.risanjayy.format.shifter.domain.model

/**
 * Supported image formats for conversion
 */
enum class ImageFormat(val extension: String, val displayName: String, val mimeType: String) {
    HEIC("heic", "HEIC", "image/heic"),
    PNG("png", "PNG", "image/png"),
    JPG("jpg", "JPEG", "image/jpeg");
    
    companion object {
        fun fromExtension(extension: String): ImageFormat? {
            return values().find { it.extension.equals(extension, ignoreCase = true) }
        }
    }
}