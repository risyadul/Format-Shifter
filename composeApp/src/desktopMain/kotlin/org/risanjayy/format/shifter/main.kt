package org.risanjayy.format.shifter

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.risanjayy.format.shifter.view.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Format Shifter",
    ) {
        App()
    }
}