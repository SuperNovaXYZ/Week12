package com.example.novacode.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.novacode.R
import com.example.novacode.model.Direction

@Composable
fun PlayerCharacter(
    direction: Direction,
    isMoving: Boolean = false,
    modifier: Modifier = Modifier
) {
    // Create rotation animation when moving
    val rotation by if (isMoving) {
        val infiniteTransition = rememberInfiniteTransition(label = "rotate")
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1500,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "rotation"
        )
    } else {
        remember { mutableStateOf(0f) }
    }

    // Add bouncing animation when idle
    val infiniteTransition = rememberInfiniteTransition(label = "bounce")
    val bounce by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -8f,  // Negative value to bounce upward
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = EaseInOutQuad
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Box(
        modifier = modifier.offset(y = if (!isMoving) bounce.dp else 0.dp),  // Apply bounce only when not moving
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_player),
            contentDescription = "Player",
            modifier = Modifier
                .rotate(rotation)  // Apply spinning animation only when moving
                .rotate(  // Always apply direction-based rotation
                    when (direction) {
                        Direction.UP -> 270f
                        Direction.RIGHT -> 0f
                        Direction.DOWN -> 90f
                        Direction.LEFT -> 180f
                    }
                )
        )
    }
}