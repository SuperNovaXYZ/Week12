package com.example.novacode.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.novacode.viewmodels.UserViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun ParentLoginScreen(navController: NavController) {
    val viewModel: UserViewModel = viewModel()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.loginState) {
        viewModel.loginState.collect { state ->
            when (state) {
                is UserViewModel.LoginState.Success -> {
                    if (state.user.isParent) {
                        navController.navigate("parentDashboard") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    } else {
                        showError = true
                        errorMessage = "This account is not a parent account"
                    }
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
        Text("Parent Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Parent Username") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) 
                VisualTransformation.None 
            else 
                PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) 
                            Icons.Default.Visibility 
                        else 
                            Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) 
                            "Hide password" 
                        else 
                            "Show password"
                    )
                }
            }
        )
        
        if (showError) {
            Text(
                errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.login(username, password, true) },
            enabled = username.isNotBlank() && password.isNotBlank()
        ) {
            Text("Login")
        }
        TextButton(onClick = { navController.navigate("parentRegister") }) {
            Text("Create Parent Account")
        }
        TextButton(onClick = { navController.navigate("welcome") }) {
            Text("Back")
        }
    }
}

@Composable
fun ChildLoginScreen(navController: NavController) {
    val viewModel: UserViewModel = viewModel()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.loginState) {
        viewModel.loginState.collect { state ->
            when (state) {
                is UserViewModel.LoginState.Success -> {
                    if (!state.user.isParent) {
                        navController.navigate("mainMenu")
                    } else {
                        showError = true
                        errorMessage = "This account is not a child account"
                    }
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
        Text("Child Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Child Username") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        if (showError) {
            Text(
                errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Button(onClick = { viewModel.login(username, password, false) }) {
            Text("Login")
        }
        TextButton(onClick = { navController.navigate("childRegister") }) {
            Text("Create Child Account")
        }
        TextButton(onClick = { navController.navigate("welcome") }) {
            Text("Back")
        }
    }
} 