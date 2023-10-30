package com.example.definitely_not_spotify.service

import com.example.definitely_not_spotify.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    val currentUser: Flow<User>

    suspend fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit)
    suspend fun createAnonymousAccount()
    suspend fun linkAccount(email: String, password: String, onResult: (Throwable?) -> Unit)
    suspend fun signOut()
}