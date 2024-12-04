package com.example.novacode.screens.levels

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.novacode.components.CommandSlot
import com.example.novacode.components.DraggableCommandBlock
import com.example.novacode.components.GameGrid
import com.example.novacode.data.GameProgress
import com.example.novacode.model.Command
import com.example.novacode.model.Direction
import com.example.novacode.model.GridPosition
import com.example.novacode.model.Level
import com.example.novacode.model.SlotPosition
import com.example.novacode.model.TileType
import com.example.novacode.screens.calculateSteps
import com.example.novacode.services.MusicService
import com.example.novacode.viewmodels.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch




@Composable
fun Level3Screen(navController: NavController, gameViewModel: GameViewModel) {
    val context = LocalContext.current

    // Start music when entering Level 3
    LaunchedEffect(Unit) {
        val playIntent = Intent(context, MusicService::class.java).apply {
            action = "PLAY"
        }
        context.startService(playIntent)
    }

    // Handle music when leaving Level 3
    DisposableEffect(Unit) {
        onDispose {
            // Always stop music when leaving Level 3
            val stopIntent = Intent(context, MusicService::class.java).apply {
                action = "STOP"
            }
            context.startService(stopIntent)
        }
    }

    val level = remember {
        Level(
            grid = Array(12) { row ->
                Array(8) { col ->
                    when {
                        // Start position
                        row == 9 && col == 1 -> TileType.START
                        // End position
                        row == 2 && col == 6 -> TileType.END
                        (row == 9 && col in 1..4) ||  // Bottom horizontal
                                (row in 5..9 && col == 4) ||  // Right vertical up
                                (row == 5 && col in 4..6) ||  // Upper horizontal right
                                (row in 2..5 && col == 6) ||  // Final vertical to end
                                (row == 7 && col in 2..4) ||  // Middle platform
                                (row in 4..7 && col == 2)     // Left vertical connection
                        -> TileType.PATH
                        // Walls/Obstacles
                        (row == 6 && col == 3) ||     // Block middle path
                                (row == 4 && col == 5)        // Block upper path
                        -> TileType.WALL
                        else -> TileType.GRASS
                    }
                }
            },
            startPosition = GridPosition(9, 1),
            endPosition = GridPosition(2, 6),
            initialDirection = Direction.RIGHT,
            maxCommands = 8,
            coinPositions = setOf(
                GridPosition(9, 3),
                GridPosition(7, 4),
                GridPosition(5, 5),
                GridPosition(3, 6)
            ),
            parScore = 200
        )
    }

    var currentPosition by remember { mutableStateOf(level.startPosition) }
    var currentDirection by remember { mutableStateOf(level.initialDirection) }
    var commandSlots by remember { mutableStateOf(Array<Command?>(level.maxCommands) { null }) }
    var slotPositions by remember { mutableStateOf(mapOf<Int, SlotPosition>()) }
    var isExecuting by remember { mutableStateOf(false) }
    var isMoving by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showFailureDialog by remember { mutableStateOf(false) }
    var failureReason by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }
    var collectedCoins by remember { mutableStateOf(setOf<GridPosition>()) }

    val scope = rememberCoroutineScope()

    fun executeCommands() {
        if (isExecuting) return
        isExecuting = true

        val commands = commandSlots.filterNotNull()
        val moveCount = commands.size

        scope.launch {
            var currentPos = currentPosition

            for (command in commands) {
                isMoving = true
                currentDirection = level.getDirectionFromCommand(command)

                val finalPos = level.moveUntilBlocked(currentPos, command)
                val steps = calculateSteps(currentPos, finalPos)

                for (step in steps) {
                    currentPosition = step
                    delay(300)

                    if (level.coinPositions.contains(currentPosition) &&
                        !collectedCoins.contains(currentPosition)) {
                        collectedCoins = collectedCoins + currentPosition
                        score += 10
                    }
                }

                currentPos = finalPos

                if (currentPos == level.endPosition) {
                    val finalScore = score + level.parScore - (moveCount * 5)
                    score = maxOf(0, finalScore)

                    val progress = GameProgress(
                        userId = "default_user",
                        levelId = 3,
                        gameId = 1,
                        completed = true,
                        score = score
                    )

                    scope.launch {
                        gameViewModel.addProgress(progress)
                    }

                    isMoving = false
                    isExecuting = false
                    showSuccessDialog = true
                    return@launch
                }

                isMoving = false
                delay(500)
            }

            failureReason = "Didn't reach the goal! Try a different sequence."
            showFailureDialog = true
            isExecuting = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(90.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                GameGrid(
                    grid = level.grid,
                    currentPosition = currentPosition,
                    currentDirection = currentDirection,
                    isMoving = isMoving,
                    coinPositions = level.coinPositions,
                    collectedCoins = collectedCoins,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                tonalElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(60.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Empty command slots (left side)
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(4) { index ->
                            CommandSlot(
                                index = index,
                                command = commandSlots[index],
                                onSlotPositioned = { slotPosition ->
                                    slotPositions = slotPositions + (index to slotPosition)
                                },
                                onDragStart = { command ->
                                    val newSlots = commandSlots.clone()
                                    newSlots[index] = null
                                    commandSlots = newSlots
                                },
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(50.dp)
                            )
                        }
                    }

                    // Available commands (right side)
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        level.availableCommands.forEach { command ->
                            DraggableCommandBlock(
                                command = command,
                                onDragEnd = { draggedCommand, offset ->
                                    val targetSlot = slotPositions.entries.minByOrNull { (_, slot) ->
                                        val slotCenterX = (slot.bounds.left + slot.bounds.right) / 2
                                        val slotCenterY = (slot.bounds.top + slot.bounds.bottom) / 2
                                        val dx = offset.x - slotCenterX
                                        val dy = offset.y - slotCenterY
                                        dx * dx + dy * dy
                                    }

                                    if (targetSlot != null) {
                                        val (index, slot) = targetSlot
                                        val slotCenterX = (slot.bounds.left + slot.bounds.right) / 2
                                        val slotCenterY = (slot.bounds.top + slot.bounds.bottom) / 2
                                        val dx = offset.x - slotCenterX
                                        val dy = offset.y - slotCenterY
                                        val distance = Math.sqrt((dx * dx + dy * dy).toDouble())

                                        if (distance < 200) {
                                            val newSlots = commandSlots.clone()
                                            newSlots[index] = draggedCommand
                                            commandSlots = newSlots
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(50.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { executeCommands() },
                    enabled = !isExecuting && commandSlots.any { it != null }
                ) {
                    Text(if (isExecuting) "Running..." else "Run")
                }

                Button(
                    onClick = {
                        commandSlots = Array(level.maxCommands) { null }
                        currentPosition = level.startPosition
                        currentDirection = level.initialDirection
                    },
                    enabled = !isExecuting && commandSlots.any { it != null }
                ) {
                    Text("Reset")
                }

                Button(onClick = {
                    val stopIntent = Intent(context, MusicService::class.java).apply {
                        action = "STOP"
                    }
                    context.startService(stopIntent)
                    navController.navigate("mainMenu")
                }) {
                    Text("Back")
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Congratulations!") },
            text = {
                Column {
                    Text("You've mastered the final level!")
                    Text("Score: $score")
                    Text("Coins collected: ${collectedCoins.size}/${level.coinPositions.size}")
                    if (score > level.parScore) {
                        Text("Perfect! You beat the par score!",
                            color = MaterialTheme.colorScheme.primary)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    navController.navigate("mainMenu")
                }) {
                    Text("Back to Menu")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    currentPosition = level.startPosition
                    currentDirection = level.initialDirection
                    commandSlots = Array(level.maxCommands) { null }
                    slotPositions = mapOf()
                    collectedCoins = emptySet()
                    score = 0
                    isExecuting = false
                    isMoving = false
                }) {
                    Text("Try Again")
                }
            }
        )
    }

    if (showFailureDialog) {
        AlertDialog(
            onDismissRequest = { showFailureDialog = false },
            title = { Text("Try Again") },
            text = { Text(failureReason) },
            confirmButton = {
                Button(onClick = {
                    showFailureDialog = false
                    currentPosition = level.startPosition
                    currentDirection = level.initialDirection
                    commandSlots = Array(level.maxCommands) { null }
                    slotPositions = mapOf()
                    isExecuting = false
                    isMoving = false
                }) {
                    Text("OK")
                }
            }
        )
    }}
