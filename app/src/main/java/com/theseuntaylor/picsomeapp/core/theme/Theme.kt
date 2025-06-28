package com.theseuntaylor.picsomeapp.core.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme: ColorScheme = lightColorScheme(
    primary = Color(0xFFD35DA3),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFAD9EB),
    onPrimaryContainer = Color(0xFF3C0A33),
    secondary = Color(0xFFA64D8F),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF2CFE6),
    onSecondaryContainer = Color(0xFF320A26),
    tertiary = Color(0xFF885D9E),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFEBD7F7),
    onTertiaryContainer = Color(0xFF310A3C),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = Color(0xFFFAD9EB),
    onPrimary = Color(0xFF3C0A33),
    primaryContainer = Color(0xFFD35DA3),
    onPrimaryContainer = Color(0xFFFFFFFF),

    secondary = Color(0xFFF2CFE6),
    onSecondary = Color(0xFF320A26),
    secondaryContainer = Color(0xFFA64D8F),
    onSecondaryContainer = Color(0xFFFFFFFF),

    tertiary = Color(0xFFEBD7F7),
    onTertiary = Color(0xFF310A3C),
    tertiaryContainer = Color(0xFF885D9E),
    onTertiaryContainer = Color(0xFFFFFFFF),

    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFFFFFFF),

    error = Color(0xFFF9DEDC),
    onError = Color(0xFF410E0B),
    errorContainer = Color(0xFFB3261E),
    onErrorContainer = Color(0xFFFFFFFF)
)


@Composable
fun ProvideWindowInsetsController(
) {
    val isLightTheme = colorScheme.background.luminance() > 0.5
    val activity = LocalActivity.current as Activity
    SideEffect {
        val window = activity.window
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = isLightTheme
        controller.isAppearanceLightNavigationBars = isLightTheme
    }
}

@Composable
fun PickSomeApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}