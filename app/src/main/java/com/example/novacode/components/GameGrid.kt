package com.example.novacode.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.novacode.R
import com.example.novacode.model.*
import com.example.novacode.model.Direction


@Composable
fun GameGrid(
    grid: Array<Array<TileType>>,
    currentPosition: GridPosition,
    currentDirection: Direction,
    isMoving: Boolean = false,
    coinPositions: Set<GridPosition> = emptySet(),
    collectedCoins: Set<GridPosition> = emptySet(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        grid.forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEachIndexed { colIndex, tile ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        // Base layer - always grass
                        Image(
                            painter = painterResource(R.drawable.grass),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        
                        // Path layer - wood for the path
                        if (tile == TileType.PATH || tile == TileType.START || tile == TileType.END) {
                            Image(
                                painter = painterResource(R.drawable.wood),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // Coin layer
                        val position = GridPosition(rowIndex, colIndex)
                        if (coinPositions.contains(position) && !collectedCoins.contains(position)) {
                            Image(
                                painter = painterResource(R.drawable.coin),
                                contentDescription = "Coin",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(4.dp)
                            )
                        }

                        // Player character
                        if (currentPosition.x == rowIndex && currentPosition.y == colIndex) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(y = (-35).dp),
                                contentAlignment = Alignment.Center
                            ) {
                                PlayerCharacter(
                                    direction = currentDirection,
                                    isMoving = isMoving,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
} 