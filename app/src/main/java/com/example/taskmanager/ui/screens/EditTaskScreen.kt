package com.example.taskmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
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
fun EditTaskScreen(navController: NavController, taskId: Int, viewModel: TaskViewModel) {
    
    var task by remember { mutableStateOf<TaskEntity?>(null) }
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Pending") }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(taskId) {
        val fetchedTask = viewModel.getTaskById(taskId)
        if (fetchedTask != null) {
            task = fetchedTask
            title = fetchedTask.title
            desc = fetchedTask.description
            status = fetchedTask.status
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Task", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        if (task != null) {
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

                // Status Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F2F6))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = status, color = Color.Black)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.85f).background(Color.White)
                    ) {
                        listOf("Pending", "In Progress", "Completed").forEach { s ->
                            DropdownMenuItem(
                                text = { Text(s) },
                                onClick = {
                                    status = s
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            viewModel.updateTask(
                                task!!.copy(
                                    title = title,
                                    description = desc,
                                    status = status
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
                    Text("Update", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MainBlue)
            }
        }
    }
}
