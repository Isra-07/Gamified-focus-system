package com.thrive.data.repository

import com.thrive.data.local.DistractionDao
import com.thrive.data.local.DistractionEntity
import com.thrive.domain.DistractionEvent
import com.thrive.domain.DistractionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DistractionRepositoryImpl @Inject constructor(
    private val dao: DistractionDao
) : DistractionRepository {

    override suspend fun saveDistraction(event: DistractionEvent) =
        dao.save(event.toEntity())

    override suspend fun getDistractionsForSession(sessionId: String): List<DistractionEvent> =
        dao.getForSession(sessionId).map { it.toDomain() }

    private fun DistractionEntity.toDomain() = DistractionEvent(
        id = id, sessionId = sessionId, packageName = packageName,
        appName = appName, timestamp = timestamp
    )

    private fun DistractionEvent.toEntity() = DistractionEntity(
        id = id, sessionId = sessionId, packageName = packageName,
        appName = appName, timestamp = timestamp
    )
}
