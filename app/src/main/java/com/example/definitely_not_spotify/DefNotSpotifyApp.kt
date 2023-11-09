package com.example.definitely_not_spotify

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.definitely_not_spotify.screen.playlist.PlaylistScreen
import com.example.definitely_not_spotify.screen.playlist.PlaylistViewModel
import com.example.definitely_not_spotify.screen.playlist.SongSelectionScreen
import com.example.definitely_not_spotify.screen.sign_up.SignUpScreen
import com.example.definitely_not_spotify.screen.songdetail.SongDetailScreen
import com.example.definitely_not_spotify.screen.songlist.SongListScreen
import com.example.definitely_not_spotify.ui.theme.DefinitelyNotSpotifyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefNotSpotifyApp() {
    DefinitelyNotSpotifyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val navController = rememberNavController()

            Scaffold(
                topBar =  {
                    TopAppBar(
                        title = { Text(stringResource(R.string.songs)) },
                        actions = {
                            IconButton(onClick = { navController.navigate(SIGN_UP) }) {
                                Icon(imageVector = Icons.Default.Person, contentDescription = "Action")
                            }
                            IconButton(onClick = { navController.navigate(PLAYLIST) }) {
                                Icon(imageVector = Icons.Default.List, contentDescription = "Playlists")
                            }
                        }
                    )
                }
            ) { innerPaddingModifier ->
                NavHost(
                    navController = navController,
                    startDestination = SONG_LIST,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    composable(SONG_LIST) {
                        SongListScreen(onSongClick = {
                                songId ->
                            val route = "${SONG_DETAIL}?$SONG_ID=$songId"
                            navController.navigate(route)
                        })
                    }

                    composable(
                        route = "${SONG_DETAIL}$SONG_ID_ARG",
                        arguments = listOf(navArgument(SONG_ID) {
                            nullable = false
                        })
                    ) {
                        SongDetailScreen()
                    }

                    composable(PLAYLIST) {
                        PlaylistScreen()
                    }

                    composable("$SONG_SELECTION/{$PLAYLIST_ID}") { backStackEntry ->
                        val viewModel: PlaylistViewModel = hiltViewModel()
                        val songs = viewModel.songs.collectAsState(emptyList()).value

                        SongSelectionScreen(
                            songs = songs,
                            onSongSelected = { song, selected ->
                                viewModel.selectSong(song, selected)
                            },
                            onConfirmSelection = {
                                // Handle the confirmation of selected songs here
                            },
                        )
                    }



                    composable(SIGN_UP) {
                        SignUpScreen(loggedIn = { navController.navigate(SONG_LIST) })
                    }
                }
            }
        }
    }
}
