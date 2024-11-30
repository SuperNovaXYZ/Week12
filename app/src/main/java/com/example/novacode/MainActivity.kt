package com.example.novacode

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.novacode.ui.theme.NovaCodeTheme
import com.example.novacode.screens.*
import com.example.novacode.services.MusicService

class MainActivity : ComponentActivity() {
    private var musicServiceIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Create and start music service
        musicServiceIntent = Intent(this, MusicService::class.java)
        startService(musicServiceIntent)
        
        setContent {
            NovaCodeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NovaCodeApp()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop music service when app is destroyed
        musicServiceIntent?.let { stopService(it) }
    }
}

@Composable
fun NovaCodeApp() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("parentLogin") { ParentLoginScreen(navController) }
        composable("childLogin") { ChildLoginScreen(navController) }
        composable("parentRegister") { ParentRegisterScreen(navController) }
        composable("childRegister") { ChildRegisterScreen(navController) }
        composable("mainMenu") { MainMenuScreen(navController) }
        composable("level1") { Level1Screen(navController) }
        composable("level2") { Level2Screen(navController) }
        composable("parentDashboard") { ParentDashboardScreen(navController) }
    }
}