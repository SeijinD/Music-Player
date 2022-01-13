package eu.seijindemon.musicplayer.ui.composable.general

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.*

@Composable
fun SetLanguage(language: Int) {
    val locale = Locale(
        when(language) {
            0 -> "en"
            1 -> "el"
            else -> "en"
        }
    )
    val configuration = LocalConfiguration.current
    configuration.setLocale(locale)
    val resources = LocalContext.current.resources
    resources.updateConfiguration(configuration, resources.displayMetrics)
}