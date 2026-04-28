package com.thrive.domain

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUser(): Flow<User>
    suspend fun getUser(): User?
    suspend fun saveUser(user: User)
    suspend fun addXp(xp: Int)
    suspend fun updateProfile(name: String, email: String)
    suspend fun updatePassword(password: String)
    suspend fun deleteUser()
    suspend fun updateStreakOnChallengeComplete()   // called only on daily challenge complete
    suspend fun checkAndResetStreakIfBroken()       // called on app launch
}

interface SessionRepository {
    suspend fun saveSession(session: FocusSession)
    fun observeAllSessions(): Flow<List<FocusSession>>
    suspend fun getTotalXp(): Int
}

interface DistractionRepository {
    suspend fun saveDistraction(event: DistractionEvent)
    suspend fun getDistractionsForSession(sessionId: String): List<DistractionEvent>
}
