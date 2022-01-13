package eu.seijindemon.musicplayer.ui.composable.settings

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import eu.seijindemon.musicplayer.R
import eu.seijindemon.myinformation.ui.composable.general.AutoSizeText
import eu.seijindemon.musicplayer.ui.composable.general.SetLanguage
import eu.seijindemon.musicplayer.ui.theme.MusicPlayerTheme
import eu.seijindemon.musicplayer.ui.viewmodel.LanguageViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    navController: NavController,
    languageViewModel: LanguageViewModel
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
                                contentDescription = stringResource(id = R.string.back),
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                    contentColor = MaterialTheme.colors.onPrimary,
                ) {

                }
            }
        ) {
            SettingsContent(
                navController = navController,
                languageViewModel = languageViewModel
            )
        }
    }
}

@Composable
fun SettingsContent(
    navController: NavController,
    languageViewModel: LanguageViewModel
) {
    val context = LocalContext.current
    val packageName = context.packageName

    // Privacy Policyα
    val openPrivacyPolicy = remember {
        Intent(Intent.ACTION_VIEW, Uri.parse("https://my-informations.flycricket.io/privacy.html"))
    }
    // Rate
    val openRate1 = remember {
        Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
    }
    val openRate2 = remember {
        Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=$packageName"))
    }
    // Share
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_SUBJECT, "My Information")
        putExtra(Intent.EXTRA_TEXT, "Download this App now: http://play.google.com/store/apps/details?id=${context.packageName}")
        type = "text/plain"
    }
    val openShare = remember { Intent.createChooser(sendIntent, null) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(all = 10.dp)
                .fillMaxWidth()
        ) {
            DropDownMenuLanguage(
                languageViewModel = languageViewModel
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = settingsRowModifier(
                context = context,
                intent = openShare
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(id = R.string.share)
            )
            AutoSizeText(
                modifier = Modifier.padding(start = 10.dp),
                text = stringResource(id = R.string.share),
                maxFontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                color = MaterialTheme.colors.onBackground
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = settingsRowModifier(
                context = context,
                intent = openRate1,
                intent2 = openRate2
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = stringResource(id = R.string.rate),
            )
            AutoSizeText(
                modifier = Modifier.padding(start = 10.dp),
                text = stringResource(id = R.string.rate),
                maxFontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                color = MaterialTheme.colors.onBackground
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = settingsRowModifier(
                context = context,
                intent = openPrivacyPolicy
            )
        ) {
            Icon(
                imageVector = Icons.Filled.PrivacyTip,
                contentDescription = stringResource(id = R.string.privacy_policy)
            )
            AutoSizeText(
                modifier = Modifier.padding(start = 10.dp),
                text = stringResource(id = R.string.privacy_policy),
                maxFontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Composable
fun DropDownMenuLanguage(
    languageViewModel: LanguageViewModel
) {
    val resetLanguage = remember { mutableStateOf(false) }
    val languageScope = rememberCoroutineScope()
    var currentLanguage = languageViewModel.language.observeAsState().value

    val expanded = remember { mutableStateOf(false) }
    val languages = listOf(stringResource(id = R.string.english), stringResource(id = R.string.greek))
    val selectedIndex = remember { mutableStateOf(0) }
    val selectedText = remember { mutableStateOf(value = "") }

    when(currentLanguage) {
        0 -> selectedText.value = stringResource(id = R.string.english)
        1 -> selectedText.value = stringResource(id = R.string.greek)
        else -> selectedText.value = stringResource(id = R.string.english)
    }

    val textFieldSize = remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    OutlinedTextField(
        value = selectedText.value,
        onValueChange = { selectedText.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                textFieldSize.value = coordinates.size.toSize()
            },
        label = {Text("Language")},
        trailingIcon = {
            Icon(
                tint = MaterialTheme.colors.secondary,
                imageVector = icon,
                contentDescription = "contentDescription",
                modifier = Modifier
                    .clickable {
                        expanded.value= !expanded.value
                    }
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(

        )
    )
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        modifier = Modifier.width(with(LocalDensity.current){textFieldSize.value.width.toDp()})
    ) {
        languages.forEachIndexed { index, s ->
            DropdownMenuItem(
                onClick = {
                    selectedIndex.value = index
                    selectedText.value = s
                    expanded.value = false
                    languageScope.launch {
                        languageViewModel.saveLanguage(selectedIndex.value)
                    }
                    resetLanguage.value = true
                }
            ) {
                Text(text = s)
            }
        }
    }
    if (resetLanguage.value) {
        currentLanguage = languageViewModel.language.observeAsState().value
        SetLanguage(language = currentLanguage!!)
        resetLanguage.value = false
        val activity = (LocalContext.current as? Activity)
        activity?.finish()
        activity?.startActivity(activity.intent)
    }
}

@Composable
fun settingsRowModifier(
    context: Context,
    intent: Intent,
    intent2: Intent? = null
): Modifier {
    return Modifier
        .padding(all = 10.dp)
        .fillMaxWidth()
        .clickable {
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                if (intent2 != null) {
                    context.startActivity(intent2)
                }
            }
        }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SettingsContentPreview() {
    val navController = rememberNavController()
    val languageViewModel: LanguageViewModel = viewModel()
    MusicPlayerTheme {
        SettingsContent(
            navController = navController,
            languageViewModel = languageViewModel
        )
    }
}