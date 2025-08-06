package org.risanjayy.format.shifter.di

import org.risanjayy.format.shifter.domain.service.FilePicker
import org.risanjayy.format.shifter.domain.service.SelectedFile

/**
 * WASM/JS implementation of FilePickerProvider (placeholder)
 */
actual object FilePickerProvider {
    actual fun provide(): FilePicker = WasmFilePicker()
}

private class WasmFilePicker : FilePicker {
    override suspend fun pickImages(allowMultiple: Boolean): List<SelectedFile> {
        // TODO: Implement web file picker using HTML input element
        return emptyList()
    }
}