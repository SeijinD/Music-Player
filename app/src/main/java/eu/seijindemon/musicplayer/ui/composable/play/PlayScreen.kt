package eu.seijindemon.musicplayer.ui.composable.play

import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import eu.seijindemon.musicplayer.R
import eu.seijindemon.musicplayer.ui.theme.MusicPlayerTheme
import eu.seijindemon.musicplayer.ui.theme.dimens
import eu.seijindemon.musicplayer.ui.viewmodel.AppViewModel

@Composable
fun PlayScreen (
    navController: NavController,
    viewModel: AppViewModel
) {
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
            },
            bottomBar = {
                BottomAppBar(
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                    contentColor = MaterialTheme.colors.onPrimary
                ) {

                }
            }
        ) {
            PlayContent(navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun PlayContent(
    navController: NavController,
    viewModel: AppViewModel
) {
    val context = LocalContext.current
    val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.hide_and_seek_music)
    AudioPlayer(
        viewModel = viewModel,
        mediaPlayer = mediaPlayer
    )
}

@Composable
fun AudioPlayer(
    viewModel: AppViewModel,
    mediaPlayer: MediaPlayer
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        viewModel.getFirstColor(),
                        viewModel.getSecondColor()
                    )
                )
            )
            .padding(horizontal = MaterialTheme.dimens.SpacingHalf_8dp)
    ) {

    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun PlayContentPreview() {
    val navController = rememberNavController()
    val viewModel: AppViewModel = viewModel()
    MusicPlayerTheme {
        PlayContent(
            navController = navController,
            viewModel = viewModel
        )
    }
}