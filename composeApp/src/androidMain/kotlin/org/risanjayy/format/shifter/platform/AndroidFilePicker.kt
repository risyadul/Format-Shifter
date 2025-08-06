package org.risanjayy.format.shifter.platform

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.suspendCancellableCoroutine
import org.risanjayy.format.shifter.domain.service.FilePicker
import org.risanjayy.format.shifter.domain.service.SelectedFile
import kotlin.coroutines.resume

/**
 * Android implementation of FilePicker using Activity Result API
 */
class AndroidFilePicker(
    private val activity: ComponentActivity
) : FilePicker {
    
    private var filePickerLauncher: ActivityResultLauncher<String>? = null
    private var multipleFilePickerLauncher: ActivityResultLauncher<String>? = null
    
    init {
        setupLaunchers()
    }
    
    private fun setupLaunchers() {
        // Single file picker
        filePickerLauncher = activity.registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            currentSingleCallback?.invoke(uri)
            currentSingleCallback = null
        }
        
        // Multiple file picker
        multipleFilePickerLauncher = activity.registerForActivityResult(
            ActivityResultContracts.GetMultipleContents()
        ) { uris ->
            currentMultipleCallback?.invoke(uris)
            currentMultipleCallback = null
        }
    }
    
    private var currentSingleCallback: ((Uri?) -> Unit)? = null
    private var currentMultipleCallback: ((List<Uri>) -> Unit)? = null
    
    override suspend fun pickImages(allowMultiple: Boolean): List<SelectedFile> {
        return suspendCancellableCoroutine { continuation ->
            if (allowMultiple) {
                currentMultipleCallback = { uris ->
                    val files = uris.mapNotNull { uri ->
                        try {
                            uriToSelectedFile(uri)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    continuation.resume(files)
                }
                
                // Launch multiple file picker with HEIC/image filter
                multipleFilePickerLauncher?.launch("image/*")
            } else {
                currentSingleCallback = { uri ->
                    val files = if (uri != null) {
                        try {
                            listOf(uriToSelectedFile(uri))
                        } catch (e: Exception) {
                            emptyList()
                        }
                    } else {
                        emptyList()
                    }
                    continuation.resume(files)
                }
                
                // Launch single file picker with HEIC/image filter
                filePickerLauncher?.launch("image/*")
            }
            
            continuation.invokeOnCancellation {
                currentSingleCallback = null
                currentMultipleCallback = null
            }
        }
    }
    
    private fun uriToSelectedFile(uri: Uri): SelectedFile {
        val context = activity.applicationContext
        val contentResolver = context.contentResolver
        
        // Get file name and size
        var fileName = "unknown"
        var fileSize = 0L
        
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex) ?: "unknown"
                }
                if (sizeIndex != -1) {
                    fileSize = cursor.getLong(sizeIndex)
                }
            }
        }
        
        // Get MIME type
        val mimeType = contentResolver.getType(uri)
        
        // Read file data
        val data = contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.readBytes()
        } ?: byteArrayOf()
        
        return SelectedFile(
            name = fileName,
            size = fileSize,
            mimeType = mimeType,
            data = data
        )
    }
}