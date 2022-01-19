package eu.seijindemon.musicplayer.ui.composable.home

import android.content.ContentUris
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import eu.seijindemon.musicplayer.R
import eu.seijindemon.musicplayer.data.model.Song
import eu.seijindemon.musicplayer.ui.theme.MusicPlayerTheme
import eu.seijindemon.musicplayer.ui.viewmodel.AppViewModel

@ExperimentalPermissionsApi
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: AppViewModel
) {
    val context = LocalContext.current
    var songsList = mutableListOf<Song>()
    val openDialogPermission = remember { mutableStateOf(false) }
    // Track if the user doesn't want to see the rationale any more.
    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }
    val readExternalPermissionState = rememberPermissionState(permission = android.Manifest.permission.READ_EXTERNAL_STORAGE)

    PermissionRequired(
        permissionState = readExternalPermissionState,
        permissionNotGrantedContent = {
            if (doNotShowRationale) {
                Toast.makeText(
                    context,
                    "Feature not available",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                openDialogPermission.value = true
                if (openDialogPermission.value) {
                    Dialog(onDismissRequest = { openDialogPermission.value = false }) {
                        Column(
                            modifier = Modifier
                                .padding(all = 5.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "The permission is important for this app. Please grant the permission.",
                                textAlign = TextAlign.Center
                            )
                            Row {
                                Button(onClick = {
                                    readExternalPermissionState.launchPermissionRequest()
                                }) {
                                    Text("Ok!")
                                }
                                Spacer(Modifier.width(8.dp))
                                Button(onClick = {
                                    doNotShowRationale = true
                                }) {
                                    Text("Nope")
                                }
                            }
                        }
                    }
                }
            }
        },
        permissionNotAvailableContent = {
            Toast.makeText(
                context,
                "Read Storage Permission Not Available",
                Toast.LENGTH_SHORT
            ).show()
        }) {
//        Toast.makeText(
//            context,
//            "Read Permission is granted.",
//            Toast.LENGTH_SHORT
//        ).show()
        songsList = loadSongs()
    }

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
                songs = songsList,
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

@Composable
private fun loadSongs(): MutableList<Song> {
    val context = LocalContext.current

    val songsList = mutableListOf<Song>()

    val selection = "${MediaStore.Audio.Media.IS_MUSIC}  !=0"
    val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"
    val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.SIZE,
        MediaStore.Audio.Media.ARTIST
    )
    val query = context.applicationContext.contentResolver.query(
        collection,
        projection,
        selection,
        null,
        sortOrder
    )

    query?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
        val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
        val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val title = cursor.getString(titleColumn)
            val duration = cursor.getInt(durationColumn)
            val size = cursor.getInt(sizeColumn)
            val artist = cursor.getString(artistColumn)

            val contentUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                id
            )
            songsList += Song(id,title, artist, contentUri, duration, size)
        }
    }
    return songsList
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
        Song(1L, "I love you1", "Anonymous1", Uri.EMPTY, 100, 3),
        Song(2L, "I love you2", "Anonymous2", Uri.EMPTY, 200, 7),
        Song(3L, "I love you3", "Anonymous3", Uri.EMPTY, 300, 2),
        Song(4L, "I love you4", "Anonymous4", Uri.EMPTY, 200, 5),
        Song(5L, "I love you5", "Anonymous5", Uri.EMPTY, 100, 10),
        Song(6L, "I love you6", "Anonymous6", Uri.EMPTY, 300, 6),
        Song(7L, "I love you7", "Anonymous7", Uri.EMPTY, 200, 2),
        Song(8L, "I love you8", "Anonymous8", Uri.EMPTY, 300, 4),
        Song(9L, "I love you9", "Anonymous9", Uri.EMPTY, 400, 5)
    )

    MusicPlayerTheme {
        HomeContent(
            songs = songs,
            navController = navController,
            viewModel = viewModel
        )
    }
}