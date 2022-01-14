package eu.seijindemon.musicplayer.ui.composable.general

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.seijindemon.musicplayer.R
import java.util.concurrent.TimeUnit

@Composable
fun FormatTime(millis: Int): String {
    val millisLong = millis.toLong()
    val hours = TimeUnit.MILLISECONDS.toHours(millisLong) % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millisLong) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millisLong) % 60

    return when {
        hours == 0L && minutes == 0L -> String.format(
            stringResource(id = R.string.time_seconds_formatter), seconds
        )

        hours == 0L && minutes > 0L -> String.format(
            stringResource(R.string.time_minutes_seconds_formatter), minutes, seconds
        )

        else -> stringResource(R.string.time_hours_minutes_seconds_formatter, hours, minutes, seconds)
    }
}