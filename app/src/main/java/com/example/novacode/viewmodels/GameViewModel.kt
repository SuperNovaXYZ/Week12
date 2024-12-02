package com.example.novacode.viewmodels

import androidx.lifecycle.ViewModel
import com.example.novacode.data.GameProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _progress = MutableStateFlow<List<GameProgress>>(emptyList())
    val progress: StateFlow<List<GameProgress>> = _progress

    fun addProgress(newProgress: GameProgress) {
        _progress.update { currentProgress ->
            val updatedProgress = currentProgress + newProgress
            updatedProgress.sortedBy { it.levelId }
        }
    }
} 