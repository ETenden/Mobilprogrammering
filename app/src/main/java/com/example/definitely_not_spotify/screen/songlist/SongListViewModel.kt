package com.example.definitely_not_spotify.screen.songlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.definitely_not_spotify.model.Song
import com.example.definitely_not_spotify.service.AccountService
import com.example.definitely_not_spotify.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService
) : ViewModel() {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs = _songs.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        createAnonymousAccount()

        // Observe changes in the search query and update the filtered songs
        viewModelScope.launch {
            combine(storageService.songs, _searchQuery) { allSongs, query ->
                if (query.isBlank()) {
                    allSongs
                } else {
                    allSongs.filter { it.title.lowercase().contains(query.lowercase()) }
                }
            }.collect { filteredSongs ->
                _songs.value = filteredSongs
            }
        }
    }

    private fun createAnonymousAccount() {
        viewModelScope.launch {
            if (!accountService.hasUser)
                accountService.createAnonymousAccount()
        }
    }

    // Function to update the search query
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
