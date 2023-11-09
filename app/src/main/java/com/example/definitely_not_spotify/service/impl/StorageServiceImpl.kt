package com.example.definitely_not_spotify.service.impl

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

class StorageServiceImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
) : StorageService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val songs: Flow<List<Song>> = auth.currentUser.flatMapLatest { user ->
        firestore.collection(SONG_COLLECTION)
            .where(
                Filter.or(Filter.equalTo(USER_ID_FIELD, user.id),
                    Filter.equalTo(USER_ID_FIELD, ""))
            )
            .dataObjects()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val playlists: Flow<List<Playlist>> = auth.currentUser.flatMapLatest { user ->
        firestore.collection(PLAYLIST_COLLECTION)
            .where(
                Filter.or(Filter.equalTo(USER_ID_FIELD, user.id),
                    Filter.equalTo(USER_ID_FIELD, ""))
            )
            .dataObjects()
    }


    override suspend fun getSong(songId: String): Song? =
        firestore.collection(SONG_COLLECTION).document(songId).get().await().toObject()

    override suspend fun createPlaylist(playlist: Playlist): String {
        val playlistWithUserId = playlist.copy(userId = auth.currentUserId)
        return firestore.collection(PLAYLIST_COLLECTION).add(playlistWithUserId).await().id
    }

    override suspend fun getPlaylist(playlistId: String): Playlist? {
        return firestore.collection(PLAYLIST_COLLECTION).document(playlistId)
            .get()
            .await()
            .toObject()
    }
    override suspend fun savePlaylist(playlist: Playlist): String {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        // You can use the document ID (uid) to update the playlist
        val playlistId = playlist.uid
        val playlistRef = firestore.collection(PLAYLIST_COLLECTION).document(playlistId)

        // Update the playlist document in Firestore
        playlistRef.set(playlist, SetOptions.merge())
            .addOnSuccessListener { /* Handle success, if needed */ }
            .addOnFailureListener { exception ->
                /* Handle failure or error, if needed */
            }
    }

    override suspend fun save(song: Song): String {
        val songWithUserId = song.copy(userId = auth.currentUserId)
        return firestore.collection(SONG_COLLECTION).add(songWithUserId).await().id
    }

    override suspend fun getAllPlaylists(): List<Playlist> {
        TODO("Not yet implemented")
    }

    companion object {
        private const val SONG_COLLECTION = "songs"
        private const val PLAYLIST_COLLECTION = "playlists"
        private const val USER_ID_FIELD = "userId"
    }
}
