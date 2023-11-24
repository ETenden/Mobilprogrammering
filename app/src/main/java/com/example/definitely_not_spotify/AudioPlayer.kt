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

    //Mottar en audioUrl som parameter som er sangen som skal spilles.
    fun togglePlayback(audioUrl: String) {

        //Pauser en sang som spilles av
        if (isPlaying.value) {
            mediaPlayer.pause()
            isPlaying.value = false
            println("CURRENT POSITION: ${mediaPlayer.currentPosition}")
        }
        //Fortsetter avspilling av en sang som har blitt pauset
        else if (mediaPlayer.currentPosition > 0) {
            mediaPlayer.start()
            isPlaying.value = true
        }
        //Prøver å starte en sang fra starten av, siden det ikke er noen sang som spilles av allerede.
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
            }
        }
    }

    //Frigjør ressursene som er lagret i mediaplayer.
    fun release() {
        mediaPlayer.release()
    }

    //Brukes for å oppdatere verdien for hvor i sangen brukeren er.
    fun updateCurrentTime() {
        currentTime.intValue = mediaPlayer.currentPosition / 1000
    }

    //Brukes for å hente ut hvor i sangen brukeren er.
    fun getCurrentTime(): Int{
        return currentTime.intValue
    }


    //Brukes for å spole fram i sangen. På seekbaren ser det visuelt ut som 5 sekunder selv om det burde teknisk sett være 4.
    fun skipAhead() {

        val fiveSeconds = mediaPlayer.currentPosition + 4000

        mediaPlayer.seekTo(fiveSeconds)
    }


    //Brukes for å spole tilbake i sangen. Ser ut som 5 sekunder visuelt selv om det er 6 sekunder.
    fun skipBack() {

        val fiveSeconds = mediaPlayer.currentPosition - 6000

        mediaPlayer.seekTo(fiveSeconds)
    }
}
