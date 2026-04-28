package com.thrive.domain.usecase

import com.thrive.domain.challenge.ChallengeEngine
import com.thrive.domain.model.Challenge
import com.thrive.domain.model.DistractionEvent
import com.thrive.domain.model.FocusSession
import com.thrive.domain.model.Rank
import com.thrive.domain.model.SummaryStats
import com.thrive.domain.model.User
import com.thrive.domain.repository.AnalyticsRepository
import com.thrive.domain.repository.ChallengeRepository
import com.thrive.domain.repository.SessionRepository
import com.thrive.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlin.math.roundToInt

class CalculateXpUseCase {
    operator fun invoke(minutes: Int, distractions: Int, streakDays: Int): Int {
        val base = minutes * 10
        val distractionPenalty = (distractions * 25).coerceAtMost(base / 2)
        val multiplier = if (streakDays >= 3) 1.2 else 1.0
        return ((base - distractionPenalty) * multiplier).roundToInt()
    }
}

class CheckRankPromotionUseCase {
    operator fun invoke(totalXp: Int): Rank = Rank.fromXp(totalXp)
}

class StartSessionUseCase(
    private val users: UserRepository,
    private val sessions: SessionRepository
) {
    suspend operator fun invoke(minutes: Int): FocusSession {
        val user = users.requireUser()
        return sessions.startSession(user.id, minutes, System.currentTimeMillis())
    }
}

class CompleteSessionUseCase(
    private val users: UserRepository,
    private val sessions: SessionRepository,
    private val calculateXp: CalculateXpUseCase
) {
    suspend operator fun invoke(session: FocusSession): Int {
        val user = users.requireUser()
        val xp = calculateXp(session.targetMinutes, session.distractionCount, user.streakDays)
        sessions.completeSession(session.id, System.currentTimeMillis(), xp)
        users.addXp(user.id, xp)
        users.updateStreak(user.id, System.currentTimeMillis())
        return xp
    }
}

class RecordDistractionUseCase(
    private val sessions: SessionRepository
) {
    suspend operator fun invoke(sessionId: Long, appName: String) {
        sessions.recordDistraction(
            DistractionEvent(
                sessionId = sessionId,
                packageName = appName.lowercase().replace(" ", "."),
                appName = appName,
                detectedAt = System.currentTimeMillis(),
                durationSeconds = 5
            )
        )
    }
}

class ObserveHomeUseCase(
    private val users: UserRepository,
    private val analytics: AnalyticsRepository,
    private val challenges: ChallengeRepository
) {
    operator fun invoke(): Flow<HomeSnapshot> =
        combine(users.observeUser(), analytics.observeSummary(), challenges.observeChallenges()) { user, summary, challengeList ->
            HomeSnapshot(user, summary, challengeList.firstOrNull())
        }
}

data class HomeSnapshot(
    val user: User?,
    val summary: SummaryStats,
    val activeChallenge: Challenge?
)

class EvaluateChallengesUseCase(
    private val challengeRepository: ChallengeRepository,
    private val sessionRepository: SessionRepository,
    private val engine: ChallengeEngine = ChallengeEngine()
) {
    fun observe(): Flow<List<Challenge>> =
        combine(challengeRepository.observeChallenges(), sessionRepository.observeCompletedSessions()) { challenges, sessions ->
            engine.evaluate(challenges, sessions)
        }
}
