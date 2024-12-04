package com.example.novacode.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.novacode.data.AppDatabase
import com.example.novacode.data.GameProgress
import com.example.novacode.data.entities.GameProgressEntity
import com.example.novacode.utils.GameLogger
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val gameProgressDao = database.gameProgressDao()
    private val logger = GameLogger(application)

    // Convert Flow<List<GameProgressEntity>> to Flow<List<GameProgress>>
    val progress: StateFlow<List<GameProgress>> = gameProgressDao.getAllProgress()
        .map { entities ->
            entities.map { entity ->
                GameProgress(
                    userId = entity.userId,
                    levelId = entity.levelId,
                    gameId = entity.gameId,
                    completed = entity.completed,
                    score = entity.score,
                    timestamp = entity.timestamp
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    suspend fun shouldResetProgress(userId: String): Boolean {
        val completedLevels = gameProgressDao.getCompletedLevelsCount(userId)
        return completedLevels >= 3
    }

    fun addProgress(newProgress: GameProgress) {
        viewModelScope.launch {
            val entity = GameProgressEntity(
                userId = newProgress.userId,
                levelId = newProgress.levelId,
                gameId = newProgress.gameId,
                completed = newProgress.completed,
                score = newProgress.score,
                timestamp = newProgress.timestamp
            )
            gameProgressDao.insertProgress(entity)
            
            // Log the progress
            logger.logProgress(
                userId = newProgress.userId,
                levelId = newProgress.levelId,
                score = newProgress.score,
                completed = newProgress.completed
            )
        }
    }
    
    suspend fun resetProgress(userId: String) {
        gameProgressDao.clearUserProgress(userId)
        logger.logProgress(userId, 0, 0, false) // Log the reset
    }

    suspend fun getProgressForLevel(userId: String, levelId: Int): GameProgress? {
        return gameProgressDao.getProgressForLevel(userId, levelId)?.let { entity ->
            GameProgress(
                userId = entity.userId,
                levelId = entity.levelId,
                gameId = entity.gameId,
                completed = entity.completed,
                score = entity.score,
                timestamp = entity.timestamp
            )
        }
    }

    fun getProgressLogs(): List<String> {
        return logger.getLogs()
    }
} 