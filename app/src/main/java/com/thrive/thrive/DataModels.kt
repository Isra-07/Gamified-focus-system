package com.thrive.thrive

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val totalSessions: Int = 0,
    val currentLevel: Int = 1,
    val totalPoints: Int = 0,
    val unlockedFurniture: List<String> = emptyList()
)

data class Challenge(
    val id: Int,
    val title: String,
    val targetSessions: Int,
    val currentProgress: Int = 0,
    val reward: String = "",
    val icon: String = "",
    val description: String = "",
    val rewardType: String = "",
    val rewardId: String = ""
)

data class Furniture(
    val id: String,
    val name: String,
    val levelRequired: Int,
    val icon: String,
    val positionX: Float = 0f,
    val positionY: Float = 0f
)