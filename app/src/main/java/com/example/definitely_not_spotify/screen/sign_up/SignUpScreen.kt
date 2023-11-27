package com.example.definitely_not_spotify.screen.sign_up

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.definitely_not_spotify.R

@Composable
fun SignUpScreen(
    loggedIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    //Oppdaterer UI state fra ViewModel
    val uiState by viewModel.uiState
    //Ser på om user er anonym fra ViewModel
    val isAnonymous by viewModel.isAnonymous.collectAsState(initial = true)

    //Modifier for å style input feltene
    val fieldModifier = Modifier
        .fillMaxWidth()
        .padding(16.dp, 4.dp)
    //Sjekker om bruker er anonym for å bestemme UI som skal vises
    if (isAnonymous) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
             //Viser error hvis det er en
            if (uiState.errorMessage != 0)
                Text(text = stringResource(id = uiState.errorMessage),
                    Modifier.padding(vertical = 8.dp))

            //Input felt for email, passord og gjenta passord
            EmailField(uiState.email, viewModel::onEmailChange, fieldModifier)
            PasswordField(uiState.password, viewModel::onPasswordChange, fieldModifier)
            RepeatPasswordField(uiState.repeatPassword, viewModel::onRepeatPasswordChange, fieldModifier)

            //Knapper for å logge inn og sign in
            Row {
                Button(
                    onClick = { viewModel.onLoginClick(loggedIn) },
                    modifier = Modifier
                        .padding(16.dp, 8.dp),
                ) {
                    Text(text = stringResource(R.string.login), fontSize = 16.sp)
                }
                Button(
                    onClick = { viewModel.onSignUpClick(loggedIn) },
                    modifier = Modifier
                        .padding(16.dp, 8.dp),
                ) {
                    Text(text = stringResource(R.string.create_account), fontSize = 16.sp)
                }
            }
        }
    }
    else {
        //Viser en knapp for å signere ut hvis brukeren ikke er anonym
        Button(
            onClick = { viewModel.onSignOutClick() },
            modifier = Modifier
                .padding(16.dp, 8.dp),
        ) {
            Text(text = stringResource(R.string.sign_out), fontSize = 16.sp)
        }
    }
}

// Funksjon for å lage en email input felt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(R.string.email)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") }
    )
}

//Funksjon for å lage passord input felt
@Composable
fun PasswordField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    PasswordField(value, R.string.password, onNewValue, modifier)
}

// Funksjon for å lage en gjenta passord input felt
@Composable
fun RepeatPasswordField(
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PasswordField(value, R.string.repeat_password, onNewValue, modifier)
}
// Funksjon for å lage passord input felt med toggle og placeholder
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordField(
    value: String,
    @StringRes placeholder: Int,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    //State som holder på passord sin synlighet
    var isVisible by remember { mutableStateOf(false) }

    // Bestemmer synlighets ikon basert på state
    val icon =
        if (isVisible) painterResource(R.drawable.ic_visibility_on)
        else painterResource(R.drawable.ic_visibility_off)

    // Bestemmer visuelle transformasjonen basert på synlighet
    val visualTransformation =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

    //Lager passord input felt
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = stringResource(placeholder)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
        trailingIcon = {
            // Knapp for å trykke på passord synlighet
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(painter = icon, contentDescription = "Visibility")
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation
    )
}