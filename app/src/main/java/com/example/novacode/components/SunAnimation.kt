package com.example.novacode.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SunAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val rayScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width * 0.5f, size.height * 0.3f)
        val radius = size.minDimension * 0.15f
        
        // Draw sun rays
        rotate(rotation, center) {
            for (i in 0..11) {
                val angle = (i * 30f) * (Math.PI / 180f)
                val startX = center.x + cos(angle).toFloat() * (radius * 1.2f)
                val startY = center.y + sin(angle).toFloat() * (radius * 1.2f)
                val endX = center.x + cos(angle).toFloat() * (radius * 2f * rayScale)
                val endY = center.y + sin(angle).toFloat() * (radius * 2f * rayScale)
                
                drawLine(
                    color = Color(0xFFFFC107),
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = 8f
                )
            }
        }
        
        // Draw sun body
        scale(scale, center) {
            drawCircle(
                color = Color(0xFFFFEB3B),
                radius = radius,
                center = center
            )
            
            // Draw sun face
            val smileRadius = radius * 0.6f
            val smilePath = Path().apply {
                moveTo(center.x - smileRadius * 0.5f, center.y)
                quadraticBezierTo(
                    center.x, center.y + smileRadius * 0.5f,
                    center.x + smileRadius * 0.5f, center.y
                )
            }
            
            // Eyes
            drawCircle(
                color = Color(0xFF795548),
                radius = radius * 0.1f,
                center = Offset(center.x - radius * 0.3f, center.y - radius * 0.2f)
            )
            drawCircle(
                color = Color(0xFF795548),
                radius = radius * 0.1f,
                center = Offset(center.x + radius * 0.3f, center.y - radius * 0.2f)
            )
            
            // Smile
            drawPath(
                path = smilePath,
                color = Color(0xFF795548),
                style = Stroke(
                    width = 8f,
                    cap = StrokeCap.Round
                )
            )
        }
    }
} 