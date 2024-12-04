package com.example.novacode.screens

import android.view.MotionEvent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.layout.*

@Composable
fun MainMenuScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF87CEEB),  // Sky blue
                        Color(0xFF98FB98)   // Light green
                    )
                )
            )
    ) {
        // Floating clouds animation
        FloatingClouds()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp) // Adds consistent spacing
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Title with bouncing animation
            item {
                Text(
                    text = "NovaCode",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = Color.White,
                            offset = Offset(2f, 2f),
                            blurRadius = 3f
                        )
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.bounceEffect()
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Level buttons
            item {
                LevelButton(
                    text = "Level 1: First Steps",
                    description = "Learn the basics!",
                    onClick = { navController.navigate("level1") },
                    icon = Icons.Filled.StarHalf
                )
            }

            item {
                LevelButton(
                    text = "Level 2: Getting Harder",
                    description = "More challenges await!",
                    onClick = { navController.navigate("level2") },
                    icon = Icons.Filled.Star
                )
            }

            item {
                LevelButton(
                    text = "Level 3: Expert Mode",
                    description = "Are you ready?",
                    onClick = { navController.navigate("level3") },
                    icon = Icons.Filled.Stars
                )
            }

            // Spacer to fill remaining space

            // Parent dashboard button
            item {
                Button(
                    onClick = { navController.navigate("parentDashboard") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary, // A more prominent color
                        contentColor = MaterialTheme.colorScheme.onPrimary  // Contrasting text/icon color
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp) // Adjust spacing from the level buttons
                        .alpha(1.0f)          // Ensure full opacity
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Parent Dashboard")
                }
            }
            item {
                Button(
                    onClick = { navController.navigate("welcome") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary, // A more prominent color
                        contentColor = MaterialTheme.colorScheme.onPrimary  // Contrasting text/icon color
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp) // Adjust spacing from the level buttons
                        .alpha(1.0f)          // Ensure full opacity
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Logout")
                }
            }
        }
    }
}

@Composable
private fun LevelButton(
    text: String,
    description: String,
    onClick: () -> Unit,
    icon: ImageVector
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(80.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun FloatingClouds() {
    // Add floating cloud animations in the background
    Box(modifier = Modifier.fillMaxSize()) {
        repeat(3) { index ->
            val infiniteTransition = rememberInfiniteTransition(label = "cloud$index")
            val xOffset by infiniteTransition.animateFloat(
                initialValue = -200f,
                targetValue = 1000f,
                animationSpec = infiniteRepeatable(
                    animation = tween(20000 + index * 5000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "cloudX$index"
            )

            Icon(
                imageVector = Icons.Filled.Cloud,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier
                    .size(100.dp + (index * 20).dp)
                    .offset(x = xOffset.dp, y = (100 + index * 120).dp)
            )
        }
    }
}

// Extension function for bounce animation
private fun Modifier.bounceEffect(): Modifier = composed {
    var isAnimating by remember { mutableStateOf(true) }
    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    LaunchedEffect(Unit) {
        isAnimating = true
    }

    this.scale(scale)
}






