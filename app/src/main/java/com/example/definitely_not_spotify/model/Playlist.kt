package com.example.definitely_not_spotify.model

import com.google.firebase.firestore.DocumentId

data class Playlist(
    @DocumentId val uid: String = "",
    val name: String = "",
    val songIds: List<String> = emptyList(),
    val userId: String = "" // User ID
)
