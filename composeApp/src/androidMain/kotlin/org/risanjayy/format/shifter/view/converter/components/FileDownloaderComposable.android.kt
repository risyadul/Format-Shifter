package org.risanjayy.format.shifter.view.converter.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.risanjayy.format.shifter.di.FileDownloaderProvider
import org.risanjayy.format.shifter.domain.service.FileDownloader

/**
 * Android implementation of rememberFileDownloader
 */
@Composable
actual fun rememberFileDownloader(): FileDownloader {
    val context = LocalContext.current
    return FileDownloaderProvider.provide(context)
}