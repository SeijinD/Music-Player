package eu.seijindemon.musicplayer.ui.composable.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import eu.seijindemon.musicplayer.ui.composable.general.AutoSizeText
import eu.seijindemon.musicplayer.ui.composable.general.loadSongs
import eu.seijindemon.musicplayer.ui.theme.dimens
import eu.seijindemon.musicplayer.ui.viewmodel.AppViewModel

@Composable
fun DialogSort(
    viewModel: AppViewModel,
    navController: NavController,
    openDialogSort: MutableState<Boolean>
) {
    val listPair = listOf(Pair("Title", "_display_name"), Pair("Artist", "artist"), Pair("Duration", "duration"), Pair("Size", "_size"))
    val selectedSort = viewModel.selectedSort?.observeAsState()
    val selected = remember { mutableStateOf(selectedSort?.value ?: listPair[0].second) }
    val oldSelected = remember { mutableStateOf(selectedSort?.value ?: listPair[0].second) }

    Dialog(onDismissRequest = { openDialogSort.value = false }) {
        Column(
            modifier = Modifier
                .padding(all = MaterialTheme.dimens.SpacingCustom_6dp)
                .clip(RoundedCornerShape(MaterialTheme.dimens.SpacingHalf_8dp))
                .fillMaxWidth()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.SpacingHalf_8dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AutoSizeText(
                text = "Sort:",
                maxFontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                color = MaterialTheme.colors.onBackground
            )
            Divider()
            listPair.forEach {
                Row {
                    RadioButton(
                        selected = it.second == selected.value,
                        onClick = {
                            selected.value = it.second
                        }
                    )
                    AutoSizeText(
                        text = it.first,
                        maxFontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }
            Log.d("TAG1", selected.value)
            if (selected.value != oldSelected.value) {
                viewModel.getSongs(loadSongs(selected.value))
                viewModel.getSelectedSort(selected.value)
                oldSelected.value = selected.value
                navController.navigate("home")
            }
        }
    }
}