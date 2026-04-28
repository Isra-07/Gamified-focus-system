package com.thrive.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thrive.domain.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isSaving: Boolean = false,
    val isSavingPassword: Boolean = false,
    val saveSuccess: Boolean = false,
    val passwordSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val user = userRepository.getUser()
            _state.update {
                it.copy(
                    name = user?.name.orEmpty(),
                    email = user?.email.orEmpty()
                )
            }
        }
    }

    fun setName(name: String) {
        _state.update { it.copy(name = name, error = null) }
    }

    fun setEmail(email: String) {
        _state.update { it.copy(email = email, error = null) }
    }

    fun setCurrentPassword(password: String) {
        _state.update { it.copy(currentPassword = password, error = null) }
    }

    fun setNewPassword(password: String) {
        _state.update { it.copy(newPassword = password, error = null) }
    }

    fun setConfirmPassword(password: String) {
        _state.update { it.copy(confirmPassword = password, error = null) }
    }

    fun saveProfile() {
        val name = state.value.name.trim()
        val email = state.value.email.trim()
        val error = when {
            name.isBlank() -> "Name is required"
            email.isBlank() -> "Email is required"
            !email.contains("@") || !email.contains(".") -> "Enter a valid email address"
            else -> null
        }
        if (error != null) {
            _state.update { it.copy(error = error) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            userRepository.updateProfile(name = name, email = email)
            _state.update { it.copy(isSaving = false, saveSuccess = true) }
            delay(2000)
            _state.update { it.copy(saveSuccess = false) }
        }
    }

    fun changePassword() {
        val current = state.value.currentPassword
        val newPassword = state.value.newPassword
        val confirm = state.value.confirmPassword
        viewModelScope.launch {
            val user = userRepository.getUser()
            val error = when {
                user == null -> "User not found"
                user.password.isNotBlank() && current != user.password -> "Current password is incorrect"
                newPassword.length < 6 -> "New password must be at least 6 characters"
                newPassword != confirm -> "Passwords do not match"
                else -> null
            }
            if (error != null) {
                _state.update { it.copy(error = error) }
                return@launch
            }

            _state.update { it.copy(isSavingPassword = true, error = null) }
            userRepository.updatePassword(newPassword)
            _state.update {
                it.copy(
                    currentPassword = "",
                    newPassword = "",
                    confirmPassword = "",
                    isSavingPassword = false,
                    passwordSuccess = true
                )
            }
            delay(2000)
            _state.update { it.copy(passwordSuccess = false) }
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            userRepository.deleteUser()
            onDone()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLoggedOut: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showLogoutConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) snackbarHostState.showSnackbar("Profile updated ✓")
    }

    LaunchedEffect(state.passwordSuccess) {
        if (state.passwordSuccess) snackbarHostState.showSnackbar("Password updated")
    }

    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            title = { Text("Log Out?") },
            text = { Text("You’ll need to sign in again to continue.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutConfirm = false
                        viewModel.logout(onDone = onLoggedOut)
                    }
                ) { Text("Log Out") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Profile & Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            val initial = state.name.trim().firstOrNull()?.uppercase() ?: "?"

            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Text(initial, color = Color.White, fontWeight = FontWeight.Black, style = MaterialTheme.typography.headlineLarge)
            }

            Text(
                text = state.name.ifBlank { "Your Name" },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = state.email.ifBlank { "you@example.com" },
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(Modifier.height(6.dp))
            Text("Account", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f))

            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::setName,
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::setEmail,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            if (state.error != null) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = viewModel::saveProfile,
                enabled = !state.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                if (state.isSaving) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                else Text("Save Changes", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(8.dp))
            Text("Change Password", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f))

            OutlinedTextField(
                value = state.currentPassword,
                onValueChange = viewModel::setCurrentPassword,
                label = { Text("Current Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )
            OutlinedTextField(
                value = state.newPassword,
                onValueChange = viewModel::setNewPassword,
                label = { Text("New Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = viewModel::setConfirmPassword,
                label = { Text("Confirm New Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Button(
                onClick = viewModel::changePassword,
                enabled = !state.isSavingPassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                if (state.isSavingPassword) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                else Text("Update Password", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(8.dp))
            Text("Account Actions", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f))

            OutlinedButton(
                onClick = { showLogoutConfirm = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Log Out", fontWeight = FontWeight.Bold)
            }
        }
    }
}
