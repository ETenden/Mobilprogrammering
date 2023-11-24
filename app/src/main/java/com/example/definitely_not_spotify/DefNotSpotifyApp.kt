package com.example.definitely_not_spotify

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.definitely_not_spotify.screen.playlist.PlaylistScreen
import com.example.definitely_not_spotify.screen.playlist.SongSelectionScreen
import com.example.definitely_not_spotify.screen.sign_up.SignUpScreen
import com.example.definitely_not_spotify.screen.songdetail.SongDetailScreen
import com.example.definitely_not_spotify.screen.songlist.SongListScreen
import com.example.definitely_not_spotify.ui.theme.DefinitelyNotSpotifyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefNotSpotifyApp() {
    DefinitelyNotSpotifyTheme {
        Surface(color = MaterialTheme.colorScheme.background,) {
            val navController = rememberNavController()

            Scaffold(
                //Navigasjonsbar på bunnen av siden, med 3 knapper. En for innloggingsskjerm, en for spilleliste skjerm, og en for hjemskjermen, hvor sanger ligger.
                bottomBar =  {
                    BottomAppBar(
                        actions = {
                            IconButton(onClick = { navController.navigate(SIGN_UP) }) {
                                Icon(imageVector = Icons.Default.Person, contentDescription = "Action")
                            }
                            IconButton(onClick = { navController.navigate(PLAYLIST) }) {
                                Icon(imageVector = Icons.Default.List, contentDescription = "Playlists")
                            }
                            IconButton(onClick = { navController.navigate(SONG_LIST) }) {
                                Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
                            }
                        }
                    )
                }
            ) { innerPaddingModifier ->
                Column(
                    modifier = Modifier

                ) {

                    //Bruker en navhost for å manøvrere seg igjennom appen.
                    NavHost(
                        navController = navController,
                        startDestination = SONG_LIST,
                        modifier = Modifier.padding(innerPaddingModifier)
                    ) {
                        composable(SONG_LIST) {
                            SongListScreen(onSongClick = { songId ->
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
                            PlaylistScreen(navController = navController)
                        }

                        composable("$SONG_SELECTION/{$PLAYLIST_ID}") { backStackEntry ->
                            val playlistId = backStackEntry.arguments?.getString(PLAYLIST_ID) ?: ""
                            SongSelectionScreen(playlistId = playlistId, navController = navController,
                                onSongClick = { songId ->
                                    val route = "${SONG_DETAIL}?$SONG_ID=$songId"
                                    navController.navigate(route)
                                })
                        }

                        composable(SIGN_UP) {
                            SignUpScreen(loggedIn = { navController.navigate(SONG_LIST) })
                        }
                    }
                }

            }


        }
    }
}
