package com.example.taskmanager.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.taskmanager.data.TaskDatabase
import com.example.taskmanager.ui.TaskViewModel
import com.example.taskmanager.ui.theme.MainBlue
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, viewModel: TaskViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userDao = TaskDatabase.getDB(context).userDao()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Upper section with a colored background and title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .background(
                    color = MainBlue,
                    shape = RoundedCornerShape(bottomStart = 80.dp)
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            @Suppress("DEPRECATION")
            Text(
                text = "Manage your\ndaily tasks",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 40.sp,
                modifier = Modifier.padding(start = 32.dp)
            )
        }

        // Lower section with the login form
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Login",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text("Email*", color = Color.Gray, fontSize = 14.sp)
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Enter your email", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF1F2F6),
                    unfocusedContainerColor = Color(0xFFF1F2F6),
                    focusedIndicatorColor = MainBlue,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Password*", color = Color.Gray, fontSize = 14.sp)
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Enter your password", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF1F2F6),
                    unfocusedContainerColor = Color(0xFFF1F2F6),
                    focusedIndicatorColor = MainBlue,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        scope.launch {
                            val user = userDao.getUserByEmail(email)
                            if (user == null) {
                                Toast.makeText(context, "Account not found, sign up first", Toast.LENGTH_SHORT).show()
                            } else if (user.password != password) {
                                Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.setUserEmail(email)
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MainBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Don't have account ? ", color = Color.Gray, fontSize = 14.sp)
                TextButton(onClick = { navController.navigate("signup") }) {
                    Text(
                        text = "Sign up here",
                        color = MainBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
