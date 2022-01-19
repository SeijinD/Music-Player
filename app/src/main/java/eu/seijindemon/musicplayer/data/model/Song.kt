package eu.seijindemon.musicplayer.data.model

import android.net.Uri

data class Song(
    val id: Long,
    val title: String = "",
    val artist: String = "",
    val url: Uri,
    val duration: Int = 0,
    val size: Int = 0
)
