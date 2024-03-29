package com.example.definitely_not_spotify.screen.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.definitely_not_spotify.R
import com.example.definitely_not_spotify.model.Playlist
import com.example.definitely_not_spotify.model.Song


@Composable
fun SongSelectionScreen(
    playlistId: String,
    navController: NavController,
    onSongClick: (String) -> Unit,
    viewModel: PlaylistViewModel = hiltViewModel()
) {
    // Variabler for å hente spilleliste og sanger
    val playlist = viewModel.playlist.value
    val songsState = remember { mutableStateOf<List<Song>>(emptyList()) }

    // Gjør det mulig å bruke playlist i UI
    playlist?.let { selectedPlaylist ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Viser det som ligger i playlist
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
                        onSongClick = onSongClick,
                    )
                }
            }

            // Knapp som skulle brukes til å bekrefte sangene som brukeren skulle legge til
            // (Ikke implementert)
            Button(
                onClick = {
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

    // Henter inn sanger når spillelisten endrer på seg.
    LaunchedEffect(playlistId) {
        viewModel.getPlaylist(playlistId)
        val fetchedSongs = viewModel.getSongsForPlaylist(playlistId)
        println("Fetched songs size: ${fetchedSongs.size}")
        songsState.value = fetchedSongs
    }
}

//Composable som henter spesifikke sanger med cover og gjør at de kan trykkes på.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongItem(song: Song, onSongClick: (String) -> Unit) {
    //Viser frem sanger i Card oppsett med litt UI kode
    Card(
        onClick = { onSongClick(song.uid) },
        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)
    ) {
        Box(
            modifier = Modifier.wrapContentSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(song.posterUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.song_poster),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(180.dp)
                    .height(270.dp)
            )

            Box(modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                .fillMaxWidth()) {
                Text(text = song.title,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(start = 4.dp, top = 0.dp, end = 4.dp, bottom = 4.dp))
            }
        }
    }

}
