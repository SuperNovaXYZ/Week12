package com.example.novacode.screens

import androidx.compose.foundation.layout.*
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
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Parent Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Debug info
        Text(
            "Number of progress entries: ${progressList.size}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
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
        
        Button(
            onClick = { navController.navigate("mainMenu") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Back to Menu")
        }
    }
} 