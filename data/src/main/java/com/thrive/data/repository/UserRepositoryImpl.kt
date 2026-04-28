package com.thrive.data.repository

import com.thrive.data.local.UserDao
import com.thrive.data.local.UserEntity
import com.thrive.domain.User
import com.thrive.domain.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao
) : UserRepository {

    override fun observeUser(): Flow<User> =
        dao.observe().filterNotNull().map { it.toDomain() }

    override suspend fun getUser(): User? = dao.get()?.toDomain()

    override suspend fun saveUser(user: User) = dao.save(user.toEntity())

    override suspend fun addXp(xp: Int) {
        val existing = dao.get()
        if (existing == null) {
            dao.save(UserEntity(totalXp = xp))
        } else {
            dao.addXp(xp)
        }
    }

    override suspend fun deleteUser() = dao.deleteAll()

    override suspend fun updateProfile(name: String, email: String) =
        dao.updateProfile(name, email)

    override suspend fun updatePassword(password: String) =
        dao.updatePassword(password)

    /**
     * Called ONLY when a daily challenge is completed.
     * Rules:
     * - If challenge already completed today → do nothing (streak already counted)
     * - If last challenge was yesterday → streak continues (+1)
     * - If last challenge was before yesterday → streak resets to 1
     * - Update lastChallengeDate to today
     */
    override suspend fun updateStreakOnChallengeComplete() {
        val user = dao.get() ?: UserEntity().also { dao.save(it) }
        val today = LocalDate.now().toString()
        val yesterday = LocalDate.now().minusDays(1).toString()

        // Already completed a challenge today → don't increase streak again
        if (user.lastChallengeDate == today) return

        val newStreak = when (user.lastChallengeDate) {
            yesterday -> user.currentStreak + 1  // continued streak
            null -> 1                             // first ever challenge
            else -> 1                             // streak broken, restart
        }

        dao.updateStreak(newStreak, today)
        dao.updateLastChallengeDate(today)
    }

    /**
     * Called on app launch to check if streak was broken.
     * If user did not complete a challenge yesterday or today → reset streak.
     */
    override suspend fun checkAndResetStreakIfBroken() {
        val user = dao.get() ?: return
        val today = LocalDate.now().toString()
        val yesterday = LocalDate.now().minusDays(1).toString()

        val lastChallenge = user.lastChallengeDate ?: return

        // If last challenge was not today or yesterday → streak is broken
        if (lastChallenge != today && lastChallenge != yesterday) {
            dao.updateStreak(0, today)
        }
    }

    private fun UserEntity.toDomain() = User(
        id = id,
        name = name,
        email = email,
        password = password,
        totalXp = totalXp,
        currentStreak = currentStreak,
        lastActiveDate = lastActiveDate,
        lastChallengeDate = lastChallengeDate
    )

    private fun User.toEntity() = UserEntity(
        id = id,
        name = name,
        email = email,
        password = password,
        totalXp = totalXp,
        currentStreak = currentStreak,
        lastActiveDate = lastActiveDate,
        lastChallengeDate = lastChallengeDate
    )
}
