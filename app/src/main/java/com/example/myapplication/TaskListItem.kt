package com.example.myapplication

import com.example.myapplication.domain.Task

// Representa un ítem de la lista: puede ser un encabezado de categoría o una tarea
sealed class TaskListItem {

    // Encabezado de sección (una por categoría)
    data class Header(val category: Category) : TaskListItem()

    // Fila normal de tarea
    data class TaskItem(val task: Task) : TaskListItem()
}
