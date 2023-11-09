package com.example.definitely_not_spotify

import android.media.MediaPlayer
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import java.io.IOException
import javax.inject.Inject

class AudioPlayer @Inject constructor(){
    val mediaPlayer = MediaPlayer()
    val isPlaying = mutableStateOf(false)
    var currentTime = mutableIntStateOf(0)

    fun togglePlayback(audioUrl: String) {
        if (isPlaying.value) {
            mediaPlayer.pause()
            isPlaying.value = false
            println("CURRENT POSITION: ${mediaPlayer.currentPosition}")
        }else if (mediaPlayer.currentPosition > 0) {
            mediaPlayer.start()
            isPlaying.value = true
        }
        else {
            try {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(audioUrl)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener { mp ->
                    mp.start()
                    isPlaying.value = true
                }
            } catch (e: IOException) {

                println("DEN BLE CATCHET!!!!")
                // Handle the exception
            }
        }
    }

    fun release() {
        mediaPlayer.release()
    }

    fun updateCurrentTime() {
        currentTime.intValue = mediaPlayer.currentPosition / 1000
    }

    fun getCurrentTime(): Int{
        return currentTime.intValue
    }


    fun skipAhead() {

        val fiveSeconds = mediaPlayer.currentPosition + 4000

        mediaPlayer.seekTo(fiveSeconds)
    }


    fun skipBack() {

        val fiveSeconds = mediaPlayer.currentPosition - 6000

        mediaPlayer.seekTo(fiveSeconds)
    }
}
