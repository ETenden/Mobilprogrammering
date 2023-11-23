package com.example.definitely_not_spotify.model

import com.google.firebase.firestore.DocumentId

//Klasse som brukes for å håndtere sanger i firestore og kunne spille de av
data class Song(
    @DocumentId val uid: String = "",
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val audioUrl: String = "",
    val duration: String = "",
    val posterUrl: String = "",
    val userId: String = ""
)
