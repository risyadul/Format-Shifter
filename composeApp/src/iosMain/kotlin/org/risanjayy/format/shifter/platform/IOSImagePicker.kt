package org.risanjayy.format.shifter.platform

import kotlinx.cinterop.*
import org.risanjayy.format.shifter.domain.service.SelectedFile
import platform.Foundation.*
import platform.UIKit.*
import platform.PhotosUI.*
import platform.darwin.NSObject
import platform.UniformTypeIdentifiers.*

/**
 * iOS implementation of image picker using PHPickerViewController
 */
class IOSImagePicker(
    private val onFilesSelected: (List<SelectedFile>) -> Unit
) : NSObject(), PHPickerViewControllerDelegateProtocol {
    
    fun showPicker(allowMultiple: Boolean = true) {
        val configuration = PHPickerConfiguration().apply {
            // Set selection limit based on allowMultiple parameter
            selectionLimit = if (allowMultiple) 0 else 1 // 0 means unlimited
            
            // Only allow images
            filter = PHPickerFilter.imagesFilter()
            
            // Prefer original format (HEIC)
            preferredAssetRepresentationMode = PHPickerConfigurationAssetRepresentationModeAutomatic
        }
        
        val picker = PHPickerViewController(configuration)
        picker.delegate = this
        
        // Present the picker
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(picker, animated = true, completion = null)
    }
    
    override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
        picker.dismissViewControllerAnimated(true, completion = null)
        
        val results = didFinishPicking.filterIsInstance<PHPickerResult>()
        
        if (results.isEmpty()) {
            onFilesSelected(emptyList())
            return
        }
        
        val selectedFiles = mutableListOf<SelectedFile>()
        var processedCount = 0
        
        results.forEach { result ->
            val itemProvider = result.itemProvider
            
            if (itemProvider.hasItemConformingToTypeIdentifier("public.heic")) {
                itemProvider.loadDataRepresentationForTypeIdentifier("public.heic") { data, error ->
                    if (error == null && data != null) {
                        val bytes = data.toByteArray()
                        
                        val fileName = "image_${kotlin.random.Random.nextLong()}.heic"
                        val selectedFile = SelectedFile(
                            name = fileName,
                            size = data.length.toLong(),
                            mimeType = "image/heic",
                            data = bytes
                        )
                        selectedFiles.add(selectedFile)
                    }
                    
                    processedCount++
                    if (processedCount == results.size) {
                        onFilesSelected(selectedFiles)
                    }
                }
            } else {
                processedCount++
                if (processedCount == results.size) {
                    onFilesSelected(selectedFiles)
                }
            }
        }
    }
}

// Extension function to convert NSData to ByteArray
@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val length = this.length.toInt()
    val byteArray = ByteArray(length)
    
    if (length > 0) {
        byteArray.usePinned { pinned ->
            platform.posix.memcpy(pinned.addressOf(0), this.bytes, this.length)
        }
    }
    
    return byteArray
}