package org.risanjayy.format.shifter.view.converter.components

import androidx.compose.runtime.Composable
import org.risanjayy.format.shifter.di.FileDownloaderProvider
import org.risanjayy.format.shifter.domain.service.FileDownloader

/**
 * WASM/JS implementation of rememberFileDownloader
 */
@Composable
actual fun rememberFileDownloader(): FileDownloader {
    return FileDownloaderProvider.provide()
}