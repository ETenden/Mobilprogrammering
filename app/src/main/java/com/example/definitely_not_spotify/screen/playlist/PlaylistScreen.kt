package com.example.definitely_not_spotify.screen.playlist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.definitely_not_spotify.SONG_SELECTION
import com.example.definitely_not_spotify.model.Playlist
import com.example.definitely_not_spotify.model.Song

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel = hiltViewModel(),
    navController: NavController
) {
    // Bruker viewmodel for å hente ut spillelister
    val playlistsState by viewModel.playlists.collectAsState(emptyList())

    // Mutable state for å holde på navnet som brukeren skriver inn
    var playlistName by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Input felt for å skrive inn navnet på en ny spilleliste
            TextField(
                value = playlistName,
                onValueChange = { playlistName = it },
                label = { Text("Playlist Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )

            // Knapp for å lage en ny spilleliste
            Button(
                onClick = {
                    // Sjekker at verdien ikke er null
                    if (playlistName.isNotBlank()) {
                        // Lager en ny spilleliste basert på input fra brukeren
                        val newPlaylist = Playlist(name = playlistName)

                        // Trigger spilleliste funksjonen i ViewModel
                        viewModel.createPlaylist(newPlaylist)

                        // Input feltet blir tom etter spillelisten er laget
                        playlistName = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            ) {
                Text("Create Playlist")
            }

            // Spacer for å lage litt mer rom imellom
            Spacer(modifier = Modifier.height(16.dp))

            // Liste av spillelister
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(playlistsState) { playlist ->
                    // Bruke en spilleliste item composable for playlists
                    PlaylistItem(
                        playlist = playlist,
                        onPlaylistClick = {
                            // Navigere til SongSelectionScreen basert på playlistId
                            navController.navigate("$SONG_SELECTION/${playlist.uid}")
                        }
                    )
                }
            }
        }
    }
}

//Composable for et UI element som brukes for å vise et playlist item
@Composable
fun PlaylistItem(
    playlist: Playlist,
    onPlaylistClick: (Playlist) -> Unit
) {
    //Kort composable for å vise frem spillelister
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onPlaylistClick(playlist) } //Trykker på en spilleliste
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = playlist.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}








