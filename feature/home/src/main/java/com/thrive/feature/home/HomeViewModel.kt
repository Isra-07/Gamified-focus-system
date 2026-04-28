package com.thrive.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thrive.domain.FocusSession
import com.thrive.domain.SessionRepository
import com.thrive.domain.UserRepository
import com.thrive.domain.model.Challenge
import com.thrive.domain.repository.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val userName: String = "You",
    val totalXp: Int = 0,
    val currentStreak: Int = 0,
    val todayMinutes: Int = 0,
    val todayXp: Int = 0,
    val weekDays: List<Boolean> = List(7) { false },
    val recentSessions: List<FocusSession> = emptyList(),
    val activeChallenge: Challenge? = null,
    val xpGoal: Int = 2000,
    val messageIndex: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val challengeRepository: ChallengeRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())

    val state: StateFlow<HomeUiState> = combine(
        userRepository.observeUser(),
        sessionRepository.observeAllSessions(),
        challengeRepository.observeChallenges(),
        _state
    ) { user, sessions, challenges, current ->
        val today = LocalDate.now()
        val todaySessions = sessions.filter { it.completedAt == today.toString() }
        val weekDays = (1..7).map { dayOfWeek ->
            val daysFromToday = today.dayOfWeek.value - dayOfWeek
            val date = today.minusDays(daysFromToday.toLong())
            sessions.any { it.completedAt == date.toString() }
        }

        HomeUiState(
            userName = user.name.ifBlank { "You" },
            totalXp = user.totalXp,
            currentStreak = user.currentStreak,
            todayMinutes = todaySessions.sumOf { it.actualSeconds / 60 },
            todayXp = todaySessions.sumOf { it.xpEarned },
            weekDays = weekDays,
            recentSessions = sessions.take(3),
            activeChallenge = challenges.firstOrNull { !it.completed },
            xpGoal = current.xpGoal,
            messageIndex = current.messageIndex,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun nextMessage() {
        _state.update { it.copy(messageIndex = (it.messageIndex + 1) % 6) }
    }
}
