package com.example.definitely_not_spotify.screen.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.definitely_not_spotify.model.Playlist
import com.example.definitely_not_spotify.model.Song
import com.example.definitely_not_spotify.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    val storageService: StorageService
) : ViewModel() {
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists
    private val _selectedSongs = MutableStateFlow<Set<Song>>(emptySet())
    val selectedSongs: StateFlow<Set<Song>> = _selectedSongs
    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs
    private val _selectedPlaylist = MutableStateFlow<Playlist?>(null)


    // Initialize playlists by collecting data from the storage service
    init {
        viewModelScope.launch {
            storageService.playlists.collect { playlists ->
                _playlists.value = playlists
            }
        }
    }

    fun getPlaylistById(playlistId: String): Playlist? {
        return playlists.value.find { it.uid == playlistId }
    }

    // Function to select a playlist
    fun selectPlaylist(playlist: Playlist) {
        _selectedPlaylist.value = playlist
    }

    fun selectSong(song: Song, selected: Boolean) {
        viewModelScope.launch {
            // Create a copy of the current selected songs set to make modifications
            val updatedSelectedSongs = _selectedSongs.value.toMutableSet()

            if (selected) {
                // If the song is selected, add it to the set
                updatedSelectedSongs.add(song)
            } else {
                // If the song is deselected, remove it from the set
                updatedSelectedSongs.remove(song)
            }

            // Update the selected songs set with the changes
            _selectedSongs.value = updatedSelectedSongs
        }
    }

    // Function to create a new playlist
    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            val playlistId = storageService.createPlaylist(playlist)

            if (_playlists.value.none { it.uid == playlistId }) {
                val updatedPlaylists = _playlists.value + playlist.copy(uid = playlistId)
                _playlists.value = updatedPlaylists
            }
        }
    }

    // Function to add songs to a playlist
    fun addSongsToPlaylist(playlistId: String, selectedSongs: List<Song>) {
        viewModelScope.launch {
            // Fetch the playlist using its ID
            val playlist = storageService.getPlaylist(playlistId)

            if (playlist != null) {
                // Update the list of song IDs in the playlist
                val updatedSongIds = playlist.songIds.toMutableList()
                updatedSongIds.addAll(selectedSongs.map { it.uid })

                // Create an updated playlist with the new list of song IDs
                val updatedPlaylist = playlist.copy(songIds = updatedSongIds)

                // Use the storage service to update the playlist
                storageService.updatePlaylist(updatedPlaylist)
            }
        }
    }
}




