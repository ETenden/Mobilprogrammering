package com.example.definitely_not_spotify.service.impl

import com.example.definitely_not_spotify.model.Song
import com.example.definitely_not_spotify.service.AccountService
import com.example.definitely_not_spotify.service.StorageService
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore,
            private val auth: AccountService) : StorageService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val songs: Flow<List<Song>>
        get() = auth.currentUser.flatMapLatest { user ->
            firestore.collection(SONG_COLLECTION)
                .where(
                    Filter.or(Filter.equalTo(USER_ID_FIELD, user.id),
                        Filter.equalTo(USER_ID_FIELD, "")))
                .dataObjects()
        }

    override suspend fun getSong(songId: String): Song? =
        firestore.collection(SONG_COLLECTION).document(songId).get().await().toObject()


    override suspend fun save(song: Song): String {
        val songWithUserId = song.copy(userId = auth.currentUserId)
        return firestore.collection(SONG_COLLECTION).add(songWithUserId).await().id
    }



    companion object {
        private const val SONG_COLLECTION = "songs"
        private const val USER_ID_FIELD = "userId"
    }
}