package org.risanjayy.format.shifter.di

import kotlinx.coroutines.suspendCancellableCoroutine
import org.risanjayy.format.shifter.domain.service.FilePicker
import org.risanjayy.format.shifter.domain.service.SelectedFile
import org.risanjayy.format.shifter.platform.IOSImagePicker
import kotlin.coroutines.resume

/**
 * iOS implementation of FilePicker using PHPickerViewController
 */
class IOSFilePicker : FilePicker {
    
    override suspend fun pickImages(allowMultiple: Boolean): List<SelectedFile> = suspendCancellableCoroutine { continuation ->
        val imagePicker = IOSImagePicker { selectedFiles ->
            continuation.resume(selectedFiles)
        }
        
        imagePicker.showPicker(allowMultiple)
    }
}

actual object FilePickerProvider {
    actual fun provide(): FilePicker = IOSFilePicker()
}