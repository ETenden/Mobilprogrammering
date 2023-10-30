package com.example.definitely_not_spotify.model

import com.google.firebase.firestore.DocumentId

data class Song(
    @DocumentId val uid : String = "",
    val title : String = "",
    var description : String = "",
    var posterUrl : String = "",
    var userId: String = "")