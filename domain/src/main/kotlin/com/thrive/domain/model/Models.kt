package com.thrive.domain.model

enum class Rank(val minXp: Int) {
    Bronze(0),
    Silver(1_000),
    Gold(2_500),
    Platinum(5_000);

    companion object {
        fun fromXp(xp: Int): Rank = entries.last { xp >= it.minXp }
        fun nextAfter(rank: Rank): Rank? = entries.getOrNull(rank.ordinal + 1)
    }
}

data class User(
    val id: Long = 0,
    val email: String,
    val displayName: String,
    val totalXp: Int = 0,
    val weeklyXp: Int = 0,
    val streakDays: Int = 0,
    val rank: Rank = Rank.Bronze,
    val onboardingComplete: Boolean = false
)

enum class TimerState {
    Idle,
    Running,
    Paused,
    Completed,
    Abandoned
}

data class FocusSession(
    val id: Long = 0,
    val userId: Long,
    val targetMinutes: Int,
    val startedAt: Long,
    val endedAt: Long? = null,
    val completed: Boolean = false,
    val abandoned: Boolean = false,
    val distractionCount: Int = 0,
    val xpEarned: Int = 0
)

data class DistractionEvent(
    val id: Long = 0,
    val sessionId: Long,
    val packageName: String,
    val appName: String,
    val detectedAt: Long,
    val durationSeconds: Int = 0
)

enum class ChallengeType {
    TotalMinutes,
    DistractionFree,
    ConsecutiveSessions
}

data class Challenge(
    val id: Long = 0,
    val title: String,
    val description: String,
    val type: ChallengeType,
    val target: Int,
    val rewardXp: Int,
    val progress: Int = 0,
    val completed: Boolean = false
)

data class MoodEntry(
    val id: Long = 0,
    val userId: Long,
    val mood: String,
    val note: String,
    val createdAt: Long
)

data class SummaryStats(
    val focusMinutes: Int = 0,
    val completedSessions: Int = 0,
    val distractions: Int = 0,
    val averageMinutes: Int = 0,
    val focusRate: Int = 100
)

data class FriendScore(
    val name: String,
    val weeklyXp: Int,
    val focusMinutes: Int
)
