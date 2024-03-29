package eu.seijindemon.musicplayer.ui.viewmodel

import androidx.compose.ui.graphics.Color
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.seijindemon.musicplayer.data.model.Song
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(

): ViewModel() {

    private var _songs = MutableLiveData<List<Song>>()
    var songs: LiveData<List<Song>> = _songs

    fun getSongs(list: List<Song>) {
        _songs.value = list
    }

    private var _currentSong = MutableLiveData<Song>()
    var currentSong: LiveData<Song> = _currentSong

    fun getCurrentSong(song: Song) {
        _currentSong.value = song
    }

    // DialogSort

    private var _selectedSort = MutableLiveData<String>()
    var selectedSort: LiveData<String>? = _selectedSort

    fun getSelectedSort(sort: String) {
        _selectedSort.value = sort
    }

    // Media
    private var currentDuration: CountDownTimer? = null

    private val _currentMinutes = MutableLiveData(0)
    val currentMinutes: LiveData<Int> = _currentMinutes

    private val _audioFinish = MutableLiveData(false)
    val audioFinish: LiveData<Boolean> = _audioFinish

    fun getMediaDuration(mediaPlayer: MediaPlayer) {
        currentDuration = object : CountDownTimer(mediaPlayer.duration.toLong(), 500) {
            override fun onTick(p0: Long) {
                _currentMinutes.value = mediaPlayer.currentPosition
            }
            override fun onFinish() {
                _audioFinish.value = true
                Log.d("TAG", "onFinish: Media Player Finished")
            }
        }

        currentDuration!!.start()
    }

    // General

    fun getFirstColor(): Color {
        val random = Random()
        val color: Int = android.graphics.Color.argb(
            255,
            random.nextInt(256),
            random.nextInt(256),
            random.nextInt(256)
        )
        return Color(color)
    }

    fun getSecondColor(): Color {
        val random = Random()
        val color: Int = android.graphics.Color.argb(
            255,
            random.nextInt(256),
            random.nextInt(256),
            random.nextInt(256)
        )
        return Color(color)
    }

}