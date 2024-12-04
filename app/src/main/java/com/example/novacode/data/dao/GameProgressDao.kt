package com.example.novacode.data.dao

import androidx.room.*
import com.example.novacode.data.entities.GameProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameProgressDao {
    @Query("SELECT * FROM game_progress ORDER BY timestamp DESC")
    fun getAllProgress(): Flow<List<GameProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: GameProgressEntity)

    @Query("DELETE FROM game_progress WHERE userId = :userId")
    suspend fun clearUserProgress(userId: String)

    @Query("SELECT COUNT(*) FROM game_progress WHERE userId = :userId AND completed = 1")
    suspend fun getCompletedLevelsCount(userId: String): Int

    @Query("SELECT * FROM game_progress WHERE userId = :userId AND levelId = :levelId LIMIT 1")
    suspend fun getProgressForLevel(userId: String, levelId: Int): GameProgressEntity?
} 