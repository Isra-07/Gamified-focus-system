package com.thrive.feature.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thrive.domain.User
import com.thrive.domain.UserRepository
import com.thrive.domain.model.Challenge
import com.thrive.domain.repository.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class ChallengeUiState(
    val challenges: List<Challenge> = emptyList(),
    val lastChallengeDate: String? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChallengeUiState())
    val state: StateFlow<ChallengeUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            challengeRepository.seedDefaults()
            if (userRepository.getUser() == null) {
                userRepository.saveUser(User())
            }
            recoverCompletedChallengeIfNeeded()
        }
        viewModelScope.launch {
            combine(
                challengeRepository.observeChallenges(),
                userRepository.observeUser()
            ) { challenges, user ->
                challenges to user.lastChallengeDate
            }.collect { (challenges, lastChallengeDate) ->
                _state.update { it.copy(challenges = challenges, lastChallengeDate = lastChallengeDate, isLoading = false) }
            }
        }
    }

    private suspend fun recoverCompletedChallengeIfNeeded() {
        val user = userRepository.getUser() ?: return
        if (user.lastChallengeDate != LocalDate.now().toString()) return

        val challenges = challengeRepository.observeChallenges().first()
        if (challenges.isNotEmpty() && challenges.none { it.completed }) {
            challengeRepository.saveChallenges(
                challenges.toMutableList().apply {
                    this[0] = this[0].copy(completed = true, progress = this[0].target)
                }
            )
        }
    }
}

// ── Detail ViewModel ──────────────────────────────────────────────────────────
@HiltViewModel
class ChallengeDetailViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    init {
        viewModelScope.launch {
            challengeRepository.seedDefaults()
            if (userRepository.getUser() == null) {
                userRepository.saveUser(User())
            }
            recoverCompletedChallengeIfNeeded()
        }
    }

    private suspend fun recoverCompletedChallengeIfNeeded() {
        val user = userRepository.getUser() ?: return
        if (user.lastChallengeDate != LocalDate.now().toString()) return

        val challenges = challengeRepository.observeChallenges().first()
        if (challenges.isNotEmpty() && challenges.none { it.completed }) {
            challengeRepository.saveChallenges(
                challenges.toMutableList().apply {
                    this[0] = this[0].copy(completed = true, progress = this[0].target)
                }
            )
        }
    }

    val challenges: StateFlow<List<Challenge>> = challengeRepository
        .observeChallenges()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lastChallengeDate: StateFlow<String?> = userRepository.observeUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), com.thrive.domain.User())
        .let { flow -> flow.map { it.lastChallengeDate } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
