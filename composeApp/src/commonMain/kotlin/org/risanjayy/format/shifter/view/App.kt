package org.risanjayy.format.shifter.view

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.risanjayy.format.shifter.view.converter.ImageConverterScreen
import org.risanjayy.format.shifter.view.theme.FormatShifterTheme

@Composable
@Preview
fun App() {
    FormatShifterTheme {
        ImageConverterScreen(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets(0, 0, 0, 0))
        )
    }
}