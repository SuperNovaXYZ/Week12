package com.example.novacode.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MainMenuScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("NovaCode", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { navController.navigate("level1") }) {
            Text("Level 1 (Easy)")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("level2") }) {
            Text("Level 2 (Hard)")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("level3") }) {
            Text("Level 3 (Expert)")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("parentDashboard") }) {
            Text("Parent Dashboard")
        }
    }
} 