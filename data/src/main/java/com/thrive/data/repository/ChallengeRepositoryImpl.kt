package com.thrive.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import com.thrive.domain.model.Challenge
import com.thrive.domain.model.ChallengeType
import com.thrive.domain.ChallengeEvalResult
import com.thrive.domain.repository.ChallengeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChallengeRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : ChallengeRepository {
    private val prefs = context.getSharedPreferences("challenge_state", Context.MODE_PRIVATE)
    private val _challenges = MutableStateFlow(loadChallenges())

    override fun observeChallenges(): Flow<List<Challenge>> = _challenges.asStateFlow()

    override suspend fun seedDefaults() {
        if (_challenges.value.isEmpty()) {
            saveChallengeState(defaultChallenges())
        }
    }

    /**
     * NOTE: The current project domain API exposes `seedDefaults()` rather than a DAO-backed
     * `initDefaultChallenges()`. This helper preserves progress by never overwriting once seeded.
     */
    override suspend fun initDefaultChallenges() {
        if (_challenges.value.isNotEmpty()) return
        seedDefaults()
    }

    override suspend fun evaluateChallenge(
        totalSessions: Int,
        totalMinutes: Int,
        hadCleanSession: Boolean,
        lastSessionMinutes: Int,
        lastSessionDistractions: Int,
        currentStreak: Int,
        challengeAlreadyCompletedToday: Boolean
    ): ChallengeEvalResult {
        if (challengeAlreadyCompletedToday) {
            return ChallengeEvalResult(challengeCompleted = false)
        }

        val challenges = _challenges.value
        val activeIndex = challenges.indexOfFirst { !it.completed }
        if (activeIndex < 0) return ChallengeEvalResult(challengeCompleted = false)

        val active = challenges[activeIndex]
        val isComplete = when (active.type) {
            ChallengeType.ConsecutiveSessions -> totalSessions >= active.target
            ChallengeType.TotalMinutes -> totalMinutes >= active.target
            ChallengeType.DistractionFree -> hadCleanSession && lastSessionDistractions == 0
        }

        return if (isComplete) {
            val updated = challenges.toMutableList().apply {
                this[activeIndex] = active.copy(completed = true, progress = active.target)
            }
            saveChallengeState(updated)
            ChallengeEvalResult(
                challengeCompleted = true,
                completedTitle = active.title,
                completedXpReward = active.rewardXp
            )
        } else {
            val newProgress = when (active.type) {
                ChallengeType.ConsecutiveSessions -> totalSessions.coerceAtMost(active.target)
                ChallengeType.TotalMinutes -> totalMinutes.coerceAtMost(active.target)
                ChallengeType.DistractionFree -> if (hadCleanSession && lastSessionDistractions == 0) 1 else active.progress
            }
            val updated = challenges.toMutableList().apply {
                this[activeIndex] = active.copy(progress = newProgress)
            }
            saveChallengeState(updated)
            ChallengeEvalResult(challengeCompleted = false)
        }
    }

    override suspend fun saveChallenges(challenges: List<Challenge>) {
        saveChallengeState(challenges)
    }

    private fun saveChallengeState(challenges: List<Challenge>) {
        _challenges.value = challenges
        prefs.edit()
            .putString(KEY_CHALLENGES, challenges.joinToString("|") { "${it.id},${it.progress},${it.completed}" })
            .apply()
    }

    private fun loadChallenges(): List<Challenge> {
        val saved = prefs.getString(KEY_CHALLENGES, null) ?: return emptyList()
        val savedById = saved.split("|")
            .mapNotNull { row ->
                val parts = row.split(",")
                val id = parts.getOrNull(0)?.toLongOrNull() ?: return@mapNotNull null
                val progress = parts.getOrNull(1)?.toIntOrNull() ?: 0
                val completed = parts.getOrNull(2)?.toBooleanStrictOrNull() ?: false
                id to (progress to completed)
            }
            .toMap()

        return defaultChallenges().map { challenge ->
            val savedState = savedById[challenge.id]
            if (savedState == null) challenge
            else challenge.copy(progress = savedState.first, completed = savedState.second)
        }
    }

    private fun defaultChallenges() = listOf(
        Challenge(1, "Focus Rookie", "Complete 30 minutes of focus time", ChallengeType.TotalMinutes, 30, 120, progress = 0, completed = false),
        Challenge(2, "Distraction Free", "Complete a session without any distractions", ChallengeType.DistractionFree, 1, 150, progress = 0, completed = false),
        Challenge(3, "Consistency", "Complete 3 focus sessions", ChallengeType.ConsecutiveSessions, 3, 180, progress = 0, completed = false),
        Challenge(4, "Deep Focus", "Focus for a total of 60 minutes", ChallengeType.TotalMinutes, 60, 220, progress = 0, completed = false),
        Challenge(5, "Double Down", "Complete 2 distraction-free sessions", ChallengeType.DistractionFree, 2, 260, progress = 0, completed = false),
        Challenge(6, "Momentum", "Complete 5 focus sessions", ChallengeType.ConsecutiveSessions, 5, 300, progress = 0, completed = false),
        Challenge(7, "Focus Builder", "Focus for a total of 120 minutes", ChallengeType.TotalMinutes, 120, 340, progress = 0, completed = false),
        Challenge(8, "Expert Mode", "Complete 3 distraction-free sessions", ChallengeType.DistractionFree, 3, 400, progress = 0, completed = false),
        Challenge(9, "Marathon", "Focus for a total of 180 minutes", ChallengeType.TotalMinutes, 180, 450, progress = 0, completed = false),
        Challenge(10, "Legendary", "Complete 10 focus sessions", ChallengeType.ConsecutiveSessions, 10, 550, progress = 0, completed = false)
    )

    private companion object {
        const val KEY_CHALLENGES = "challenges"
    }
}
