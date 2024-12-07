package com.example.novacode.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
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
    val animatedX by animateFloatAsState(
        targetValue = currentPosition.x.toFloat(),
        animationSpec = tween(
            durationMillis = 500,
            easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
        ),
        label = "positionX"
    )

    val animatedY by animateFloatAsState(
        targetValue = currentPosition.y.toFloat(),
        animationSpec = tween(
            durationMillis = 500,
            easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
        ),
        label = "positionY"
    )

    val characterKey = remember { mutableStateOf(0) }
    LaunchedEffect(currentPosition) {
        characterKey.value = characterKey.value + 1
    }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
                                Box {
                                    Image(
                                        painter = painterResource(R.drawable.wood),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    // Start marker
                                    if (tile == TileType.START) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(4.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PlayArrow,
                                                contentDescription = "Start",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .background(
                                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                                                        shape = CircleShape
                                                    )
                                                    .padding(4.dp)
                                            )
                                        }
                                    }

                                    // End marker
                                    if (tile == TileType.END) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(4.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Flag,
                                                contentDescription = "End",
                                                tint = MaterialTheme.colorScheme.secondary,
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .background(
                                                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                                                        shape = CircleShape
                                                    )
                                                    .padding(4.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // Coin layer
                            val position = GridPosition(rowIndex, colIndex)
                            if (coinPositions.contains(position) && !collectedCoins.contains(position)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .offset(y = (-35).dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.coin),
                                        contentDescription = "Coin",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .padding(4.dp)
                                    )
                                }
                            }

                            // Player character with key
                            if (rowIndex == currentPosition.x && colIndex == currentPosition.y) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .offset(
                                            x = ((animatedY - currentPosition.y) * 100).dp,
                                            y = ((animatedX - currentPosition.x) * 100 - 35).dp
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    key(characterKey.value) {
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
    }
}