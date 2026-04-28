package com.thrive.data.di

import android.content.Context
import androidx.room.Room
import com.thrive.data.local.ThriveDatabase
import com.thrive.data.repository.AnalyticsRepositoryImpl
import com.thrive.data.repository.ChallengeRepositoryImpl
import com.thrive.data.repository.DistractionRepositoryImpl
import com.thrive.data.repository.SessionRepositoryImpl
import com.thrive.data.repository.UserRepositoryImpl
import com.thrive.domain.DistractionRepository
import com.thrive.domain.SessionRepository
import com.thrive.domain.UserRepository
import com.thrive.domain.repository.AnalyticsRepository
import com.thrive.domain.repository.ChallengeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): ThriveDatabase =
        Room.databaseBuilder(ctx, ThriveDatabase::class.java, "thrive_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideUserDao(db: ThriveDatabase) = db.userDao()

    @Provides
    @Singleton
    fun provideSessionDao(db: ThriveDatabase) = db.sessionDao()

    @Provides
    @Singleton
    fun provideDistractionDao(db: ThriveDatabase) = db.distractionDao()

    @Provides
    @Singleton
    fun provideUserRepository(impl: UserRepositoryImpl): UserRepository = impl

    @Provides
    @Singleton
    fun provideSessionRepository(impl: SessionRepositoryImpl): SessionRepository = impl

    @Provides
    @Singleton
    fun provideDistractionRepository(impl: DistractionRepositoryImpl): DistractionRepository = impl

    @Provides
    @Singleton
    fun provideChallengeRepository(impl: ChallengeRepositoryImpl): ChallengeRepository = impl

    @Provides
    @Singleton
    fun provideAnalyticsRepository(impl: AnalyticsRepositoryImpl): AnalyticsRepository = impl
}
