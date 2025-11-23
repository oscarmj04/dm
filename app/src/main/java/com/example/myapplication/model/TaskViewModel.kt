package com.example.myapplication.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.myapplication.AppDatabase
import com.example.myapplication.Category
import com.example.myapplication.Task
import com.example.myapplication.TaskDao
import com.example.myapplication.TaskListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: TaskDao = AppDatabase.getInstance(application).taskDao()

    /** Lista “cruda” de tareas tal y como viene de Room */
    val tasks: LiveData<List<Task>> = dao.getAll()

    /** Lista transformada para la UI: headers por categoría + tareas */
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

    /** Observa una tarea concreta por id (para detalle/edición) */
    fun getTaskById(id: Int): LiveData<Task> = dao.observeById(id)

    /** Insertar (id lo autogenera Room) */
    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(task.copy(id = 0))
        }
    }

    /** Actualizar tarea existente */
    fun updateTask(updated: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(updated)
        }
    }

    /** Eliminar por id */
    fun deleteTask(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}
