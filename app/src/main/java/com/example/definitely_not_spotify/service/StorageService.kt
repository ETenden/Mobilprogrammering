package com.example.definitely_not_spotify.service

import com.example.definitely_not_spotify.model.Song
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val songs: Flow<List<Song>>
    suspend fun getSong(songId: String): Song?
    suspend fun save(song: Song): String
}