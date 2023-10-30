package com.example.definitely_not_spotify.screen.songdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.definitely_not_spotify.R

@Composable
fun SongDetailScreen(modifier: Modifier = Modifier,
                     viewModel: SongDetailViewModel = hiltViewModel()
) {
    val song by viewModel.song

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()) {
        Text(text = song.title,
            style = MaterialTheme.typography.headlineLarge)
        Text(text = song.description,
            style = MaterialTheme.typography.bodyMedium)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(song.posterUrl)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.song_poster),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(16.dp).width(400.dp).height(600.dp)
        )
    }
}