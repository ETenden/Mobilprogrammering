package com.example.definitely_not_spotify.service

import com.example.definitely_not_spotify.model.Playlist
import com.example.definitely_not_spotify.model.Song
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val songs: Flow<List<Song>>
    val playlists: Flow<List<Playlist>>

    suspend fun getSong(songId: String): Song?
    suspend fun getPlaylist(playlistId: String): Playlist?
    suspend fun getSongsForPlaylist(playlistId: String): List<Song>
    suspend fun createPlaylist(playlist: Playlist): String
}



