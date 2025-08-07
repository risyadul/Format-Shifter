package org.risanjayy.format.shifter.view.converter.components

import androidx.compose.runtime.*
import org.risanjayy.format.shifter.domain.service.SelectedFile
import org.risanjayy.format.shifter.platform.IOSImagePicker

@Composable
actual fun rememberFilePickerLauncher(
    onFilesSelected: (List<SelectedFile>) -> Unit
): () -> Unit {
    // Create and remember the iOS image picker with callback
    val imagePicker = remember { IOSImagePicker(onFilesSelected) }
    
    return {
        // Use PHPickerViewController to pick images
        imagePicker.showPicker()
    }
}