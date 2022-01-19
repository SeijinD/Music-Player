package eu.seijindemon.musicplayer.ui.composable.home

import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import eu.seijindemon.musicplayer.R
import eu.seijindemon.musicplayer.data.model.Song
import eu.seijindemon.musicplayer.ui.composable.general.SetLanguage
import eu.seijindemon.musicplayer.ui.theme.MusicPlayerTheme
import eu.seijindemon.musicplayer.ui.viewmodel.AppViewModel
import eu.seijindemon.musicplayer.ui.viewmodel.LanguageViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: AppViewModel,
    languageViewModel: LanguageViewModel
) {
    // Language
    val currentLanguage = languageViewModel.language.observeAsState().value
    SetLanguage(language = currentLanguage!!)

//    val songs = listOf<Song>() // Change with viewModel list
    val songs = listOf(
        Song(1, "I love you1", "Anonymous1", "This is song for a big love."),
        Song(2, "I love you2", "Anonymous2", "This is song for a big love."),
        Song(3, "I love you3", "Anonymous3", "This is song for a big love."),
        Song(4, "I love you4", "Anonymous4", "This is song for a big love."),
        Song(5, "I love you5", "Anonymous5", "This is song for a big love."),
        Song(6, "I love you6", "Anonymous6", "This is song for a big love."),
        Song(7, "I love you7", "Anonymous7", "This is song for a big love."),
        Song(8, "I love you8", "Anonymous8", "This is song for a big love."),
        Song(9, "I love you9", "Anonymous9", "This is song for a big love.")
    )

    MusicPlayerTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                    contentColor = MaterialTheme.colors.onPrimary,
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
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
            HomeContent(
                songs = songs,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun HomeContent(
    songs: List<Song>,
    navController: NavController,
    viewModel: AppViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        viewModel.getFirstColor(),
                        viewModel.getSecondColor()
                    )
                )
            ),
        contentPadding = PaddingValues(
            all = 5.dp
        )
    ) {
        items(songs) { song ->
            SongCard(
                song = song,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = UI_MODE_TYPE_NORMAL
)
@Composable
fun HomeContentPreview() {
    val navController = rememberNavController()
    val viewModel: AppViewModel = viewModel()
    val songs = listOf(
        Song(1, "I love you1", "Anonymous1", "This is song for a big love."),
        Song(2, "I love you2", "Anonymous2", "This is song for a big love."),
        Song(3, "I love you3", "Anonymous3", "This is song for a big love."),
        Song(4, "I love you4", "Anonymous4", "This is song for a big love."),
        Song(5, "I love you5", "Anonymous5", "This is song for a big love."),
        Song(6, "I love you6", "Anonymous6", "This is song for a big love."),
        Song(7, "I love you7", "Anonymous7", "This is song for a big love."),
        Song(8, "I love you8", "Anonymous8", "This is song for a big love."),
        Song(9, "I love you9", "Anonymous9", "This is song for a big love.")
    )

    MusicPlayerTheme {
        HomeContent(
            songs = songs,
            navController = navController,
            viewModel = viewModel
        )
    }
}