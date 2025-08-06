package org.risanjayy.format.shifter.view.converter.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.LowPriority
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.risanjayy.format.shifter.domain.model.ImageFormat
import org.risanjayy.format.shifter.view.theme.AppColors
import org.risanjayy.format.shifter.view.theme.FSTextStyle

/**
 * Conversion options section for selecting output format and quality
 * Mobile-optimized with large touch targets
 */
@Composable
fun ConversionOptionsSection(
    outputFormat: ImageFormat,
    quality: Int,
    onOutputFormatChanged: (ImageFormat) -> Unit,
    onQualityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Conversion Options",
                style = FSTextStyle.bodyBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // Output Format Selection
            Text(
                text = "Output Format",
                style = FSTextStyle.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Column(
                modifier = Modifier.selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(ImageFormat.PNG, ImageFormat.JPG).forEach { format ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (format == outputFormat),
                                onClick = { onOutputFormatChanged(format) },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (format == outputFormat),
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = format.displayName,
                                style = FSTextStyle.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = when (format) {
                                    ImageFormat.PNG -> "Lossless compression, larger file size"
                                    ImageFormat.JPG -> "Lossy compression, smaller file size"
                                    else -> ""
                                },
                                style = FSTextStyle.captionRegular,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            // Quality Slider (only for JPEG)
            if (outputFormat == ImageFormat.JPG) {
                Spacer(modifier = Modifier.height(8.dp))
                
                QualitySliderSection(
                    quality = quality,
                    onQualityChanged = onQualityChanged,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Enhanced Quality Slider Section with visual feedback and animations
 */
@Composable
private fun QualitySliderSection(
    quality: Int,
    onQualityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Determine quality level for color coding
    val qualityLevel = when {
        quality < 30 -> QualityLevel.LOW
        quality < 70 -> QualityLevel.MEDIUM
        else -> QualityLevel.HIGH
    }
    
    // Animated colors based on quality level
    val qualityColor by animateColorAsState(
        targetValue = when (qualityLevel) {
            QualityLevel.LOW -> AppColors.Warning
            QualityLevel.MEDIUM -> AppColors.Info
            QualityLevel.HIGH -> AppColors.Success
        },
        animationSpec = tween(300),
        label = "qualityColor"
    )
    
    val qualityColorLight = qualityColor.copy(alpha = 0.1f)
    
    // Scale animation for quality indicator
    val indicatorScale by animateFloatAsState(
        targetValue = 1f + (quality / 100f) * 0.2f, // Scale from 1.0 to 1.2
        animationSpec = tween(300),
        label = "indicatorScale"
    )
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header with quality indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "JPEG Quality",
                style = FSTextStyle.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Quality badge
            Box(
                modifier = Modifier
                    .background(
                        color = qualityColorLight,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .scale(indicatorScale),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = when (qualityLevel) {
                            QualityLevel.LOW -> Icons.Default.LowPriority
                            QualityLevel.MEDIUM, QualityLevel.HIGH -> Icons.Default.HighQuality
                        },
                        contentDescription = null,
                        tint = qualityColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "$quality%",
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        ),
                        color = qualityColor
                    )
                }
            }
        }
        
        // Custom Enhanced Slider
        CustomQualitySlider(
            value = quality.toFloat(),
            onValueChange = { onQualityChanged(it.toInt()) },
            valueRange = 10f..100f,
            steps = 17,
            qualityColor = qualityColor,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Quality level indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            QualityIndicator(
                icon = Icons.Default.LowPriority,
                label = "Lower quality",
                subtitle = "Smaller file",
                color = AppColors.Warning,
                isActive = qualityLevel == QualityLevel.LOW
            )
            
            QualityIndicator(
                icon = Icons.Default.HighQuality,
                label = "Higher quality",
                subtitle = "Larger file",
                color = AppColors.Success,
                isActive = qualityLevel == QualityLevel.HIGH
            )
        }
        
        // Quality description
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = qualityColorLight
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = when (qualityLevel) {
                    QualityLevel.LOW -> "⚠️ Low quality - Significant compression, very small file size"
                    QualityLevel.MEDIUM -> "⚖️ Balanced quality - Good compression with acceptable quality"
                    QualityLevel.HIGH -> "✨ High quality - Minimal compression, larger file size"
                },
                style = FSTextStyle.captionRegular,
                color = qualityColor,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

/**
 * Quality indicator component
 */
@Composable
private fun QualityIndicator(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    subtitle: String,
    color: Color,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val indicatorColor by animateColorAsState(
        targetValue = if (isActive) color else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        animationSpec = tween(300),
        label = "indicatorColor"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.1f else 1f,
        animationSpec = tween(300),
        label = "indicatorScale"
    )
    
    Column(
        modifier = modifier.scale(scale),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = indicatorColor,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            style = FSTextStyle.captionRegular,
            color = indicatorColor
        )
        Text(
            text = subtitle,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Light
            ),
            color = indicatorColor.copy(alpha = 0.8f)
        )
    }
}

/**
 * Custom Quality Slider with enhanced visual design
 */
@Composable
private fun CustomQualitySlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    qualityColor: Color,
    modifier: Modifier = Modifier
) {
    var isDragging by remember { mutableStateOf(false) }
    
    val thumbScale by animateFloatAsState(
        targetValue = if (isDragging) 1.4f else 1.2f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "thumbScale"
    )
    
    val trackHeight by animateDpAsState(
        targetValue = if (isDragging) 10.dp else 8.dp,
        animationSpec = tween(200),
        label = "trackHeight"
    )
    
    Box(
        modifier = modifier.padding(vertical = 20.dp)
    ) {
        // Custom gradient track background
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .align(Alignment.Center)
        ) {
            val trackHeightPx = trackHeight.toPx()
            val trackY = size.height / 2
            val cornerRadius = trackHeightPx / 2
            
            // Background track with gradient
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        AppColors.Warning.copy(alpha = 0.3f),
                        AppColors.Info.copy(alpha = 0.3f),
                        AppColors.Success.copy(alpha = 0.3f)
                    )
                ),
                topLeft = Offset(0f, trackY - trackHeightPx / 2),
                size = Size(size.width, trackHeightPx),
                cornerRadius = CornerRadius(cornerRadius)
            )
            
            // Active track
            val progress = (value - valueRange.start) / (valueRange.endInclusive - valueRange.start)
            val activeWidth = size.width * progress
            
            drawRoundRect(
                color = qualityColor,
                topLeft = Offset(0f, trackY - trackHeightPx / 2),
                size = Size(activeWidth, trackHeightPx),
                cornerRadius = CornerRadius(cornerRadius)
            )
        }
        
        // Main slider with enhanced styling
        Slider(
            value = value,
            onValueChange = { 
                onValueChange(it)
                isDragging = true
            },
            onValueChangeFinished = {
                isDragging = false
            },
            valueRange = valueRange,
            steps = steps,
            modifier = Modifier
                .fillMaxWidth()
                .scale(thumbScale),
            colors = SliderDefaults.colors(
                thumbColor = qualityColor,
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            )
        )
    }
}

/**
 * Quality levels for color coding
 */
private enum class QualityLevel {
    LOW, MEDIUM, HIGH
}