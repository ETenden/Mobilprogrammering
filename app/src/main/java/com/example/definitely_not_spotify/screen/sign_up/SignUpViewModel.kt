package com.example.definitely_not_spotify.screen.sign_up

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.definitely_not_spotify.R
import com.example.definitely_not_spotify.common.isValidEmail
import com.example.definitely_not_spotify.common.isValidPassword
import com.example.definitely_not_spotify.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService,
) : ViewModel() {
    // Mutable state som representerer UI for sign-up skjerm
    var uiState = mutableStateOf(SignUpUiState())
        private set

    // Hjelpe props for å aksessere email og passord fra UI
    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    // Observerer om bruker er anonym
    val isAnonymous = accountService.currentUser.map { it.isAnonymous }

    //Funksjon som håndterer endringer i email feltet
    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    //Funksjon som håndterer endringer i passord feltet
    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    //Funksjon som håndterer endringer i gjenta passord feltet
    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    //Funksjon for å håndtere at login knappen blir trykket
    fun onLoginClick(loggedIn: () -> Unit) {
        //Validerer email formatet
        if (!email.isValidEmail()) {
            uiState.value = uiState.value.copy(errorMessage = R.string.email_error)
            return
        }

        //Validerer passord formatet
        else if (!password.isValidPassword()) {
            uiState.value = uiState.value.copy(errorMessage = R.string.password_error)
            return
        }

        // Ufører autentikasjon og håndterer suksess/error
        viewModelScope.launch {
            try {
                accountService.authenticate(email, password) { error ->
                    if (error == null)
                        loggedIn()
                }
            }
            catch(e: Exception) {
                uiState.value = uiState.value.copy(errorMessage = R.string.could_not_log_in)
            }
        }
    }

    // Funksjon som håndterer at sign-up knapp blir trykket
    fun onSignUpClick(loggedIn: () -> Unit) {
        if (!email.isValidEmail()) {
            uiState.value = uiState.value.copy(errorMessage = R.string.email_error)
            return
        }

        // Validerer passord formatet
        else if (!password.isValidPassword()) {
            uiState.value = uiState.value.copy(errorMessage = R.string.password_error)
            return
        }
        //Validerer at passord er lik gjenta passord
        else if (!(password == uiState.value.repeatPassword)) {
            uiState.value = uiState.value.copy(errorMessage = R.string.password_match_error)
            return
        }

        // Utfører bruker kobling og håndterer suksess/error
        viewModelScope.launch {
            try {
                accountService.linkAccount(email, password) { error ->
                    if (error == null)
                        loggedIn()
                }
            }
            catch(e: Exception) {
                uiState.value = uiState.value.copy(errorMessage = R.string.could_not_create_account)
            }
        }
    }

    // Funksjon for å håndtere at sign-out knappen blir trykket på
    fun onSignOutClick() {
        viewModelScope.launch {
            accountService.signOut()
        }
    }
}