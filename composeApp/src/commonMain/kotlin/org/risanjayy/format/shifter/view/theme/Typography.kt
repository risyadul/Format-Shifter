package org.risanjayy.format.shifter.view.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import format_shifter.composeapp.generated.resources.Poppins_Bold
import format_shifter.composeapp.generated.resources.Poppins_Light
import format_shifter.composeapp.generated.resources.Poppins_Medium
import format_shifter.composeapp.generated.resources.Poppins_Regular
import format_shifter.composeapp.generated.resources.Poppins_SemiBold
import format_shifter.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

/**
 * Object containing font definitions for the Format Shifter application.
 * This object provides access to the Poppins font family with different weights.
 */
object FSFonts {
    /**
     * The Poppins font family with various weights (Regular, Medium, Bold, Light).
     * This property must be used within a @Composable function.
     */
    val poppins: FontFamily
        @Composable
        get() = FontFamily(
            Font(
                resource = Res.font.Poppins_Regular,
                weight = FontWeight.Normal
            ),
            Font(
                resource = Res.font.Poppins_Medium,
                weight = FontWeight.Medium
            ),
            Font(
                resource = Res.font.Poppins_Bold,
                weight = FontWeight.Bold
            ),
            Font(
                resource = Res.font.Poppins_Light,
                weight = FontWeight.Light
            ),
            Font(
                resource = Res.font.Poppins_SemiBold,
                weight = FontWeight.SemiBold
            )
        )
}

/**
 * Object containing predefined text styles for the Format Shifter application.
 * This object provides a comprehensive set of text styles with different sizes and weights.
 *
 * Text styles are organized in the following categories:
 * - H1-H5: Heading styles (60sp to 20sp)
 * - Subtitle/Body: Content styles (16sp to 10sp)
 * - Button and Link: Special purpose styles
 *
 * Each category has variations in weight:
 * - Bold (700)
 * - SemiBold (600)
 * - Medium (500)
 * - Regular (400)
 *
 * Usage example:
 * ```
 * Text(
 *     text = "Hello World",
 *     style = FSTextStyle.h1Bold
 * )
 * ```
 */
object FSTextStyle {
    // H1 Styles (60sp)
    /**
     * Largest heading style with bold weight
     * Size: 60sp, Weight: Bold
     */
    val h1Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 60.sp
        )

    val h1SemiBold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 60.sp
        )

    val h1Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 60.sp
        )

    val h1Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 60.sp
        )

    // H1 Light Style
    val h1Light: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Light,
            fontSize = 60.sp
        )

    // H2 Styles (48sp)
    /**
     * Second largest heading style
     * Size: 48sp, with various weights
     */
    val h2Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp
        )

    val h2SemiBold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 48.sp
        )

    val h2Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 48.sp
        )

    val h2Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 48.sp
        )

    // H2 Light Style
    val h2Light: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Light,
            fontSize = 48.sp
        )

    // H3 Styles (34sp)
    /**
     * Medium heading style
     * Size: 34sp, with various weights
     */
    val h3Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp
        )

    val h3SemiBold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 34.sp
        )

    val h3Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 34.sp
        )

    val h3Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 34.sp
        )

    // H3 Light Style
    val h3Light: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Light,
            fontSize = 34.sp
        )

    // H4 Styles (24sp)
    /**
     * Small heading style
     * Size: 24sp, with various weights
     */
    val h4Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

    val h4SemiBold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp
        )

    val h4Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp
        )

    val h4Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp
        )

    // H4 Light Style
    val h4Light: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Light,
            fontSize = 24.sp
        )

    // H5 Styles (20sp)
    /**
     * Smallest heading style
     * Size: 20sp, with various weights
     */
    val h5Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

    val h5SemiBold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )

    val h5Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp
        )

    val h5Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        )

    // H5 Light Style
    val h5Light: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Light,
            fontSize = 20.sp
        )
        
    // H6 Styles (16sp)
    /**
     * Smallest heading style
     * Size: 16sp, with various weights
     */
    val h6Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        
    val h6SemiBold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
        
    val h6Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        
    val h6Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
        
    // H6 Light Style
    val h6Light: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Light,
            fontSize = 16.sp
        )

    // Body and Subtitle Styles (16sp)
    /**
     * Primary content text style
     * Size: 16sp, with various weights
     * Used for main content and important information
     */
    val subtitle1Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

    val body1Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

    val subtitle1SemiBold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

    val body1SemiBold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

    val subtitle1Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

    val subtitle1Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )

    val body1Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )

    // Body1 Light Style
    val body1Light: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Light,
            fontSize = 16.sp
        )

    val subtitle1Light: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Light,
            fontSize = 16.sp
        )

    // Body2 and Subtitle2 Styles (14sp)
    /**
     * Secondary content text style
     * Size: 14sp, with various weights
     * Used for less prominent content
     */
    val subtitle2Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

    val body2Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

    val subtitle2Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )

    val body2Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )

    val subtitle2Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )

    val body2Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )

    val subtitle2SemiBold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )

    val body2SemiBold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )

    // Body2 Light Style
    val body2Light: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp
        )

    val subtitle2Light: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp
        )

    // Button Styles
    /**
     * Button text style
     * Size: 14sp, Weight: SemiBold
     * Specifically designed for button labels
     */
    val button: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )

    // Link Text Styles
    /**
     * Hyperlink text styles
     * Available in two sizes: 14sp (linkText1) and 12sp (linkText2)
     * Weight: SemiBold
     */
    val linkText1: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )

    val linkText2: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )

    // Body3 Styles (12sp)
    /**
     * Small text style
     * Size: 12sp, with various weights
     * Used for captions, footnotes, and supporting text
     */
    val body3Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )

    val body3SemiBold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )

    val body3Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )

    val body3Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )

    // Body3 Light Style
    val body3Light: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp
        )

    // Body4 Styles (10sp)
    /**
     * Smallest text style
     * Size: 10sp, with various weights
     * Used for very small labels or metadata
     */
    val body4Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )

    val body4SemiBold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp
        )

    val body4Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp
        )

    val body4Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp
        )

    // Body4 Light Style
    val body4Light: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Light,
            fontSize = 10.sp
        )

    // Caption Styles (12sp)
    /**
     * Caption text style
     * Size: 12sp, with various weights
     * Used for supplementary information and labels
     */
    val caption: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )

    val captionBold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )

    val captionSemiBold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )

    val captionMedium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )

    val captionLight: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp
        )

    // Additional useful text styles
    val bodyRegular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )

    val bodyLargeSemiBold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )

    val captionRegular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )

    val bodyMedium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

    val bodyBold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = FSFonts.poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
}