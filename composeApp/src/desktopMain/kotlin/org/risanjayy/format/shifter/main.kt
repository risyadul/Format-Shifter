package org.risanjayy.format.shifter

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "format-shifter",
    ) {
        App()
    }
}