package com.thrive.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UserEntity::class, SessionEntity::class, DistractionEntity::class],
    version = 4,
    exportSchema = false
)
abstract class ThriveDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun sessionDao(): SessionDao
    abstract fun distractionDao(): DistractionDao
}
