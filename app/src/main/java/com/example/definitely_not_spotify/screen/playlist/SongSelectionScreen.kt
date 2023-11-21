package com.example.definitely_not_spotify.screen.playlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.definitely_not_spotify.model.Playlist
import com.example.definitely_not_spotify.model.Song


@Composable
fun SongSelectionScreen(
    playlistId: String,
    navController: NavController,
    viewModel: PlaylistViewModel = hiltViewModel()
) {
    // Fetch playlist and songs directly in the composable
    val playlist = viewModel.playlist.value
    val songsState = remember { mutableStateOf<List<Song>>(emptyList()) }

    // Now you can use the 'playlist' and 'songs' variables in your UI
    playlist?.let { selectedPlaylist ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Display playlist details
            Text(
                text = "Playlist: ${selectedPlaylist.name}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                items(songsState.value) { song ->
                    SongItem(
                        song = song,
                        onPlayClicked = { /* Implement play logic */ },
                        onAddToPlaylistClicked = {
                            // Handle adding the song to the playlist
                            viewModel.selectSong(song, true)
                        }
                    )
                }
            }

            // Button to confirm song selection
            Button(
                onClick = {
                    // Handle the confirmation of selected songs here
                    // You can navigate back or perform other actions
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Confirm Selection")
            }
        }
    } ?: Text("Playlist details not available")

    // Trigger fetching songs when the playlistId changes
    LaunchedEffect(playlistId) {
        viewModel.getPlaylist(playlistId)
        val fetchedSongs = viewModel.getSongsForPlaylist(playlistId)
        println("Fetched songs size: ${fetchedSongs.size}")
        songsState.value = fetchedSongs
    }
}

@Composable
fun SongItem(
    song: Song,
    onPlayClicked: () -> Unit,
    onAddToPlaylistClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable { onPlayClicked() }
        ) {
            // Display song information
            Text(
                text = song.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = song.artist,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            // Button to add the song to the playlist
            Button(
                onClick = { onAddToPlaylistClicked() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                Text("Add to Playlist")
            }
        }
    }
}
