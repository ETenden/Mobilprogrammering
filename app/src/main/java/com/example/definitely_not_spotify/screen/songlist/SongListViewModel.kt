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

    //Tom liste med sanger, som vil bli fylt opp av søkeresultatet
    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    //Lager en read_only versjon av _songs
    val songs = _songs.asStateFlow()

    //Variabel som holder styr på hva som står i søkefeltet
    private val _searchQuery = MutableStateFlow("")

    init {
        //Blir tracket som en anonym bruker om de ikke er logget inn
        createAnonymousAccount()

        //Filtrerer sangene som dukker opp til å bare være de som matcher det som står i søkefeltet
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

    //Brukes til å gjøre _searchQuery lik hva enn som blir skrevet i søkefeltet
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
