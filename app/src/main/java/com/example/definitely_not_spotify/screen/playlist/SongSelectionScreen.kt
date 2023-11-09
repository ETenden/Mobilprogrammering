package com.example.definitely_not_spotify.screen.playlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.definitely_not_spotify.model.Playlist
import com.example.definitely_not_spotify.model.Song


@Composable
fun SongSelectionScreen(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    onSongSelected: (Song, Boolean) -> Unit,
    onConfirmSelection: () -> Unit,
    viewModel: PlaylistViewModel = hiltViewModel()
) {
    // Fetch the playlistId from the route arguments
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val playlistId = navBackStackEntry?.arguments?.getString("playlistId")

    // Collect selectedSongs directly from the ViewModel
    val selectedSongs = viewModel.selectedSongs.collectAsState(emptySet())
    val selectedSongsSet = selectedSongs.value.toSet()

    // Retrieve the selected playlist
    val selectedPlaylist: Playlist? = playlistId?.let { viewModel.getPlaylistById(it) }

    // Collect songs directly from the storage service
    val songs = viewModel.songs.collectAsState(emptyList())

    // Render a list of songs and use Checkbox for song selection
    Column {
        // Display the selected playlist's name (if available)
        selectedPlaylist?.let { playlist ->
            Text(
                text = "Selected Playlist: ${playlist.name}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
        // List of songs
        LazyColumn {
            items(songs.value) { song ->
                SongItemWithCheckbox(
                    song = song,
                    isSelected = selectedSongsSet.contains(song),
                    onSongSelected = { selected ->
                        onSongSelected(song, selected)
                    }
                )
            }
        }

        Button(
            onClick = {
                if (selectedPlaylist != null) {
                    // Perform any actions you need here, like adding songs to the selected playlist
                    val selectedSongsList = songs.value.filter { selectedSongsSet.contains(it) }
                    viewModel.addSongsToPlaylist(selectedPlaylist.uid, selectedSongsList)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Add Selected Songs to Playlist")
        }
    }
}



@Composable
fun SongItemWithCheckbox(
    song: Song,
    isSelected: Boolean,
    onSongSelected: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { selected ->
                onSongSelected(selected)
            },
            modifier = Modifier
                .padding(end = 8.dp)
        )

        // Display the song's information (you can customize this part)
        Text(
            text = song.title,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
