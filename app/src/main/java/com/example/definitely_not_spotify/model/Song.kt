package com.example.definitely_not_spotify.model

import com.google.firebase.firestore.DocumentId

data class Song(
    @DocumentId val uid: String = "", // Document ID
    val title: String = "", // Title
    val artist: String = "", // Artist
    val album: String = "", // Album
    val audioUrl: String = "", // Audio URL
    val duration: String = "", // Duration
    val posterUrl: String = "", // Poster URL
    val userId: String = "" // User ID
)
