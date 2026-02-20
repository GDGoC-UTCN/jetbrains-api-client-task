package com.jetbrains.apiclient.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object AppTheme {
    val Primary = Color(0xFF0969DA)
    val OnPrimary = Color(0xFFFFFFFF)
    val PrimaryContainer = Color(0xFFE8F4FD)
    val OnPrimaryContainer = Color(0xFF24292F)

    val Secondary = Color(0xFF656D76)
    val OnSecondary = Color(0xFFFFFFFF)
    val SecondaryContainer = Color(0xFFF6F8FA)
    val OnSecondaryContainer = Color(0xFF24292F)

    val Tertiary = Color(0xFF0969DA)
    val OnTertiary = Color(0xFFFFFFFF)
    val TertiaryContainer = Color(0xFFF6F8FA)
    val OnTertiaryContainer = Color(0xFF24292F)

    val Error = Color(0xFFCF222E)
    val OnError = Color(0xFFFFFFFF)
    val ErrorContainer = Color(0xFFFFEBE9)
    val OnErrorContainer = Color(0xFFCF222E)

    val Background = Color(0xFFFFFFFF)
    val OnBackground = Color(0xFF24292F)
    val Surface = Color(0xFFFFFFFF)
    val OnSurface = Color(0xFF24292F)
    val SurfaceVariant = Color(0xFFF6F8FA)
    val OnSurfaceVariant = Color(0xFF656D76)
    val Outline = Color(0xFF656D76)

    val colorScheme: ColorScheme = lightColorScheme(
        primary = Primary,
        onPrimary = OnPrimary,
        primaryContainer = PrimaryContainer,
        onPrimaryContainer = OnPrimaryContainer,
        secondary = Secondary,
        onSecondary = OnSecondary,
        secondaryContainer = SecondaryContainer,
        onSecondaryContainer = OnSecondaryContainer,
        tertiary = Tertiary,
        onTertiary = OnTertiary,
        tertiaryContainer = TertiaryContainer,
        onTertiaryContainer = OnTertiaryContainer,
        error = Error,
        onError = OnError,
        errorContainer = ErrorContainer,
        onErrorContainer = OnErrorContainer,
        background = Background,
        onBackground = OnBackground,
        surface = Surface,
        onSurface = OnSurface,
        surfaceVariant = SurfaceVariant,
        onSurfaceVariant = OnSurfaceVariant,
        outline = Outline
    )
}
