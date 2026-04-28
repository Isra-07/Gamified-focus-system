package com.thrive.data.repository

import com.thrive.data.local.SessionDao
import com.thrive.data.local.SessionEntity
import com.thrive.domain.FocusSession
import com.thrive.domain.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepositoryImpl @Inject constructor(
    private val dao: SessionDao
) : SessionRepository {

    override suspend fun saveSession(session: FocusSession) =
        dao.save(session.toEntity())

    override fun observeAllSessions(): Flow<List<FocusSession>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getTotalXp(): Int = dao.getTotalXp()

    private fun SessionEntity.toDomain() = FocusSession(
        id = id, startTime = startTime, targetSeconds = targetSeconds,
        actualSeconds = actualSeconds, xpEarned = xpEarned,
        distractionCount = distractionCount, focusRate = focusRate,
        completedAt = completedAt
    )

    private fun FocusSession.toEntity() = SessionEntity(
        id = id, startTime = startTime, targetSeconds = targetSeconds,
        actualSeconds = actualSeconds, xpEarned = xpEarned,
        distractionCount = distractionCount, focusRate = focusRate,
        completedAt = completedAt
    )
}
