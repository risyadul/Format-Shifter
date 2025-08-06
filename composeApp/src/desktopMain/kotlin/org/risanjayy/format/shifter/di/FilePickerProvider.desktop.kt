package org.risanjayy.format.shifter.di

import org.risanjayy.format.shifter.domain.service.FilePicker
import org.risanjayy.format.shifter.domain.service.SelectedFile

/**
 * Desktop implementation of FilePickerProvider (placeholder)
 */
actual object FilePickerProvider {
    actual fun provide(): FilePicker = DesktopFilePicker()
}

private class DesktopFilePicker : FilePicker {
    override suspend fun pickImages(allowMultiple: Boolean): List<SelectedFile> {
        // TODO: Implement desktop file picker using JFileChooser or similar
        return emptyList()
    }
}