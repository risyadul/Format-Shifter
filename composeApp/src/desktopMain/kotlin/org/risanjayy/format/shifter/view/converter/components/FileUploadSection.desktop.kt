package org.risanjayy.format.shifter.view.converter.components

import androidx.compose.runtime.Composable
import org.risanjayy.format.shifter.domain.service.SelectedFile

/**
 * Desktop implementation of file picker launcher (placeholder)
 */
@Composable
actual fun rememberFilePickerLauncher(
    onFilesSelected: (List<SelectedFile>) -> Unit
): () -> Unit {
    return {
        // TODO: Implement desktop file picker using JFileChooser
        // For now, create mock files for testing
        val mockFiles = listOf(
            SelectedFile(
                name = "sample_desktop.heic",
                size = 1024 * 1024, // 1MB
                mimeType = "image/heic",
                data = byteArrayOf()
            )
        )
        onFilesSelected(mockFiles)
    }
}