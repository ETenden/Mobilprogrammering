package com.example.definitely_not_spotify.service.impl

import com.example.definitely_not_spotify.model.User
import com.example.definitely_not_spotify.service.AccountService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// Implementasjon av AccountService som bruer FirebaseAuth for autentisering
class AccountServiceImpl @Inject constructor(private val auth: FirebaseAuth) : AccountService {

    // Henter ut bruker ID eller en tom ID dersom brukeren ikke er autentisert (logget inn)
    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    //Sjekker om brukeren er autentisert
    override val hasUser: Boolean
        get() = auth.currentUser != null

    // Henter ut en flow av brukere som oppdateres basert på FirebaseAuth sin state
    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    // Bruker object basert på den nåværende autentisering sin state
                    this.trySend(auth.currentUser?.let { User(it.uid, it.isAnonymous) } ?: User())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    //Autentiserer brukeren basert på email og passord som er blitt gitt
    override suspend fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { onResult(it.exception) }.await()
    }
    //Lager en anonym bruker
    override suspend fun createAnonymousAccount() {
        auth.signInAnonymously().await()
    }

    //Lenker en ny bruker med den gitte emailen og passordet skrevet inn av brukeren
    override suspend fun linkAccount(email: String, password: String, onResult: (Throwable?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { onResult(it.exception) }.await()
    }

    //Logger inn anonymt etter å ha logget ut for å opprettholde sesjonen
    override suspend fun signOut() {
        auth.signOut()

        auth.signInAnonymously()
    }
}