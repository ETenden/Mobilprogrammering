package com.example.definitely_not_spotify.screen.songdetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.definitely_not_spotify.SONG_ID
import com.example.definitely_not_spotify.model.Song
import com.example.definitely_not_spotify.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storageService: StorageService
) : ViewModel() {
    val song = mutableStateOf(Song())

    init {
        val songId = savedStateHandle.get<String>(SONG_ID)
        if (songId != null) {
            viewModelScope.launch {
                song.value = storageService.getSong(songId) ?: Song()
            }
        }
    }
}