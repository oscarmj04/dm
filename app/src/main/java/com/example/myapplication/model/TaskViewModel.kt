package com.example.myapplication.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.myapplication.TaskListItem
import com.example.myapplication.data.repository.TaskRepository
import com.example.myapplication.domain.Task
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {
    // LiveData que expone la lista de tareas del repositorio
    val tasks: LiveData<List<Task>> = repository.getTasks()

    val taskListItems: LiveData<List<TaskListItem>> =
        tasks.map { list ->
            list
                .sortedWith(compareBy<Task> { it.category }.thenBy { it.dueDate })
                .groupBy { it.category }
                .flatMap { (category, tasksInCategory) ->
                    listOf(TaskListItem.Header(category)) +
                            tasksInCategory.map { TaskListItem.TaskItem(it) }
                }
        }
    init {
        // Al crear el ViewModel, actualiza Room con los datos remotos
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                repository.refreshFromRemote()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addTask(task: Task, onFinished: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.addTask(task)
            onFinished?.invoke()
        }
    }

    fun updateTask(task: Task, onFinished: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.updateTask(task)
            onFinished?.invoke()
        }
    }

    fun deleteTask(task: Task, onFinished: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.deleteTask(task)
            onFinished?.invoke()
        }
    }

    fun getTaskById(localId: Int): Task? =
        tasks.value?.find { it.localId == localId }
}
