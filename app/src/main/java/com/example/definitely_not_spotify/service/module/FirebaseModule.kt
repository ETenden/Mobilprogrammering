package com.example.definitely_not_spotify.service.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

//Dagger hilt modul som gir Firebase dependencies for dependency injection
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    //Gir en instanse av FirebaseAuth for autentisering
    @Provides
    fun auth(): FirebaseAuth = Firebase.auth
    //Gir en instanse av FirebaseFirestore for Firestore database funksjoner
    @Provides
    fun firestore(): FirebaseFirestore = Firebase.firestore
}