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
    primary = BizPrimaryDark,
    onPrimary = Color(0xFF002F6C),
    primaryContainer = BizPrimary,
    onPrimaryContainer = Color(0xFFD8E2FF),
    secondary = BizSecondaryDark,
    onSecondary = Color(0xFF003731),
    secondaryContainer = BizSecondary,
    onSecondaryContainer = Color(0xFF70F7E8),
    tertiary = BizTertiaryLight,
    background = BizDarkBackground,
    surface = BizDarkSurface,
    surfaceVariant = BizDarkSurfaceVariant,
    onBackground = BizDarkOnSurface,
    onSurface = BizDarkOnSurface,
    onSurfaceVariant = BizDarkOnSurfaceVariant,
)

private val LightColorScheme = lightColorScheme(
    primary = BizPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD8E2FF),
    onPrimaryContainer = Color(0xFF001A41),
    secondary = BizSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCCFBF1),
    onSecondaryContainer = Color(0xFF00201C),
    tertiary = BizTertiary,
    background = BizBackground,
    surface = BizSurface,
    surfaceVariant = BizSurfaceVariant,
    onBackground = BizOnSurface,
    onSurface = BizOnSurface,
    onSurfaceVariant = BizOnSurfaceVariant,
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
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
