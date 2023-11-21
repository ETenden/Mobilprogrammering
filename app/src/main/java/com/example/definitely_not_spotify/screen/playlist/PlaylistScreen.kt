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
    // Use the viewModel to get the playlists
    val playlistsState by viewModel.playlists.collectAsState(emptyList())

    // Create a mutable state to hold the playlist name entered by the user
    var playlistName by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // If needed, add search or filter functionality here

            // Input field for entering the playlist name
            TextField(
                value = playlistName,
                onValueChange = { playlistName = it },
                label = { Text("Playlist Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )

            // Button to create a new playlist
            Button(
                onClick = {
                    // Check if the playlist name is not empty
                    if (playlistName.isNotBlank()) {
                        // Create a new playlist based on the entered name
                        val newPlaylist = Playlist(name = playlistName)

                        // Trigger the playlist creation in the ViewModel
                        viewModel.createPlaylist(newPlaylist)

                        // Clear the input field after creating the playlist
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

            // Spacer to add some vertical space
            Spacer(modifier = Modifier.height(16.dp))

            // List of playlists
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(playlistsState) { playlist ->
                    // Use a custom item composable for playlists
                    PlaylistItem(
                        playlist = playlist,
                        onPlaylistClick = {
                            // Navigate to SongSelectionScreen and pass the playlistId
                            navController.navigate("$SONG_SELECTION/${playlist.uid}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PlaylistItem(
    playlist: Playlist,
    onPlaylistClick: (Playlist) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onPlaylistClick(playlist) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // You can customize the appearance of the playlist item
            Text(
                text = playlist.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Add other details if needed
        }
    }
}








