package com.example.novacode.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.novacode.data.GameProgress
import com.example.novacode.components.ProgressChart
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.novacode.viewmodels.GameViewModel

@Composable
fun ParentDashboardScreen(navController: NavController, gameViewModel: GameViewModel) {
    val progressList by gameViewModel.progress.collectAsState()
    var showLogDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Parent Dashboard",
                style = MaterialTheme.typography.headlineMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { showLogDialog = true }) {
                    Text("View Logs")
                }
                
                Button(onClick = { navController.navigate("mainMenu") }) {
                    Text("Back to Menu")
                }
            }
        }

        // Debug info
        progressList.forEach { progress ->
            Text(
                "Level ${progress.levelId}: Score ${progress.score}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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