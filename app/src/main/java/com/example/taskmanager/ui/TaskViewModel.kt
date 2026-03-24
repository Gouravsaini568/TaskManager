package com.example.taskmanager.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.TaskDatabase
import com.example.taskmanager.data.TaskEntity
import com.example.taskmanager.data.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val db = TaskDatabase.getDB(application)
    private val taskDao = db.taskDao()
    private val userDao = db.userDao()

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    private val _selectedTab = MutableStateFlow("All")
    val selectedTab: StateFlow<String> = _selectedTab

    fun setUserEmail(email: String) {
        _userEmail.value = email
        fetchUserDetails(email)
    }

    private fun fetchUserDetails(email: String) {
        viewModelScope.launch {
            _currentUser.value = userDao.getUserByEmail(email)
        }
    }

    val tasks: StateFlow<List<TaskEntity>> = combine(
        _userEmail.flatMapLatest { email ->
            taskDao.getAllTasks(email)
        },
        _selectedTab
    ) { tasks, tab ->
        when (tab) {
            "All" -> tasks
            "Important" -> tasks.filter { it.isImportant }
            "Pending" -> tasks.filter { it.status == "Pending" }
            "In Progress" -> tasks.filter { it.status == "In Progress" }
            "Completed" -> tasks.filter { it.status == "Completed" }
            else -> tasks
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: String) {
        _selectedTab.value = tab
    }

    fun addTask(task: TaskEntity) {
        viewModelScope.launch {
            taskDao.insert(task)
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            taskDao.update(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            taskDao.delete(task)
        }
    }

    fun toggleImportance(task: TaskEntity) {
        viewModelScope.launch {
            taskDao.update(task.copy(isImportant = !task.isImportant))
        }
    }

    suspend fun getTaskById(id: Int): TaskEntity? {
        return taskDao.getTaskById(id)
    }

    fun logout() {
        _userEmail.value = ""
        _currentUser.value = null
    }
}
