package com.example.novacode.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.example.novacode.model.Command
import com.example.novacode.model.SlotPosition

@Composable
fun CommandSlot(
    index: Int,
    command: Command?,
    onSlotPositioned: (SlotPosition) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .onGloballyPositioned { coordinates ->
                val bounds = coordinates.boundsInWindow()
                println("Slot $index bounds: $bounds")
                onSlotPositioned(
                    SlotPosition(
                        index = index,
                        bounds = bounds
                    )
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (command != null) {
            CommandBlock(
                command = command,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
} 