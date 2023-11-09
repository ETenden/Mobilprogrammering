package com.example.definitely_not_spotify.screen.songlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.definitely_not_spotify.model.Song
import com.example.definitely_not_spotify.service.AccountService
import com.example.definitely_not_spotify.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(private val storageService: StorageService, private val accountService: AccountService) : ViewModel() {

    val songs = storageService.songs

    init {
        createAnonymousAccount()
    }

    fun createSong(song: Song) {
        viewModelScope.launch {
            storageService.save(song)
        }
    }

    private fun createAnonymousAccount() {
        viewModelScope.launch {
            if (!accountService.hasUser)
                accountService.createAnonymousAccount()
        }
    }
}
