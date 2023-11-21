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

    // Flow to collect playlists
    val playlists: Flow<List<Playlist>> = storageService.playlists
    val songs: Flow<List<Song>> = storageService.songs
    private val _selectedSongs = mutableSetOf<Song>()
    private val _songsForPlaylist = MutableStateFlow<List<Song>>(emptyList())
    val songsForPlaylist: StateFlow<List<Song>> get() = _songsForPlaylist
    val playlist = mutableStateOf<Playlist?>(null)

    fun selectSong(song: Song, isSelected: Boolean) {
        if (isSelected) {
            _selectedSongs.add(song)
        } else {
            _selectedSongs.remove(song)
        }
    }

    // Function to create a new playlist
    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            // Trigger the playlist creation in the storage service
            storageService.createPlaylist(playlist)
        }
    }

    fun getPlaylist(playlistId: String) {
        viewModelScope.launch {
            // Retrieve the playlist from the storage service
            val fetchedPlaylist = storageService.getPlaylist(playlistId)

            // Update the state with the retrieved playlist
            playlist.value = fetchedPlaylist

            // Print some information for debugging
            Log.d("PlaylistViewModel", "Fetched playlist: $fetchedPlaylist")
        }
    }

    suspend fun getSongsForPlaylist(playlistId: String): List<Song> {
        // Retrieve songs for the playlist from the storage service
        return storageService.getSongsForPlaylist(playlistId)
    }

}












