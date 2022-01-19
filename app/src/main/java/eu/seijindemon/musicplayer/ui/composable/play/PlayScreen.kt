package eu.seijindemon.musicplayer.ui.composable.play

import android.content.res.Configuration
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import eu.seijindemon.musicplayer.R
import eu.seijindemon.musicplayer.data.model.Song
import eu.seijindemon.musicplayer.ui.theme.MusicPlayerTheme
import eu.seijindemon.musicplayer.ui.viewmodel.AppViewModel

@Composable
fun PlayScreen (
    navController: NavController,
    viewModel: AppViewModel
) {
    val currentSong = viewModel.currentSong.observeAsState()

    MusicPlayerTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                    contentColor = MaterialTheme.colors.onPrimary,
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate("home") }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.back)
                            )
                        }
                    },
                    actions = {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(id = R.string.settings),
                            modifier = Modifier
                                .padding(all = 5.dp)
                                .clickable(onClick = {
                                    navController.navigate("settings")
                                })
                        )
                    }
                )
            }
        ) {
            if (currentSong != null) {
                PlayContent(
                    viewModel = viewModel,
                    currentSong = currentSong.value!!
                )
            }
        }
    }
}

@Composable
fun PlayContent(
    viewModel: AppViewModel,
    currentSong: Song
) {
    val context = LocalContext.current
    val mediaPlayer: MediaPlayer = MediaPlayer.create(context, currentSong.url)
    AudioPlayer(
        viewModel = viewModel,
        mediaPlayer = mediaPlayer,
        currentSong = currentSong
    )
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun PlayContentPreview() {
    val viewModel: AppViewModel = viewModel()
    val currentSong = Song(1L, "I love you1", "Anonymous1", Uri.EMPTY, 100, 3)

    MusicPlayerTheme {
        PlayContent(
            viewModel = viewModel,
            currentSong = currentSong
        )
    }
}