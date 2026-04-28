package com.thrive.domain.repository

import com.thrive.domain.ChallengeEvalResult
import com.thrive.domain.model.Challenge
import com.thrive.domain.model.DistractionEvent
import com.thrive.domain.model.FocusSession
import com.thrive.domain.model.FriendScore
import com.thrive.domain.model.MoodEntry
import com.thrive.domain.model.SummaryStats
import com.thrive.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    suspend fun signIn(email: String, password: String): User
    suspend fun createAccount(email: String, displayName: String, password: String): User
    suspend fun completeOnboarding(userId: Long)
}

interface UserRepository {
    fun observeUser(): Flow<User?>
    suspend fun requireUser(): User
    suspend fun addXp(userId: Long, xp: Int)
    suspend fun updateStreak(userId: Long, completedAt: Long)
}

interface SessionRepository {
    fun observeActiveSession(): Flow<FocusSession?>
    fun observeCompletedSessions(): Flow<List<FocusSession>>
    suspend fun startSession(userId: Long, targetMinutes: Int, startedAt: Long): FocusSession
    suspend fun completeSession(sessionId: Long, endedAt: Long, xp: Int)
    suspend fun abandonSession(sessionId: Long, endedAt: Long)
    suspend fun activeSession(): FocusSession?
    suspend fun recordDistraction(event: DistractionEvent)
}

interface ChallengeRepository {
    fun observeChallenges(): Flow<List<Challenge>>
    suspend fun seedDefaults()
    suspend fun saveChallenges(challenges: List<Challenge>)
    suspend fun initDefaultChallenges()
    suspend fun evaluateChallenge(
        totalSessions: Int,
        totalMinutes: Int,
        hadCleanSession: Boolean,
        lastSessionMinutes: Int,
        lastSessionDistractions: Int,
        currentStreak: Int,
        challengeAlreadyCompletedToday: Boolean
    ): ChallengeEvalResult
}

interface AnalyticsRepository {
    fun observeSummary(): Flow<SummaryStats>
    suspend fun saveMood(entry: MoodEntry)
}

interface FriendsRepository {
    fun observeLeaderboard(): Flow<List<FriendScore>>
}

interface PreferencesRepository {
    fun observeCrashRecoveryPrompt(): Flow<Boolean>
    suspend fun markTimerRunning(sessionId: Long)
    suspend fun clearTimerRunning()
}
