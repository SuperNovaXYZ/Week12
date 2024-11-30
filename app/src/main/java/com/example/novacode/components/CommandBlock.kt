package com.example.novacode.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.novacode.R
import com.example.novacode.model.Command

@Composable
fun CommandBlock(
    command: Command,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                RoundedCornerShape(8.dp)
            )
            .border(
                2.dp,
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = when (command) {
                Command.MOVE_UP -> painterResource(R.drawable.ic_move_up)
                Command.MOVE_RIGHT -> painterResource(R.drawable.ic_move_right)
                Command.MOVE_DOWN -> painterResource(R.drawable.ic_move_down)
                Command.MOVE_LEFT -> painterResource(R.drawable.ic_move_left)
            },
            contentDescription = command.name,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
    }
} 