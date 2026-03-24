package com.example.taskmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.taskmanager.data.TaskEntity
import com.example.taskmanager.ui.TaskViewModel
import com.example.taskmanager.ui.theme.MainBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(navController: NavController, viewModel: TaskViewModel) {
    val userEmail by viewModel.userEmail.collectAsState()

    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var isImportant by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Task", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
                .padding(24.dp)
        ) {
            Text("Title*", color = Color.LightGray, fontSize = 12.sp)
            TextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF1F2F6),
                    unfocusedContainerColor = Color(0xFFF1F2F6),
                    focusedIndicatorColor = MainBlue,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Description*", color = Color.LightGray, fontSize = 12.sp)
            TextField(
                value = desc,
                onValueChange = { desc = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF1F2F6),
                    unfocusedContainerColor = Color(0xFFF1F2F6),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Is This task important?", modifier = Modifier.weight(1f), color = Color.Gray)
                Checkbox(
                    checked = isImportant,
                    onCheckedChange = { isImportant = it },
                    colors = CheckboxDefaults.colors(checkedColor = MainBlue)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        viewModel.addTask(
                            TaskEntity(
                                userEmail = userEmail,
                                title = title,
                                description = desc,
                                status = "Pending",
                                isImportant = isImportant
                            )
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MainBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
