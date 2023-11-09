package com.example.definitely_not_spotify

import android.media.MediaPlayer
import androidx.compose.runtime.mutableStateOf
import java.io.IOException
import javax.inject.Inject

class AudioPlayer @Inject constructor(){
    private val mediaPlayer = MediaPlayer()
    val isPlaying = mutableStateOf(false)

    fun togglePlayback(audioUrl: String) {
        if (isPlaying.value) {
            mediaPlayer.pause()
        } else {
            try {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(audioUrl)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener { mp ->
                    mp.start()
                    isPlaying.value = true
                }
            } catch (e: IOException) {
                // Handle the exception
            }
        }
    }

    fun release() {
        mediaPlayer.release()
    }
}
