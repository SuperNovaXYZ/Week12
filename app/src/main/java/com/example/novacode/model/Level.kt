package com.example.novacode.model

data class GridPosition(val x: Int, val y: Int)

enum class TileType {
    GRASS,
    WOOD,
    START,
    END,
    WALL,
    PATH,
    COIN
}

enum class Direction {
    UP, RIGHT, DOWN, LEFT
}

enum class Command {
    MOVE_FORWARD,
    TURN_LEFT,
    TURN_RIGHT
}

data class Level(
    val grid: Array<Array<TileType>>,
    val startPosition: GridPosition,
    val endPosition: GridPosition,
    val initialDirection: Direction,
    val availableCommands: List<Command>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Level
        return grid.contentDeepEquals(other.grid) &&
                startPosition == other.startPosition &&
                endPosition == other.endPosition &&
                initialDirection == other.initialDirection &&
                availableCommands == other.availableCommands
    }

    override fun hashCode(): Int {
        var result = grid.contentDeepHashCode()
        result = 31 * result + startPosition.hashCode()
        result = 31 * result + endPosition.hashCode()
        result = 31 * result + initialDirection.hashCode()
        result = 31 * result + availableCommands.hashCode()
        return result
    }
}

data class SlotPosition(
    val index: Int,
    val bounds: androidx.compose.ui.geometry.Rect
) 