package com.example.novacode.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.novacode.components.CommandBlock
import com.example.novacode.components.CommandSlot
import com.example.novacode.components.DraggableCommandBlock
import com.example.novacode.components.GameGrid
import com.example.novacode.model.*
import com.example.novacode.model.Direction
import java.lang.Math
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

// Add these functions at the top level
private fun getNextPosition(currentPos: GridPosition, command: Command): GridPosition {
    return when (command) {
        Command.MOVE_UP -> GridPosition(currentPos.x - 1, currentPos.y)
        Command.MOVE_RIGHT -> GridPosition(currentPos.x, currentPos.y + 1)
        Command.MOVE_DOWN -> GridPosition(currentPos.x + 1, currentPos.y)
        Command.MOVE_LEFT -> GridPosition(currentPos.x, currentPos.y - 1)
    }
}

private fun getNextDirection(currentDir: Direction, command: Command): Direction {
    return when (command) {
        Command.MOVE_UP -> currentDir
        Command.MOVE_RIGHT -> when (currentDir) {
            Direction.UP -> Direction.RIGHT
            Direction.RIGHT -> Direction.DOWN
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
        }
        Command.MOVE_DOWN -> when (currentDir) {
            Direction.UP -> Direction.DOWN
            Direction.RIGHT -> Direction.UP
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.RIGHT
        }
        Command.MOVE_LEFT -> when (currentDir) {
            Direction.UP -> Direction.LEFT
            Direction.RIGHT -> Direction.UP
            Direction.DOWN -> Direction.RIGHT
            Direction.LEFT -> Direction.DOWN
        }
    }
}

@Composable
fun Level1Screen(navController: NavController) {
    val level = remember {
        Level(
            grid = Array(12) { row ->
                Array(8) { col ->
                    when {
                        row == 7 && col == 1 -> TileType.START
                        row == 5 && col == 6 -> TileType.END
                        (row == 7 && col in 1..3) ||
                        (row in 5..7 && col == 3) ||
                        (row == 5 && col in 3..5) ||
                        (row == 5 && col in 5..6) -> TileType.PATH
                        else -> TileType.GRASS
                    }
                }
            },
            startPosition = GridPosition(7, 1),
            endPosition = GridPosition(5, 6),
            initialDirection = Direction.RIGHT,
            availableCommands = listOf(
                Command.MOVE_UP,
                Command.MOVE_RIGHT,
                Command.MOVE_DOWN,
                Command.MOVE_LEFT
            )
        )
    }

    var currentPosition by remember { mutableStateOf(level.startPosition) }
    var currentDirection by remember { mutableStateOf(level.initialDirection) }
    var commandSlots by remember { mutableStateOf(Array<Command?>(4) { null }) }
    var slotPositions by remember { mutableStateOf(mapOf<Int, SlotPosition>()) }
    var isExecuting by remember { mutableStateOf(false) }
    var isMoving by remember { mutableStateOf(false) }
    
    // Add coroutine scope
    val scope = rememberCoroutineScope()

    // Add function to execute commands
    fun executeCommands() {
        if (isExecuting) return
        isExecuting = true

        val commands = commandSlots.filterNotNull()
        currentPosition = level.startPosition

        scope.launch {
            commands.forEach { command ->
                isMoving = true
                delay(500) // Wait half a second between commands
                
                val nextPos = getNextPosition(currentPosition, command)
                if (nextPos.x in level.grid.indices && 
                    nextPos.y in level.grid[0].indices &&
                    level.grid[nextPos.x][nextPos.y] != TileType.GRASS) {
                    currentPosition = nextPos
                }
                
                isMoving = false
            }
            isExecuting = false

            if (currentPosition == level.endPosition) {
                // TODO: Show success message
            }
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
            modifier = Modifier.fillMaxSize()
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
                                    println("Drop position: $offset")
                                    
                                    val targetSlot = slotPositions.entries.minByOrNull { (_, slot) ->
                                        val slotCenterX = (slot.bounds.left + slot.bounds.right) / 2
                                        val slotCenterY = (slot.bounds.top + slot.bounds.bottom) / 2
                                        val dx = offset.x - slotCenterX
                                        val dy = offset.y - slotCenterY
                                        dx * dx + dy * dy
                                    }
                                    
                                    println("Closest slot: ${targetSlot?.key}, distance: ${
                                        targetSlot?.let { (_, slot) ->
                                            val dx = offset.x - (slot.bounds.left + slot.bounds.right) / 2
                                            val dy = offset.y - (slot.bounds.top + slot.bounds.bottom) / 2
                                            Math.sqrt((dx * dx + dy * dy).toDouble())
                                        }
                                    }")
                                    
                                    if (targetSlot != null) {
                                        val (index, slot) = targetSlot
                                        val dx = offset.x - (slot.bounds.left + slot.bounds.right) / 2
                                        val dy = offset.y - (slot.bounds.top + slot.bounds.bottom) / 2
                                        val distance = Math.sqrt((dx * dx + dy * dy).toDouble())
                                        
                                        if (distance < 100) {
                                            val newSlots = commandSlots.clone()
                                            newSlots[index] = draggedCommand
                                            commandSlots = newSlots
                                            println("Command ${draggedCommand.name} placed in slot $index")
                                        } else {
                                            println("Drop too far from slot center")
                                        }
                                    } else {
                                        println("No slot found for position $offset")
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
                        commandSlots = Array(4) { null }
                        // Reset position to start
                        currentPosition = level.startPosition
                        currentDirection = level.initialDirection
                    },
                    enabled = !isExecuting && commandSlots.any { it != null }
                ) {
                    Text("Reset")
                }
                
                Button(onClick = { navController.navigate("mainMenu") }) {
                    Text("Back")
                }
            }
        }
    }
}

@Composable
fun Level2Screen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Level 2", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("mainMenu") }) {
            Text("Back to Menu")
        }
    }
}

@Composable
fun ParentDashboardScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Parent Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("mainMenu") }) {
            Text("Back to Menu")
        }
    }
} 