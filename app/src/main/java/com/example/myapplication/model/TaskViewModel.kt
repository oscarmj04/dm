package com.example.myapplication.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.AppDatabase
import com.example.myapplication.Task
import com.example.myapplication.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: TaskDao = AppDatabase.getInstance(application).taskDao()

    /** Lista reactiva directamente desde Room (LiveData) */
    val tasks: LiveData<List<Task>> = dao.getAll()

    /** Observa una tarea concreta por id (para detalle/edición) */
    fun getTaskById(id: Int): LiveData<Task> = dao.observeById(id)

    /** Insertar (id lo autogenera Room) */
    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(task.copy(id = 0))
        }
    }

    /** Actualizar (título, descripción, dueDate, category, done/isDone, etc.) */
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
