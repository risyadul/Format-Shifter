package org.risanjayy.format.shifter.view.converter.components

import androidx.compose.runtime.Composable
import org.risanjayy.format.shifter.domain.service.SelectedFile

/**
 * WASM/JS implementation of file picker launcher (placeholder)
 */
@Composable
actual fun rememberFilePickerLauncher(
    onFilesSelected: (List<SelectedFile>) -> Unit
): () -> Unit {
    return {
        // TODO: Implement web file picker using HTML input element
        // For now, create mock files for testing
        val mockFiles = listOf(
            SelectedFile(
                name = "sample_web.heic",
                size = 1536 * 1024, // 1.5MB
                mimeType = "image/heic",
                data = byteArrayOf()
            )
        )
        onFilesSelected(mockFiles)
    }
}