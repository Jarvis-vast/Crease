package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TurfGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = TurfGreenContainer,
    onPrimaryContainer = TurfGreenLight,
    secondary = CricketGold,
    onSecondary = CricketNavyDark,
    secondaryContainer = Color(0xFF4D3800),
    onSecondaryContainer = CricketGoldLight,
    tertiary = LiveRed,
    onTertiary = Color.White,
    background = CricketNavyDark,
    onBackground = TextPrimaryLight,
    surface = CricketNavy,
    onSurface = TextPrimaryLight,
    surfaceVariant = CricketSurfaceDark,
    onSurfaceVariant = TextSecondaryLight,
    outline = BorderSubtle,
    error = BoundaryRed
)

private val LightColorScheme = lightColorScheme(
    primary = TurfGreenDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC7F9DC),
    onPrimaryContainer = Color(0xFF002114),
    secondary = Color(0xFF8A5B00),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDF9E),
    onSecondaryContainer = Color(0xFF2C1A00),
    tertiary = LiveRed,
    onTertiary = Color.White,
    background = OffWhite,
    onBackground = TextPrimaryDark,
    surface = Color.White,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = TextSecondaryDark,
    outline = Color(0xFFCBD5E1),
    error = BoundaryRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to dark stadium theme for cricket intelligence
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
