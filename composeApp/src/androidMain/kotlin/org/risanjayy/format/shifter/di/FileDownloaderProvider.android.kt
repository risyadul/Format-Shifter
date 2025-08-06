package org.risanjayy.format.shifter.di

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.risanjayy.format.shifter.domain.service.AndroidFileDownloader
import org.risanjayy.format.shifter.domain.service.FileDownloader

/**
 * Android implementation of FileDownloaderProvider
 */
actual object FileDownloaderProvider {
    actual fun provide(): FileDownloader {
        throw IllegalStateException("Use provide(context) for Android implementation")
    }
    
    fun provide(context: Context): FileDownloader {
        return AndroidFileDownloader(context)
    }
}

/**
 * Composable helper to get FileDownloader with Android context
 */
@Composable
fun rememberFileDownloader(): FileDownloader {
    val context = LocalContext.current
    return FileDownloaderProvider.provide(context)
}