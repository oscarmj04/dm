package com.example.myapplication.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Category
import com.example.myapplication.Task
import com.example.myapplication.TaskListItem
import com.example.myapplication.network.RetrofitInstance
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    // LiveData para exponer la lista de tareas al UI
    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks
    val taskListItems: LiveData<List<TaskListItem>> = tasks.map { list ->
        if (list.isNullOrEmpty()) {
            emptyList()
        } else {
            list
                .sortedWith(
                    compareBy<Task> { it.category.name }
                        .thenBy { it.dueDate }
                )
                .groupBy { it.category }
                .flatMap { (category: Category, categoryTasks: List<Task>) ->
                    listOf(TaskListItem.Header(category)) +
                            categoryTasks.map { TaskListItem.TaskItem(it) }
                }
        }
    }

    // Cargar todas las tareas desde CrudCrud
    fun loadTasks() {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getTasks()
                _tasks.postValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Crear nueva tarea
    fun addTask(task: Task, onFinished: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.addTask(task)
                loadTasks()  // refrescar lista
                onFinished?.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Actualizar una tarea existente
    fun updateTask(task: Task, onFinished: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                val id = task.id ?: return@launch
                // CrudCrud NO permite enviar el campo _id
                val taskToSend = task.copy(id = null)

                RetrofitInstance.api.updateTask(id, taskToSend)
                loadTasks()
                onFinished?.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Eliminar tarea
    fun deleteTask(taskId: String, onFinished: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.deleteTask(taskId)
                loadTasks()
                onFinished?.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getTaskById(id: String): Task? {
        return _tasks.value?.find { it.id == id }
    }

}
