package org.risanjayy.format.shifter.domain.model

/**
 * Represents a single image conversion task
 */
data class ConversionTask(
    val id: String,
    val fileName: String,
    val inputFormat: ImageFormat,
    val outputFormat: ImageFormat,
    val quality: Int = 90, // 1-100 for JPEG quality
    val status: ConversionStatus = ConversionStatus.PENDING,
    val inputData: ByteArray? = null,
    val outputData: ByteArray? = null,
    val errorMessage: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ConversionTask

        if (id != other.id) return false
        if (fileName != other.fileName) return false
        if (inputFormat != other.inputFormat) return false
        if (outputFormat != other.outputFormat) return false
        if (quality != other.quality) return false
        if (status != other.status) return false
        if (inputData != null) {
            if (other.inputData == null) return false
            if (!inputData.contentEquals(other.inputData)) return false
        } else if (other.inputData != null) return false
        if (outputData != null) {
            if (other.outputData == null) return false
            if (!outputData.contentEquals(other.outputData)) return false
        } else if (other.outputData != null) return false
        if (errorMessage != other.errorMessage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + inputFormat.hashCode()
        result = 31 * result + outputFormat.hashCode()
        result = 31 * result + quality
        result = 31 * result + status.hashCode()
        result = 31 * result + (inputData?.contentHashCode() ?: 0)
        result = 31 * result + (outputData?.contentHashCode() ?: 0)
        result = 31 * result + (errorMessage?.hashCode() ?: 0)
        return result
    }
}

/**
 * Status of conversion task
 */
enum class ConversionStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED
}