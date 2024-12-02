package com.example.novacode.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.novacode.data.GameProgress

@Composable
fun ProgressChart(
    progressList: List<GameProgress>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.padding(16.dp),
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Progress Overview",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Blue
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
            ) {
                val width = size.width
                val height = size.height
                val maxScore = progressList.maxOfOrNull { it.score } ?: 100
                
                // Draw grid lines
                for (i in 0..4) {
                    val y = height * (1 - i / 4f)
                    drawLine(
                        color = Color.Gray.copy(alpha = 0.3f),
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1f
                    )
                }
                
                // Draw progress line
                if (progressList.isNotEmpty()) {
                    val path = Path()
                    val xStep = width / (progressList.size - 1).coerceAtLeast(1)
                    val points = progressList.map { it.score.toFloat() / maxScore }
                    
                    if (points.size == 1) {
                        // If only one point, draw a dot
                        val x = width / 2
                        val y = height * (1 - points[0])
                        drawCircle(
                            color = Color.Blue,
                            radius = 4.dp.toPx(),
                            center = Offset(x, y)
                        )
                    } else {
                        // Draw connected line for multiple points
                        path.moveTo(0f, height * (1 - points.first()))
                        points.forEachIndexed { index, point ->
                            path.lineTo(index * xStep, height * (1 - point))
                        }
                        
                        drawPath(
                            path = path,
                            color = Color.Blue,
                            style = Stroke(width = 3f)
                        )
                    }
                }
            }
            
            // Level labels
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                progressList.forEach { progress ->
                    Text(
                        "Level ${progress.levelId}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
} 