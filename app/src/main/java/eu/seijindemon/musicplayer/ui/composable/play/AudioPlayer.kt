package eu.seijindemon.musicplayer.ui.composable.play

import android.content.res.Configuration
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.seijindemon.musicplayer.R
import eu.seijindemon.musicplayer.data.model.Song
import eu.seijindemon.musicplayer.ui.composable.general.FormatTime
import eu.seijindemon.musicplayer.ui.theme.MusicPlayerTheme
import eu.seijindemon.musicplayer.ui.theme.dimens
import eu.seijindemon.musicplayer.ui.viewmodel.AppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AudioPlayer(
    viewModel: AppViewModel,
    mediaPlayer: MediaPlayer,
    currentSong: Song
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
            .padding(horizontal = MaterialTheme.dimens.SpacingCustom_12dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.SpacingCustom_12dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.SpacingCustom_24dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Image Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .sizeIn(maxWidth = 500.dp, maxHeight = 500.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(MaterialTheme.dimens.SpacingHalf_8dp))
                    .weight(10f)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.SpacingCustom_24dp))
            SongDescription(
                title = currentSong.title,
                name = currentSong.artist
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.SpacingDouble_32dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(10f)
            ) {
                PlayerSlider(
                    mediaPlayer = mediaPlayer,
                    viewModel = viewModel
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.SpacingDouble_32dp))
                PlayerButtons(
                    viewModel = viewModel,
                    modifier = Modifier.padding(vertical = MaterialTheme.dimens.SpacingHalf_8dp),
                    mediaPlayer = mediaPlayer
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun SongDescription(
    title: String,
    name: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.h5,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = Color.White,
        fontWeight = FontWeight.Bold
    )

    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            text = name,
            style = MaterialTheme.typography.body2,
            maxLines = 1,
            color = Color.White
        )
    }
}

@Composable
fun PlayerSlider(
    viewModel: AppViewModel,
    mediaPlayer: MediaPlayer
) {
    val currentMinutes = viewModel.currentMinutes.observeAsState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Slider(
            value = currentMinutes.value!!.toFloat(),
            onValueChange = {},
            valueRange = 0f..mediaPlayer.duration.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = FormatTime(millis = currentMinutes.value!!),
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = FormatTime(millis = mediaPlayer.duration),
                color = Color.White
            )
        }
    }
}

@Composable
fun PlayerButtons(
    viewModel: AppViewModel,
    modifier: Modifier,
    mediaPlayer: MediaPlayer
) {
    val scope = rememberCoroutineScope()
    val audioFinish = viewModel.audioFinish.observeAsState()
    val audioFlag = remember { mutableStateOf(true) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val buttonModifier = Modifier
            .size(MaterialTheme.dimens.SpacingDouble_32dp)
            .semantics { role = Role.Button }

        Image(
            imageVector = Icons.Filled.SkipPrevious,
            contentDescription = "Skip",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = buttonModifier
        )
        Image(
            imageVector = Icons.Filled.Replay10,
            contentDescription = "Reply 10 Sec",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = buttonModifier
        )

        Image(
            imageVector = if (audioFinish.value == false) {
                if (audioFlag.value) {
                    Icons.Filled.PlayCircleFilled
                } else {
                    Icons.Filled.PauseCircleFilled
                }
            } else {
                Icons.Filled.PlayCircleFilled
            },
            contentDescription = "Play / Pause",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .size(MaterialTheme.dimens.SpacingQuadruple_64dp)
                .semantics { role = Role.Button }
                .clickable {
                    if (audioFlag.value) {
                        mediaPlayer.start()
                        scope.launch {
                            delay(500)
                            viewModel.getMediaDuration(mediaPlayer = mediaPlayer)
                        }
                        audioFlag.value = false
                    } else {
                        audioFlag.value = true
                        mediaPlayer.pause()
                    }
                }
        )

        Image(
            imageVector = Icons.Filled.Forward10,
            contentDescription = "Forward",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = buttonModifier
        )
        Image(
            imageVector = Icons.Filled.SkipNext,
            contentDescription = "Next",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = buttonModifier
        )
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun AudioPlayerPreview() {
    val viewModel: AppViewModel = viewModel()
    val context = LocalContext.current
    val mediaPlayer: MediaPlayer = MediaPlayer.create(context, Uri.EMPTY)
    val currentSong = Song(1L, "I love you1", "Anonymous1", Uri.EMPTY, 100, 3)

    MusicPlayerTheme() {
        AudioPlayer(
            viewModel = viewModel,
            mediaPlayer = mediaPlayer,
            currentSong = currentSong
        )
    }
}