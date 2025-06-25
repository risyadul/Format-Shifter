package org.risanjayy.format.shifter.view.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Color palette for Format Shifter
 */
object AppColors {
    // Theme Colors
    val Background = Color(0xFFF8F9FA) // Putih Gading
    val Surface = Color(0xFFFFFFFF) // Putih Murni
    val PrimaryText = Color(0xFF212529) // Abu-abu Sangat Gelap

    // Accent Colors (tambahan untuk membuat UI lebih menarik)
    val Primary = Color(0xFF206BC4) // Biru
    val Secondary = Color(0xFF6C757D) // Abu-abu netral
    val Error = Color(0xFFDC3545) // Merah
    val Success = Color(0xFF198754) // Hijau
    val Warning = Color(0xFFFFC107) // Kuning
    val Info = Color(0xFF0DCAF0) // Biru muda
}

/**
 * Light color scheme for the application
 */
private val LightColorScheme = lightColorScheme(
    primary = AppColors.Primary,
    onPrimary = Color.White,
    secondary = AppColors.Secondary,
    onSecondary = Color.White,
    background = AppColors.Background,
    onBackground = AppColors.PrimaryText,
    surface = AppColors.Surface,
    onSurface = AppColors.PrimaryText,
    error = AppColors.Error,
    onError = Color.White
)

/**
 * Dark color scheme for the application
 * Note: Currently using the same light scheme, can be customized later
 */
private val DarkColorScheme = LightColorScheme

/**
 * Format Shifter theme
 * @param darkTheme Whether to use dark theme. Default: based on system theme
 * @param content Content to be displayed with this theme
 */
@Composable
fun FormatShifterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
