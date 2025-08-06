package org.risanjayy.format.shifter.di

import org.risanjayy.format.shifter.domain.service.FilePicker

/**
 * Platform-specific provider for FilePicker instances
 * Each platform will implement this to provide their specific FilePicker
 */
expect object FilePickerProvider {
    fun provide(): FilePicker
}