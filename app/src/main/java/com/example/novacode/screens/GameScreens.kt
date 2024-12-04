package com.example.novacode.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.novacode.components.*
import com.example.novacode.model.*
import com.example.novacode.model.Direction
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import com.example.novacode.data.GameProgress
import com.example.novacode.services.MusicService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.novacode.viewmodels.GameViewModel
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.LaunchedEffect


 fun calculateSteps(start: GridPosition, end: GridPosition): List<GridPosition> {
    val steps = mutableListOf<GridPosition>()
    var current = start

    while (current != end) {
        val dx = end.x - current.x
        val dy = end.y - current.y

        current = when {
            dx > 0 -> GridPosition(current.x + 1, current.y)
            dx < 0 -> GridPosition(current.x - 1, current.y)
            dy > 0 -> GridPosition(current.x, current.y + 1)
            else -> GridPosition(current.x, current.y - 1)
        }

        steps.add(current)
    }

    return steps
}

