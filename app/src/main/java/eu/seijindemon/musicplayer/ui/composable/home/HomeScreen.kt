package eu.seijindemon.musicplayer.ui.composable.home

import android.content.ContentUris
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import eu.seijindemon.musicplayer.R
import eu.seijindemon.musicplayer.data.model.Song
import eu.seijindemon.musicplayer.ui.composable.general.AutoSizeText
import eu.seijindemon.musicplayer.ui.composable.general.loadSongs
import eu.seijindemon.musicplayer.ui.theme.MusicPlayerTheme
import eu.seijindemon.musicplayer.ui.theme.dimens
import eu.seijindemon.musicplayer.ui.viewmodel.AppViewModel

@ExperimentalPermissionsApi
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: AppViewModel
) {
    val context = LocalContext.current
    val openDialogPermission = remember { mutableStateOf(false) }
    val openDialogSort = remember { mutableStateOf(false) }
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
                                .padding(all = MaterialTheme.dimens.SpacingCustom_6dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(MaterialTheme.dimens.SpacingHalf_8dp))
                                .background(Color.White),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.SpacingHalf_8dp),
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
                                Spacer(Modifier.width(MaterialTheme.dimens.SpacingHalf_8dp))
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
        viewModel.getSongs(loadSongs())
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
                            imageVector = Icons.Filled.Sort,
                            contentDescription = "Sort",
                            modifier = Modifier
                                .padding(all = MaterialTheme.dimens.SpacingCustom_6dp)
                                .clickable(onClick = {
                                    openDialogSort.value = true
                                })
                        )
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(id = R.string.settings),
                            modifier = Modifier
                                .padding(all = MaterialTheme.dimens.SpacingCustom_6dp)
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
                navController = navController,
                viewModel = viewModel
            )
            if (openDialogSort.value) {
                DialogSort(
                    viewModel = viewModel,
                    openDialogSort = openDialogSort,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun HomeContent(
    navController: NavController,
    viewModel: AppViewModel
) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }

    Column {
        Search(textState = textState)
        ListOfSongs(
            viewModel = viewModel,
            navController = navController,
            textState = textState
        )
    }
}

@Composable
fun Search(textState: MutableState<TextFieldValue>) {
    TextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = textState.value,
        onValueChange = { textState.value = it },
        maxLines = 1,
        singleLine = true,
        textStyle = MaterialTheme.typography.subtitle1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (textState.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        textState.value = TextFieldValue("")
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        shape = RectangleShape
    )
}

@Composable
fun ListOfSongs(
    viewModel: AppViewModel,
    navController: NavController,
    textState: MutableState<TextFieldValue>
) {
    val songs = viewModel.songs.observeAsState().value ?: listOf()
    var filteredSongs: List<Song>

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
        filteredSongs = if (textState.value.text.isEmpty()) {
            songs
        } else {
            val result = mutableListOf<Song>()
            for (song in songs) {
                if (song.title.contains(textState.value.text, ignoreCase = true)) {
                    result.add(song)
                }
            }
            result
        }
        items(filteredSongs) { song ->
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
    val songs = mutableListOf(
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
            navController = navController,
            viewModel = viewModel
        )
    }
}