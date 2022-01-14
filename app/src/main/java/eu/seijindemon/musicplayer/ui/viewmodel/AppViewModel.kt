package eu.seijindemon.musicplayer.ui.viewmodel

import androidx.compose.ui.graphics.Color
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(

): ViewModel() {

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