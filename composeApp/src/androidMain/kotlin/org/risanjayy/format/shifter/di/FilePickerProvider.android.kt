package org.risanjayy.format.shifter.di

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.risanjayy.format.shifter.domain.service.FilePicker
import org.risanjayy.format.shifter.platform.AndroidFilePicker

/**
 * Android implementation of FilePickerProvider
 */
actual object FilePickerProvider {
    private var currentFilePicker: FilePicker? = null
    
    actual fun provide(): FilePicker {
        return currentFilePicker ?: throw IllegalStateException(
            "FilePicker not initialized. Call initialize() first."
        )
    }
    
    fun initialize(activity: ComponentActivity) {
        currentFilePicker = AndroidFilePicker(activity)
    }
}

/**
 * Composable function to get FilePicker instance in Android
 */
@Composable
fun rememberFilePicker(): FilePicker {
    val context = LocalContext.current
    return if (context is ComponentActivity) {
        FilePickerProvider.initialize(context)
        FilePickerProvider.provide()
    } else {
        throw IllegalStateException("Context must be ComponentActivity to use FilePicker")
    }
}