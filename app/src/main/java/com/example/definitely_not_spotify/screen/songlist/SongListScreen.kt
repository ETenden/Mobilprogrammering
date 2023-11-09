package com.example.definitely_not_spotify.screen.songlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.definitely_not_spotify.R
import com.example.definitely_not_spotify.model.Song

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongListScreen(modifier: Modifier = Modifier,
                   onSongClick: (String) -> Unit,
                   viewModel: SongListViewModel = hiltViewModel()) {

    val songs = viewModel.songs.collectAsStateWithLifecycle(emptyList())
    val songTitle = remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TextField(value = songTitle.value,
                onValueChange = { songTitle.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp))
            LazyVerticalGrid(columns = GridCells.FixedSize(180.dp), content = {
                items(songs.value, key = { it.uid }) { song ->
                    SongItem(song = song,
                        onSongClick = onSongClick)
                }
            }, modifier = modifier.padding(16.dp))
        }
        FloatingActionButton(onClick = { viewModel.createSong(Song()) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)) {
            Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add_icon),)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongItem(song: Song, onSongClick: (String) -> Unit) {
    Card(
        onClick = { onSongClick(song.uid) },
        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)
    ) {
        Box(
            modifier = Modifier.wrapContentSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(if (song.posterUrl == "") R.drawable.rickastley else song.posterUrl)
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