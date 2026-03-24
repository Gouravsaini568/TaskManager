package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taskmanager.ui.TaskViewModel
import com.example.taskmanager.ui.screens.*
import com.example.taskmanager.ui.theme.TaskManagerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TaskManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val taskViewModel: TaskViewModel = viewModel()

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { 
                            LoginScreen(navController, taskViewModel) 
                        }
                        composable("signup") { 
                            SignUpScreen(navController) 
                        }
                        composable("home") { 
                            TaskScreen(navController, taskViewModel) 
                        }
                        composable("profile") {
                            ProfileScreen(navController, taskViewModel)
                        }
                        composable("add") { 
                            AddTaskScreen(navController, taskViewModel) 
                        }
                        composable(
                            route = "edit/{taskId}",
                            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0
                            EditTaskScreen(navController, taskId, taskViewModel)
                        }
                    }
                }
            }
        }
    }
}
