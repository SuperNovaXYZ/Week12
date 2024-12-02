package com.example.novacode.model

data class GridPosition(val x: Int, val y: Int)

enum class TileType {
    GRASS,
    WOOD,
    START,
    END,
    WALL,
    PATH
}

enum class Direction {
    UP, RIGHT, DOWN, LEFT
}

enum class Command {
    MOVE_UP,
    MOVE_RIGHT,
    MOVE_DOWN,
    MOVE_LEFT
}

data class Level(
    val grid: Array<Array<TileType>>,
    val startPosition: GridPosition,
    val endPosition: GridPosition,
    val initialDirection: Direction,
    val maxCommands: Int,
    val availableCommands: List<Command> = listOf(
        Command.MOVE_UP,
        Command.MOVE_RIGHT,
        Command.MOVE_DOWN,
        Command.MOVE_LEFT
    ),
    val coinPositions: Set<GridPosition> = emptySet(),
    val parScore: Int = 100
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Level
        return grid.contentDeepEquals(other.grid) &&
                startPosition == other.startPosition &&
                endPosition == other.endPosition &&
                initialDirection == other.initialDirection &&
                maxCommands == other.maxCommands &&
                availableCommands == other.availableCommands &&
                coinPositions == other.coinPositions &&
                parScore == other.parScore
    }

    override fun hashCode(): Int {
        var result = grid.contentDeepHashCode()
        result = 31 * result + startPosition.hashCode()
        result = 31 * result + endPosition.hashCode()
        result = 31 * result + initialDirection.hashCode()
        result = 31 * result + maxCommands
        result = 31 * result + availableCommands.hashCode()
        result = 31 * result + coinPositions.hashCode()
        result = 31 * result + parScore
        return result
    }

    fun isWalkable(position: GridPosition): Boolean {
        return position.x in grid.indices &&
               position.y in grid[0].indices &&
               grid[position.x][position.y] != TileType.GRASS
    }

    private fun commandToDirection(command: Command): Direction {
        return when (command) {
            Command.MOVE_UP -> Direction.UP
            Command.MOVE_RIGHT -> Direction.RIGHT
            Command.MOVE_DOWN -> Direction.DOWN
            Command.MOVE_LEFT -> Direction.LEFT
        }
    }

    fun getNextPosition(current: GridPosition, direction: Direction): GridPosition {
        return when (direction) {
            Direction.UP -> GridPosition(current.x - 1, current.y)
            Direction.RIGHT -> GridPosition(current.x, current.y + 1)
            Direction.DOWN -> GridPosition(current.x + 1, current.y)
            Direction.LEFT -> GridPosition(current.x, current.y - 1)
        }
    }

    fun moveUntilBlocked(start: GridPosition, command: Command): GridPosition {
        val direction = commandToDirection(command)
        var current = start
        var next = getNextPosition(current, direction)
        
        while (isWalkable(next)) {
            current = next
            next = getNextPosition(current, direction)
        }
        
        return current
    }

    fun getDirectionFromCommand(command: Command): Direction {
        return when (command) {
            Command.MOVE_UP -> Direction.UP
            Command.MOVE_RIGHT -> Direction.RIGHT
            Command.MOVE_DOWN -> Direction.DOWN
            Command.MOVE_LEFT -> Direction.LEFT
        }
    }
}

data class SlotPosition(
    val index: Int,
    val bounds: androidx.compose.ui.geometry.Rect
) 