package eu.seijindemon.musicplayer.ui.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import eu.seijindemon.musicplayer.ui.composable.home.HomeScreen
import eu.seijindemon.musicplayer.ui.composable.play.PlayScreen
import eu.seijindemon.musicplayer.ui.composable.settings.SettingsScreen
import eu.seijindemon.musicplayer.ui.composable.splash.SplashScreen
import eu.seijindemon.musicplayer.ui.theme.MusicPlayerTheme
import eu.seijindemon.musicplayer.ui.viewmodel.AppViewModel
import eu.seijindemon.musicplayer.ui.viewmodel.LanguageViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme {
                NavigationComponent()
            }
        }
    }
}

@Composable
fun NavigationComponent() {
    val navController = rememberNavController()
    val viewModel: AppViewModel = viewModel()
    val languageViewModel: LanguageViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("home") { HomeScreen(navController, viewModel, languageViewModel) }
        composable("play") { PlayScreen(navController, viewModel) }
        composable("settings") { SettingsScreen(navController, languageViewModel) }
    }
}