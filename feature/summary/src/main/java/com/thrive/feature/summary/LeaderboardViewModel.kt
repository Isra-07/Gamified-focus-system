package com.thrive.feature.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thrive.domain.LeaderboardEntry
import com.thrive.domain.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val fakeUsers = listOf(
        LeaderboardEntry(0, "Aria S.", 2840, 21),
        LeaderboardEntry(0, "Marcus T.", 2610, 18),
        LeaderboardEntry(0, "Priya K.", 2390, 15),
        LeaderboardEntry(0, "Leon W.", 2100, 14),
        LeaderboardEntry(0, "Sofia R.", 1980, 12),
        LeaderboardEntry(0, "James B.", 1750, 10),
        LeaderboardEntry(0, "Yuki N.", 1540, 9),
        LeaderboardEntry(0, "Omar A.", 1320, 7),
        LeaderboardEntry(0, "Chloe M.", 1100, 5),
    )

    val board: StateFlow<List<LeaderboardEntry>> =
        userRepository.observeUser()
            .map { me ->
                val myEntry = LeaderboardEntry(
                    0,
                    "You (${me.name})",
                    me.totalXp,
                    me.currentStreak,
                    isMe = true
                )
                (fakeUsers + myEntry)
                    .sortedByDescending { it.xp }
                    .mapIndexed { i, e -> e.copy(rank = i + 1) }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

data class AnalyticsUiState(
    val todayMinutes: Int = 0,
    val weekMinutes: Int = 0,
    val avgSessionMinutes: Int = 0,
    val dailyMinutes: List<Pair<String, Int>> = emptyList(), // day label to minutes
    val totalXp: Int = 0,
    val weekXp: Int = 0,
    val lostXp: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val recentSessions: List<com.thrive.domain.FocusSession> = emptyList(),
    val distractionsByApp: List<Pair<String, Int>> = emptyList(), // appName to count
    val topDistractingApp: String? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val sessionRepository: com.thrive.domain.SessionRepository,
    private val userRepository: com.thrive.domain.UserRepository
) : ViewModel() {

    val state: StateFlow<AnalyticsUiState> = combine(
        sessionRepository.observeAllSessions(),
        userRepository.observeUser()
    ) { sessions, user ->
        val today = java.time.LocalDate.now()
        val weekStart = today.minusDays(6)

        // Today's sessions
        val todaySessions = sessions.filter {
            it.completedAt == today.toString()
        }
        val todayMinutes = todaySessions.sumOf { it.actualSeconds / 60 }

        // This week's sessions
        val weekSessions = sessions.filter {
            try {
                val date = java.time.LocalDate.parse(it.completedAt)
                !date.isBefore(weekStart)
            } catch (e: Exception) {
                false
            }
        }
        val weekMinutes = weekSessions.sumOf { it.actualSeconds / 60 }

        // Average session
        val avgMinutes = if (sessions.isEmpty()) 0
        else sessions.sumOf { it.actualSeconds / 60 } / sessions.size

        // Daily minutes for chart (last 7 days)
        val dayLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val dailyMinutes = (6 downTo 0).mapIndexed { i, daysAgo ->
            val date = today.minusDays(daysAgo.toLong())
            val dayName = dayLabels[date.dayOfWeek.value - 1]
            val mins = sessions.filter { it.completedAt == date.toString() }
                .sumOf { it.actualSeconds / 60 }
            dayName to mins
        }

        // XP
        val totalXpEarned = sessions.sumOf { it.xpEarned }
        val totalDistractions = sessions.sumOf { it.distractionCount }
        val lostXp = totalDistractions * 11
        val weekXp = weekSessions.sumOf { it.xpEarned }

        // Distraction analysis — count by app name
        val distrByApp = sessions
            .groupBy { "Instagram" } // placeholder — real impl uses DistractionRepository
            .map { (app, list) -> app to list.sumOf { it.distractionCount } }
            .sortedByDescending { it.second }
            .take(3)

        // Top distracting app
        val topApp = if (totalDistractions > 2) "Instagram" else null

        AnalyticsUiState(
            todayMinutes = todayMinutes,
            weekMinutes = weekMinutes,
            avgSessionMinutes = avgMinutes,
            dailyMinutes = dailyMinutes,
            totalXp = totalXpEarned,
            weekXp = weekXp,
            lostXp = lostXp,
            currentStreak = user.currentStreak,
            bestStreak = maxOf(user.currentStreak, 0),
            recentSessions = sessions.take(5),
            distractionsByApp = distrByApp,
            topDistractingApp = topApp,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnalyticsUiState())

    fun formatMinutes(minutes: Int): String {
        return if (minutes >= 60) "${minutes / 60}h ${minutes % 60}m"
        else "$minutes min"
    }
}

