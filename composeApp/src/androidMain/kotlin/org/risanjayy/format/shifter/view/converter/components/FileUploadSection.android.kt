package org.risanjayy.format.shifter.view.converter.components

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.suspendCancellableCoroutine
import org.risanjayy.format.shifter.domain.service.SelectedFile
import kotlin.coroutines.resume

/**
 * Android implementation of file picker launcher using Activity Result API
 */
@Composable
actual fun rememberFilePickerLauncher(
    onFilesSelected: (List<SelectedFile>) -> Unit
): () -> Unit {
    val context = LocalContext.current
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        val files = uris.mapNotNull { uri ->
            try {
                uriToSelectedFile(uri, context)
            } catch (e: Exception) {
                null
            }
        }
        onFilesSelected(files)
    }
    
    return {
        launcher.launch("image/*")
    }
}

private fun uriToSelectedFile(uri: Uri, context: android.content.Context): SelectedFile {
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