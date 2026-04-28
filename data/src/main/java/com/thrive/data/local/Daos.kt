package com.thrive.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    fun observe(): Flow<UserEntity?>

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun get(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(user: UserEntity)

    @Query("UPDATE users SET name = :name, email = :email")
    suspend fun updateProfile(name: String, email: String)

    @Query("UPDATE users SET password = :password WHERE id = 'local_user'")
    suspend fun updatePassword(password: String)

    @Query("DELETE FROM users")
    suspend fun deleteAll()

    @Query("UPDATE users SET totalXp = totalXp + :xp")
    suspend fun addXp(xp: Int)

    @Query("UPDATE users SET currentStreak = :streak, lastActiveDate = :date WHERE id = 'local_user'")
    suspend fun updateStreak(streak: Int, date: String)

    @Query("UPDATE users SET lastChallengeDate = :date WHERE id = 'local_user'")
    suspend fun updateLastChallengeDate(date: String)

    @Query("SELECT lastChallengeDate FROM users WHERE id = 'local_user'")
    suspend fun getLastChallengeDate(): String?
}

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(session: SessionEntity)

    @Query("SELECT * FROM sessions ORDER BY startTime DESC")
    fun observeAll(): Flow<List<SessionEntity>>

    @Query("SELECT COALESCE(SUM(xpEarned), 0) FROM sessions")
    suspend fun getTotalXp(): Int
}

@Dao
interface DistractionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(event: DistractionEntity)

    @Query("SELECT * FROM distractions WHERE sessionId = :sessionId")
    suspend fun getForSession(sessionId: String): List<DistractionEntity>
}
