package com.example.novacode.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.novacode.viewmodels.UserViewModel

@Composable
fun ParentRegisterScreen(navController: NavController) {
    val viewModel: UserViewModel = viewModel()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.loginState) {
        viewModel.loginState.collect { state ->
            when (state) {
                is UserViewModel.LoginState.Success -> {
                    navController.navigate("parentLogin")
                }
                is UserViewModel.LoginState.Error -> {
                    showError = true
                    errorMessage = state.message
                }
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Create Parent Account", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Parent Username") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        if (showError) {
            Text(
                errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Button(onClick = { 
            viewModel.register(username, password, true, email)
        }) {
            Text("Register")
        }
        TextButton(onClick = { navController.navigate("parentLogin") }) {
            Text("Back to Login")
        }
    }
}

@Composable
fun ChildRegisterScreen(navController: NavController) {
    val viewModel: UserViewModel = viewModel()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var parentEmail by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.loginState) {
        viewModel.loginState.collect { state ->
            when (state) {
                is UserViewModel.LoginState.Success -> {
                    navController.navigate("childLogin")
                }
                is UserViewModel.LoginState.Error -> {
                    showError = true
                    errorMessage = state.message
                }
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Create Child Account", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Child Username") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = parentEmail,
            onValueChange = { parentEmail = it },
            label = { Text("Parent's Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        if (showError) {
            Text(
                errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Button(onClick = { 
            viewModel.register(username, password, false, null, parentEmail)
        }) {
            Text("Register")
        }
        TextButton(onClick = { navController.navigate("childLogin") }) {
            Text("Back to Login")
        }
    }
} 