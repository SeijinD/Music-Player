package eu.seijindemon.musicplayer.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = Color(0xFF42A5F5),
    primaryVariant  = Color(0xFF1976D2),
    secondary = Color(0xFF03DAC6),
    secondaryVariant = Color(0xFF018786),
    background = Color.White,
    surface = Color.White,
    error = Color(0xFFB60021),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color(0xFF1976D2),
    onSurface = Color.Black,
    onError = Color.White
)

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = Color(0xFF42A5F5),
    primaryVariant = Color(0xFF1976D2),
    secondary = Color(0xFF03DAC6),
    secondaryVariant = Color(0xFF018786),
    background = Color(0xFF534F4F),
    surface = Color(0xFF534F4F),
    error = Color(0xFFB60021),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color(0xFF42A5F5),
    onSurface = Color.White,
    onError = Color.Black
)

@Composable
fun MusicPlayerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) { DarkColorPalette } else { LightColorPalette }

    CompositionLocalProvider(LocalDimens provides Dimens()) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}