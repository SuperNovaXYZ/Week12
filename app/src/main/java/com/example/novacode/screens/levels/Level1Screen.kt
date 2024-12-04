package com.example.novacode.screens.levels

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.novacode.components.*
import com.example.novacode.data.GameProgress
import com.example.novacode.model.*
import com.example.novacode.model.Direction
import com.example.novacode.screens.calculateSteps
import com.example.novacode.services.MusicService
import com.example.novacode.viewmodels.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Level1Screen(navController: NavController, gameViewModel: GameViewModel) {
    var showResetDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Check if we need to reset on entering the level
    LaunchedEffect(Unit) {
        if (gameViewModel.shouldResetProgress("default_user")) {
            showResetDialog = true
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("New Round Starting") },
            text = { Text("You've completed all levels! Starting a new round.") },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        gameViewModel.resetProgress("default_user")
                        showResetDialog = false
                    }
                }) {
                    Text("Start New Round")
                }
            }
        )
    }

    val context = LocalContext.current

    // Start music when entering Level 1
    LaunchedEffect(Unit) {
        val playIntent = Intent(context, MusicService::class.java).apply {
            action = "PLAY"
        }
        context.startService(playIntent)
    }

    // Handle music when leaving Level 1
    DisposableEffect(Unit) {
        onDispose {
            // Only stop music if not going to Level 2 or MainMenu
            if (navController.currentBackStackEntry?.destination?.route !in listOf("level2", "mainMenu")) {
                val stopIntent = Intent(context, MusicService::class.java).apply {
                    action = "STOP"
                }
                context.startService(stopIntent)
            }
        }
    }

    val level = remember {
        Level(
            grid = Array(12) { row ->
                Array(8) { col ->
                    when {
                        row == 7 && col == 1 -> TileType.START
                        row == 5 && col == 6 -> TileType.END
                        (row == 7 && col in 1..3) ||
                                (row in 5..7 && col == 3) ||
                                (row == 5 && col in 3..6) -> TileType.PATH
                        else -> TileType.GRASS
                    }
                }
            },
            startPosition = GridPosition(7, 1),
            endPosition = GridPosition(5, 6),
            initialDirection = Direction.RIGHT,
            maxCommands = 4,
            coinPositions = setOf(
                GridPosition(7, 2),
                GridPosition(6, 3)
            )
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
                        levelId = 1,
                        gameId = 1,
                        completed = true,
                        score = score
                    )

                    gameViewModel.addProgress(progress)

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
        GameGrid(
            grid = level.grid,
            currentPosition = currentPosition,
            currentDirection = currentDirection,
            isMoving = isMoving,
            coinPositions = level.coinPositions,
            collectedCoins = collectedCoins,
            modifier = Modifier
                .fillMaxWidth() // Ensure it fills the screen width
                .fillMaxHeight() // Ensure it fills the screen height
        )

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
                                    // Remove the command from the slot when dragging starts
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
                        // Reset command slots
                        commandSlots = Array(level.maxCommands) { null }
                        // Reset position to start
                        currentPosition = level.startPosition
                        currentDirection = level.initialDirection
                    },
                    enabled = !isExecuting && commandSlots.any { it != null }
                ) {
                    Text("Reset")
                }

                Button(onClick = {
                    // Stop music only when going back to menu
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
            title = { Text("Level Complete!") },
            text = {
                Column {
                    Text("You've completed the level!")
                    Text("Score: $score")
                    Text("Coins collected: ${collectedCoins.size}/${level.coinPositions.size}")
                }
            },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    navController.navigate("level2")
                }) {
                    Text("Next Level")
                }
            },
            dismissButton = {
                Button(onClick = {
                    // Reset all game state
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
                    // Reset position and commands
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
    }
}