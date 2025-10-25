package com.example.myapplication

import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger
import Task

object TaskRepository {
    private val autoId = AtomicInteger(1000)
    private val tasks = mutableListOf<Task>()
    private var nextID = 1
    fun getAll(): List<Task> = tasks.toList()
    fun getById(id: Int): Task? = tasks.firstOrNull { it.id == id }

    fun add(task: Task): Task {
        task.id = nextID++
        tasks.add(task)
        return task
    }

    fun addOrUpdate(task: Task): Task {
        val idx = tasks.indexOfFirst { it.id == task.id }
        return if (idx >= 0) { tasks[idx] = task; task } else add(task)
    }

   fun ensureSeed() {
        if (tasks.isNotEmpty()) return
        tasks += listOf(
            Task(nextID++, "Aprender Kotlin", "Fundamentos", LocalDate.now(), Category.PERSONAL, false),
            Task(nextID++, "Sacar al perro", "Parque 20min", LocalDate.now().plusDays(1), Category.URGENTE, true),
            Task(nextID++, "Leer libro", "Cap. 4", LocalDate.now().plusDays(2), Category.PERSONAL, false),
        )
    }
}
