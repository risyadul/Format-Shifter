package org.risanjayy.format.shifter.di

import org.risanjayy.format.shifter.domain.service.ImageConverter

/**
 * Platform-specific provider for ImageConverter implementations
 */
expect object ImageConverterProvider {
    fun provide(): ImageConverter
}