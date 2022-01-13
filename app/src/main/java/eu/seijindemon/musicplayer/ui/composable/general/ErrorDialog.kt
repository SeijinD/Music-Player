package eu.seijindemon.myinformation.ui.composable.general

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import eu.seijindemon.myinformation.R

@Composable
fun ErrorDialog(
    errorMessage: MutableState<String>,
    openErrorDialog: MutableState<Boolean>
) {
    Dialog(
        onDismissRequest = {
            openErrorDialog.value = false
        }
    ) {
        Column(
            modifier = Modifier
                .padding(all = 5.dp)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AutoSizeText(
                text = errorMessage.value,
                modifier = Modifier.padding(16.dp),
                maxFontSize = 20.sp,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.error
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
fun ErrorScreenPreview() {
    val openErrorDialog = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    ErrorDialog(
        errorMessage = errorMessage,
        openErrorDialog = openErrorDialog
    )
}