package eu.seijindemon.musicplayer.ui.composable.home

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import eu.seijindemon.musicplayer.data.model.Song
import eu.seijindemon.musicplayer.ui.theme.MusicPlayerTheme
import eu.seijindemon.musicplayer.ui.viewmodel.AppViewModel

@Composable
fun SongCard(
    song: Song,
    navController: NavController,
    viewModel: AppViewModel
) {

}

@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SongCardPreview() {
    val navController = rememberNavController()
    val viewModel: AppViewModel = viewModel()
    val song = Song(1, "I love you", "Anonymous", "This is song for a big love.")

    MusicPlayerTheme {
        SongCard(
            song = song,
            navController = navController,
            viewModel = viewModel
        )
    }
}