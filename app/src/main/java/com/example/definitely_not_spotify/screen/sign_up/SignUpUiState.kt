package com.example.definitely_not_spotify.screen.sign_up

import androidx.annotation.StringRes

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    @StringRes val errorMessage: Int = 0
)