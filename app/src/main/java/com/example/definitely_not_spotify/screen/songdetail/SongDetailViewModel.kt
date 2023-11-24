package com.example.definitely_not_spotify.screen.songdetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.definitely_not_spotify.AudioPlayer
import com.example.definitely_not_spotify.SONG_ID
import com.example.definitely_not_spotify.model.Song
import com.example.definitely_not_spotify.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storageService: StorageService,
    val audioPlayer: AudioPlayer // Inject the AudioPlayer
    ) : ViewModel() {

    //Variabel for sangen vi spiller av for øyeblikket
    val song = mutableStateOf(Song())

    //Henter verdien av sangen vi har trykket på når den initialiserer
    init {
        val songId = savedStateHandle.get<String>(SONG_ID)
        if (songId != null) {
            viewModelScope.launch {
                song.value = storageService.getSong(songId) ?: Song()
            }
        }
    }

    //Toggle for å pause of starte sangen
    fun togglePlayback(song: Song) {
        audioPlayer.togglePlayback(song.audioUrl)
    }

    //Frigjør ressurser
    override fun onCleared() {
        audioPlayer.release()
    }
}
