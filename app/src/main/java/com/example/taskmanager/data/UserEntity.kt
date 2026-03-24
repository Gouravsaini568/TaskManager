package com.example.taskmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val email: String,
    val name: String,
    val phone: String,
    val city: String,
    val state: String,
    val country: String,
    val pincode: String,
    val password: String
)
