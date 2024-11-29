package com.example.novacode.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.novacode.model.Command

@Composable
fun CommandQueue(
    commands: List<Command>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(commands) { command ->
            CommandBlock(
                command = command,
                modifier = Modifier.size(40.dp)
            )
        }
    }
} 