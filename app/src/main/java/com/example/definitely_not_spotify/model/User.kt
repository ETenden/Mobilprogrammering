package com.example.definitely_not_spotify.model

//Brukes for å håndtere brukere i firestore og kunne logge inn
data class User(
    val id: String = "",
    val isAnonymous: Boolean = true
)