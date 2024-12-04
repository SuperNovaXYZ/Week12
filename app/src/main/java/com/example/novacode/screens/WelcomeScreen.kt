package com.example.novacode.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.novacode.components.SunAnimation

@Composable
fun WelcomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF64B5F6), // Light blue sky
                        Color(0xFFBBDEFB)  // Lighter blue at bottom
                    )
                )
            )
    ) {
        // Sun animation
        SunAnimation(
            modifier = Modifier.fillMaxSize()
        )

        // Content
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 120.dp),
                horizontalArrangement = Arrangement.spacedBy(120.dp)
            ) {
                Text(
                    "Nova",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFFFFC107),
                    modifier = Modifier.scale(1.2f)
                )
                
                Text(
                    "Code",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFFFFC107),
                    modifier = Modifier.scale(1.2f)
                )
            }

            // Buttons
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate("childLogin") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    modifier = Modifier
                        .width(200.dp)
                        .height(56.dp)
                ) {
                    Text(
                        "I am a Child",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                
                Button(
                    onClick = { navController.navigate("parentLogin") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    ),
                    modifier = Modifier
                        .width(200.dp)
                        .height(56.dp)
                ) {
                    Text(
                        "I am a Parent",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
