package com.example.definitely_not_spotify.service.impl

import android.util.Log
import com.example.definitely_not_spotify.model.Playlist
import com.example.definitely_not_spotify.model.Song
import com.example.definitely_not_spotify.service.AccountService
import com.example.definitely_not_spotify.service.StorageService
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//Implementasjon av StorageService som bruker firestore for å lagre og hente ut sanger og spillelister
class StorageServiceImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
) : StorageService {

    //Flow som representerer en liste av sanger tilgjengelig for brukeren
    //Denne oppdateres basert på brukerens autentikasjon
    @OptIn(ExperimentalCoroutinesApi::class)
    override val songs: Flow<List<Song>> = auth.currentUser.flatMapLatest { user ->
        firestore.collection(SONG_COLLECTION)
            .where(
                Filter.or(Filter.equalTo(USER_ID_FIELD, user.id),
                    Filter.equalTo(USER_ID_FIELD, ""))
            )
            .dataObjects()
    }

    //Flow som representerer en liste av spillelister tilgjengelig for brukeren
    //Denne oppdateres basert på brukerens autentikasjon
    @OptIn(ExperimentalCoroutinesApi::class)
    override val playlists: Flow<List<Playlist>> = auth.currentUser.flatMapLatest { user ->
        firestore.collection(PLAYLIST_COLLECTION)
            .where(
                Filter.or(Filter.equalTo(USER_ID_FIELD, user.id),
                    Filter.equalTo(USER_ID_FIELD, ""))
            )
            .dataObjects()
    }

    //Hente ut en spesifikk sang basert på ID fra firestore
    override suspend fun getSong(songId: String): Song? =
        firestore.collection(SONG_COLLECTION).document(songId).get().await().toObject()

    //Lager en ny spilleliste i firestore og retunerer ID
    override suspend fun createPlaylist(playlist: Playlist): String {
        val playlistWithUserId = playlist.copy(userId = auth.currentUserId)
        return firestore.collection(PLAYLIST_COLLECTION).add(playlistWithUserId).await().id
    }

    //Henter ut en spesifikk spilleliste basert på IDen i firestore
    override suspend fun getPlaylist(playlistId: String): Playlist? {
        return firestore.collection(PLAYLIST_COLLECTION).document(playlistId)
            .get()
            .await()
            .toObject()
    }


    //Henter ut en liste av sanger basert på spillelisten
    //Returnerer en tom liste hvis det ikke er noen sanger i spillelisten
    //Koden ble laget med hjelp av Chat GPT
    override suspend fun getSongsForPlaylist(playlistId: String): List<Song> {
        val playlistSnapshot = firestore.collection("playlists")
            .document(playlistId)
            .get()
            .await()

        val songIds = playlistSnapshot.get("songIds") as? List<String> ?: emptyList()

        if (songIds.isNotEmpty()) {
            val songs = songIds.mapNotNull { songId ->
                val songSnapshot = firestore.collection("songs")
                    .document(songId)
                    .get()
                    .await()

                val song = songSnapshot.toObject(Song::class.java)
                if (song != null) {
                    println("Fetched song: $song")
                } else {
                    println("Failed to fetch song for ID: $songId")
                }
                song
            }
            return songs
        }

        return emptyList()
    }

    companion object {
        private const val SONG_COLLECTION = "songs"
        private const val PLAYLIST_COLLECTION = "playlists"
        private const val USER_ID_FIELD = "userId"
    }
}
