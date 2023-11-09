package com.example.definitely_not_spotify.screen.playlist

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.definitely_not_spotify.model.Playlist
import com.example.definitely_not_spotify.model.Song

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel = hiltViewModel(),

    ) {
    val playlists = viewModel.playlists.collectAsState(emptyList())
    val songs = viewModel.songs.collectAsState(emptyList()).value
    val selectedSongs = viewModel.selectedSongs.collectAsState(emptySet()).value

    // Create a mutable state to hold the playlist name entered by the user
    var playlistName by remember { mutableStateOf("") }

    // Create a mutable state to hold the index of the selected playlist
    var selectedPlaylistIndex by remember { mutableStateOf(-1) }

    // Create a mutable state to show/hide the dialog
    var showDialog by remember { mutableStateOf(false) }


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
                    // Log a message to check if this block is executed
                    Log.d("PlaylistScreen", "Create Playlist button clicked")
                    // Check if the playlist name is not empty
                    if (playlistName.isNotBlank()) {
                        // Create a new playlist based on the entered name
                        val newPlaylist = Playlist(name = playlistName, songIds = selectedSongs.map { it.uid })

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
            // Button to add songs to the playlist
            Button(
                onClick = {
                    showDialog = true // Set showDialog to true to show the dialog
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Songs to Playlist")
            }

            // Spacer to add some vertical space
            Spacer(modifier = Modifier.height(16.dp))

            // List of playlists
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(playlists.value) { playlist ->
                    // Use a custom item composable for playlists
                    PlaylistItem(
                        playlist = playlist,
                        onPlaylistClick = { selectedPlaylist ->
                            // Handle the selection of the playlist here
                            viewModel.selectPlaylist(selectedPlaylist)
                        }
                    )
                }
            }
        }
    }

    // Handle the selected playlist and navigate to SongSelectionScreen
    if (selectedPlaylistIndex >= 0) {
        SongSelectionScreen(
            songs = songs,
            onSongSelected = { song, selected ->
                // Handle the selected songs within your ViewModel
                viewModel.selectSong(song, selected)
            },
            onConfirmSelection = {
                // Handle the confirmation of selected songs here
                // You can use the selectedPlaylist to pass the chosen playlist
            }
        )
    }
    // Create a single dialog for adding songs to a playlist
    AddSongsToPlaylistDialog(
        showDialog,
        songs = songs,
        selectedSongs = selectedSongs,
        playlists = playlists.value,
        onAddSongs = { selectedSongs, selectedPlaylist ->
            // Handle adding selected songs to the selected playlist here
        },
        onDismiss = {
            // Handle dialog dismissal
            // E.g., setting showAddToPlaylistDialog to false
        }
    )
}

@Composable
fun PlaylistItem(
    playlist: Playlist,
    onPlaylistClick: (Playlist) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onPlaylistClick(playlist) } // Pass the 'playlist' parameter to the callback
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Display playlist information
            Text(
                text = playlist.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            // Add more Text or Image components to display additional playlist details
        }
    }
}
@Composable
fun AddSongsToPlaylistDialog(
    showDialog: Boolean,
    songs: List<Song>,
    selectedSongs: Set<Song>,
    playlists: List<Playlist>,
    onAddSongs: (Set<Song>, Playlist) -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        var selectedPlaylistIndex by remember { mutableStateOf(0) }
        var selectedSongsSet by remember { mutableStateOf(selectedSongs) }

        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = {
                Text("Add Songs to Playlist")
            },
            text = {
                // Display a list of songs with checkboxes for selection
                LazyColumn {
                    items(songs) { song ->
                        val isChecked = song in selectedSongsSet
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = {
                                    if (it) {
                                        selectedSongsSet = selectedSongsSet + song
                                    } else {
                                        selectedSongsSet = selectedSongsSet - song
                                    }
                                }
                            )
                            Text(
                                text = song.title,
                                modifier = Modifier.clickable {
                                    // Toggle the checkbox when the text is clicked
                                    val newSelectedSongsSet = if (isChecked) {
                                        selectedSongsSet - song
                                    } else {
                                        selectedSongsSet + song
                                    }
                                    selectedSongsSet = newSelectedSongsSet
                                }
                            )
                        }
                    }
                }

                // Dropdown for selecting the target playlist
                Spacer(modifier = Modifier.height(16.dp))
                Text("Select Playlist:")
                /*DropdownMenu(
                    expanded = selectedPlaylistIndex >= 0,
                    onDismissRequest = {
                        // Dismiss the playlist selection dropdown
                        selectedPlaylistIndex = -1
                    }
                ) {
                    playlists.forEachIndexed { index, playlist ->
                        DropdownMenuItem(onClick = {
                            selectedPlaylistIndex = index
                        }) { Text(text = playlist.name) }

                    }
                }*/
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (selectedPlaylistIndex >= 0) {
                            val selectedPlaylist = playlists[selectedPlaylistIndex]
                            onAddSongs(selectedSongsSet, selectedPlaylist)
                        }
                        onDismiss()
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("Cancel")
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        )
    }
}


