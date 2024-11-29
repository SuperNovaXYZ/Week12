package com.example.novacode.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.novacode.R
import com.example.novacode.model.Direction

@Composable
fun PlayerCharacter(
    direction: Direction,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_player),
            contentDescription = "Player",
            modifier = Modifier
                .size(64.dp)
                .rotate(
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