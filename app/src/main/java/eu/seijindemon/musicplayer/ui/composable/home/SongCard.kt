package eu.seijindemon.musicplayer.ui.composable.home

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import eu.seijindemon.musicplayer.R
import eu.seijindemon.musicplayer.data.model.Song
import eu.seijindemon.musicplayer.ui.composable.general.AutoSizeText
import eu.seijindemon.musicplayer.ui.theme.MusicPlayerTheme
import eu.seijindemon.musicplayer.ui.theme.dimens
import eu.seijindemon.musicplayer.ui.viewmodel.AppViewModel

@Composable
fun SongCard(
    song: Song,
    navController: NavController,
    viewModel: AppViewModel
) {
    Row(
        modifier = Modifier
            .padding(all = MaterialTheme.dimens.SpacingCustom_6dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        viewModel.getFirstColor(),
                        viewModel.getSecondColor()
                    )
                )
            )
            .clickable(onClick = {
                viewModel.getCurrentSong(song = song)
                navController.navigate("play")
            }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(all = MaterialTheme.dimens.SpacingDivider_2dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Song Image"
        )
        Column(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
                .padding(all = MaterialTheme.dimens.SpacingDivider_2dp)
        ) {
            AutoSizeText(
                text = song.title,
                maxFontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            AutoSizeText(
                text = song.artist,
                maxFontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
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
    val song = Song(1L, "I love you", "Anonymous", Uri.EMPTY, 0, 0)

    MusicPlayerTheme {
        SongCard(
            song = song,
            navController = navController,
            viewModel = viewModel
        )
    }
}