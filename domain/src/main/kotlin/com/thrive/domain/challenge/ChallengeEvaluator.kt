package com.thrive.domain.challenge

import com.thrive.domain.model.Challenge
import com.thrive.domain.model.ChallengeType
import com.thrive.domain.model.FocusSession

interface ChallengeEvaluator {
    val type: ChallengeType
    fun evaluate(challenge: Challenge, sessions: List<FocusSession>): Challenge
}

class TotalMinutesEvaluator : ChallengeEvaluator {
    override val type = ChallengeType.TotalMinutes
    override fun evaluate(challenge: Challenge, sessions: List<FocusSession>): Challenge {
        val minutes = sessions.filter { it.completed }.sumOf { it.targetMinutes }
        return challenge.copy(progress = minutes.coerceAtMost(challenge.target), completed = minutes >= challenge.target)
    }
}

class DistractionFreeEvaluator : ChallengeEvaluator {
    override val type = ChallengeType.DistractionFree
    override fun evaluate(challenge: Challenge, sessions: List<FocusSession>): Challenge {
        val count = sessions.count { it.completed && it.distractionCount == 0 }
        return challenge.copy(progress = count.coerceAtMost(challenge.target), completed = count >= challenge.target)
    }
}

class ConsecutiveSessionsEvaluator : ChallengeEvaluator {
    override val type = ChallengeType.ConsecutiveSessions
    override fun evaluate(challenge: Challenge, sessions: List<FocusSession>): Challenge {
        val streak = sessions.sortedByDescending { it.startedAt }
            .takeWhile { it.completed && it.distractionCount == 0 }
            .count()
        return challenge.copy(progress = streak.coerceAtMost(challenge.target), completed = streak >= challenge.target)
    }
}

class ChallengeEngine(
    evaluators: List<ChallengeEvaluator> = listOf(
        TotalMinutesEvaluator(),
        DistractionFreeEvaluator(),
        ConsecutiveSessionsEvaluator()
    )
) {
    private val evaluatorsByType = evaluators.associateBy { it.type }

    fun evaluate(challenges: List<Challenge>, sessions: List<FocusSession>): List<Challenge> =
        challenges.map { challenge -> evaluatorsByType.getValue(challenge.type).evaluate(challenge, sessions) }
}
