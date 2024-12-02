package com.example.novacode.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import com.example.novacode.model.Command

@Composable
fun DraggableCommandBlock(
    command: Command,
    onDragEnd: (Command, Offset) -> Unit,
    onDragStart: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var startPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .onGloballyPositioned { coordinates ->
                if (startPosition == Offset.Zero) {
                    startPosition = coordinates.positionInWindow()
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { onDragStart() },
                    onDragEnd = {
                        val finalPosition = Offset(
                            startPosition.x + offsetX,
                            startPosition.y + offsetY
                        )
                        onDragEnd(command, finalPosition)
                        offsetX = 0f
                        offsetY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                )
            }
    ) {
        CommandBlock(
            command = command,
            modifier = Modifier.fillMaxSize()
        )
    }
} 