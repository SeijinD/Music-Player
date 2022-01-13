package eu.seijindemon.musicplayer.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val SpacingOctuple_128dp: Dp = 128.dp,
    val SpacingSeptuple_112dp: Dp = 112.dp,
    val SpacingSextuple_96dp: Dp = 96.dp,
    val SpacingQuintuple_80dp: Dp = 80.dp,
    val SpacingQuadruple_64dp: Dp = 64.dp,
    val SpacingTriple_48dp: Dp = 48.dp,
    val SpacingDouble_32dp: Dp = 32.dp,
    val SpacingDefault_16dp: Dp = 16.dp,
    val SpacingHalf_8dp: Dp = 8.dp,
    val SpacingQuarter_4dp: Dp = 4.dp,
    val SpacingEighth_2dp: Dp = 2.dp,

    val SpacingCustom_6dp: Dp = 6.dp,
    val SpacingCustom_12dp: Dp = 12.dp,
    val SpacingCustom_24dp: Dp = 24.dp,

    val SpacingDivider_1dp: Dp = 1.dp,
    val SpacingDivider_2dp: Dp = 2.dp,
    val SpacingDivider_4dp: Dp = 4.dp,

    val ElevationCustom_2dp: Dp = 2.dp,
    val ElevationCustom_4dp: Dp = 4.dp,
    val ElevationCustom_8dp: Dp = 8.dp,

)

val LocalDimens = compositionLocalOf { Dimens() }

val MaterialTheme.dimens: Dimens
    @Composable
    @ReadOnlyComposable
    get() = LocalDimens.current