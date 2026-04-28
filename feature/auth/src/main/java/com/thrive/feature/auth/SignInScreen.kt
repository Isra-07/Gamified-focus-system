package com.thrive.feature.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thrive.domain.User
import com.thrive.domain.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// ── ViewModel ─────────────────────────────────────────────────────────────────

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isDone: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    // null = checking, true = logged in, false = not logged in
    val isLoggedIn: StateFlow<Boolean?> = flow {
        emit(userRepository.getUser() != null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun signIn(email: String, password: String) {
        val error = validate(email, password)
        if (error != null) {
            _state.value = AuthUiState(error = error)
            return
        }
        viewModelScope.launch {
            _state.value = AuthUiState(isLoading = true)
            val existing = userRepository.getUser()
            if (existing != null) {
                // User already in DB → go to Home
                _state.value = when {
                    existing.email.isNotBlank() && existing.email != email -> AuthUiState(error = "No account found for this email")
                    existing.password.isNotBlank() && existing.password != password -> AuthUiState(error = "Incorrect password")
                    else -> AuthUiState(isDone = true)
                }
            } else {
                // User was logged out (deleted from DB) → recreate from email
                userRepository.saveUser(
                    User(
                        name = email.substringBefore("@"),
                        email = email,
                        password = password
                    )
                )
                _state.value = AuthUiState(isDone = true)
            }
        }
    }

    fun signUp(email: String, password: String, confirm: String) {
        val error = validate(email, password)
            ?: if (password != confirm) "Passwords do not match" else null
        if (error != null) {
            _state.value = AuthUiState(error = error)
            return
        }
        viewModelScope.launch {
            _state.value = AuthUiState(isLoading = true)
            userRepository.saveUser(
                User(
                    name = email.substringBefore("@"),
                    email = email,
                    password = password
                )
            )
            _state.value = AuthUiState(isDone = true)
        }
    }

    fun continueWithGoogle() {
        viewModelScope.launch {
            _state.value = AuthUiState(isLoading = true)
            val existing = userRepository.getUser()
            if (existing == null) {
                userRepository.saveUser(User(name = "Google User"))
            }
            _state.value = AuthUiState(isDone = true)
        }
    }

    fun logout(onDone: () -> Unit) = viewModelScope.launch {
        userRepository.deleteUser()
        _state.value = AuthUiState()
        onDone()
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun validate(email: String, password: String): String? = when {
        email.isBlank() -> "Email is required"
        !email.contains("@") || !email.contains(".") -> "Enter a valid email address"
        password.length < 6 -> "Password must be at least 6 characters"
        else -> null
    }
}

// ── Sign In Screen ─────────────────────────────────────────────────────────────

@Composable
fun SignInScreen(
    onSignedIn: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state.isDone) { if (state.isDone) onSignedIn() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎯", fontSize = 56.sp)
        Spacer(Modifier.height(8.dp))
        Text(
            "Thrive",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Gamified Focus System",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
        )

        Spacer(Modifier.height(36.dp))

        // Google button (fake)
        OutlinedButton(
            onClick = { viewModel.continueWithGoogle() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("🔵  Continue with Google", fontSize = 15.sp)
        }

        Spacer(Modifier.height(16.dp))

        // Divider
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(Modifier.weight(1f))
            Text(
                "  or  ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(0.4f)
            )
            HorizontalDivider(Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; viewModel.clearError() },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            isError = state.error != null
        )

        Spacer(Modifier.height(12.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; viewModel.clearError() },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (showPass) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = { showPass = !showPass }) {
                    Text(
                        if (showPass) "Hide" else "Show",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            },
            singleLine = true,
            isError = state.error != null
        )

        // Error message
        if (state.error != null) {
            Spacer(Modifier.height(6.dp))
            Text(
                state.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(20.dp))

        // Sign In button
        Button(
            onClick = { viewModel.signIn(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = !state.isLoading
        ) {
            if (state.isLoading)
                CircularProgressIndicator(Modifier.size(20.dp), color = Color.White)
            else
                Text("Sign In", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        // Sign Up link
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Don't have an account?",
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
            )
            TextButton(onClick = onNavigateToSignUp) {
                Text("Sign Up", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ── Sign Up Screen ─────────────────────────────────────────────────────────────

@Composable
fun SignUpScreen(
    onSignedUp: () -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state.isDone) { if (state.isDone) onSignedUp() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Join Thrive and start focusing",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
        )

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it; viewModel.clearError() },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            isError = state.error != null
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; viewModel.clearError() },
            label = { Text("Password (min 6 characters)") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            isError = state.error != null
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = confirm,
            onValueChange = { confirm = it; viewModel.clearError() },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            isError = state.error != null
        )

        if (state.error != null) {
            Spacer(Modifier.height(6.dp))
            Text(
                state.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { viewModel.signUp(email, password, confirm) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = !state.isLoading
        ) {
            if (state.isLoading)
                CircularProgressIndicator(Modifier.size(20.dp), color = Color.White)
            else
                Text("Create Account", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(12.dp))
        TextButton(onClick = onBack) {
            Text("← Back to Sign In")
        }
    }
}
