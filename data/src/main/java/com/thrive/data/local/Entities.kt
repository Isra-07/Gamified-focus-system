package com.thrive.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "local_user",
    val name: String = "You",
    val email: String = "",
    val password: String = "",
    val totalXp: Int = 0,
    val currentStreak: Int = 0,
    val lastActiveDate: String? = null,
    val lastChallengeDate: String? = null
)

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey val id: String,
    val startTime: Long,
    val targetSeconds: Int,
    val actualSeconds: Int,
    val xpEarned: Int,
    val distractionCount: Int,
    val focusRate: Float,
    val completedAt: String
)

@Entity(tableName = "distractions")
data class DistractionEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val packageName: String,
    val appName: String,
    val timestamp: Long
)
