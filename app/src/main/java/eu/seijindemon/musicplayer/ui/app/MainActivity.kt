package eu.seijindemon.musicplayer.ui.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import eu.seijindemon.musicplayer.ui.composable.general.SetLanguage
import eu.seijindemon.musicplayer.ui.composable.home.HomeScreen
import eu.seijindemon.musicplayer.ui.composable.play.PlayScreen
import eu.seijindemon.musicplayer.ui.composable.settings.SettingsScreen
import eu.seijindemon.musicplayer.ui.composable.splash.SplashScreen
import eu.seijindemon.musicplayer.ui.theme.MusicPlayerTheme
import eu.seijindemon.musicplayer.ui.viewmodel.AppViewModel
import eu.seijindemon.musicplayer.ui.viewmodel.LanguageViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme {
                NavigationComponent()
            }
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun NavigationComponent() {
    val navController = rememberNavController()
    val viewModel: AppViewModel = viewModel()
    val languageViewModel: LanguageViewModel = viewModel()

    LoadLanguage(languageViewModel = languageViewModel)

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("home") { HomeScreen(navController, viewModel) }
        composable("play") { PlayScreen(navController, viewModel) }
        composable("settings") { SettingsScreen(navController, languageViewModel) }
    }
}

@Composable
fun LoadLanguage(
    languageViewModel: LanguageViewModel
) {
    val currentLanguage = languageViewModel.language.observeAsState().value
    SetLanguage(language = currentLanguage!!)
}