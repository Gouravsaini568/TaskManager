package com.example.taskmanager.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.taskmanager.data.UserEntity
import com.example.taskmanager.ui.theme.MainBlue
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userDao = TaskDatabase.getDB(context).userDao()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    color = MainBlue,
                    shape = RoundedCornerShape(bottomStart = 80.dp)
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Create your\naccount",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 40.sp,
                modifier = Modifier.padding(start = 32.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Sign Up",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            SignUpTextField("Full Name*", name) { name = it }
            SignUpTextField("Email*", email) { email = it }
            SignUpTextField("Phone No*", phone) { phone = it }
            SignUpTextField("City*", city) { city = it }
            SignUpTextField("State*", state) { state = it }
            SignUpTextField("Country*", country) { country = it }
            SignUpTextField("Pincode*", pincode) { pincode = it }

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
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank() || name.isBlank() || phone.isBlank()) {
                        Toast.makeText(context, "Please fill all mandatory fields", Toast.LENGTH_SHORT).show()
                    } else {
                        scope.launch {
                            val existingUser = userDao.getUserByEmail(email)
                            if (existingUser != null) {
                                Toast.makeText(context, "Account already exists", Toast.LENGTH_SHORT).show()
                            } else {
                                userDao.insertUser(
                                    UserEntity(
                                        email = email,
                                        name = name,
                                        phone = phone,
                                        city = city,
                                        state = state,
                                        country = country,
                                        pincode = pincode,
                                        password = password
                                    )
                                )
                                Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
                                navController.navigate("login")
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
                Text("Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Already have an account? ", color = Color.Gray, fontSize = 14.sp)
                TextButton(onClick = { navController.navigate("login") }) {
                    Text(
                        text = "Login here",
                        color = MainBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SignUpTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Text(label, color = Color.Gray, fontSize = 14.sp)
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Enter $label".replace("*", ""), color = Color.LightGray) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF1F2F6),
            unfocusedContainerColor = Color(0xFFF1F2F6),
            focusedIndicatorColor = MainBlue,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp)
    )
}
