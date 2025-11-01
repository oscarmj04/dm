package com.example.myapplication.model
import com.example.myapplication.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class TaskViewModel : ViewModel() {

    // Estado interno (mutable)
    private val _tasks = MutableLiveData<List<Task>>(
        listOf(
            Task(1, "Aprender Kotlin", "Fundamentos", LocalDate.now(), Category.PERSONAL, false),
            Task(2, "Sacar al perro", "Parque 20min", LocalDate.now().plusDays(1), Category.URGENTE, true),
            Task(3, "Leer libro", "Cap. 4", LocalDate.now().plusDays(2), Category.PERSONAL, false)
        )
    )

    // Expuesto sólo como LiveData
    val tasks: LiveData<List<Task>> = _tasks

    // ===== Helpers =====

    private fun nextId(): Int = (_tasks.value?.maxOfOrNull { it.id } ?: 0) + 1

    fun getTaskById(id: Int): Task? = _tasks.value?.firstOrNull { it.id == id }

    fun addTask(task: Task) {
        val current = _tasks.value.orEmpty()
        val withId = if (task.id == 0) task.copy(id = nextId()) else task
        _tasks.value = current + withId
    }

    fun updateTask(updated: Task) {
        val current = _tasks.value.orEmpty()
        _tasks.value = current.map { if (it.id == updated.id) updated else it }
    }

    fun deleteTask(id: Int) {
        val current = _tasks.value.orEmpty()
        _tasks.value = current.filterNot { it.id == id }
    }

    fun markTaskDone(id: Int) {
        val current = _tasks.value.orEmpty()
        _tasks.value = current.map { if (it.id == id) it.copy(done = true) else it }
    }
}
