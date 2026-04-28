package com.thrive.domain

import java.time.LocalDate

data class User(
    val id: String = "local_user",
    val name: String = "You",
    val email: String = "",
    val password: String = "",
    val totalXp: Int = 0,
    val currentStreak: Int = 0,
    val lastActiveDate: String? = null,
    val lastChallengeDate: String? = null  // last day a daily challenge was completed
)

data class FocusSession(
    val id: String,
    val startTime: Long,
    val targetSeconds: Int,
    val actualSeconds: Int,
    val xpEarned: Int,
    val distractionCount: Int,
    val focusRate: Float,
    val completedAt: String  // LocalDate.now().toString()
)

data class DistractionEvent(
    val id: String,
    val sessionId: String,
    val packageName: String,
    val appName: String,
    val timestamp: Long
)

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val xp: Int,
    val streak: Int,
    val isMe: Boolean = false
)

data class ChallengeEvalResult(
    val challengeCompleted: Boolean,
    val completedTitle: String = "",
    val completedXpReward: Int = 0
)
