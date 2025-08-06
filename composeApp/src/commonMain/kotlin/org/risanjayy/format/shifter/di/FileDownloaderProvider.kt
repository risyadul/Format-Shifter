package org.risanjayy.format.shifter.di

import org.risanjayy.format.shifter.domain.service.FileDownloader

/**
 * Provider for FileDownloader implementation
 * Platform-specific implementations will be provided via expect/actual
 */
expect object FileDownloaderProvider {
    fun provide(): FileDownloader
}