package com.example.novacode.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.novacode.data.GameProgress
import com.example.novacode.components.ProgressChart
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.novacode.viewmodels.GameViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ParentDashboardScreen(navController: NavController, gameViewModel: GameViewModel) {
    val progressList by gameViewModel.progress.collectAsState()
    var showLogDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header with responsive layout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Parent\nDashboard",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 24.sp
                ),
                modifier = Modifier.weight(1f),
                maxLines = 2
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Button(
                    onClick = { showLogDialog = true },
                    modifier = Modifier.height(40.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(
                        "View Logs",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Button(
                    onClick = { navController.navigate("mainMenu") },
                    modifier = Modifier.height(40.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(
                        "Back to Menu",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Progress info
        progressList.forEach { progress ->
            Text(
                "Level ${progress.levelId}: Score ${progress.score}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progress Chart
        ProgressChart(
            progressList = progressList,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        // Coin Collection Summary
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Level Progress",
                    style = MaterialTheme.typography.titleMedium
                )

                progressList.forEach { progress ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Level ${progress.levelId}")
                        Text("Score: ${progress.score}")
                    }
                }
            }
        }

        // Add logout button at the bottom
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(
            onClick = { 
                navController.navigate("welcome") {
                    popUpTo("welcome") { inclusive = true }
                }
            },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF2196F3)
            ),
            border = BorderStroke(1.dp, Color(0xFF2196F3)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                "Logout",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        // Bottom padding
        Spacer(modifier = Modifier.height(16.dp))
    }

    // Log Dialog
    if (showLogDialog) {
        AlertDialog(
            onDismissRequest = { showLogDialog = false },
            title = { Text("Game Progress Logs") },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    items(gameViewModel.getProgressLogs()) { logEntry ->
                        Text(
                            text = logEntry,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showLogDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
} 