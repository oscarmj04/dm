package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var title: String,
    var description: String,
    var dueDate: LocalDate,
    var category: Category,
    var done: Boolean
) : Serializable
