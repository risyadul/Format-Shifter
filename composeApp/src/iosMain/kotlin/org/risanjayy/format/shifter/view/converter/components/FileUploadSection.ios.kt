package org.risanjayy.format.shifter.view.converter.components

import androidx.compose.runtime.Composable
import org.risanjayy.format.shifter.domain.service.SelectedFile

/**
 * iOS implementation of file picker launcher (placeholder)
 */
@Composable
actual fun rememberFilePickerLauncher(
    onFilesSelected: (List<SelectedFile>) -> Unit
): () -> Unit {
    return {
        // TODO: Implement iOS file picker using UIDocumentPickerViewController
        // For now, create mock files for testing
        val mockFiles = listOf(
            SelectedFile(
                name = "sample_ios.heic",
                size = 2 * 1024 * 1024, // 2MB
                mimeType = "image/heic",
                data = byteArrayOf()
            )
        )
        onFilesSelected(mockFiles)
    }
}