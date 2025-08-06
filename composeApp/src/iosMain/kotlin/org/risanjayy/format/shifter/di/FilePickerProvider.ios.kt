package org.risanjayy.format.shifter.di

import org.risanjayy.format.shifter.domain.service.FilePicker
import org.risanjayy.format.shifter.domain.service.SelectedFile

/**
 * iOS implementation of FilePickerProvider (placeholder)
 */
actual object FilePickerProvider {
    actual fun provide(): FilePicker = IOSFilePicker()
}

private class IOSFilePicker : FilePicker {
    override suspend fun pickImages(allowMultiple: Boolean): List<SelectedFile> {
        // TODO: Implement iOS file picker using UIDocumentPickerViewController
        return emptyList()
    }
}