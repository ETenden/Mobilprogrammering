package com.example.definitely_not_spotify.model

import com.google.firebase.firestore.DocumentId

// Klasse som brukes for å kommunisere med firestore for å opprette spillelister og spille sanger
data class Playlist(
    @DocumentId val uid: String = "",
    val name: String = "",
    val songIds: List<String> = emptyList(),
    val userId: String = ""
)
