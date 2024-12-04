package com.example.novacode.utils

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class GameLogger(private val context: Context) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    
    fun logProgress(userId: String, levelId: Int, score: Int, completed: Boolean) {
        val timestamp = dateFormat.format(Date())
        val logEntry = "$timestamp - User: $userId, Level: $levelId, Score: $score, Completed: $completed\n"
        
        try {
            val file = File(context.getExternalFilesDir(null), "game_progress.log")
            file.appendText(logEntry)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getLogs(): List<String> {
        return try {
            val file = File(context.getExternalFilesDir(null), "game_progress.log")
            if (file.exists()) {
                file.readLines()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun clearLogs() {
        try {
            val file = File(context.getExternalFilesDir(null), "game_progress.log")
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
} 