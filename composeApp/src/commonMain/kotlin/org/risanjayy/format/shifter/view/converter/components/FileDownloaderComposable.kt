package org.risanjayy.format.shifter.view.converter.components

import androidx.compose.runtime.Composable
import org.risanjayy.format.shifter.domain.service.FileDownloader

/**
 * Platform-specific composable to get FileDownloader instance
 */
@Composable
expect fun rememberFileDownloader(): FileDownloader