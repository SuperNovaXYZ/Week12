package com.example.novacode.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_progress")
data class GameProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val levelId: Int,
    val gameId: Int,
    val completed: Boolean,
    val score: Int,
    val timestamp: Long = System.currentTimeMillis()
) 