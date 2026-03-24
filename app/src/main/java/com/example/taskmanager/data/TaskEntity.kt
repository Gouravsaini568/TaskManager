package com.example.taskmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userEmail: String, // Associate task with a specific user
    val title: String,
    val description: String,
    val status: String,
    val isImportant: Boolean,
    val createdAt: Long = System.currentTimeMillis()
)
