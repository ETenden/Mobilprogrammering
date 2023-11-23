package com.example.definitely_not_spotify.screen.playlist

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.definitely_not_spotify.model.Playlist
import com.example.definitely_not_spotify.model.Song
import com.example.definitely_not_spotify.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val storageService: StorageService
) : ViewModel() {

    // Flow for å hente ut playlists
    val playlists: Flow<List<Playlist>> = storageService.playlists
    val playlist = mutableStateOf<Playlist?>(null)

    // Funksjon for å lage en ny playlist
    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            // Trigger spilleliste i storage service
            storageService.createPlaylist(playlist)
        }
    }

    fun getPlaylist(playlistId: String) {
        viewModelScope.launch {
            // Hente spilleliste fra storage service
            val fetchedPlaylist = storageService.getPlaylist(playlistId)

            // Oppdater state med spillelisten som er hentet
            playlist.value = fetchedPlaylist

        }
    }

    suspend fun getSongsForPlaylist(playlistId: String): List<Song> {
        // Hente sanger for spillelisten fra storage service
        return storageService.getSongsForPlaylist(playlistId)
    }

}












