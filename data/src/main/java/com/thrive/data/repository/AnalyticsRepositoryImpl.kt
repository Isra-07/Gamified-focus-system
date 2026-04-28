package com.thrive.data.repository

import com.thrive.domain.model.MoodEntry
import com.thrive.domain.model.SummaryStats
import com.thrive.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsRepositoryImpl @Inject constructor() : AnalyticsRepository {
    override fun observeSummary(): Flow<SummaryStats> = flowOf(
        SummaryStats(
            focusMinutes = 120,
            completedSessions = 5,
            distractions = 12,
            averageMinutes = 24,
            focusRate = 85
        )
    )

    override suspend fun saveMood(entry: MoodEntry) {
        // No-op for now
    }
}
