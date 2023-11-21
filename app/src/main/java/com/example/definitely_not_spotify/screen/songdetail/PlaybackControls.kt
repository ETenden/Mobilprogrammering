package com.example.definitely_not_spotify.screen.songdetail

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PlaybackControls(
    viewModel: SongDetailViewModel = hiltViewModel()
    ) {

    val song by viewModel.song

    val mediaPlayer = remember { MediaPlayer() }

    var sliderPosition by remember { mutableFloatStateOf(0f) }

    val songLength = viewModel.audioPlayer.mediaPlayer.duration / 1000

    Slider(
    value = sliderPosition,
    onValueChange = { sliderPosition = it; viewModel.audioPlayer.mediaPlayer.seekTo((viewModel.audioPlayer.mediaPlayer.duration * sliderPosition).toInt()) }
    )
    Row {
        Text(text = "${formatTime((sliderPosition*songLength).toInt())} / ", style = MaterialTheme.typography.bodyMedium)

        Text(text = "${song.duration} / ", style = MaterialTheme.typography.bodyMedium)

        Text(text = formatTime(songLength), style = MaterialTheme.typography.bodyMedium)
    }



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
}