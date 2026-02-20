package com.jetbrains.apiclient

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.jetbrains.apiclient.resources.Strings
import com.jetbrains.apiclient.ui.theme.AppTheme
import java.awt.Dimension

fun main() = application {
    val windowState = rememberWindowState(width = 1280.dp, height = 800.dp)

    Window(onCloseRequest = ::exitApplication, title = Strings.App.TITLE, state = windowState) {
        MaterialTheme(colorScheme = AppTheme.colorScheme) { ApiClientApp() }
    }

    val window = java.awt.Window.getWindows().firstOrNull()
    if (window is java.awt.Frame) {
        window.minimumSize = Dimension(1024, 700)
    }
}
