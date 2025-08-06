package org.risanjayy.format.shifter.domain.service

/**
 * Platform-specific file picker interface
 * Handles file selection across different platforms (Android, iOS, Desktop, Web)
 */
interface FilePicker {
    /**
     * Opens file picker to select HEIC images
     * @param allowMultiple Whether to allow multiple file selection
     * @return List of selected file data or empty list if cancelled
     */
    suspend fun pickImages(allowMultiple: Boolean = true): List<SelectedFile>
}

/**
 * Represents a selected file with its metadata
 */
data class SelectedFile(
    val name: String,
    val size: Long,
    val mimeType: String?,
    val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SelectedFile

        if (name != other.name) return false
        if (size != other.size) return false
        if (mimeType != other.mimeType) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + (mimeType?.hashCode() ?: 0)
        result = 31 * result + data.contentHashCode()
        return result
    }
}