package com.example.myapplication
import java.io.Serializable
import java.time.LocalDate

data class Task(
    var id: Int,
    var title: String,
    var description: String,
    var dueDate: LocalDate,
    var category: Category,
    var isDone: Boolean
) : Serializable
