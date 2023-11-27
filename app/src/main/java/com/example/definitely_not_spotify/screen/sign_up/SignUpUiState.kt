package com.example.definitely_not_spotify.screen.sign_up

import androidx.annotation.StringRes

//Klasse for å lage bruker som samarbeider med å logge inn
data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    @StringRes val errorMessage: Int = 0
)