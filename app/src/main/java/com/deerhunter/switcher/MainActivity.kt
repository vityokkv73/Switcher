package com.deerhunter.switcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deerhunter.switcher.ui.settings.SettingsScreen
import com.deerhunter.switcher.ui.switcher.SwitchScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SwitcherApp()
        }
    }
}

@Composable
fun SwitcherApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "switch_screen"
    ) {
        composable(
            "switch_screen",
            enterTransition = { fadeIn(animationSpec = tween(100)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) }
        ) {
            SwitchScreen(
                onSettingsClick = {
                    navController.navigate("settings_screen")
                }
            )
        }
        composable(
            "settings_screen",
            enterTransition = { fadeIn(animationSpec = tween(100)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) }
        ) {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
