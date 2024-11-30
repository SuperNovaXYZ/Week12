package com.example.novacode.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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