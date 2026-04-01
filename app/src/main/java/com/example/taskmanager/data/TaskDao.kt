package com.example.taskmanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    // Your TaskDao handles all the CRUD (Create, Read, Update, Delete) operations for the tasks table.
// interface that defines how you interact with your data SQL to kotlin intraction

    @Insert
    suspend fun insert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE userEmail = :email ORDER BY createdAt DESC")
    fun getAllTasks(email: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Int): TaskEntity?
}
