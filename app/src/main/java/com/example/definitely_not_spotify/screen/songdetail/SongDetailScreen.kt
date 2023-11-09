package com.example.definitely_not_spotify.screen.songdetail

import android.media.MediaPlayer
import android.widget.SeekBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.delay
import java.io.IOException

@Composable
fun SongDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: SongDetailViewModel = hiltViewModel()
) {
    val song by viewModel.song

    val mediaPlayer = remember { MediaPlayer() }

    var sliderPosition by remember { mutableFloatStateOf(0f) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {

        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it; viewModel.audioPlayer.mediaPlayer.seekTo((viewModel.audioPlayer.mediaPlayer.duration * sliderPosition).toInt()) }
        )
        Text(text = sliderPosition.toString())


        
        Row {

            Button(
                onClick = {
                    viewModel.audioPlayer.skipBack()
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "<<")
            }

            // Play/Pause Button
            Button(
                onClick = {
                    viewModel.togglePlayback(song)
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(if (viewModel.audioPlayer.isPlaying.value) "Pause" else "Play")
            }
            
            Button(
                onClick = {
                    viewModel.audioPlayer.skipAhead()
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = ">>")
            }
        }
        
        
        
        

        Text(text = song.title, style = MaterialTheme.typography.headlineLarge)

        Row {
            Text(text = "${formatTime(viewModel.audioPlayer.getCurrentTime())} / ", style = MaterialTheme.typography.bodyMedium)

            Text(text = "${song.duration} / ", style = MaterialTheme.typography.bodyMedium)

            Text(text = formatTime(viewModel.audioPlayer.mediaPlayer.duration / 1000), style = MaterialTheme.typography.bodyMedium)
        }

        LaunchedEffect(viewModel.audioPlayer.isPlaying.value){
            while (viewModel.audioPlayer.isPlaying.value){
                viewModel.audioPlayer.updateCurrentTime()
                sliderPosition = viewModel.audioPlayer.getCurrentTime().toFloat() / (viewModel.audioPlayer.mediaPlayer.duration / 1000)

                delay(1000)
            }
        }


        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(song.posterUrl)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.song_poster),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(16.dp)
                .width(400.dp)
                .height(
                    600.dp
                ))
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
}

fun formatTime(seconds: Int): String{
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}


